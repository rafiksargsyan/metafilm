package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.Config;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.*;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Movie;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.MovieTranslation;
import com.rsargsyan.metafilm.main_ctx.core.domain.event.MovieTmdbIdSetEvent;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.MovieRepository;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.MovieTranslationRepository;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class MovieService {

  private final MovieRepository movieRepository;
  private final MovieTranslationRepository movieTranslationRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final S3Presigner s3Presigner;
  private final String s3Bucket;

  @Autowired
  public MovieService(MovieRepository movieRepository,
                      MovieTranslationRepository movieTranslationRepository,
                      ApplicationEventPublisher eventPublisher,
                      S3Presigner s3Presigner,
                      Config config) {
    this.movieRepository = movieRepository;
    this.movieTranslationRepository = movieTranslationRepository;
    this.eventPublisher = eventPublisher;
    this.s3Presigner = s3Presigner;
    this.s3Bucket = config.s3Bucket;
  }

  public Page<MovieDTO> listMovies(Pageable pageable) {
    return movieRepository.findAll(pageable).map(MovieDTO::from);
  }

  @Transactional
  public MovieDetailDTO getMovieDetail(String movieIdStr) {
    Long movieId = Util.validateTSID(movieIdStr);
    Movie movie = movieRepository.findById(movieId).orElseThrow(ResourceNotFoundException::new);
    List<MovieTranslation> translations = movieTranslationRepository.findByMovieIdWithImages(movieId);
    List<MovieTranslationDTO> translationDTOs = translations.stream()
        .map(this::toTranslationDTO)
        .sorted(Comparator.comparing(t -> t.locale().name()))
        .toList();
    return MovieDetailDTO.from(movie, movie.isSyncInProgress(), translationDTOs);
  }

  private MovieTranslationDTO toTranslationDTO(MovieTranslation t) {
    List<MovieImageDTO> images = t.getImages().stream()
        .map(img -> new MovieImageDTO(
            img.getType(),
            presignUrl(img.getPath()),
            img.getBlurhash(),
            img.getExternalSource() != null ? img.getExternalSource().name() : null,
            img.getExternalPath()
        ))
        .toList();
    return new MovieTranslationDTO(t.getStrId(), t.getLocale(), t.getTitle(), t.getOverview(), t.getTagline(), images);
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
  public MovieDTO createMovie(MovieCreationDTO dto) {
    Movie movie = new Movie(dto.originalTitle(), dto.originalLocale(), dto.releaseDate(),
        null, null, null);
    movieRepository.save(movie);
    return MovieDTO.from(movie);
  }

  @Transactional
  public MovieDTO setTmdbId(String movieIdStr, Long tmdbId) {
    Long movieId = Util.validateTSID(movieIdStr);
    Movie movie = movieRepository.findById(movieId).orElseThrow(ResourceNotFoundException::new);
    movie.setTmdbId(tmdbId);
    movieRepository.save(movie);
    eventPublisher.publishEvent(new MovieTmdbIdSetEvent(movieIdStr));
    return MovieDTO.from(movie);
  }
}
