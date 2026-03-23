package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.MovieTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieTranslationRepository extends JpaRepository<MovieTranslation, Long> {
  Optional<MovieTranslation> findByMovieIdAndLocale(Long movieId, Locale locale);
  List<MovieTranslation> findByMovieId(Long movieId);
}
