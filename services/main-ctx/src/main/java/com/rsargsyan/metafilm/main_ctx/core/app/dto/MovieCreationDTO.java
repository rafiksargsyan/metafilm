package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.time.LocalDate;

public record MovieCreationDTO(String originalTitle, Locale originalLocale, LocalDate releaseDate) {
}
