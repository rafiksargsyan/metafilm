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

export interface MovieTranslation {
  id: string;
  locale: string;
  title: string | null;
  overview: string | null;
  tagline: string | null;
  images: MovieImage[];
}

export interface MovieDetail extends Movie {
  syncInProgress: boolean;
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
