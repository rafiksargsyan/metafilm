package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Movie;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.time.LocalDate;

public record MovieDTO(String id, String originalTitle, Locale originalLanguage,
                       LocalDate releaseDate, Integer runtime, Long tmdbId, String imdbId) {

  public static MovieDTO from(Movie movie) {
    return new MovieDTO(
        movie.getStrId(),
        movie.getOriginalTitle(),
        movie.getOriginalLanguage(),
        movie.getReleaseDate(),
        movie.getRuntime(),
        movie.getTmdbId(),
        movie.getImdbId()
    );
  }
}
