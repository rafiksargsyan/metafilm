package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowDTO;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TVShowService {

  private final TVShowRepository tvShowRepository;

  @Autowired
  public TVShowService(TVShowRepository tvShowRepository) {
    this.tvShowRepository = tvShowRepository;
  }

  @Transactional
  public TVShowDTO createTVShow(TVShowCreationDTO dto) {
    TVShow tvShow = new TVShow(dto.originalTitle(), dto.originalLocale(), dto.firstAirDate(),
        null, null, null, null);
    tvShowRepository.save(tvShow);
    return TVShowDTO.from(tvShow);
  }
}
