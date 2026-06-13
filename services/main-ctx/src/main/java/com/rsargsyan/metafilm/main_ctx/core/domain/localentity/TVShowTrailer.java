package com.rsargsyan.metafilm.main_ctx.core.domain.localentity;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShowTranslation;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tvshow_trailer")
public class TVShowTrailer {

  @Id
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "translation_id")
  private TVShowTranslation translation;

  @Getter
  @Column(nullable = false)
  private String site;

  @Getter
  @Column(nullable = false)
  private String key;

  @SuppressWarnings("unused")
  TVShowTrailer() {}

  public TVShowTrailer(TVShowTranslation translation, String site, String key) {
    this.translation = translation;
    this.site = site;
    this.key = key;
  }

  public void update(String site, String key) {
    this.site = site;
    this.key = key;
  }
}
