package com.rsargsyan.metafilm.main_ctx.core.ports.external;

import java.time.LocalDate;
import java.util.List;

public record ExternalTVShowData(
    String originalTitle,
    LocalDate firstAirDate,
    LocalDate lastAirDate,
    List<ExternalTranslationData> translations,
    List<ExternalSeasonData> seasons
) {
}
