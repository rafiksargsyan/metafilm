package com.rsargsyan.metafilm.main_ctx.adapters.driven.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.*;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbTVShowClient;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbVideoData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.rsargsyan.metafilm.main_ctx.adapters.driven.tmdb.TmdbLocaleResolver.*;

@Component
public class TmdbTVShowClientImpl implements TmdbTVShowClient {

  private final RestClient restClient;
  private final String apiKey;

  public TmdbTVShowClientImpl(
      @Value("${tmdb.base-url}") String baseUrl,
      @Value("${tmdb.api-key}") String apiKey) {
    this.apiKey = apiKey;
    this.restClient = RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader("Accept-Encoding", "identity")
        .build();
  }

  @Override
  public ExternalTVShowData fetchTVShow(Long tmdbId) {
    TVShowResponse response = restClient.get()
        .uri("/tv/{id}?append_to_response=translations,images&api_key={key}", tmdbId, apiKey)
        .retrieve()
        .body(TVShowResponse.class);

    Optional<Locale> originalLocale = resolveFromLanguageAndCountries(
        response.originalLanguage(),
        response.originCountry() != null ? response.originCountry() : List.of());

    List<ImageEntry> posters = response.images() != null ? response.images().posters() : List.of();
    List<ImageEntry> backdrops = response.images() != null ? response.images().backdrops() : List.of();

    List<ExternalTranslationData> translations = mapShowTranslations(response, posters, backdrops);

    List<Integer> genreIds = response.genres() != null
        ? response.genres().stream().map(GenreEntry::id).toList()
        : List.of();

    List<ExternalSeasonData> seasons = response.seasons() != null
        ? response.seasons().stream()
            .filter(s -> s.seasonNumber() != null && s.seasonNumber() > 0) // skip season 0 (Specials)
            .map(s -> fetchSeason(tmdbId, s.seasonNumber(), originalLocale))
            .toList()
        : List.of();

    return new ExternalTVShowData(
        response.originalName(),
        response.overview(),
        response.tagline(),
        response.posterPath(),
        response.backdropPath(),
        originalLocale,
        parseDate(response.firstAirDate()),
        parseDate(response.lastAirDate()),
        response.voteAverage(),
        genreIds,
        translations,
        seasons
    );
  }

  private ExternalSeasonData fetchSeason(Long tmdbId, Integer seasonNumber,
                                         Optional<Locale> originalLocale) {
    SeasonResponse season = restClient.get()
        .uri("/tv/{id}/season/{seasonNumber}?append_to_response=translations,images&api_key={key}",
            tmdbId, seasonNumber, apiKey)
        .retrieve()
        .body(SeasonResponse.class);

    List<ImageEntry> posters = season.images() != null ? season.images().posters() : List.of();

    List<ExternalTranslationData> translations = season.translations() != null
        ? season.translations().translations().stream()
            .map(t -> resolveLocale(t.languageCode(), t.countryCode()).map(locale ->
                new ExternalTranslationData(
                    locale,
                    t.data() != null ? t.data().name() : null,
                    t.data() != null ? t.data().overview() : null,
                    null,
                    bestImage(posters, t.languageCode()),
                    null
                )))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList()
        : List.of();

    List<ExternalEpisodeData> episodes = season.episodes() != null
        ? season.episodes().stream()
            .map(e -> mapEpisode(e, originalLocale))
            .toList()
        : List.of();

    return new ExternalSeasonData(
        season.seasonNumber(),
        season.name(),
        parseDate(season.airDate()),
        translations,
        episodes
    );
  }

  private ExternalEpisodeData mapEpisode(EpisodeEntry e, Optional<Locale> originalLocale) {
    List<ExternalTranslationData> translations = originalLocale
        .map(locale -> List.of(new ExternalTranslationData(
            locale, e.name(), e.overview(), null, null, null)))
        .orElse(List.of());

    return new ExternalEpisodeData(
        e.seasonNumber(),
        e.episodeNumber(),
        null,
        parseDate(e.airDate()),
        e.runtime(),
        e.stillPath(),
        translations
    );
  }

  @Override
  public Map<Locale, TmdbVideoData> fetchTVShowVideos(Long tmdbId, Set<Locale> locales) {
    List<String> langs = locales.stream().map(Locale::getLang).distinct().collect(Collectors.toList());
    List<VideoEntry> allVideos = new ArrayList<>();
    Set<String> seen = new HashSet<>();
    for (int i = 0; i < langs.size(); i += 5) {
      String batch = String.join(",", langs.subList(i, Math.min(i + 5, langs.size())));
      VideosResponse response = restClient.get()
          .uri("/tv/{id}/videos?api_key={key}&include_video_language={langs}", tmdbId, apiKey, batch)
          .retrieve()
          .body(VideosResponse.class);
      if (response != null && response.results() != null) {
        for (VideoEntry v : response.results()) {
          if (v.key() != null && seen.add(v.key())) allVideos.add(v);
        }
      }
    }
    return selectVideosForLocales(allVideos, locales);
  }

  private Map<Locale, TmdbVideoData> selectVideosForLocales(List<VideoEntry> videos, Set<Locale> locales) {
    Map<Locale, TmdbVideoData> result = new HashMap<>();
    for (Locale locale : locales) {
      selectBestVideo(videos, locale).ifPresent(v -> result.put(locale, new TmdbVideoData(v.site(), v.key())));
    }
    return result;
  }

  private Optional<VideoEntry> selectBestVideo(List<VideoEntry> videos, Locale locale) {
    String lang = locale.getLang();
    boolean localeHasCountry = locale.getTag().contains("-");
    return videos.stream()
        .filter(v -> videoMatchesLocale(v, lang, localeHasCountry, locale))
        .max(Comparator
            .comparingInt((VideoEntry v) -> v.official() ? 1 : 0)
            .thenComparingInt(v -> "Trailer".equals(v.type()) ? 1 : 0)
            .thenComparing(v -> v.publishedAt() != null ? v.publishedAt() : ""));
  }

  private boolean videoMatchesLocale(VideoEntry v, String lang, boolean localeHasCountry, Locale locale) {
    if (v.languageCode() == null || v.languageCode().isBlank()) return false;
    if (!v.languageCode().equalsIgnoreCase(lang)) return false;
    String vCountry = (v.countryCode() != null && !v.countryCode().isBlank()) ? v.countryCode() : null;
    if (vCountry != null && localeHasCountry) {
      return resolveLocale(v.languageCode(), v.countryCode()).map(l -> l == locale).orElse(false);
    }
    return true;
  }

  @Override
  public List<ExternalTranslationData> fetchEpisodeTranslations(Long tvShowTmdbId, Integer seasonNumber,
                                                                 Integer episodeNumber) {
    EpisodeTranslationsResponse response = restClient.get()
        .uri("/tv/{id}/season/{s}/episode/{e}/translations?api_key={key}",
            tvShowTmdbId, seasonNumber, episodeNumber, apiKey)
        .retrieve()
        .body(EpisodeTranslationsResponse.class);

    if (response == null || response.translations() == null) return List.of();

    return response.translations().stream()
        .map(t -> resolveLocale(t.languageCode(), t.countryCode()).map(locale ->
            new ExternalTranslationData(
                locale,
                t.data() != null ? t.data().name() : null,
                t.data() != null ? t.data().overview() : null,
                null, null, null
            )))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private List<ExternalTranslationData> mapShowTranslations(TVShowResponse response,
                                                             List<ImageEntry> posters,
                                                             List<ImageEntry> backdrops) {
    List<TranslationEntry> raw = response.translations() != null
        ? response.translations().translations() : List.of();

    return raw.stream()
        .map(t -> resolveLocale(t.languageCode(), t.countryCode()).map(locale ->
            new ExternalTranslationData(
                locale,
                t.data() != null ? t.data().name() : null,
                t.data() != null ? t.data().overview() : null,
                t.data() != null ? t.data().tagline() : null,
                bestImage(posters, t.languageCode()),
                bestImage(backdrops, t.languageCode())
            )))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private static LocalDate parseDate(String date) {
    if (date == null || date.isBlank()) return null;
    try {
      return LocalDate.parse(date);
    } catch (Exception e) {
      return null;
    }
  }

  // ── TMDB response types ──────────────────────────────────────────────────

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TVShowResponse(
      @JsonProperty("original_name") String originalName,
      @JsonProperty("original_language") String originalLanguage,
      @JsonProperty("first_air_date") String firstAirDate,
      @JsonProperty("last_air_date") String lastAirDate,
      @JsonProperty("overview") String overview,
      @JsonProperty("tagline") String tagline,
      @JsonProperty("poster_path") String posterPath,
      @JsonProperty("backdrop_path") String backdropPath,
      @JsonProperty("vote_average") Double voteAverage,
      @JsonProperty("origin_country") List<String> originCountry,
      @JsonProperty("genres") List<GenreEntry> genres,
      @JsonProperty("seasons") List<SeasonEntry> seasons,
      @JsonProperty("translations") TranslationsWrapper translations,
      @JsonProperty("images") ImagesWrapper images
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record VideosResponse(
      @JsonProperty("results") List<VideoEntry> results
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record VideoEntry(
      @JsonProperty("key") String key,
      @JsonProperty("site") String site,
      @JsonProperty("type") String type,
      @JsonProperty("official") boolean official,
      @JsonProperty("iso_639_1") String languageCode,
      @JsonProperty("iso_3166_1") String countryCode,
      @JsonProperty("published_at") String publishedAt
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record GenreEntry(
      @JsonProperty("id") Integer id
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record SeasonEntry(
      @JsonProperty("season_number") Integer seasonNumber,
      @JsonProperty("name") String name,
      @JsonProperty("air_date") String airDate
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record SeasonResponse(
      @JsonProperty("season_number") Integer seasonNumber,
      @JsonProperty("name") String name,
      @JsonProperty("air_date") String airDate,
      @JsonProperty("episodes") List<EpisodeEntry> episodes,
      @JsonProperty("translations") TranslationsWrapper translations,
      @JsonProperty("images") SeasonImagesWrapper images
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record EpisodeEntry(
      @JsonProperty("season_number") Integer seasonNumber,
      @JsonProperty("episode_number") Integer episodeNumber,
      @JsonProperty("name") String name,
      @JsonProperty("overview") String overview,
      @JsonProperty("air_date") String airDate,
      @JsonProperty("runtime") Integer runtime,
      @JsonProperty("still_path") String stillPath
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TranslationsWrapper(
      @JsonProperty("translations") List<TranslationEntry> translations
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record EpisodeTranslationsResponse(
      @JsonProperty("translations") List<TranslationEntry> translations
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TranslationEntry(
      @JsonProperty("iso_639_1") String languageCode,
      @JsonProperty("iso_3166_1") String countryCode,
      @JsonProperty("data") TranslationData data
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record TranslationData(
      @JsonProperty("name") String name,
      @JsonProperty("overview") String overview,
      @JsonProperty("tagline") String tagline
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record ImagesWrapper(
      @JsonProperty("posters") List<ImageEntry> posters,
      @JsonProperty("backdrops") List<ImageEntry> backdrops
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record SeasonImagesWrapper(
      @JsonProperty("posters") List<ImageEntry> posters
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record ImageEntry(
      @JsonProperty("file_path") String filePath,
      @JsonProperty("iso_639_1") String languageCode,
      @JsonProperty("vote_average") Double voteAverage
  ) implements TmdbLocaleResolver.TmdbImage {}
}
