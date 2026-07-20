package com.rsargsyan.metafilm.main_ctx.core.ports.external;

import java.time.LocalDate;
import java.util.List;

public record ExternalSeasonData(
    Integer seasonNumber,
    String originalName,
    String originalOverview,
    LocalDate airDate,
    List<ExternalTranslationData> translations,
    List<ExternalEpisodeData> episodes
) {
}
