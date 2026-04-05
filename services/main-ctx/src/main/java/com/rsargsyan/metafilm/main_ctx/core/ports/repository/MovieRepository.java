package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Movie;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
  Optional<Movie> findByTmdbId(Long tmdbId);
  Optional<Movie> findByImdbId(String imdbId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT m FROM Movie m WHERE m.id = :id")
  Optional<Movie> findByIdForUpdate(@Param("id") Long id);
}
