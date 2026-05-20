package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.util.List;

public record SeasonTranslationDTO(
    String id,
    Locale locale,
    String title,
    String overview,
    List<SeasonImageDTO> images
) {}
