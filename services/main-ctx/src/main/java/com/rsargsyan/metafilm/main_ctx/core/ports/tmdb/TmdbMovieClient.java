package com.rsargsyan.metafilm.main_ctx.core.ports.tmdb;

public interface TmdbMovieClient {
  TmdbMovieData fetchMovie(Long tmdbId);
}
