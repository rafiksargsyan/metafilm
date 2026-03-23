package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
public class Movie extends AggregateRoot {

  @Getter
  @Column(nullable = false)
  private String originalTitle;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Locale originalLanguage;

  @Getter
  private LocalDate releaseDate;

  @Getter
  private Integer runtime; // minutes

  @Getter
  @Column(unique = true)
  private Long tmdbId;

  @Getter
  @Column(unique = true)
  private String imdbId;

  @SuppressWarnings("unused")
  Movie() {}

  public Movie(String originalTitle, Locale originalLanguage, LocalDate releaseDate,
               Integer runtime, Long tmdbId, String imdbId) {
    this.originalTitle = originalTitle;
    this.originalLanguage = originalLanguage;
    this.releaseDate = releaseDate;
    this.runtime = runtime;
    this.tmdbId = tmdbId;
    this.imdbId = imdbId;
  }

  public void update(String originalTitle, Locale originalLanguage, LocalDate releaseDate,
                     Integer runtime, Long tmdbId, String imdbId) {
    this.originalTitle = originalTitle;
    this.originalLanguage = originalLanguage;
    this.releaseDate = releaseDate;
    this.runtime = runtime;
    this.tmdbId = tmdbId;
    this.imdbId = imdbId;
    touch();
  }
}
