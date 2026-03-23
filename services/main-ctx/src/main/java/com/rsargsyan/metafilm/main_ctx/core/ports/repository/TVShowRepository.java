package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TVShowRepository extends JpaRepository<TVShow, Long> {
  Optional<TVShow> findByTmdbId(Long tmdbId);
  Optional<TVShow> findByImdbId(String imdbId);
  Optional<TVShow> findByTvdbId(Long tvdbId);
}
