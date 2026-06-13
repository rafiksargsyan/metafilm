package com.rsargsyan.metafilm.main_ctx.core.domain.localentity;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.MovieTranslation;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "movie_trailer")
public class MovieTrailer {

  @Id
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "translation_id")
  private MovieTranslation translation;

  @Getter
  @Column(nullable = false)
  private String site;

  @Getter
  @Column(nullable = false)
  private String key;

  @SuppressWarnings("unused")
  MovieTrailer() {}

  public MovieTrailer(MovieTranslation translation, String site, String key) {
    this.translation = translation;
    this.site = site;
    this.key = key;
  }

  public void update(String site, String key) {
    this.site = site;
    this.key = key;
  }
}
