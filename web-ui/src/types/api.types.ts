export interface UserDTO {
  id: string;
  accountId: string;
  fullName: string | null;
}

export interface Movie {
  id: string;
  originalTitle: string;
  originalLanguage: string;
  releaseDate: string | null;
  runtime: number | null;
  tmdbId: number | null;
  imdbId: string | null;
}

export interface MovieImage {
  type: 'POSTER' | 'BACKDROP';
  url: string;
  blurhash: string | null;
}

export interface MovieTrailer {
  site: string;
  key: string;
}

export interface MovieTranslation {
  id: string;
  locale: string;
  title: string | null;
  overview: string | null;
  tagline: string | null;
  images: MovieImage[];
  trailer: MovieTrailer | null;
}

export interface Tag {
  id: string;
  key: string;
  name: string;
  localizations: Record<string, string>;
}

export interface MovieDetail extends Movie {
  voteAverage: number | null;
  syncInProgress: boolean;
  tags: Tag[];
  translations: MovieTranslation[];
}

export interface TVShow {
  id: string;
  originalTitle: string;
  originalLanguage: string;
  firstAirDate: string | null;
  lastAirDate: string | null;
  tmdbId: number | null;
  imdbId: string | null;
  tvdbId: number | null;
  useTvdb: boolean;
}

export interface TVShowImage {
  type: 'POSTER' | 'BACKDROP';
  url: string | null;
  blurhash: string | null;
}

export interface TVShowTrailer {
  site: string;
  key: string;
}

export interface TVShowTranslation {
  id: string;
  locale: string;
  title: string | null;
  overview: string | null;
  tagline: string | null;
  images: TVShowImage[];
  trailer: TVShowTrailer | null;
}

export interface TVShowDetail extends TVShow {
  voteAverage: number | null;
  tags: Tag[];
  translations: TVShowTranslation[];
}

export interface Season {
  id: string;
  tvShowId: string;
  seasonNumber: number;
  originalName: string | null;
  airDate: string | null;
}

export interface SeasonImage {
  type: 'POSTER';
  url: string | null;
  blurhash: string | null;
}

export interface SeasonTranslation {
  id: string;
  locale: string;
  title: string | null;
  overview: string | null;
  images: SeasonImage[];
}

export interface Episode {
  id: string;
  tvShowId: string;
  seasonNumber: number | null;
  episodeNumber: number | null;
  absoluteNumber: number | null;
  airDate: string | null;
  runtime: number | null;
}

export interface SeasonDetail extends Season {
  episodes: Episode[];
  translations: SeasonTranslation[];
}

export interface EpisodeImage {
  type: 'STILL';
  url: string | null;
  blurhash: string | null;
}

export interface EpisodeTranslation {
  id: string;
  locale: string;
  title: string | null;
  overview: string | null;
  images: EpisodeImage[];
}

export interface EpisodeDetail extends Episode {
  translations: EpisodeTranslation[];
}

export interface ApiKey {
  id: string;
  key: string | null;
  description: string;
  disabled: boolean;
  lastAccessTime: string | null;
}

export interface PageResponse<T> {
  content: T[];
  page: {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
  };
}
