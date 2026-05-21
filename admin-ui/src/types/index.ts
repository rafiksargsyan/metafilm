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

export interface Tag {
  id: string;
  key: string;
  name: string;
  localizations: Record<string, string>;
}

export interface MovieDetail extends Movie {
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
  externalSource: string | null;
  externalPath: string | null;
}

export interface TVShowTranslation {
  id: string;
  locale: string;
  title: string | null;
  overview: string | null;
  tagline: string | null;
  images: TVShowImage[];
}

export interface TVShowDetail extends TVShow {
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
  externalSource: string | null;
  externalPath: string | null;
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

export interface EpisodeImage {
  type: 'STILL';
  url: string | null;
  blurhash: string | null;
  externalSource: string | null;
  externalPath: string | null;
}

export interface EpisodeTranslation {
  id: string;
  locale: string;
  title: string | null;
  overview: string | null;
  images: EpisodeImage[];
}

export interface SeasonDetail extends Season {
  episodes: Episode[];
  translations: SeasonTranslation[];
}

export interface EpisodeDetail extends Episode {
  translations: EpisodeTranslation[];
}

export const LOCALES: { value: string; label: string }[] = [
  { value: 'EN', label: 'English' },
  { value: 'EN_US', label: 'English (US)' },
  { value: 'EN_GB', label: 'English (GB)' },
  { value: 'EN_AU', label: 'English (AU)' },
  { value: 'FR', label: 'French' },
  { value: 'FR_FR', label: 'French (France)' },
  { value: 'FR_CA', label: 'French (Canada)' },
  { value: 'DE', label: 'German' },
  { value: 'ES', label: 'Spanish' },
  { value: 'ES_ES', label: 'Spanish (Spain)' },
  { value: 'ES_419', label: 'Spanish (Latin American)' },
  { value: 'IT', label: 'Italian' },
  { value: 'PT', label: 'Portuguese' },
  { value: 'PT_BR', label: 'Portuguese (Brazil)' },
  { value: 'PT_PT', label: 'Portuguese (Portugal)' },
  { value: 'RU', label: 'Russian' },
  { value: 'JA', label: 'Japanese' },
  { value: 'KO', label: 'Korean' },
  { value: 'ZH', label: 'Chinese' },
  { value: 'ZH_HANS_CN', label: 'Chinese (Simplified)' },
  { value: 'ZH_HANT_TW', label: 'Chinese (Traditional, TW)' },
  { value: 'ZH_HANT_HK', label: 'Chinese (Traditional, HK)' },
  { value: 'AR', label: 'Arabic' },
  { value: 'HI', label: 'Hindi' },
  { value: 'TR', label: 'Turkish' },
  { value: 'PL', label: 'Polish' },
  { value: 'NL', label: 'Dutch' },
  { value: 'NL_NL', label: 'Dutch (Netherlands)' },
  { value: 'NL_BE', label: 'Dutch (Belgium)' },
  { value: 'SV', label: 'Swedish' },
  { value: 'HY', label: 'Armenian' },
  { value: 'UK', label: 'Ukrainian' },
];
