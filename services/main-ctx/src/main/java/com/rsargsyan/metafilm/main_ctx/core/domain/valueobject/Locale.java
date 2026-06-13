package com.rsargsyan.metafilm.main_ctx.core.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum Locale {
  EN("en", "en", "English"),
  EN_US("en-US", "en", "English (US)"),
  EN_GB("en-GB", "en", "English (GB)"),
  EN_AU("en-AU", "en", "English (AU)"),
  RU("ru", "ru", "Russian"),
  FR("fr", "fr", "French"),
  FR_FR("fr-FR", "fr", "French (France)"),
  FR_CA("fr-CA", "fr", "French (Canada)"),
  DE("de", "de", "German"),
  ES("es", "es", "Spanish"),
  ES_ES("es-ES", "es", "Spanish (Spain)"),
  ES_419("es-419", "es", "Spanish (Latin American)"),
  IT("it", "it", "Italian"),
  PT("pt", "pt", "Portuguese"),
  PT_BR("pt-BR", "pt", "Portuguese (Brazil)"),
  PT_PT("pt-PT", "pt", "Portuguese (Portugal)"),
  JA("ja", "ja", "Japanese"),
  KO("ko", "ko", "Korean"),
  ZH("zh", "zh", "Chinese"),
  ZH_HANS_CN("zh-Hans-CN", "zh", "Chinese (Simplified)"),
  ZH_HANT_TW("zh-Hant-TW", "zh", "Chinese (Traditional)"),
  ZH_HANT_HK("zh-Hant-HK", "zh", "Chinese (HK)"),
  HI("hi", "hi", "Hindi"),
  BN("bn", "bn", "Bengali"),
  TR("tr", "tr", "Turkish"),
  EL("el", "el", "Greek"),
  TH("th", "th", "Thai"),
  ID("id", "id", "Indonesian"),
  MS("ms", "ms", "Malay"),
  VI("vi", "vi", "Vietnamese"),
  DA("da", "da", "Danish"),
  SV("sv", "sv", "Swedish"),
  NB("nb", "nb", "Norwegian Bokmål"),
  NL("nl", "nl", "Dutch"),
  NL_NL("nl-NL", "nl", "Dutch (Netherlands)"),
  NL_BE("nl-BE", "nl", "Dutch (Belgium)"),
  PL("pl", "pl", "Polish"),
  UK("uk", "uk", "Ukrainian"),
  BE("be", "be", "Belarusian"),
  BG("bg", "bg", "Bulgarian"),
  CS("cs", "cs", "Czech"),
  SK("sk", "sk", "Slovak"),
  SL("sl", "sl", "Slovenian"),
  HR("hr", "hr", "Croatian"),
  SR("sr", "sr", "Serbian"),
  MK("mk", "mk", "Macedonian"),
  HU("hu", "hu", "Hungarian"),
  RO("ro", "ro", "Romanian"),
  ET("et", "et", "Estonian"),
  LV("lv", "lv", "Latvian"),
  LT("lt", "lt", "Lithuanian"),
  FI("fi", "fi", "Finnish"),
  HY("hy", "hy", "Armenian"),
  FA("fa", "fa", "Farsi"),
  AR("ar", "ar", "Arabic"),
  HE("he", "he", "Hebrew"),
  TA("ta", "ta", "Tamil"),
  TE("te", "te", "Telugu"),
  ML("ml", "ml", "Malayalam"),
  UR("ur", "ur", "Urdu"),
  TL("tl", "tl", "Filipino"),
  BS("bs", "bs", "Bosnian"),
  SQ("sq", "sq", "Albanian"),
  AZ("az", "az", "Azerbaijani"),
  KA("ka", "ka", "Georgian"),
  MYN("myn", "myn", "Mayan");

  private final String tag;
  private final String lang;
  private final String displayName;

  Locale(String tag, String lang, String displayName) {
    this.tag = tag;
    this.lang = lang;
    this.displayName = displayName;
  }

  @JsonValue
  public String getTag() { return tag; }

  public String getLang() { return lang; }

  public String getDisplayName() { return displayName; }

  @JsonCreator
  public static Locale fromJson(String value) {
    if (value == null) return null;
    for (Locale l : values()) {
      if (l.tag.equalsIgnoreCase(value) || l.name().equalsIgnoreCase(value)) return l;
    }
    return null;
  }

  public static Optional<Locale> fromTag(String tag) {
    if (tag == null) return Optional.empty();
    for (Locale l : values()) {
      if (l.tag.equalsIgnoreCase(tag) || l.name().equalsIgnoreCase(tag)) return Optional.of(l);
    }
    return Optional.empty();
  }

  public static Optional<Locale> fromLanguageCode(String iso639) {
    if (iso639 == null) return Optional.empty();
    return switch (iso639.toLowerCase()) {
      case "en" -> Optional.of(EN);
      case "ru" -> Optional.of(RU);
      case "fr" -> Optional.of(FR);
      case "de" -> Optional.of(DE);
      case "es" -> Optional.of(ES);
      case "it" -> Optional.of(IT);
      case "pt" -> Optional.of(PT);
      case "ja" -> Optional.of(JA);
      case "ko" -> Optional.of(KO);
      case "zh" -> Optional.of(ZH);
      case "hi" -> Optional.of(HI);
      case "bn" -> Optional.of(BN);
      case "tr" -> Optional.of(TR);
      case "el" -> Optional.of(EL);
      case "th" -> Optional.of(TH);
      case "id" -> Optional.of(ID);
      case "ms" -> Optional.of(MS);
      case "vi" -> Optional.of(VI);
      case "da" -> Optional.of(DA);
      case "sv" -> Optional.of(SV);
      case "nb" -> Optional.of(NB);
      case "nl" -> Optional.of(NL);
      case "pl" -> Optional.of(PL);
      case "uk" -> Optional.of(UK);
      case "be" -> Optional.of(BE);
      case "bg" -> Optional.of(BG);
      case "cs" -> Optional.of(CS);
      case "sk" -> Optional.of(SK);
      case "sl" -> Optional.of(SL);
      case "hr" -> Optional.of(HR);
      case "sr" -> Optional.of(SR);
      case "mk" -> Optional.of(MK);
      case "hu" -> Optional.of(HU);
      case "ro" -> Optional.of(RO);
      case "et" -> Optional.of(ET);
      case "lv" -> Optional.of(LV);
      case "lt" -> Optional.of(LT);
      case "fi" -> Optional.of(FI);
      case "hy" -> Optional.of(HY);
      case "fa" -> Optional.of(FA);
      case "ar" -> Optional.of(AR);
      case "he" -> Optional.of(HE);
      case "ta" -> Optional.of(TA);
      case "te" -> Optional.of(TE);
      case "ml" -> Optional.of(ML);
      case "ur" -> Optional.of(UR);
      case "tl" -> Optional.of(TL);
      case "bs" -> Optional.of(BS);
      case "sq" -> Optional.of(SQ);
      case "az" -> Optional.of(AZ);
      case "ka" -> Optional.of(KA);
      case "myn" -> Optional.of(MYN);
      default -> Optional.empty();
    };
  }
}
