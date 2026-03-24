package com.rsargsyan.metafilm.main_ctx.core.ports.tvdb;

import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTVShowData;

public interface TvdbTVShowClient {
  ExternalTVShowData fetchTVShow(Long tvdbId);
}
