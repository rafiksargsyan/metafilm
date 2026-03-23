package com.rsargsyan.metafilm.main_ctx.core.domain.valueobject;

import com.rsargsyan.metafilm.main_ctx.core.exception.InvalidFullNameException;

public record FullName(String value) {
  public static final int MAX_LENGTH = 100;

  public FullName {
    if (value != null && value.length() > MAX_LENGTH) {
      throw new InvalidFullNameException("Full name must not exceed " + MAX_LENGTH + " characters");
    }
  }

  public static FullName fromString(String value) {
    if (value == null || value.isBlank()) return null;
    return new FullName(value.trim());
  }

  @Override
  public String toString() {
    return value;
  }
}
