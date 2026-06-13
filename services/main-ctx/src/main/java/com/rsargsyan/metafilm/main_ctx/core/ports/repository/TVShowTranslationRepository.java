package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShowTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TVShowTranslationRepository extends JpaRepository<TVShowTranslation, Long> {
  Optional<TVShowTranslation> findByTvShowIdAndLocale(Long tvShowId, Locale locale);
  List<TVShowTranslation> findByTvShowId(Long tvShowId);

  @Query("SELECT DISTINCT t FROM TVShowTranslation t LEFT JOIN FETCH t.images WHERE t.tvShow.id = :tvShowId")
  List<TVShowTranslation> findByTvShowIdWithImages(@Param("tvShowId") Long tvShowId);

  @Query("SELECT DISTINCT t FROM TVShowTranslation t LEFT JOIN FETCH t.images LEFT JOIN FETCH t.trailer WHERE t.tvShow.id = :tvShowId")
  List<TVShowTranslation> findByTvShowIdWithImagesAndTrailer(@Param("tvShowId") Long tvShowId);
}
