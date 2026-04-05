import type { User } from 'firebase/auth';
import { apiRequest } from './client';
import type { Movie, MovieDetail, PageResponse } from '../types/api.types';

export function getMovie(user: User, accountId: string, id: string): Promise<MovieDetail> {
  return apiRequest<MovieDetail>(`/movie/${id}`, user, { accountId });
}

export function listMovies(
  user: User,
  accountId: string,
  page = 0,
  size = 20,
): Promise<PageResponse<Movie>> {
  return apiRequest<PageResponse<Movie>>(
    `/movie?page=${page}&size=${size}`,
    user,
    { accountId },
  );
}
