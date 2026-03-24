package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Episode;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Season;

import java.time.LocalDate;
import java.util.List;

public record SeasonDetailDTO(String id, String tvShowId, Integer seasonNumber,
                               String originalName, LocalDate airDate,
                               List<EpisodeDTO> episodes) {

  public static SeasonDetailDTO from(Season season, List<Episode> episodes) {
    return new SeasonDetailDTO(
        season.getStrId(),
        season.getTvShow().getStrId(),
        season.getSeasonNumber(),
        season.getOriginalName(),
        season.getAirDate(),
        episodes.stream().map(EpisodeDTO::from).toList()
    );
  }
}
