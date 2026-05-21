package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import java.util.Map;

public record TagUpdateDTO(String name, Map<String, String> localizations) {}
