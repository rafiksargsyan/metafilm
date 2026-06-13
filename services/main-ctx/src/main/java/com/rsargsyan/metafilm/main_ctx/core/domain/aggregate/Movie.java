package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

  @Getter
  private Double voteAverage;

  @Getter
  @Column(nullable = false, columnDefinition = "boolean not null default false")
  private boolean syncInProgress;

  @Getter
  private Instant syncStartedAt;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "movie_tag",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags = new HashSet<>();

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

  public Set<Tag> getTags() {
    return Collections.unmodifiableSet(tags);
  }

  public void addTag(Tag tag) {
    tags.add(tag);
    touch();
  }

  public void removeTag(Tag tag) {
    tags.remove(tag);
    touch();
  }

  public void setTmdbId(Long tmdbId) {
    this.tmdbId = tmdbId;
    touch();
  }

  public void markSyncStarted() {
    this.syncInProgress = true;
    this.syncStartedAt = Instant.now();
    touch();
  }

  public void markSyncFinished() {
    this.syncInProgress = false;
    touch();
  }

  public void update(String originalTitle, Locale originalLanguage, LocalDate releaseDate,
                     Integer runtime, Long tmdbId, String imdbId, Double voteAverage) {
    this.originalTitle = originalTitle;
    this.originalLanguage = originalLanguage;
    this.releaseDate = releaseDate;
    this.runtime = runtime;
    this.tmdbId = tmdbId;
    this.imdbId = imdbId;
    this.voteAverage = voteAverage;
    touch();
  }
}
