import type { User } from 'firebase/auth';
import { apiRequest } from './client';
import type { Page, Season, SeasonDetail, TVShow } from '../types';

export function listTVShows(user: User, page = 0, size = 20): Promise<Page<TVShow>> {
  return apiRequest(`/tvshow?page=${page}&size=${size}`, user);
}

export function getTVShow(user: User, id: string): Promise<TVShow> {
  return apiRequest(`/tvshow/${id}`, user);
}

export function createTVShow(
  user: User,
  data: { originalTitle: string; originalLocale: string; firstAirDate: string | null },
): Promise<TVShow> {
  return apiRequest('/tvshow', user, { method: 'POST', body: JSON.stringify(data) });
}

export function setTVShowTmdbId(user: User, id: string, tmdbId: number): Promise<TVShow> {
  return apiRequest(`/tvshow/${id}/tmdb-id`, user, {
    method: 'PATCH',
    body: JSON.stringify(tmdbId),
  });
}

export function setTVShowTvdbId(user: User, id: string, tvdbId: number): Promise<TVShow> {
  return apiRequest(`/tvshow/${id}/tvdb-id`, user, {
    method: 'PATCH',
    body: JSON.stringify(tvdbId),
  });
}

export function setTVShowUseTvdb(user: User, id: string, useTvdb: boolean): Promise<TVShow> {
  return apiRequest(`/tvshow/${id}/use-tvdb`, user, {
    method: 'PATCH',
    body: JSON.stringify(useTvdb),
  });
}

export function listSeasons(user: User, tvShowId: string): Promise<Season[]> {
  return apiRequest(`/tvshow/${tvShowId}/season`, user);
}

export function getSeason(user: User, tvShowId: string, seasonId: string): Promise<SeasonDetail> {
  return apiRequest(`/tvshow/${tvShowId}/season/${seasonId}`, user);
}
