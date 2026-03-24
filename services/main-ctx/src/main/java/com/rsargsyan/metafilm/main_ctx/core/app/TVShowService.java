package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowDTO;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShowTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.event.TVShowSyncRequestedEvent;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowTranslationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TVShowService {

  private final TVShowRepository tvShowRepository;
  private final TVShowTranslationRepository tvShowTranslationRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Autowired
  public TVShowService(TVShowRepository tvShowRepository,
                       TVShowTranslationRepository tvShowTranslationRepository,
                       ApplicationEventPublisher eventPublisher) {
    this.tvShowRepository = tvShowRepository;
    this.tvShowTranslationRepository = tvShowTranslationRepository;
    this.eventPublisher = eventPublisher;
  }

  public Page<TVShowDTO> listTVShows(Pageable pageable) {
    return tvShowRepository.findAll(pageable).map(TVShowDTO::from);
  }

  public TVShowDTO getTVShow(String tvShowIdStr) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    return TVShowDTO.from(tvShowRepository.findById(tvShowId).orElseThrow(ResourceNotFoundException::new));
  }

  @Transactional
  public TVShowDTO createTVShow(TVShowCreationDTO dto) {
    TVShow tvShow = new TVShow(dto.originalTitle(), dto.originalLocale(), dto.firstAirDate(),
        null, null, null, null);
    tvShowRepository.save(tvShow);
    return TVShowDTO.from(tvShow);
  }

  @Transactional
  public TVShowDTO setTmdbId(String tvShowIdStr, Long tmdbId) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.findById(tvShowId).orElseThrow(ResourceNotFoundException::new);
    tvShow.setTmdbId(tmdbId);
    tvShowRepository.save(tvShow);
    eventPublisher.publishEvent(new TVShowSyncRequestedEvent(tvShowIdStr));
    return TVShowDTO.from(tvShow);
  }

  @Transactional
  public TVShowDTO setTvdbId(String tvShowIdStr, Long tvdbId) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.findById(tvShowId).orElseThrow(ResourceNotFoundException::new);
    tvShow.setTvdbId(tvdbId);
    tvShowRepository.save(tvShow);
    eventPublisher.publishEvent(new TVShowSyncRequestedEvent(tvShowIdStr));
    return TVShowDTO.from(tvShow);
  }

  @Transactional
  public TVShowDTO setUseTvdb(String tvShowIdStr, boolean useTvdb) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.findById(tvShowId).orElseThrow(ResourceNotFoundException::new);
    tvShow.setUseTvdb(useTvdb);
    tvShowRepository.save(tvShow);
    eventPublisher.publishEvent(new TVShowSyncRequestedEvent(tvShowIdStr));
    return TVShowDTO.from(tvShow);
  }

  @Transactional
  public void updateFromExternal(String tvShowIdStr, String originalTitle,
                                  LocalDate firstAirDate, LocalDate lastAirDate) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.findById(tvShowId).orElseThrow(ResourceNotFoundException::new);
    tvShow.updateFromExternal(originalTitle, firstAirDate, lastAirDate);
    tvShowRepository.save(tvShow);
  }

  @Transactional
  public void upsertTranslation(String tvShowIdStr, Locale locale,
                                 String title, String overview, String tagline) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.getReferenceById(tvShowId);
    TVShowTranslation translation = tvShowTranslationRepository
        .findByTvShowIdAndLocale(tvShowId, locale)
        .orElseGet(() -> new TVShowTranslation(tvShow, locale, null, null, null));
    translation.update(title, overview, tagline);
    tvShowTranslationRepository.save(translation);
  }

  @Transactional
  public void upsertTranslationImage(String tvShowIdStr, Locale locale, ImageType imageType,
                                      String path, ExternalSource externalSource, String externalPath,
                                      String blurhash) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShowTranslation translation = tvShowTranslationRepository
        .findByTvShowIdAndLocale(tvShowId, locale)
        .orElseThrow(ResourceNotFoundException::new);
    translation.upsertImage(imageType, path, externalSource, externalPath, blurhash);
    tvShowTranslationRepository.save(translation);
  }
}
