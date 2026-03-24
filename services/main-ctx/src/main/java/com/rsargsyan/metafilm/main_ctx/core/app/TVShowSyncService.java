package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.Config;
import com.rsargsyan.metafilm.main_ctx.core.BlurhashUtil;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.TVShow;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalEpisodeData;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalSeasonData;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTranslationData;
import com.rsargsyan.metafilm.main_ctx.core.ports.external.ExternalTVShowData;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.TVShowRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbTVShowClient;
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

@Slf4j
@Service
public class TVShowSyncService {

  private final TVShowRepository tvShowRepository;
  private final TmdbTVShowClient tmdbTVShowClient;
  private final TvdbTVShowClient tvdbTVShowClient;
  private final TVShowService tvShowService;
  private final SeasonService seasonService;
  private final EpisodeService episodeService;
  private final S3Client s3Client;
  private final String s3Bucket;
  private final String tmdbImageBaseUrl;
  private final RestClient imageDownloadClient;

  @Autowired
  public TVShowSyncService(TVShowRepository tvShowRepository,
                           TmdbTVShowClient tmdbTVShowClient,
                           TvdbTVShowClient tvdbTVShowClient,
                           TVShowService tvShowService,
                           SeasonService seasonService,
                           EpisodeService episodeService,
                           S3Client s3Client,
                           Config config,
                           @Value("${tmdb.image-base-url}") String tmdbImageBaseUrl) {
    this.tvShowRepository = tvShowRepository;
    this.tmdbTVShowClient = tmdbTVShowClient;
    this.tvdbTVShowClient = tvdbTVShowClient;
    this.tvShowService = tvShowService;
    this.seasonService = seasonService;
    this.episodeService = episodeService;
    this.s3Client = s3Client;
    this.s3Bucket = config.s3Bucket;
    this.tmdbImageBaseUrl = tmdbImageBaseUrl;
    this.imageDownloadClient = RestClient.create();
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

    tvShowService.updateFromExternal(tvShowIdStr, data.originalTitle(), data.firstAirDate(), data.lastAirDate());

    for (ExternalTranslationData t : data.translations()) {
      try {
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
      } catch (Exception e) {
        log.error("Failed to sync translation {} for tvShow {}", t.locale(), tvShowIdStr, e);
      }
    }

    for (ExternalSeasonData season : data.seasons()) {
      try {
        syncSeason(tvShowIdStr, season, externalSource);
      } catch (Exception e) {
        log.error("Failed to sync season {} for tvShow {}", season.seasonNumber(), tvShowIdStr, e);
      }
    }
  }

  private void syncSeason(String tvShowIdStr, ExternalSeasonData season, ExternalSource externalSource) {
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
            episodeService.upsertTranslationImage(episodeIdStr, t.locale(), ImageType.POSTER, r.s3Key(), externalSource, episode.stillPath(), r.blurhash());
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
      byte[] imageBytes = imageDownloadClient.get()
          .uri(URI.create(tmdbImageBaseUrl + externalPath))
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
