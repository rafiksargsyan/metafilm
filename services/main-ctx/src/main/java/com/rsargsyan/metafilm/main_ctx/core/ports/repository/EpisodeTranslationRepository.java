package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.EpisodeTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodeTranslationRepository extends JpaRepository<EpisodeTranslation, Long> {
  Optional<EpisodeTranslation> findByEpisodeIdAndLocale(Long episodeId, Locale locale);
  List<EpisodeTranslation> findByEpisodeId(Long episodeId);
}
