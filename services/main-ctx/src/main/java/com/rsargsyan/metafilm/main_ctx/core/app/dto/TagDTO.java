package com.rsargsyan.metafilm.main_ctx.core.app.dto;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Tag;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;

import java.util.Map;
import java.util.stream.Collectors;

public record TagDTO(String id, String key, String name, Map<String, String> localizations) {
  public static TagDTO from(Tag tag) {
    Map<String, String> localizations = tag.getLocalizations().entrySet().stream()
        .collect(Collectors.toMap(
            e -> Locale.valueOf(e.getKey()).getTag(),
            Map.Entry::getValue
        ));
    return new TagDTO(tag.getStrId(), tag.getKey(), tag.getName(), localizations);
  }
}
