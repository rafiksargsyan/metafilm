package com.rsargsyan.metafilm.main_ctx.core.ports.tmdb;

import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTVShowData;

public interface TmdbTVShowClient {
  ExternalTVShowData fetchTVShow(Long tmdbId);
}
