package com.rsargsyan.metafilm.main_ctx.core.domain.valueobject;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class NameConverter implements AttributeConverter<FullName, String> {
  @Override
  public String convertToDatabaseColumn(FullName attribute) {
    return attribute == null ? null : attribute.value();
  }

  @Override
  public FullName convertToEntityAttribute(String dbData) {
    return dbData == null ? null : new FullName(dbData);
  }
}
