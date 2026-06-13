package com.rsargsyan.metafilm.main_ctx.core.domain.aggregate;

import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

  @Getter
  private Double voteAverage;

  @Getter
  @Column(nullable = false)
  private boolean useTvdb = false;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "tvshow_tag",
      joinColumns = @JoinColumn(name = "tvshow_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags = new HashSet<>();

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

  public void setTvdbId(Long tvdbId) {
    this.tvdbId = tvdbId;
    touch();
  }

  public void setUseTvdb(boolean useTvdb) {
    this.useTvdb = useTvdb;
    touch();
  }

  public void updateFromExternal(String originalTitle, LocalDate firstAirDate, LocalDate lastAirDate,
                                 Double voteAverage) {
    this.originalTitle = originalTitle;
    this.firstAirDate = firstAirDate;
    this.lastAirDate = lastAirDate;
    this.voteAverage = voteAverage;
    touch();
  }

  public void onSeasonCreated() {
    touch();
  }

  public void onEpisodeCreated() {
    touch();
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
