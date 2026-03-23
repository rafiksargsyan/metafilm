package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.localentity.TVShowImage;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "tvshow_translation",
    uniqueConstraints = @UniqueConstraint(columnNames = {"tvshow_id", "locale"})
)
public class TVShowTranslation extends AggregateRoot {

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tvshow_id", nullable = false)
  private TVShow tvShow;

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
  private List<TVShowImage> images = new ArrayList<>();

  @SuppressWarnings("unused")
  TVShowTranslation() {}

  public TVShowTranslation(TVShow tvShow, Locale locale, String title, String overview, String tagline) {
    this.tvShow = tvShow;
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

  public TVShowImage addImage(ImageType type, String path,
                              ExternalSource externalSource, String externalPath) {
    var image = new TVShowImage(this, type, path, externalSource, externalPath);
    images.add(image);
    touch();
    return image;
  }
}
