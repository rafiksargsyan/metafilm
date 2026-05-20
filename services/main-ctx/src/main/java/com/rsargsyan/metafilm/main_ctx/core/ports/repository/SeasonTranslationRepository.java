package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.SeasonTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeasonTranslationRepository extends JpaRepository<SeasonTranslation, Long> {
  Optional<SeasonTranslation> findBySeasonIdAndLocale(Long seasonId, Locale locale);
  List<SeasonTranslation> findBySeasonId(Long seasonId);

  @Query("SELECT DISTINCT t FROM SeasonTranslation t LEFT JOIN FETCH t.images WHERE t.season.id = :seasonId")
  List<SeasonTranslation> findBySeasonIdWithImages(@Param("seasonId") Long seasonId);
}
