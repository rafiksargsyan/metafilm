export interface Page<T> {
  content: T[];
  page: {
    totalElements: number;
    totalPages: number;
    number: number;
    size: number;
  };
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
  url: string | null;
  blurhash: string | null;
  externalSource: string | null;
  externalPath: string | null;
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
  tvdbId: number | null;
  useTvdb: boolean;
}

export interface Season {
  id: string;
  tvShowId: string;
  seasonNumber: number;
  originalName: string | null;
  airDate: string | null;
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
}

export const LOCALES: { value: string; label: string }[] = [
  { value: 'EN_US', label: 'English (US)' },
  { value: 'EN_GB', label: 'English (GB)' },
  { value: 'EN_AU', label: 'English (AU)' },
  { value: 'FR_FR', label: 'French' },
  { value: 'DE_DE', label: 'German' },
  { value: 'ES_ES', label: 'Spanish (Spain)' },
  { value: 'ES_MX', label: 'Spanish (Mexico)' },
  { value: 'IT_IT', label: 'Italian' },
  { value: 'PT_PT', label: 'Portuguese (Portugal)' },
  { value: 'PT_BR', label: 'Portuguese (Brazil)' },
  { value: 'RU_RU', label: 'Russian' },
  { value: 'JA_JP', label: 'Japanese' },
  { value: 'KO_KR', label: 'Korean' },
  { value: 'ZH_HANS_CN', label: 'Chinese (Simplified)' },
  { value: 'ZH_HANT_TW', label: 'Chinese (Traditional, TW)' },
  { value: 'ZH_HANT_HK', label: 'Chinese (Traditional, HK)' },
  { value: 'AR_SA', label: 'Arabic' },
  { value: 'HI_IN', label: 'Hindi' },
  { value: 'TR_TR', label: 'Turkish' },
  { value: 'PL_PL', label: 'Polish' },
  { value: 'NL_NL', label: 'Dutch' },
  { value: 'SV_SE', label: 'Swedish' },
  { value: 'HY_AM', label: 'Armenian' },
];
