package com.rsargsyan.metafilm.main_ctx.core.ports.tmdb;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record TmdbMovieData(
    String originalTitle,
    String originalOverview,
    String originalTagline,
    String originalPosterPath,
    String originalBackdropPath,
    String imdbId,
    Optional<Locale> originalLanguage,
    LocalDate releaseDate,
    Integer runtime,
    List<TmdbTranslationData> translations
) {
}
