package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.domain.event.MovieTmdbIdSetEvent;
import com.rsargsyan.metafilm.main_ctx.core.domain.event.TVShowSyncRequestedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ApplicationEventListener {

  private final MovieSyncService movieSyncService;
  private final TVShowSyncService tvShowSyncService;

  @Autowired
  public ApplicationEventListener(MovieSyncService movieSyncService,
                                   TVShowSyncService tvShowSyncService) {
    this.movieSyncService = movieSyncService;
    this.tvShowSyncService = tvShowSyncService;
  }

  @Async
  @TransactionalEventListener
  public void handleMovieTmdbIdSetEvent(MovieTmdbIdSetEvent event) {
    log.info("Received MovieTmdbIdSetEvent for movie {}, starting sync", event.movieId());
    movieSyncService.syncExternal(event.movieId());
  }

  @Async
  @TransactionalEventListener
  public void handleTVShowSyncRequestedEvent(TVShowSyncRequestedEvent event) {
    log.info("Received TVShowSyncRequestedEvent for tvShow {}, starting sync", event.tvShowId());
    tvShowSyncService.syncExternal(event.tvShowId());
  }
}
