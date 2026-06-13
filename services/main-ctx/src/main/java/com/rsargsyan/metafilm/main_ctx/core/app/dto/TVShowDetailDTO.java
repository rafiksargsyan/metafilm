package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.time.LocalDate;
import java.util.List;

public record TVShowDetailDTO(
    String id,
    String originalTitle,
    Locale originalLanguage,
    LocalDate firstAirDate,
    LocalDate lastAirDate,
    Long tmdbId,
    String imdbId,
    Long tvdbId,
    boolean useTvdb,
    Double voteAverage,
    List<TagDTO> tags,
    List<TVShowTranslationDTO> translations
) {
  public static TVShowDetailDTO from(TVShow tvShow, List<TagDTO> tags,
                                     List<TVShowTranslationDTO> translations) {
    return new TVShowDetailDTO(
        tvShow.getStrId(),
        tvShow.getOriginalTitle(),
        tvShow.getOriginalLanguage(),
        tvShow.getFirstAirDate(),
        tvShow.getLastAirDate(),
        tvShow.getTmdbId(),
        tvShow.getImdbId(),
        tvShow.getTvdbId(),
        tvShow.isUseTvdb(),
        tvShow.getVoteAverage(),
        tags,
        translations
    );
  }
}
