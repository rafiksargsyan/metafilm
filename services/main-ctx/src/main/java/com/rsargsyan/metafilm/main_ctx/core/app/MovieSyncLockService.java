package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Movie;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.exception.SyncInProgressException;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
public class MovieSyncLockService {

  private static final Duration STALE_LOCK_THRESHOLD = Duration.ofHours(1);

  private final MovieRepository movieRepository;

  @Autowired
  public MovieSyncLockService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void acquireLock(Long movieId) {
    Movie movie = movieRepository.findByIdForUpdate(movieId)
        .orElseThrow(ResourceNotFoundException::new);
    if (movie.isSyncInProgress()) {
      boolean isStale = movie.getSyncStartedAt() == null ||
          movie.getSyncStartedAt().isBefore(Instant.now().minus(STALE_LOCK_THRESHOLD));
      if (!isStale) {
        throw new SyncInProgressException();
      }
      log.warn("Overriding stale sync lock for movie {} (started at {})", movieId, movie.getSyncStartedAt());
    }
    movie.markSyncStarted();
    movieRepository.save(movie);
  }

  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void releaseLock(Long movieId) {
    movieRepository.findById(movieId).ifPresent(movie -> {
      movie.markSyncFinished();
      movieRepository.save(movie);
    });
  }
}
