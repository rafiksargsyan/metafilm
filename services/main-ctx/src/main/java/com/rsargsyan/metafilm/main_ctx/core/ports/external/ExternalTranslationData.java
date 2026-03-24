package com.rsargsyan.metafilm.main_ctx.core.ports.external;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

public record ExternalTranslationData(
    Locale locale,
    String title,
    String overview,
    String tagline,      // nullable — used for shows, ignored for seasons/episodes
    String posterPath,   // nullable — external path (e.g. TMDB "/abc.jpg")
    String backdropPath  // nullable — external path
) {
}
