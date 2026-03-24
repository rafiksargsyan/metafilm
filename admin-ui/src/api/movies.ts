import type { User } from 'firebase/auth';
import { apiRequest } from './client';
import type { Movie, Page } from '../types';

export function listMovies(user: User, page = 0, size = 20): Promise<Page<Movie>> {
  return apiRequest(`/movie?page=${page}&size=${size}`, user);
}

export function getMovie(user: User, id: string): Promise<Movie> {
  return apiRequest(`/movie/${id}`, user);
}

export function createMovie(
  user: User,
  data: { originalTitle: string; originalLocale: string; releaseDate: string | null },
): Promise<Movie> {
  return apiRequest('/movie', user, { method: 'POST', body: JSON.stringify(data) });
}

export function setMovieTmdbId(user: User, id: string, tmdbId: number): Promise<Movie> {
  return apiRequest(`/movie/${id}/tmdb-id`, user, {
    method: 'PATCH',
    body: JSON.stringify(tmdbId),
  });
}
