import type { User } from 'firebase/auth';
import { apiRequest } from './client';
import type { TVShow, PageResponse } from '../types/api.types';

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
