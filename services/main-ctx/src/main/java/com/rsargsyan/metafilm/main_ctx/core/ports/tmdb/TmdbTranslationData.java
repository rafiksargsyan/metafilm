package com.rsargsyan.metafilm.main_ctx.core.ports.tmdb;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

public record TmdbTranslationData(
    Locale locale,
    String title,
    String overview,
    String tagline,
    String posterPath,   // nullable — TMDB path e.g. "/abc123.jpg"
    String backdropPath  // nullable — TMDB path
) {
}
