package com.rsargsyan.metafilm.main_ctx.adapters.driven.tvdb;

import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTVShowData;
import com.rsargsyan.metafilm.main_ctx.core.ports.tvdb.TvdbTVShowClient;
import org.springframework.stereotype.Component;

@Component
public class TvdbTVShowClientImpl implements TvdbTVShowClient {

  @Override
  public ExternalTVShowData fetchTVShow(Long tvdbId) {
    // TODO: implement TVDB v4 API integration
    // TVDB requires OAuth2 token exchange (POST /login with apikey) before making requests
    throw new UnsupportedOperationException("TVDB sync not yet implemented");
  }
}
