package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TVShowRepository extends JpaRepository<TVShow, Long> {
  Optional<TVShow> findByTmdbId(Long tmdbId);
  Optional<TVShow> findByImdbId(String imdbId);
  Optional<TVShow> findByTvdbId(Long tvdbId);

  @Query("SELECT t FROM TVShow t LEFT JOIN FETCH t.tags WHERE t.id = :id")
  Optional<TVShow> findByIdWithTags(@Param("id") Long id);
}
