package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Episode;

import java.time.LocalDate;
import java.util.List;

public record EpisodeDetailDTO(
    String id,
    String tvShowId,
    Integer seasonNumber,
    Integer episodeNumber,
    Integer absoluteNumber,
    LocalDate airDate,
    Integer runtime,
    List<EpisodeTranslationDTO> translations
) {
  public static EpisodeDetailDTO from(Episode episode, List<EpisodeTranslationDTO> translations) {
    return new EpisodeDetailDTO(
        episode.getStrId(),
        episode.getTvShow().getStrId(),
        episode.getSeasonNumber(),
        episode.getEpisodeNumber(),
        episode.getAbsoluteNumber(),
        episode.getAirDate(),
        episode.getRuntime(),
        translations
    );
  }
}
