import type { User } from 'firebase/auth';
import { apiRequest } from './client';
import type { Tag } from '../types';

export function listTags(user: User): Promise<Tag[]> {
  return apiRequest('/tag', user);
}

export function createTag(
  user: User,
  data: { key: string; name: string },
): Promise<Tag> {
  return apiRequest('/tag', user, { method: 'POST', body: JSON.stringify(data) });
}

export function updateTag(
  user: User,
  tagId: string,
  data: { name: string },
): Promise<Tag> {
  return apiRequest(`/tag/${tagId}`, user, { method: 'PATCH', body: JSON.stringify(data) });
}

export function deleteTag(user: User, tagId: string): Promise<void> {
  return apiRequest(`/tag/${tagId}`, user, { method: 'DELETE' });
}

export function addTagToMovie(user: User, movieId: string, tagId: string): Promise<void> {
  return apiRequest(`/movie/${movieId}/tag/${tagId}`, user, { method: 'POST' });
}

export function removeTagFromMovie(user: User, movieId: string, tagId: string): Promise<void> {
  return apiRequest(`/movie/${movieId}/tag/${tagId}`, user, { method: 'DELETE' });
}

export function addTagToTVShow(user: User, tvShowId: string, tagId: string): Promise<void> {
  return apiRequest(`/tvshow/${tvShowId}/tag/${tagId}`, user, { method: 'POST' });
}

export function removeTagFromTVShow(user: User, tvShowId: string, tagId: string): Promise<void> {
  return apiRequest(`/tvshow/${tvShowId}/tag/${tagId}`, user, { method: 'DELETE' });
}
