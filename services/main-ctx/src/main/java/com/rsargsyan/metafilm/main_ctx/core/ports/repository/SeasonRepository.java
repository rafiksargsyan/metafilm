package com.rsargsyan.metafilm.main_ctx.core.ports.repository;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {
  Optional<Season> findByTvShowIdAndSeasonNumber(Long tvShowId, Integer seasonNumber);
  List<Season> findByTvShowId(Long tvShowId);
}
