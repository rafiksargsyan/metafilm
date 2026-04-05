package com.rsargsyan.metafilm.main_ctx.core.ports.external;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record ExternalTVShowData(
    String originalTitle,
    String originalOverview,
    String originalTagline,
    String originalPosterPath,
    String originalBackdropPath,
    Optional<Locale> originalLocale,
    LocalDate firstAirDate,
    LocalDate lastAirDate,
    List<ExternalTranslationData> translations,
    List<ExternalSeasonData> seasons
) {
}
