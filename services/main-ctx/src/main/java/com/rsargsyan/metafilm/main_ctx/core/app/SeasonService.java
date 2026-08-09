package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.Config;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.*;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Season;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.SeasonTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.exception.SeasonAlreadyExistsException;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.EpisodeRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.SeasonRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.SeasonTranslationRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SeasonService {

  private final SeasonRepository seasonRepository;
  private final SeasonTranslationRepository seasonTranslationRepository;
  private final TVShowRepository tvShowRepository;
  private final EpisodeRepository episodeRepository;
  private final S3Presigner s3Presigner;
  private final String s3Bucket;

  @Autowired
  public SeasonService(SeasonRepository seasonRepository,
                       SeasonTranslationRepository seasonTranslationRepository,
                       TVShowRepository tvShowRepository,
                       EpisodeRepository episodeRepository,
                       S3Presigner s3Presigner,
                       Config config) {
    this.seasonRepository = seasonRepository;
    this.seasonTranslationRepository = seasonTranslationRepository;
    this.tvShowRepository = tvShowRepository;
    this.episodeRepository = episodeRepository;
    this.s3Presigner = s3Presigner;
    this.s3Bucket = config.s3Bucket;
  }

  public List<SeasonDTO> listSeasons(String tvShowIdStr) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    return seasonRepository.findByTvShowId(tvShowId).stream()
        .map(SeasonDTO::from)
        .toList();
  }

  public SeasonDetailDTO getSeason(String tvShowIdStr, String seasonIdStr) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    Long seasonId = Util.validateTSID(seasonIdStr);
    Season season = seasonRepository.findById(seasonId).orElseThrow(ResourceNotFoundException::new);
    if (!season.getTvShow().getId().equals(tvShowId)) throw new ResourceNotFoundException();
    List<SeasonTranslation> translations = seasonTranslationRepository.findBySeasonIdWithImages(seasonId);
    List<SeasonTranslationDTO> translationDTOs = translations.stream()
        .map(this::toTranslationDTO)
        .sorted(Comparator.comparing(t -> t.locale().name()))
        .toList();
    return SeasonDetailDTO.from(season,
        episodeRepository.findByTvShowIdAndSeasonNumber(tvShowId, season.getSeasonNumber()),
        translationDTOs);
  }

  private SeasonTranslationDTO toTranslationDTO(SeasonTranslation t) {
    List<SeasonImageDTO> images = t.getImages().stream()
        .map(img -> new SeasonImageDTO(
            img.getType(),
            presignUrl(img.getPath()),
            img.getBlurhash(),
            img.getExternalSource() != null ? img.getExternalSource().name() : null,
            img.getExternalPath()
        ))
        .toList();
    return new SeasonTranslationDTO(t.getStrId(), t.getLocale(), t.getTitle(), t.getOverview(), images);
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
  public SeasonDTO createSeason(String tvShowIdStr, SeasonCreationDTO dto) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.findById(tvShowId)
        .orElseThrow(ResourceNotFoundException::new);
    if (seasonRepository.findByTvShowIdAndSeasonNumber(tvShowId, dto.seasonNumber()).isPresent()) {
      throw new SeasonAlreadyExistsException(dto.seasonNumber());
    }
    tvShow.onSeasonCreated();
    tvShowRepository.save(tvShow);
    Season season = new Season(tvShow, dto.seasonNumber(), dto.originalName(), null);
    seasonRepository.save(season);
    return SeasonDTO.from(season);
  }

  @Transactional
  public String upsertSeason(String tvShowIdStr, Integer seasonNumber,
                              String originalName, LocalDate airDate) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    return seasonRepository.findByTvShowIdAndSeasonNumber(tvShowId, seasonNumber)
        .map(season -> {
          season.update(originalName, airDate);
          seasonRepository.save(season);
          return season.getStrId();
        })
        .orElseGet(() -> {
          TVShow tvShow = tvShowRepository.findById(tvShowId)
              .orElseThrow(ResourceNotFoundException::new);
          tvShow.onSeasonCreated();
          tvShowRepository.save(tvShow);
          Season season = new Season(tvShow, seasonNumber, originalName, airDate);
          seasonRepository.save(season);
          return season.getStrId();
        });
  }

  @Transactional
  public void upsertTranslation(String seasonIdStr, Locale locale,
                                 String title, String overview) {
    Long seasonId = Util.validateTSID(seasonIdStr);
    Season season = seasonRepository.getReferenceById(seasonId);
    SeasonTranslation translation = seasonTranslationRepository
        .findBySeasonIdAndLocale(seasonId, locale)
        .orElseGet(() -> new SeasonTranslation(season, locale, null, null));
    translation.update(title, overview);
    seasonTranslationRepository.save(translation);
  }

  @Transactional
  public void upsertTranslationImage(String seasonIdStr, Locale locale, ImageType imageType,
                                      String path, ExternalSource externalSource, String externalPath,
                                      String blurhash) {
    Long seasonId = Util.validateTSID(seasonIdStr);
    SeasonTranslation translation = seasonTranslationRepository
        .findBySeasonIdAndLocale(seasonId, locale)
        .orElseThrow(ResourceNotFoundException::new);
    translation.upsertImage(imageType, path, externalSource, externalPath, blurhash);
    seasonTranslationRepository.save(translation);
  }

  // See TVShowService.isImageUpToDate - same reasoning, lets sync skip the download+blurhash
  // work when the source image hasn't changed since last sync.
  public boolean isImageUpToDate(String seasonIdStr, Locale locale, ImageType imageType, String externalPath) {
    Long seasonId = Util.validateTSID(seasonIdStr);
    return seasonTranslationRepository.findBySeasonIdAndLocale(seasonId, locale)
        .map(t -> t.getImages().stream()
            .anyMatch(img -> img.getType() == imageType && externalPath.equals(img.getExternalPath())))
        .orElse(false);
  }
}
