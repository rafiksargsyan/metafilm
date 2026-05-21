package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Movie;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.time.LocalDate;
import java.util.List;

public record MovieDetailDTO(
    String id,
    String originalTitle,
    Locale originalLanguage,
    LocalDate releaseDate,
    Integer runtime,
    Long tmdbId,
    String imdbId,
    boolean syncInProgress,
    List<TagDTO> tags,
    List<MovieTranslationDTO> translations
) {
  public static MovieDetailDTO from(Movie movie, boolean syncInProgress,
                                    List<TagDTO> tags,
                                    List<MovieTranslationDTO> translations) {
    return new MovieDetailDTO(
        movie.getStrId(),
        movie.getOriginalTitle(),
        movie.getOriginalLanguage(),
        movie.getReleaseDate(),
        movie.getRuntime(),
        movie.getTmdbId(),
        movie.getImdbId(),
        syncInProgress,
        tags,
        translations
    );
  }
}
