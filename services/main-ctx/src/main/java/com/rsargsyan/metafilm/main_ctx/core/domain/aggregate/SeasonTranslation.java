package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.localentity.SeasonImage;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "season_translation",
    uniqueConstraints = @UniqueConstraint(columnNames = {"season_id", "locale"})
)
public class SeasonTranslation extends AggregateRoot {

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "season_id", nullable = false)
  private Season season;

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
  private List<SeasonImage> images = new ArrayList<>();

  @SuppressWarnings("unused")
  SeasonTranslation() {}

  public SeasonTranslation(Season season, Locale locale, String title, String overview) {
    this.season = season;
    this.locale = locale;
    this.title = title;
    this.overview = overview;
  }

  public void update(String title, String overview) {
    this.title = title;
    this.overview = overview;
    touch();
  }

  public SeasonImage addImage(ImageType type, String path,
                              ExternalSource externalSource, String externalPath) {
    var image = new SeasonImage(this, type, path, externalSource, externalPath);
    images.add(image);
    touch();
    return image;
  }
}
