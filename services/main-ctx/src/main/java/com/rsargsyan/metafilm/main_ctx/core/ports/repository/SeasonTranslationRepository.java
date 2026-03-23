package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.SeasonTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonTranslationRepository extends JpaRepository<SeasonTranslation, Long> {
  Optional<SeasonTranslation> findBySeasonIdAndLocale(Long seasonId, Locale locale);
  List<SeasonTranslation> findBySeasonId(Long seasonId);
}
