package com.rsargsyan.metafilm.main_ctx;

import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.Schedules;
import com.rsargsyan.metafilm.main_ctx.core.app.MovieSyncService;
import com.rsargsyan.metafilm.main_ctx.core.app.TVShowSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Slf4j
@Configuration
public class SchedulerConfig {

  @Bean
  public Task<Void> movieDailySyncTask(MovieSyncService movieSyncService) {
    return Tasks.recurring("movie-daily-sync", Schedules.daily(LocalTime.of(2, 0)))
        .execute((instance, ctx) -> {
          log.info("Starting daily movie sync");
          movieSyncService.syncAll();
          log.info("Finished daily movie sync");
        });
  }

  @Bean
  public Task<Void> tvShowDailySyncTask(TVShowSyncService tvShowSyncService) {
    return Tasks.recurring("tvshow-daily-sync", Schedules.daily(LocalTime.of(3, 0)))
        .execute((instance, ctx) -> {
          log.info("Starting daily tvShow sync");
          tvShowSyncService.syncAll();
          log.info("Finished daily tvShow sync");
        });
  }
}
