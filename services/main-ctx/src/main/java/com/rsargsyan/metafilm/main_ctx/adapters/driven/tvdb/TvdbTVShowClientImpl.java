package com.rsargsyan.metafilm.main_ctx.adapters.driven.tvdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.*;
import com.rsargsyan.metafilm.main_ctx.core.ports.tvdb.TvdbTVShowClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TvdbTVShowClientImpl implements TvdbTVShowClient {

  private static final Logger log = LoggerFactory.getLogger(TvdbTVShowClientImpl.class);
  private static final String BASE_URL = "https://api4.thetvdb.com/v4";

  private final String apiKey;
  private final RestClient restClient;

  private volatile String authToken;
  private volatile long tokenExpiresAtEpochSeconds = 0;

  public TvdbTVShowClientImpl(@Value("${tvdb.api-key}") String apiKey) {
    this.apiKey = apiKey;
    this.restClient = RestClient.builder()
        .baseUrl(BASE_URL)
        .defaultHeader("Accept-Encoding", "identity")
        .build();
  }

  @Override
  public ExternalTVShowData fetchTVShow(Long tvdbId) {
    ensureAuthenticated();

    log.info("Fetching TVDB series extended data for tvdbId={}", tvdbId);
    TvdbSeriesExtendedResponse resp = restClient.get()
        .uri("/series/{id}/extended?meta=translations", tvdbId)
        .header("Authorization", "Bearer " + authToken)
        .retrieve()
        .body(TvdbSeriesExtendedResponse.class);

    if (resp == null || resp.data() == null) {
      throw new RuntimeException("TVDB series not found: " + tvdbId);
    }

    TvdbSeriesExtended series = resp.data();
    log.info("Got TVDB series '{}' (lang={}, seasons={}, artworks={})",
        series.name(), series.originalLanguage(),
        series.seasons() != null ? series.seasons().size() : 0,
        series.artworks() != null ? series.artworks().size() : 0);

    Optional<Locale> originalLocale = fromIso6392(series.originalLanguage());

    List<TvdbArtwork> artworks = series.artworks() != null ? series.artworks() : List.of();
    Map<String, String> bestPosterByLang = bestArtworksByLang(artworks, 2);
    Map<String, String> bestBackdropByLang = bestArtworksByLang(artworks, 3);

    List<ExternalTranslationData> translations = mapTranslations(series, bestPosterByLang, bestBackdropByLang);
    log.info("Mapped {} translations", translations.size());

    log.info("Fetching episodes for tvdbId={}", tvdbId);
    List<TvdbEpisodeData> allEpisodes = fetchAllEpisodes(tvdbId);
    log.info("Fetched {} episodes", allEpisodes.size());

    List<Integer> officialSeasonNumbers = (series.seasons() != null ? series.seasons() : List.<TvdbSeason>of())
        .stream()
        .filter(s -> s.type() != null && "official".equals(s.type().type()))
        .filter(s -> s.number() != null && s.number() > 0)
        .map(TvdbSeason::number)
        .distinct()
        .sorted()
        .toList();
    log.info("Official seasons: {}", officialSeasonNumbers);

    List<ExternalSeasonData> seasons = buildSeasons(officialSeasonNumbers, allEpisodes, originalLocale);

    String mainBackdrop = bestBackdropByLang.get(null);
    if (mainBackdrop == null) mainBackdrop = bestBackdropByLang.values().stream().findFirst().orElse(null);

    return new ExternalTVShowData(
        series.name(),
        series.overview(),
        null,
        series.image(),
        mainBackdrop,
        originalLocale,
        parseDate(series.firstAired()),
        parseDate(series.lastAired()),
        null,
        List.of(),
        translations,
        seasons
    );
  }

  private Map<String, String> bestArtworksByLang(List<TvdbArtwork> artworks, int type) {
    Map<String, TvdbArtwork> best = new HashMap<>();
    for (TvdbArtwork a : artworks) {
      if (a.type() == null || a.type() != type || a.image() == null) continue;
      String lang = a.language();
      TvdbArtwork existing = best.get(lang);
      if (existing == null || (a.score() != null && (existing.score() == null || a.score() > existing.score()))) {
        best.put(lang, a);
      }
    }
    Map<String, String> result = new HashMap<>();
    best.forEach((lang, a) -> result.put(lang, a.image()));
    return result;
  }

  private List<ExternalTranslationData> mapTranslations(TvdbSeriesExtended series,
                                                         Map<String, String> bestPosterByLang,
                                                         Map<String, String> bestBackdropByLang) {
    if (series.translations() == null) return List.of();

    Map<String, String> names = new LinkedHashMap<>();
    Map<String, String> overviews = new LinkedHashMap<>();

    for (TvdbTranslationEntry t : orEmpty(series.translations().nameTranslations())) {
      if (t.language() != null && t.name() != null && !t.name().isBlank()) names.put(t.language(), t.name());
      if (t.language() != null && t.overview() != null && !t.overview().isBlank()) overviews.put(t.language(), t.overview());
    }
    for (TvdbTranslationEntry t : orEmpty(series.translations().overviewTranslations())) {
      if (t.language() != null && t.overview() != null && !t.overview().isBlank()) overviews.putIfAbsent(t.language(), t.overview());
    }

    Set<String> langs = new LinkedHashSet<>();
    langs.addAll(names.keySet());
    langs.addAll(overviews.keySet());

    List<ExternalTranslationData> result = new ArrayList<>();
    for (String lang : langs) {
      Optional<Locale> locale = fromIso6392(lang);
      if (locale.isEmpty()) continue;
      result.add(new ExternalTranslationData(
          locale.get(),
          names.get(lang),
          overviews.get(lang),
          null,
          bestPosterByLang.get(lang),
          bestBackdropByLang.get(lang)
      ));
    }
    return result;
  }

  private List<TvdbEpisodeData> fetchAllEpisodes(Long tvdbId) {
    List<TvdbEpisodeData> result = new ArrayList<>();
    int page = 0;
    while (true) {
      final int p = page;
      TvdbEpisodesResponse resp = restClient.get()
          .uri(b -> b.path("/series/{id}/episodes/default").queryParam("page", p).build(tvdbId))
          .header("Authorization", "Bearer " + authToken)
          .retrieve()
          .body(TvdbEpisodesResponse.class);
      if (resp == null || resp.data() == null || resp.data().episodes() == null
          || resp.data().episodes().isEmpty()) break;
      result.addAll(resp.data().episodes());
      if (resp.data().episodes().size() < 500) break;
      page++;
    }
    return result;
  }

  private List<ExternalSeasonData> buildSeasons(List<Integer> officialSeasonNumbers,
                                                  List<TvdbEpisodeData> allEpisodes,
                                                  Optional<Locale> originalLocale) {
    Map<Integer, List<TvdbEpisodeData>> bySeason = allEpisodes.stream()
        .filter(e -> e.seasonNumber() != null && e.seasonNumber() > 0)
        .filter(e -> e.episodeNumber() != null && e.episodeNumber() > 0)
        .collect(Collectors.groupingBy(TvdbEpisodeData::seasonNumber));

    List<ExternalSeasonData> seasons = new ArrayList<>();
    for (int seasonNum : officialSeasonNumbers) {
      List<TvdbEpisodeData> eps = bySeason.getOrDefault(seasonNum, List.of());
      List<ExternalEpisodeData> episodes = eps.stream()
          .map(e -> toEpisodeData(e, originalLocale))
          .toList();
      seasons.add(new ExternalSeasonData(seasonNum, null, null, null, List.of(), episodes));
    }
    return seasons;
  }

  private ExternalEpisodeData toEpisodeData(TvdbEpisodeData e, Optional<Locale> originalLocale) {
    String still = e.image() != null && !e.image().isBlank() ? e.image() : null;
    List<ExternalTranslationData> translations = List.of();
    if ((e.name() != null && !e.name().isBlank()) && originalLocale.isPresent()) {
      translations = List.of(new ExternalTranslationData(
          originalLocale.get(), e.name(), e.overview(), null, null, null));
    }
    return new ExternalEpisodeData(
        e.seasonNumber(),
        e.episodeNumber(),
        e.absoluteNumber(),
        parseDate(e.aired()),
        e.runtime(),
        still,
        translations
    );
  }

  private synchronized void ensureAuthenticated() {
    long nowSeconds = Instant.now().getEpochSecond();
    if (authToken == null || tokenExpiresAtEpochSeconds - nowSeconds < 3600) {
      login();
    }
  }

  private void login() {
    TvdbLoginResponse resp = restClient.post()
        .uri("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Map.of("apikey", apiKey))
        .retrieve()
        .body(TvdbLoginResponse.class);
    if (resp == null || resp.data() == null || resp.data().token() == null) {
      throw new IllegalStateException("TVDB login failed — no token returned");
    }
    this.authToken = resp.data().token();
    this.tokenExpiresAtEpochSeconds = Instant.now().getEpochSecond() + 29L * 24 * 3600;
    log.info("TVDB authentication successful");
  }

  private static Optional<Locale> fromIso6392(String code) {
    if (code == null) return Optional.empty();
    String iso2 = switch (code.toLowerCase()) {
      case "eng" -> "en";
      case "rus" -> "ru";
      case "fra" -> "fr";
      case "deu" -> "de";
      case "spa" -> "es";
      case "ita" -> "it";
      case "por" -> "pt";
      case "jpn" -> "ja";
      case "kor" -> "ko";
      case "zho", "chi" -> "zh";
      case "hin" -> "hi";
      case "ben" -> "bn";
      case "tur" -> "tr";
      case "ell", "gre" -> "el";
      case "tha" -> "th";
      case "ind" -> "id";
      case "msa", "may", "zsm" -> "ms";
      case "vie" -> "vi";
      case "dan" -> "da";
      case "swe" -> "sv";
      case "nor", "nob" -> "nb";
      case "nld", "dut" -> "nl";
      case "pol" -> "pl";
      case "ukr" -> "uk";
      case "bel" -> "be";
      case "bul" -> "bg";
      case "ces", "cze" -> "cs";
      case "slk", "slo" -> "sk";
      case "slv" -> "sl";
      case "hrv" -> "hr";
      case "srp" -> "sr";
      case "mkd", "mac" -> "mk";
      case "hun" -> "hu";
      case "ron", "rum" -> "ro";
      case "est" -> "et";
      case "lav" -> "lv";
      case "lit" -> "lt";
      case "fin" -> "fi";
      case "hye", "arm" -> "hy";
      case "fas", "per" -> "fa";
      case "ara" -> "ar";
      case "heb" -> "he";
      case "tam" -> "ta";
      case "tel" -> "te";
      case "mal" -> "ml";
      case "urd" -> "ur";
      case "fil" -> "tl";
      case "bos" -> "bs";
      case "sqi", "alb" -> "sq";
      case "aze" -> "az";
      case "kat", "geo" -> "ka";
      default -> null;
    };
    return iso2 != null ? Locale.fromLanguageCode(iso2) : Optional.empty();
  }

  private static LocalDate parseDate(String date) {
    if (date == null || date.isBlank()) return null;
    try { return LocalDate.parse(date); } catch (Exception e) { return null; }
  }

  private static <T> List<T> orEmpty(List<T> list) {
    return list != null ? list : List.of();
  }

  // --- JSON DTOs ---

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbLoginResponse(@JsonProperty("data") TvdbTokenData data) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbTokenData(@JsonProperty("token") String token) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbSeriesExtendedResponse(@JsonProperty("data") TvdbSeriesExtended data) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbSeriesExtended(
      @JsonProperty("name") String name,
      @JsonProperty("overview") String overview,
      @JsonProperty("image") String image,
      @JsonProperty("firstAired") String firstAired,
      @JsonProperty("lastAired") String lastAired,
      @JsonProperty("originalLanguage") String originalLanguage,
      @JsonProperty("seasons") List<TvdbSeason> seasons,
      @JsonProperty("artworks") List<TvdbArtwork> artworks,
      @JsonProperty("translations") TvdbTranslationsWrapper translations
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbSeason(
      @JsonProperty("number") Integer number,
      @JsonProperty("type") TvdbSeasonType type
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbSeasonType(@JsonProperty("type") String type) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbArtwork(
      @JsonProperty("image") String image,
      @JsonProperty("type") Integer type,
      @JsonProperty("language") String language,
      @JsonProperty("score") Double score
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbTranslationsWrapper(
      @JsonProperty("nameTranslations") List<TvdbTranslationEntry> nameTranslations,
      @JsonProperty("overviewTranslations") List<TvdbTranslationEntry> overviewTranslations
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbTranslationEntry(
      @JsonProperty("language") String language,
      @JsonProperty("name") String name,
      @JsonProperty("overview") String overview
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbEpisodesResponse(@JsonProperty("data") TvdbEpisodesData data) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbEpisodesData(@JsonProperty("episodes") List<TvdbEpisodeData> episodes) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TvdbEpisodeData(
      @JsonProperty("seasonNumber") Integer seasonNumber,
      @JsonProperty("number") Integer episodeNumber,
      @JsonProperty("absoluteNumber") Integer absoluteNumber,
      @JsonProperty("runtime") Integer runtime,
      @JsonProperty("aired") String aired,
      @JsonProperty("name") String name,
      @JsonProperty("overview") String overview,
      @JsonProperty("image") String image
  ) {}
}
