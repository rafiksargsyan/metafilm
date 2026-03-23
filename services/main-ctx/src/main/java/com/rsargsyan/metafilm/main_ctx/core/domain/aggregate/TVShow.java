package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "tvshow")
public class TVShow extends AggregateRoot {

  @Getter
  @Column(nullable = false)
  private String originalTitle;

  @Getter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Locale originalLanguage;

  @Getter
  private LocalDate firstAirDate;

  @Getter
  private LocalDate lastAirDate; // null if still airing

  @Getter
  @Column(unique = true)
  private Long tmdbId;

  @Getter
  @Column(unique = true)
  private String imdbId;

  @Getter
  @Column(unique = true)
  private Long tvdbId;

  @SuppressWarnings("unused")
  TVShow() {}

  public TVShow(String originalTitle, Locale originalLanguage, LocalDate firstAirDate,
                LocalDate lastAirDate, Long tmdbId, String imdbId, Long tvdbId) {
    this.originalTitle = originalTitle;
    this.originalLanguage = originalLanguage;
    this.firstAirDate = firstAirDate;
    this.lastAirDate = lastAirDate;
    this.tmdbId = tmdbId;
    this.imdbId = imdbId;
    this.tvdbId = tvdbId;
  }

  public void update(String originalTitle, Locale originalLanguage, LocalDate firstAirDate,
                     LocalDate lastAirDate, Long tmdbId, String imdbId, Long tvdbId) {
    this.originalTitle = originalTitle;
    this.originalLanguage = originalLanguage;
    this.firstAirDate = firstAirDate;
    this.lastAirDate = lastAirDate;
    this.tmdbId = tmdbId;
    this.imdbId = imdbId;
    this.tvdbId = tvdbId;
    touch();
  }
}
