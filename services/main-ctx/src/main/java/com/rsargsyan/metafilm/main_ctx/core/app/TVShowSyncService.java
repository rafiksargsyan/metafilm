package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.Config;
import com.rsargsyan.metafilm.main_ctx.core.BlurhashUtil;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Episode;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalEpisodeData;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalSeasonData;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTranslationData;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTVShowData;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.EpisodeRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowRepository;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShowTranslation;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowTranslationRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbTVShowClient;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbVideoData;
import com.rsargsyan.metafilm.main_ctx.core.app.TmdbGenreTagMapping;
import com.rsargsyan.metafilm.main_ctx.core.ports.tvdb.TvdbTVShowClient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import org.springframework.data.domain.PageRequest;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TVShowSyncService {

  private final TVShowRepository tvShowRepository;
  private final TVShowTranslationRepository tvShowTranslationRepository;
  private final TmdbTVShowClient tmdbTVShowClient;
  private final TvdbTVShowClient tvdbTVShowClient;
  private final TVShowService tvShowService;
  private final SeasonService seasonService;
  private final EpisodeService episodeService;
  private final EpisodeRepository episodeRepository;
  private final TagService tagService;
  private final S3Client s3Client;
  private final String s3Bucket;
  private final String tmdbImageBaseUrl;
  private final RestClient imageDownloadClient;

  @Autowired
  public TVShowSyncService(TVShowRepository tvShowRepository,
                           TVShowTranslationRepository tvShowTranslationRepository,
                           TmdbTVShowClient tmdbTVShowClient,
                           TvdbTVShowClient tvdbTVShowClient,
                           TVShowService tvShowService,
                           SeasonService seasonService,
                           EpisodeService episodeService,
                           EpisodeRepository episodeRepository,
                           TagService tagService,
                           S3Client s3Client,
                           Config config,
                           @Value("${tmdb.image-base-url}") String tmdbImageBaseUrl) {
    this.tvShowRepository = tvShowRepository;
    this.tvShowTranslationRepository = tvShowTranslationRepository;
    this.tmdbTVShowClient = tmdbTVShowClient;
    this.tvdbTVShowClient = tvdbTVShowClient;
    this.tvShowService = tvShowService;
    this.seasonService = seasonService;
    this.episodeService = episodeService;
    this.episodeRepository = episodeRepository;
    this.tagService = tagService;
    this.s3Client = s3Client;
    this.s3Bucket = config.s3Bucket;
    this.tmdbImageBaseUrl = tmdbImageBaseUrl;
    this.imageDownloadClient = RestClient.builder().build();
  }

  public void syncAll() {
    int page = 0;
    int pageSize = 50;
    org.springframework.data.domain.Page<TVShow> batch;
    do {
      batch = tvShowRepository.findAll(PageRequest.of(page++, pageSize));
      for (TVShow tvShow : batch.getContent()) {
        try {
          syncExternal(tvShow.getStrId());
        } catch (Exception e) {
          log.error("Failed to sync tvShow {} during daily sync", tvShow.getStrId(), e);
        }
      }
    } while (batch.hasNext());
  }

  public void syncAllEpisodeTranslations() {
    int page = 0;
    int pageSize = 50;
    org.springframework.data.domain.Page<TVShow> batch;
    do {
      batch = tvShowRepository.findAll(PageRequest.of(page++, pageSize));
      for (TVShow tvShow : batch.getContent()) {
        if (tvShow.getTmdbId() == null) continue;
        try {
          syncEpisodeTranslationsForShow(tvShow);
        } catch (Exception e) {
          log.error("Failed to sync episode translations for tvShow {}", tvShow.getStrId(), e);
        }
      }
    } while (batch.hasNext());
  }

  private void syncEpisodeTranslationsForShow(TVShow tvShow) {
    List<Episode> episodes = episodeRepository.findByTvShowId(tvShow.getId());
    for (Episode episode : episodes) {
      if (episode.getSeasonNumber() == null || episode.getEpisodeNumber() == null) continue;
      try {
        List<ExternalTranslationData> translations = tmdbTVShowClient.fetchEpisodeTranslations(
            tvShow.getTmdbId(), episode.getSeasonNumber(), episode.getEpisodeNumber());
        for (ExternalTranslationData t : translations) {
          try {
            episodeService.upsertTranslation(episode.getStrId(), t.locale(), t.title(), t.overview());
          } catch (Exception e) {
            log.error("Failed to upsert translation {} for episode {}/{} of tvShow {}",
                t.locale(), episode.getSeasonNumber(), episode.getEpisodeNumber(), tvShow.getStrId(), e);
          }
        }
      } catch (Exception e) {
        log.error("Failed to fetch translations for episode {}/{} of tvShow {}",
            episode.getSeasonNumber(), episode.getEpisodeNumber(), tvShow.getStrId(), e);
      }
    }
  }

  @Transactional
  public void syncExternal(String tvShowIdStr) {
    Long tvShowId = Util.validateTSID(tvShowIdStr);
    TVShow tvShow = tvShowRepository.findById(tvShowId).orElseThrow(ResourceNotFoundException::new);

    if (tvShow.isUseTvdb()) {
      if (tvShow.getTvdbId() == null) {
        log.warn("TVShow {} has useTvdb=true but no tvdbId, skipping sync", tvShowIdStr);
        return;
      }
    } else {
      if (tvShow.getTmdbId() == null) {
        log.warn("TVShow {} has no tmdbId, skipping sync", tvShowIdStr);
        return;
      }
    }

    ExternalTVShowData data;
    try {
      data = tvShow.isUseTvdb()
          ? tvdbTVShowClient.fetchTVShow(tvShow.getTvdbId())
          : tmdbTVShowClient.fetchTVShow(tvShow.getTmdbId());
    } catch (Exception e) {
      log.error("Failed to fetch external data for tvShow {}", tvShowIdStr, e);
      return;
    }

    ExternalSource externalSource = tvShow.isUseTvdb() ? ExternalSource.TVDB : ExternalSource.TMDB;

    Double voteAverage = data.voteAverage();
    if (tvShow.isUseTvdb() && tvShow.getTmdbId() != null) {
      try {
        Double tmdbVoteAverage = tmdbTVShowClient.fetchVoteAverage(tvShow.getTmdbId());
        if (tmdbVoteAverage != null) voteAverage = tmdbVoteAverage;
      } catch (Exception e) {
        log.warn("Failed to fetch TMDB vote average for tvShow {}", tvShowIdStr, e);
      }
    }
    tvShowService.updateFromExternal(tvShowIdStr, data.originalTitle(), data.firstAirDate(), data.lastAirDate(), voteAverage);

    List<Integer> genreIds = List.of();
    if (tvShow.getTmdbId() != null) {
      try {
        genreIds = tmdbTVShowClient.fetchTVShowGenreIds(tvShow.getTmdbId());
      } catch (Exception e) {
        log.warn("Failed to fetch TMDB genre IDs for tvShow {}", tvShowIdStr, e);
      }
    }
    List<String> tagKeys = genreIds.stream()
        .map(TmdbGenreTagMapping.TV_GENRES::get)
        .filter(Objects::nonNull)
        .distinct()
        .toList();
    try {
      tagService.syncTVShowTags(tvShowId, tagKeys);
    } catch (Exception e) {
      log.error("Failed to sync tags for tvShow {}", tvShowIdStr, e);
    }

    for (ExternalTranslationData t : data.translations()) {
      try {
        syncTVShowTranslation(tvShowIdStr, t, externalSource);
      } catch (Exception e) {
        log.error("Failed to sync translation {} for tvShow {}", t.locale(), tvShowIdStr, e);
      }
    }

    Locale tvShowLocale = tvShow.getOriginalLanguage();

    // Sync trailers from TMDB (always, even when useTvdb=true, as long as tmdbId is set)
    if (tvShow.getTmdbId() != null) {
      try {
        Set<Locale> translationLocales = tvShowTranslationRepository.findByTvShowId(tvShowId).stream()
            .map(TVShowTranslation::getLocale)
            .collect(Collectors.toSet());
        Map<Locale, TmdbVideoData> videosByLocale =
            tmdbTVShowClient.fetchTVShowVideos(tvShow.getTmdbId(), translationLocales);
        for (Map.Entry<Locale, TmdbVideoData> entry : videosByLocale.entrySet()) {
          try {
            TVShowTranslation t = tvShowTranslationRepository
                .findByTvShowIdAndLocale(tvShowId, entry.getKey())
                .orElseThrow();
            t.upsertTrailer(entry.getValue().site(), entry.getValue().key());
            tvShowTranslationRepository.save(t);
          } catch (Exception e) {
            log.error("Failed to upsert trailer for locale {} of tvShow {}", entry.getKey(), tvShowIdStr, e);
          }
        }
      } catch (Exception e) {
        log.error("Failed to sync trailers for tvShow {}", tvShowIdStr, e);
      }
    }

    boolean covered = data.translations().stream()
        .anyMatch(t -> t.locale().equals(tvShowLocale) && t.title() != null && !t.title().isBlank());
    if (!covered) {
      try {
        syncTVShowTranslation(tvShowIdStr, new ExternalTranslationData(
            tvShowLocale,
            data.originalTitle(),
            data.originalOverview(),
            data.originalTagline(),
            data.originalPosterPath(),
            data.originalBackdropPath()
        ), externalSource);
      } catch (Exception e) {
        log.error("Failed to sync original-locale translation {} for tvShow {}", tvShowLocale, tvShowIdStr, e);
      }
    }

    for (ExternalSeasonData season : data.seasons()) {
      try {
        syncSeason(tvShowIdStr, season, externalSource, tvShowLocale);
      } catch (Exception e) {
        log.error("Failed to sync season {} for tvShow {}", season.seasonNumber(), tvShowIdStr, e);
      }
    }
    log.info("Sync completed for tvShow {}", tvShowIdStr);
  }

  private void syncTVShowTranslation(String tvShowIdStr, ExternalTranslationData t,
                                      ExternalSource externalSource) {
    tvShowService.upsertTranslation(tvShowIdStr, t.locale(), t.title(), t.overview(), t.tagline());
    if (t.posterPath() != null) {
      UploadResult r = uploadImage(
          "tvshows/%s/%s/poster".formatted(tvShowIdStr, t.locale().name().toLowerCase()),
          t.posterPath());
      if (r != null) {
        tvShowService.upsertTranslationImage(tvShowIdStr, t.locale(), ImageType.POSTER, r.s3Key(), externalSource, t.posterPath(), r.blurhash());
      }
    }
    if (t.backdropPath() != null) {
      UploadResult r = uploadImage(
          "tvshows/%s/%s/backdrop".formatted(tvShowIdStr, t.locale().name().toLowerCase()),
          t.backdropPath());
      if (r != null) {
        tvShowService.upsertTranslationImage(tvShowIdStr, t.locale(), ImageType.BACKDROP, r.s3Key(), externalSource, t.backdropPath(), r.blurhash());
      }
    }
  }

  private void syncSeason(String tvShowIdStr, ExternalSeasonData season, ExternalSource externalSource,
                           Locale originalLocale) {
    String seasonIdStr = seasonService.upsertSeason(
        tvShowIdStr, season.seasonNumber(), season.originalName(), season.airDate());

    for (ExternalTranslationData t : season.translations()) {
      try {
        seasonService.upsertTranslation(seasonIdStr, t.locale(), t.title(), t.overview());
        if (t.posterPath() != null) {
          UploadResult r = uploadImage(
              "tvshows/%s/seasons/%d/%s/poster".formatted(tvShowIdStr, season.seasonNumber(), t.locale().name().toLowerCase()),
              t.posterPath());
          if (r != null) {
            seasonService.upsertTranslationImage(seasonIdStr, t.locale(), ImageType.POSTER, r.s3Key(), externalSource, t.posterPath(), r.blurhash());
          }
        }
      } catch (Exception e) {
        log.error("Failed to sync translation {} for season {} of tvShow {}", t.locale(), season.seasonNumber(), tvShowIdStr, e);
      }
    }

    boolean covered = season.translations().stream()
        .anyMatch(t -> t.locale().equals(originalLocale)
            && t.title() != null && !t.title().isBlank()
            && t.overview() != null && !t.overview().isBlank());
    if (!covered) {
      try {
        seasonService.upsertTranslation(seasonIdStr, originalLocale, season.originalName(), season.originalOverview());
      } catch (Exception e) {
        log.error("Failed to sync original-locale translation {} for season {} of tvShow {}", originalLocale, season.seasonNumber(), tvShowIdStr, e);
      }
    }

    for (ExternalEpisodeData episode : season.episodes()) {
      try {
        syncEpisode(tvShowIdStr, season.seasonNumber(), episode, externalSource);
      } catch (Exception e) {
        log.error("Failed to sync episode {}/{} for tvShow {}", season.seasonNumber(), episode.episodeNumber(), tvShowIdStr, e);
      }
    }
  }

  private void syncEpisode(String tvShowIdStr, Integer seasonNumber, ExternalEpisodeData episode,
                            ExternalSource externalSource) {
    String episodeIdStr = episodeService.upsertEpisode(
        tvShowIdStr, episode.seasonNumber(), episode.episodeNumber(),
        episode.absoluteNumber(), episode.airDate(), episode.runtime());

    for (ExternalTranslationData t : episode.translations()) {
      try {
        episodeService.upsertTranslation(episodeIdStr, t.locale(), t.title(), t.overview());
      } catch (Exception e) {
        log.error("Failed to sync translation {} for episode {}/{} of tvShow {}",
            t.locale(), seasonNumber, episode.episodeNumber(), tvShowIdStr, e);
      }
    }

    if (episode.stillPath() != null) {
      UploadResult r = uploadImage(
          "tvshows/%s/seasons/%d/episodes/%d/still".formatted(tvShowIdStr, seasonNumber, episode.episodeNumber()),
          episode.stillPath());
      if (r != null) {
        for (ExternalTranslationData t : episode.translations()) {
          try {
            episodeService.upsertTranslationImage(episodeIdStr, t.locale(), ImageType.STILL, r.s3Key(), externalSource, episode.stillPath(), r.blurhash());
          } catch (Exception e) {
            log.error("Failed to upsert still image for translation {} of episode {}/{} of tvShow {}",
                t.locale(), seasonNumber, episode.episodeNumber(), tvShowIdStr, e);
          }
        }
      }
    }
  }

  private record UploadResult(String s3Key, String blurhash) {}

  private UploadResult uploadImage(String s3KeyBase, String externalPath) {
    try {
      String imageUrl = externalPath.startsWith("http") ? externalPath : tmdbImageBaseUrl + externalPath;
      byte[] imageBytes = imageDownloadClient.get()
          .uri(URI.create(imageUrl))
          .retrieve()
          .body(byte[].class);
      if (imageBytes == null) return null;
      String extension = externalPath.contains(".") ? externalPath.substring(externalPath.lastIndexOf('.')) : ".jpg";
      String s3Key = s3KeyBase + extension;
      s3Client.putObject(
          PutObjectRequest.builder().bucket(s3Bucket).key(s3Key).build(),
          RequestBody.fromBytes(imageBytes)
      );
      return new UploadResult(s3Key, BlurhashUtil.compute(imageBytes));
    } catch (Exception e) {
      log.error("Failed to download/upload image {}", externalPath, e);
      return null;
    }
  }
}
