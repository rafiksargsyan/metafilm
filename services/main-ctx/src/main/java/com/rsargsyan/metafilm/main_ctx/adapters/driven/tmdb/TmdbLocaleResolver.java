package com.rsargsyan.metafilm.main_ctx.adapters.driven.tmdb;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Resolves TMDB locale codes to our Locale enum.
 * TMDB uses 2-part tags (e.g. "zh-CN") for locales that require a script subtag
 * in standard BCP 47 (e.g. "zh-Hans-CN"). TMDB_LOCALE_MAP bridges the gap.
 */
class TmdbLocaleResolver {

  static final Map<String, Locale> TMDB_LOCALE_MAP = Map.of(
      "zh-CN", Locale.ZH_HANS_CN,
      "zh-TW", Locale.ZH_HANT_TW,
      "zh-HK", Locale.ZH_HANT_HK
  );

  /**
   * Resolves a TMDB language+country pair to our Locale enum.
   * Checks the override map first, then exact BCP 47 tag, then language-only fallback.
   */
  static Optional<Locale> resolveLocale(String languageCode, String countryCode) {
    if (languageCode == null) return Optional.empty();
    if (countryCode != null && !countryCode.isBlank()) {
      String tmdbTag = languageCode + "-" + countryCode;
      Locale override = TMDB_LOCALE_MAP.get(tmdbTag);
      if (override != null) return Optional.of(override);
      return Locale.fromTag(tmdbTag);
    }
    return Locale.fromLanguageCode(languageCode);
  }

  /**
   * Resolves original language using the language code and a list of country codes
   * (e.g. production_countries for movies, origin_country for TV shows).
   */
  static Optional<Locale> resolveFromLanguageAndCountries(String languageCode,
                                                           List<String> countryCodes) {
    if (languageCode == null) return Optional.empty();
    for (String country : countryCodes) {
      Optional<Locale> locale = resolveLocale(languageCode, country);
      if (locale.isPresent()) return locale;
    }
    return Locale.fromLanguageCode(languageCode);
  }

  /** Returns the file path of the highest-voted image matching the given language code.
   *  Pass null to match language-neutral images (iso_639_1 == null). */
  static <T extends TmdbImage> String bestImage(List<T> images, String languageCode) {
    return images.stream()
        .filter(img -> languageCode == null
            ? img.languageCode() == null
            : languageCode.equals(img.languageCode()))
        .max(java.util.Comparator.comparingDouble(
            img -> img.voteAverage() != null ? img.voteAverage() : 0))
        .map(TmdbImage::filePath)
        .orElse(null);
  }

  interface TmdbImage {
    String filePath();
    String languageCode();
    Double voteAverage();
  }

  private TmdbLocaleResolver() {}
}
