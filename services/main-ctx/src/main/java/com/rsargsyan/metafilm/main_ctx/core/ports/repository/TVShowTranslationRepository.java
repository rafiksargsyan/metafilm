package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShowTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TVShowTranslationRepository extends JpaRepository<TVShowTranslation, Long> {
  Optional<TVShowTranslation> findByTvShowIdAndLocale(Long tvShowId, Locale locale);
  List<TVShowTranslation> findByTvShowId(Long tvShowId);
}
