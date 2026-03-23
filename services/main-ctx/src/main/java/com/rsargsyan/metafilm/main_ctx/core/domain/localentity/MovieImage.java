package com.rsargsyan.metafilm.main_ctx.core.domain.localentity;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.MovieTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.exception.InvalidMovieImageException;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@IdClass(MovieImageId.class)
@Table(name = "movie_image")
public class MovieImage {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "translation_id", nullable = false)
  private MovieTranslation translation;

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
  private ExternalSource externalSource; // nullable

  @Getter
  private String externalPath; // nullable

  @SuppressWarnings("unused")
  MovieImage() {}

  public MovieImage(MovieTranslation translation, ImageType type, String path,
                    ExternalSource externalSource, String externalPath) {
    if (path == null || path.isBlank()) throw new InvalidMovieImageException("path is required");
    this.translation = translation;
    this.type = type;
    this.path = path;
    this.externalSource = externalSource;
    this.externalPath = externalPath;
  }
}
