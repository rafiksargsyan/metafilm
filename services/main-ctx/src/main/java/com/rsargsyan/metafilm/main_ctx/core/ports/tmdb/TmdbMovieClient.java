package com.rsargsyan.metafilm.main_ctx.core.ports.tmdb;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.util.Map;
import java.util.Set;

public interface TmdbMovieClient {
  TmdbMovieData fetchMovie(Long tmdbId);
  Map<Locale, TmdbVideoData> fetchMovieVideos(Long tmdbId, Set<Locale> locales);
}
