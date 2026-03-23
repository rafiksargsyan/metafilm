package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(
    name = "season",
    uniqueConstraints = @UniqueConstraint(columnNames = {"tvshow_id", "season_number"})
)
public class Season extends AggregateRoot {

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tvshow_id", nullable = false)
  private TVShow tvShow;

  @Getter
  @Column(name = "season_number", nullable = false)
  private Integer seasonNumber;

  @Getter
  private LocalDate airDate;

  @Getter
  @Column(unique = true)
  private Long tmdbId;

  @SuppressWarnings("unused")
  Season() {}

  public Season(TVShow tvShow, Integer seasonNumber, LocalDate airDate, Long tmdbId) {
    this.tvShow = tvShow;
    this.seasonNumber = seasonNumber;
    this.airDate = airDate;
    this.tmdbId = tmdbId;
  }

  public void update(LocalDate airDate, Long tmdbId) {
    this.airDate = airDate;
    this.tmdbId = tmdbId;
    touch();
  }
}
