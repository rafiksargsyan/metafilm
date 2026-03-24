package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.localentity.EpisodeImage;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "episode_translation",
    uniqueConstraints = @UniqueConstraint(columnNames = {"episode_id", "locale"})
)
public class EpisodeTranslation extends AggregateRoot {

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "episode_id", nullable = false)
  private Episode episode;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Locale locale;

  @Getter
  private String title;

  @Getter
  @Column(columnDefinition = "TEXT")
  private String overview;

  @OneToMany(mappedBy = "translation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<EpisodeImage> images = new ArrayList<>();

  @SuppressWarnings("unused")
  EpisodeTranslation() {}

  public EpisodeTranslation(Episode episode, Locale locale, String title, String overview) {
    this.episode = episode;
    this.locale = locale;
    this.title = title;
    this.overview = overview;
  }

  public void update(String title, String overview) {
    this.title = title;
    this.overview = overview;
    touch();
  }

  public EpisodeImage addImage(ImageType type, String path,
                               ExternalSource externalSource, String externalPath, String blurhash) {
    var image = new EpisodeImage(this, type, path, externalSource, externalPath, blurhash);
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
            () -> images.add(new EpisodeImage(this, type, path, externalSource, externalPath, blurhash))
        );
    touch();
  }
}
