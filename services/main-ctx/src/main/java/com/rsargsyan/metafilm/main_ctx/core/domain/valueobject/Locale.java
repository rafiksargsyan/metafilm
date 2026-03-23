package com.rsargsyan.metafilm.main_ctx.core.domain.valueobject;

public enum Locale {
  EN_US("en-US"),
  EN_GB("en-GB"),
  HY_AM("hy-AM"),
  RU_RU("ru-RU"),
  FR_FR("fr-FR"),
  DE_DE("de-DE"),
  ES_ES("es-ES"),
  ES_MX("es-MX"),
  IT_IT("it-IT"),
  PT_BR("pt-BR");

  private final String tag;

  Locale(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }
}
