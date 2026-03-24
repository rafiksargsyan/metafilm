package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.localentity.MovieImage;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "movie_translation",
    uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "locale"})
)
public class MovieTranslation extends AggregateRoot {

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id", nullable = false)
  private Movie movie;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Locale locale;

  @Getter
  private String title;

  @Getter
  @Column(columnDefinition = "TEXT")
  private String overview;

  @Getter
  private String tagline;

  @OneToMany(mappedBy = "translation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<MovieImage> images = new ArrayList<>();

  @SuppressWarnings("unused")
  MovieTranslation() {}

  public MovieTranslation(Movie movie, Locale locale, String title, String overview, String tagline) {
    this.movie = movie;
    this.locale = locale;
    this.title = title;
    this.overview = overview;
    this.tagline = tagline;
  }

  public void update(String title, String overview, String tagline) {
    this.title = title;
    this.overview = overview;
    this.tagline = tagline;
    touch();
  }

  public MovieImage addImage(ImageType type, String path,
                             ExternalSource externalSource, String externalPath, String blurhash) {
    var image = new MovieImage(this, type, path, externalSource, externalPath, blurhash);
    images.add(image);
    touch();
    return image;
  }

  public void upsertImage(ImageType type, String path,
                          ExternalSource externalSource, String externalPath, String blurhash) {
    images.stream()
        .filter(img -> img.getType() == type)
        .findFirst()
        .ifPresentOrElse(
            img -> img.updatePath(path, externalSource, externalPath, blurhash),
            () -> images.add(new MovieImage(this, type, path, externalSource, externalPath, blurhash))
        );
    touch();
  }
}
