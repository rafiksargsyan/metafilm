import type { User } from 'firebase/auth';
import { apiRequest } from './client';
import type { TVShow, TVShowDetail, Season, SeasonDetail, EpisodeDetail, PageResponse } from '../types/api.types';

export function listTVShows(
  user: User,
  accountId: string,
  page = 0,
  size = 20,
): Promise<PageResponse<TVShow>> {
  return apiRequest<PageResponse<TVShow>>(
    `/tvshow?page=${page}&size=${size}`,
    user,
    { accountId },
  );
}

export function getTVShow(user: User, accountId: string, id: string): Promise<TVShowDetail> {
  return apiRequest<TVShowDetail>(`/tvshow/${id}`, user, { accountId });
}

export function listSeasons(user: User, accountId: string, tvShowId: string): Promise<Season[]> {
  return apiRequest<Season[]>(`/tvshow/${tvShowId}/season`, user, { accountId });
}

export function getSeason(user: User, accountId: string, tvShowId: string, seasonId: string): Promise<SeasonDetail> {
  return apiRequest<SeasonDetail>(`/tvshow/${tvShowId}/season/${seasonId}`, user, { accountId });
}

export function getEpisode(user: User, accountId: string, tvShowId: string, episodeId: string): Promise<EpisodeDetail> {
  return apiRequest<EpisodeDetail>(`/tvshow/${tvShowId}/episode/${episodeId}`, user, { accountId });
}
