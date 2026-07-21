package com.rsargsyan.metafilm.main_ctx.core.ports.tmdb;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTranslationData;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTVShowData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TmdbTVShowClient {
  ExternalTVShowData fetchTVShow(Long tmdbId);
  List<Integer> fetchTVShowGenreIds(Long tmdbId);
  List<ExternalTranslationData> fetchEpisodeTranslations(Long tvShowTmdbId, Integer seasonNumber, Integer episodeNumber);
  Map<Locale, TmdbVideoData> fetchTVShowVideos(Long tmdbId, Set<Locale> locales);
}
