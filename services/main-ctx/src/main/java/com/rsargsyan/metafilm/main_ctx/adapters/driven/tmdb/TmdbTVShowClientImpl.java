package com.rsargsyan.metafilm.main_ctx.adapters.driven.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.*;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbTVShowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.rsargsyan.metafilm.main_ctx.adapters.driven.tmdb.TmdbLocaleResolver.*;

@Component
public class TmdbTVShowClientImpl implements TmdbTVShowClient {

  private final RestClient restClient;

  public TmdbTVShowClientImpl(
      @Value("${tmdb.base-url}") String baseUrl,
      @Value("${tmdb.api-key}") String apiKey) {
    this.restClient = RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader("Authorization", "Bearer " + apiKey)
        .build();
  }

  @Override
  public ExternalTVShowData fetchTVShow(Long tmdbId) {
    TVShowResponse response = restClient.get()
        .uri("/tv/{id}?append_to_response=translations,images", tmdbId)
        .retrieve()
        .body(TVShowResponse.class);

    Optional<Locale> originalLocale = resolveFromLanguageAndCountries(
        response.originalLanguage(),
        response.originCountry() != null ? response.originCountry() : List.of());

    List<ExternalTranslationData> translations = mapShowTranslations(response, originalLocale);

    List<ExternalSeasonData> seasons = response.seasons() != null
        ? response.seasons().stream()
            .filter(s -> s.seasonNumber() != null && s.seasonNumber() > 0) // skip season 0 (Specials)
            .map(s -> fetchSeason(tmdbId, s.seasonNumber(), originalLocale))
            .toList()
        : List.of();

    return new ExternalTVShowData(
        response.originalName(),
        parseDate(response.firstAirDate()),
        parseDate(response.lastAirDate()),
        translations,
        seasons
    );
  }

  private ExternalSeasonData fetchSeason(Long tmdbId, Integer seasonNumber,
                                         Optional<Locale> originalLocale) {
    SeasonResponse season = restClient.get()
        .uri("/tv/{id}/season/{seasonNumber}?append_to_response=translations,images",
            tmdbId, seasonNumber)
        .retrieve()
        .body(SeasonResponse.class);

    List<ImageEntry> posters = season.images() != null ? season.images().posters() : List.of();
    String neutralPosterPath = bestImage(posters, null);

    List<ExternalTranslationData> translations = season.translations() != null
        ? season.translations().translations().stream()
            .map(t -> resolveLocale(t.languageCode(), t.countryCode()).map(locale ->
                new ExternalTranslationData(
                    locale,
                    t.data() != null ? t.data().name() : null,
                    t.data() != null ? t.data().overview() : null,
                    null,
                    Optional.ofNullable(bestImage(posters, t.languageCode())).orElse(neutralPosterPath),
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
        .filter(_ -> e.name() != null || e.overview() != null)
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

  private List<ExternalTranslationData> mapShowTranslations(TVShowResponse response,
                                                             Optional<Locale> originalLocale) {
    List<ImageEntry> posters = response.images() != null ? response.images().posters() : List.of();
    List<ImageEntry> backdrops = response.images() != null ? response.images().backdrops() : List.of();
    String neutralBackdropPath = bestImage(backdrops, null);

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
                Optional.ofNullable(bestImage(backdrops, t.languageCode())).orElse(neutralBackdropPath)
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
      @JsonProperty("origin_country") List<String> originCountry,
      @JsonProperty("seasons") List<SeasonEntry> seasons,
      @JsonProperty("translations") TranslationsWrapper translations,
      @JsonProperty("images") ImagesWrapper images
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
