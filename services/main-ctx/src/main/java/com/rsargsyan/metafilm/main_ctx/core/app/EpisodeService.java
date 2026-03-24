package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.EpisodeCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.EpisodeDTO;
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

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EpisodeService {

  private final EpisodeRepository episodeRepository;
  private final EpisodeTranslationRepository episodeTranslationRepository;
  private final TVShowRepository tvShowRepository;

  @Autowired
  public EpisodeService(EpisodeRepository episodeRepository,
                        EpisodeTranslationRepository episodeTranslationRepository,
                        TVShowRepository tvShowRepository) {
    this.episodeRepository = episodeRepository;
    this.episodeTranslationRepository = episodeTranslationRepository;
    this.tvShowRepository = tvShowRepository;
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
}
