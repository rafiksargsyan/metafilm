package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.time.LocalDate;

public record TVShowDTO(String id, String originalTitle, Locale originalLanguage,
                        LocalDate firstAirDate, LocalDate lastAirDate,
                        Long tmdbId, String imdbId, Long tvdbId, boolean useTvdb) {

  public static TVShowDTO from(TVShow tvShow) {
    return new TVShowDTO(
        tvShow.getStrId(),
        tvShow.getOriginalTitle(),
        tvShow.getOriginalLanguage(),
        tvShow.getFirstAirDate(),
        tvShow.getLastAirDate(),
        tvShow.getTmdbId(),
        tvShow.getImdbId(),
        tvShow.getTvdbId(),
        tvShow.isUseTvdb()
    );
  }
}
