package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.Config;
import com.rsargsyan.metafilm.main_ctx.core.BlurhashUtil;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Movie;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.MovieTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ExternalSource;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.ImageType;
import com.rsargsyan.metafilm.main_ctx.core.domain.valueobject.Locale;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.MovieRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.MovieTranslationRepository;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbMovieClient;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbMovieData;
import com.rsargsyan.metafilm.main_ctx.core.ports.tmdb.TmdbTranslationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import org.springframework.data.domain.PageRequest;

import java.net.URI;

@Slf4j
@Service
public class MovieSyncService {

  private final MovieRepository movieRepository;
  private final MovieTranslationRepository movieTranslationRepository;
  private final TmdbMovieClient tmdbMovieClient;
  private final MovieSyncLockService syncLockService;
  private final S3Client s3Client;
  private final String s3Bucket;
  private final RestClient imageDownloadClient;

  @Autowired
  public MovieSyncService(MovieRepository movieRepository,
                          MovieTranslationRepository movieTranslationRepository,
                          TmdbMovieClient tmdbMovieClient,
                          MovieSyncLockService syncLockService,
                          S3Client s3Client,
                          Config config) {
    this.movieRepository = movieRepository;
    this.movieTranslationRepository = movieTranslationRepository;
    this.tmdbMovieClient = tmdbMovieClient;
    this.syncLockService = syncLockService;
    this.s3Client = s3Client;
    this.s3Bucket = config.s3Bucket;
    this.imageDownloadClient = RestClient.create();
  }

  public void syncAll() {
    int page = 0;
    int pageSize = 50;
    org.springframework.data.domain.Page<Movie> batch;
    do {
      batch = movieRepository.findAll(PageRequest.of(page++, pageSize));
      for (Movie movie : batch.getContent()) {
        try {
          syncExternal(movie.getStrId());
        } catch (Exception e) {
          log.error("Failed to sync movie {} during daily sync", movie.getStrId(), e);
        }
      }
    } while (batch.hasNext());
  }

  public void syncExternal(String movieIdStr) {
    Long movieId = Util.validateTSID(movieIdStr);
    syncLockService.acquireLock(movieId);
    try {
      doSyncExternal(movieId, movieIdStr);
    } finally {
      syncLockService.releaseLock(movieId);
    }
  }

  private void doSyncExternal(Long movieId, String movieIdStr) {
    Movie movie = movieRepository.findById(movieId).orElseThrow(ResourceNotFoundException::new);
    if (movie.getTmdbId() == null) {
      log.warn("Movie {} has no tmdbId, skipping sync", movieIdStr);
      return;
    }

    TmdbMovieData data;
    try {
      data = tmdbMovieClient.fetchMovie(movie.getTmdbId());
    } catch (Exception e) {
      log.error("Failed to fetch TMDB data for movie {}", movieId, e);
      return;
    }

    movie.update(
        data.originalTitle(),
        movie.getOriginalLanguage(),
        data.releaseDate(),
        data.runtime(),
        movie.getTmdbId(),
        data.imdbId()
    );
    movieRepository.save(movie);

    for (TmdbTranslationData t : data.translations()) {
      try {
        syncTranslation(movie, t);
      } catch (Exception e) {
        log.error("Failed to sync translation {} for movie {}", t.locale(), movieId, e);
      }
    }

    // Always ensure the movie's stored original language has a translation entry.
    // We use movie.getOriginalLanguage() rather than data.originalLanguage() because
    // TMDB's production_countries may not reliably resolve to the locale the admin has set
    // (e.g. EN_GB vs EN_US for a British film).
    Locale movieLocale = movie.getOriginalLanguage();
    boolean covered = data.translations().stream()
        .anyMatch(t -> t.locale().equals(movieLocale) && t.title() != null && !t.title().isBlank());
    if (!covered) {
      try {
        syncTranslation(movie, new TmdbTranslationData(
            movieLocale,
            data.originalTitle(),
            data.originalOverview(),
            data.originalTagline(),
            data.originalPosterPath(),
            data.originalBackdropPath()
        ));
      } catch (Exception e) {
        log.error("Failed to sync original-locale translation {} for movie {}", movieLocale, movieId, e);
      }
    }
  }

  private void syncTranslation(Movie movie, TmdbTranslationData t) {
    MovieTranslation translation = movieTranslationRepository
        .findByMovieIdAndLocale(movie.getId(), t.locale())
        .orElseGet(() -> new MovieTranslation(movie, t.locale(), null, null, null));

    translation.update(t.title(), t.overview(), t.tagline());
    translation = movieTranslationRepository.save(translation);

    if (t.posterPath() != null) {
      translation = syncImage(movie, translation, ImageType.POSTER, t.posterPath());
    }
    if (t.backdropPath() != null) {
      syncImage(movie, translation, ImageType.BACKDROP, t.backdropPath());
    }
  }

  private MovieTranslation syncImage(Movie movie, MovieTranslation translation,
                                     ImageType imageType, String tmdbPath) {
    byte[] imageBytes;
    try {
      imageBytes = imageDownloadClient.get()
          .uri(URI.create("https://image.tmdb.org/t/p/original" + tmdbPath))
          .retrieve()
          .body(byte[].class);
    } catch (Exception e) {
      log.error("Failed to download {} image {} for movie {}", imageType, tmdbPath, movie.getId(), e);
      return translation;
    }

    String extension = tmdbPath.contains(".") ? tmdbPath.substring(tmdbPath.lastIndexOf('.')) : ".jpg";
    String s3Key = "movies/%s/%s/%s%s".formatted(
        movie.getStrId(),
        translation.getLocale().name().toLowerCase(),
        imageType.name().toLowerCase(),
        extension
    );

    s3Client.putObject(
        PutObjectRequest.builder().bucket(s3Bucket).key(s3Key).build(),
        RequestBody.fromBytes(imageBytes)
    );

    String blurhash = BlurhashUtil.compute(imageBytes);
    translation.upsertImage(imageType, s3Key, ExternalSource.TMDB, tmdbPath, blurhash);
    return movieTranslationRepository.save(translation);
  }
}
