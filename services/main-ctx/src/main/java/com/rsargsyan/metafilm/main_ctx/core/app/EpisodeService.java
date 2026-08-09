package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.Config;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Episode;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.EpisodeTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.exception.EpisodeAlreadyExistsException;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.EpisodeRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.EpisodeTranslationRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EpisodeService {

  private final EpisodeRepository episodeRepository;
  private final EpisodeTranslationRepository episodeTranslationRepository;
  private final TVShowRepository tvShowRepository;
  private final S3Presigner s3Presigner;
  private final String s3Bucket;

  @Autowired
  public EpisodeService(EpisodeRepository episodeRepository,
                        EpisodeTranslationRepository episodeTranslationRepository,
                        TVShowRepository tvShowRepository,
                        S3Presigner s3Presigner,
                        Config config) {
    this.episodeRepository = episodeRepository;
    this.episodeTranslationRepository = episodeTranslationRepository;
    this.tvShowRepository = tvShowRepository;
    this.s3Presigner = s3Presigner;
    this.s3Bucket = config.s3Bucket;
  }

  public EpisodeDetailDTO getEpisodeDetail(String tvShowIdStr, String episodeIdStr) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    Long episodeId = Util.validateTSID(episodeIdStr);
    Episode episode = episodeRepository.findById(episodeId).orElseThrow(ResourceNotFoundException::new);
    if (!episode.getTvShow().getId().equals(tvShowId)) throw new ResourceNotFoundException();
    List<EpisodeTranslation> translations = episodeTranslationRepository.findByEpisodeIdWithImages(episodeId);
    List<EpisodeTranslationDTO> translationDTOs = translations.stream()
        .map(this::toTranslationDTO)
        .sorted(Comparator.comparing(t -> t.locale().name()))
        .toList();
    return EpisodeDetailDTO.from(episode, translationDTOs);
  }

  private EpisodeTranslationDTO toTranslationDTO(EpisodeTranslation t) {
    List<EpisodeImageDTO> images = t.getImages().stream()
        .map(img -> new EpisodeImageDTO(
            img.getType(),
            presignUrl(img.getPath()),
            img.getBlurhash(),
            img.getExternalSource() != null ? img.getExternalSource().name() : null,
            img.getExternalPath()
        ))
        .toList();
    return new EpisodeTranslationDTO(t.getStrId(), t.getLocale(), t.getTitle(), t.getOverview(), images);
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
  public EpisodeDTO createEpisode(String tvShowIdStr, EpisodeCreationDTO dto) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.findById(tvShowId)
        .orElseThrow(ResourceNotFoundException::new);

    if (dto.seasonNumber() != null && dto.episodeNumber() != null) {
      if (episodeRepository.findByTvShowIdAndSeasonNumberAndEpisodeNumber(
          tvShowId, dto.seasonNumber(), dto.episodeNumber()).isPresent()) {
        throw new EpisodeAlreadyExistsException();
      }
    } else if (dto.absoluteNumber() != null) {
      if (episodeRepository.findByTvShowIdAndAbsoluteNumber(
          tvShowId, dto.absoluteNumber()).isPresent()) {
        throw new EpisodeAlreadyExistsException();
      }
    }

    tvShow.onEpisodeCreated();
    tvShowRepository.save(tvShow);

    Episode episode = new Episode(tvShow, dto.seasonNumber(), dto.episodeNumber(),
        dto.absoluteNumber(), dto.airDate(), dto.runtime());
    episodeRepository.save(episode);
    return EpisodeDTO.from(episode);
  }

  @Transactional
  public String upsertEpisode(String tvShowIdStr, Integer seasonNumber, Integer episodeNumber,
                               Integer absoluteNumber, LocalDate airDate, Integer runtime) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);

    Optional<Episode> existing = (seasonNumber != null && episodeNumber != null)
        ? episodeRepository.findByTvShowIdAndSeasonNumberAndEpisodeNumber(tvShowId, seasonNumber, episodeNumber)
        : (absoluteNumber != null
            ? episodeRepository.findByTvShowIdAndAbsoluteNumber(tvShowId, absoluteNumber)
            : Optional.empty());

    return existing.map(episode -> {
      episode.update(seasonNumber, episodeNumber, absoluteNumber, airDate, runtime);
      episodeRepository.save(episode);
      return episode.getStrId();
    }).orElseGet(() -> {
      TVShow tvShow = tvShowRepository.findById(tvShowId).orElseThrow(ResourceNotFoundException::new);
      tvShow.onEpisodeCreated();
      tvShowRepository.save(tvShow);
      Episode episode = new Episode(tvShow, seasonNumber, episodeNumber, absoluteNumber, airDate, runtime);
      episodeRepository.save(episode);
      return episode.getStrId();
    });
  }

  @Transactional
  public void upsertTranslation(String episodeIdStr, Locale locale,
                                 String title, String overview) {
    Long episodeId = Util.validateTSID(episodeIdStr);
    Episode episode = episodeRepository.getReferenceById(episodeId);
    EpisodeTranslation translation = episodeTranslationRepository
        .findByEpisodeIdAndLocale(episodeId, locale)
        .orElseGet(() -> new EpisodeTranslation(episode, locale, null, null));
    translation.update(title, overview);
    episodeTranslationRepository.save(translation);
  }

  @Transactional
  public void upsertTranslationImage(String episodeIdStr, Locale locale, ImageType imageType,
                                      String path, ExternalSource externalSource, String externalPath,
                                      String blurhash) {
    Long episodeId = Util.validateTSID(episodeIdStr);
    EpisodeTranslation translation = episodeTranslationRepository
        .findByEpisodeIdAndLocale(episodeId, locale)
        .orElseThrow(ResourceNotFoundException::new);
    translation.upsertImage(imageType, path, externalSource, externalPath, blurhash);
    episodeTranslationRepository.save(translation);
  }

  // See TVShowService.isImageUpToDate - same reasoning, lets sync skip the download+blurhash
  // work when the source image hasn't changed since last sync.
  public boolean isImageUpToDate(String episodeIdStr, Locale locale, ImageType imageType, String externalPath) {
    Long episodeId = Util.validateTSID(episodeIdStr);
    return episodeTranslationRepository.findByEpisodeIdAndLocale(episodeId, locale)
        .map(t -> t.getImages().stream()
            .anyMatch(img -> img.getType() == imageType && externalPath.equals(img.getExternalPath())))
        .orElse(false);
  }
}
