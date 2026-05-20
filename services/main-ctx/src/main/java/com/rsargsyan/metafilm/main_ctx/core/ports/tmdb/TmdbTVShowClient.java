package com.rsargsyan.metafilm.main_ctx.core.ports.tmdb;

import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTranslationData;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTVShowData;

import java.util.List;

public interface TmdbTVShowClient {
  ExternalTVShowData fetchTVShow(Long tmdbId);
  List<ExternalTranslationData> fetchEpisodeTranslations(Long tvShowTmdbId, Integer seasonNumber, Integer episodeNumber);
}
