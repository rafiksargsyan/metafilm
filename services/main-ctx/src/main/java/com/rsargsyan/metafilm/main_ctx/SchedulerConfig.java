package com.rsargsyan.metafilm.main_ctx;

import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.Schedules;
import com.rsargsyan.metafilm.main_ctx.core.app.MovieSyncService;
import com.rsargsyan.metafilm.main_ctx.core.app.TVShowSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class SchedulerConfig {

  @Bean
  public Task<Void> movieSyncTask(
      MovieSyncService movieSyncService,
      @Value("${scheduler.movie-sync.interval:PT24H}") Duration interval) {
    return Tasks.recurring("movie-sync", Schedules.fixedDelay(interval))
        .execute((instance, ctx) -> {
          log.info("Starting movie sync");
          movieSyncService.syncAll();
          log.info("Finished movie sync");
        });
  }

  @Bean
  public Task<Void> tvShowSyncTask(
      TVShowSyncService tvShowSyncService,
      @Value("${scheduler.tvshow-sync.interval:PT24H}") Duration interval) {
    return Tasks.recurring("tvshow-sync", Schedules.fixedDelay(interval))
        .execute((instance, ctx) -> {
          log.info("Starting tvShow sync");
          tvShowSyncService.syncAll();
          log.info("Finished tvShow sync");
        });
  }

  @Bean
  public Task<Void> tvShowEpisodeTranslationSyncTask(
      TVShowSyncService tvShowSyncService,
      @Value("${scheduler.tvshow-episode-translation-sync.interval:PT24H}") Duration interval) {
    return Tasks.recurring("tvshow-episode-translation-sync", Schedules.fixedDelay(interval))
        .execute((instance, ctx) -> {
          log.info("Starting tvShow episode translation sync");
          tvShowSyncService.syncAllEpisodeTranslations();
          log.info("Finished tvShow episode translation sync");
        });
  }
}
