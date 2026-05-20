package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.EpisodeTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EpisodeTranslationRepository extends JpaRepository<EpisodeTranslation, Long> {
  Optional<EpisodeTranslation> findByEpisodeIdAndLocale(Long episodeId, Locale locale);
  List<EpisodeTranslation> findByEpisodeId(Long episodeId);

  @Query("SELECT DISTINCT t FROM EpisodeTranslation t LEFT JOIN FETCH t.images WHERE t.episode.id = :episodeId")
  List<EpisodeTranslation> findByEpisodeIdWithImages(@Param("episodeId") Long episodeId);
}
