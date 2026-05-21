package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import java.util.Map;

public record TagCreationDTO(String key, String name, Map<String, String> localizations) {}
