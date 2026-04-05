package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.MovieTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieTranslationRepository extends JpaRepository<MovieTranslation, Long> {
  Optional<MovieTranslation> findByMovieIdAndLocale(Long movieId, Locale locale);
  List<MovieTranslation> findByMovieId(Long movieId);

  @Query("SELECT DISTINCT t FROM MovieTranslation t LEFT JOIN FETCH t.images WHERE t.movie.id = :movieId")
  List<MovieTranslation> findByMovieIdWithImages(@Param("movieId") Long movieId);
}
