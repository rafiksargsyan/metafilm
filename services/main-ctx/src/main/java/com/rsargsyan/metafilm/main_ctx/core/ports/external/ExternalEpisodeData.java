package com.rsargsyan.metafilm.main_ctx.core.ports.external;

import java.time.LocalDate;
import java.util.List;

public record ExternalEpisodeData(
    Integer seasonNumber,
    Integer episodeNumber,
    Integer absoluteNumber,  // nullable
    LocalDate airDate,
    Integer runtime,
    String stillPath,        // nullable — not locale-specific
    List<ExternalTranslationData> translations
) {
}
