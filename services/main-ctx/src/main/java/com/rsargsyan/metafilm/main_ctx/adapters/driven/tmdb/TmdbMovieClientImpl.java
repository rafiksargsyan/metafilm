package com.rsargsyan.metafilm.main_ctx.adapters.driven.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbMovieClient;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbMovieData;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbTranslationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.rsargsyan.metafilm.main_ctx.adapters.driven.tmdb.TmdbLocaleResolver.*;

@Component
public class TmdbMovieClientImpl implements TmdbMovieClient {

  private final RestClient restClient;
  private final String apiKey;

  public TmdbMovieClientImpl(
      @Value("${tmdb.base-url}") String baseUrl,
      @Value("${tmdb.api-key}") String apiKey) {
    this.apiKey = apiKey;
    this.restClient = RestClient.builder()
        .baseUrl(baseUrl)
        .build();
  }

  @Override
  public TmdbMovieData fetchMovie(Long tmdbId) {
    MovieResponse response = restClient.get()
        .uri("/movie/{id}?append_to_response=translations,images&api_key={key}", tmdbId, apiKey)
        .retrieve()
        .body(MovieResponse.class);

    Optional<Locale> originalLocale = resolveFromLanguageAndCountries(
        response.originalLanguage(),
        response.productionCountries() != null
            ? response.productionCountries().stream().map(ProductionCountry::countryCode).toList()
            : List.of());

    List<ImageEntry> posters = response.images() != null ? response.images().posters() : List.of();
    List<ImageEntry> backdrops = response.images() != null ? response.images().backdrops() : List.of();

    return new TmdbMovieData(
        response.originalTitle(),
        response.overview(),
        response.tagline(),
        bestImage(posters, null),
        bestImage(backdrops, null),
        response.imdbId(),
        originalLocale,
        parseDate(response.releaseDate()),
        response.runtime(),
        mapTranslations(response, posters, backdrops)
    );
  }

  private List<TmdbTranslationData> mapTranslations(MovieResponse response,
                                                     List<ImageEntry> posters,
                                                     List<ImageEntry> backdrops) {
    List<TranslationEntry> raw = response.translations() != null
        ? response.translations().translations() : List.of();

    return raw.stream()
        .map(t -> resolveLocale(t.languageCode(), t.countryCode()).map(locale ->
            new TmdbTranslationData(
                locale,
                t.data() != null ? t.data().title() : null,
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
  record MovieResponse(
      @JsonProperty("original_title") String originalTitle,
      @JsonProperty("original_language") String originalLanguage,
      @JsonProperty("release_date") String releaseDate,
      @JsonProperty("runtime") Integer runtime,
      @JsonProperty("overview") String overview,
      @JsonProperty("tagline") String tagline,
      @JsonProperty("imdb_id") String imdbId,
      @JsonProperty("production_countries") List<ProductionCountry> productionCountries,
      @JsonProperty("translations") TranslationsWrapper translations,
      @JsonProperty("images") ImagesWrapper images
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record ProductionCountry(
      @JsonProperty("iso_3166_1") String countryCode
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
      @JsonProperty("title") String title,
      @JsonProperty("overview") String overview,
      @JsonProperty("tagline") String tagline
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record ImagesWrapper(
      @JsonProperty("posters") List<ImageEntry> posters,
      @JsonProperty("backdrops") List<ImageEntry> backdrops
  ) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  record ImageEntry(
      @JsonProperty("file_path") String filePath,
      @JsonProperty("iso_639_1") String languageCode,
      @JsonProperty("vote_average") Double voteAverage
  ) implements TmdbLocaleResolver.TmdbImage {}
}
