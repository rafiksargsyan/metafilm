package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.Config;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.*;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Tag;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TVShowService {

  private final TVShowRepository tvShowRepository;
  private final TVShowTranslationRepository tvShowTranslationRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final S3Presigner s3Presigner;
  private final String s3Bucket;

  @Autowired
  public TVShowService(TVShowRepository tvShowRepository,
                       TVShowTranslationRepository tvShowTranslationRepository,
                       ApplicationEventPublisher eventPublisher,
                       S3Presigner s3Presigner,
                       Config config) {
    this.tvShowRepository = tvShowRepository;
    this.tvShowTranslationRepository = tvShowTranslationRepository;
    this.eventPublisher = eventPublisher;
    this.s3Presigner = s3Presigner;
    this.s3Bucket = config.s3Bucket;
  }

  public Page<TVShowDTO> listTVShows(Pageable pageable) {
    return tvShowRepository.findAll(pageable).map(TVShowDTO::from);
  }

  public TVShowDTO getTVShow(String tvShowIdStr) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    return TVShowDTO.from(tvShowRepository.findById(tvShowId).orElseThrow(ResourceNotFoundException::new));
  }

  @Transactional
  public TVShowDetailDTO getTVShowDetail(String tvShowIdStr) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.findByIdWithTags(tvShowId).orElseThrow(ResourceNotFoundException::new);
    List<TVShowTranslation> translations = tvShowTranslationRepository.findByTvShowIdWithImages(tvShowId);
    List<TagDTO> tags = tvShow.getTags().stream()
        .map(TagDTO::from)
        .sorted(Comparator.comparing(TagDTO::key))
        .toList();
    List<TVShowTranslationDTO> translationDTOs = translations.stream()
        .map(this::toTranslationDTO)
        .sorted(Comparator.comparing(t -> t.locale().name()))
        .toList();
    return TVShowDetailDTO.from(tvShow, tags, translationDTOs);
  }

  private TVShowTranslationDTO toTranslationDTO(TVShowTranslation t) {
    List<TVShowImageDTO> images = t.getImages().stream()
        .map(img -> new TVShowImageDTO(
            img.getType(),
            presignUrl(img.getPath()),
            img.getBlurhash(),
            img.getExternalSource() != null ? img.getExternalSource().name() : null,
            img.getExternalPath()
        ))
        .toList();
    return new TVShowTranslationDTO(t.getStrId(), t.getLocale(), t.getTitle(), t.getOverview(), t.getTagline(), images);
  }

  private String presignUrl(String s3Key) {
    try {
      PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(r -> r
          .signatureDuration(Duration.ofHours(1))
          .getObjectRequest(GetObjectRequest.builder().bucket(s3Bucket).key(s3Key).build()));
      return presigned.url().toString();
    } catch (Exception e) {
      log.warn("Failed to presign URL for key {}", s3Key, e);
      return null;
    }
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
