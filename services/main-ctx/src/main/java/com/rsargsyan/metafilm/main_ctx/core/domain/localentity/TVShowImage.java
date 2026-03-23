package com.rsargsyan.metafilm.main_ctx.core.domain.localentity;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShowTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.exception.InvalidMovieImageException;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@IdClass(TVShowImageId.class)
@Table(name = "tvshow_image")
public class TVShowImage {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "translation_id", nullable = false)
  private TVShowTranslation translation;

  @Id
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ImageType type;

  @Getter
  @Column(nullable = false)
  private String path;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ExternalSource externalSource;

  @Getter
  private String externalPath;

  @SuppressWarnings("unused")
  TVShowImage() {}

  public TVShowImage(TVShowTranslation translation, ImageType type, String path,
                     ExternalSource externalSource, String externalPath) {
    if (path == null || path.isBlank()) throw new InvalidMovieImageException("path is required");
    this.translation = translation;
    this.type = type;
    this.path = path;
    this.externalSource = externalSource;
    this.externalPath = externalPath;
  }
}
