package com.rsargsyan.metafilm.main_ctx.core.domain.localentity;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;

import java.io.Serializable;
import java.util.Objects;

public class MovieImageId implements Serializable {
  private Long translation;
  private ImageType type;

  public MovieImageId() {}

  public MovieImageId(Long translation, ImageType type) {
    this.translation = translation;
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MovieImageId that)) return false;
    return Objects.equals(translation, that.translation) && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(translation, type);
  }
}
