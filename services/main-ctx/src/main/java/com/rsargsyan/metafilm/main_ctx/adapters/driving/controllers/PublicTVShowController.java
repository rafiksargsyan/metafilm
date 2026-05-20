package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.SeasonService;
import com.rsargsyan.metafilm.main_ctx.core.app.TVShowService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.SeasonDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.SeasonDetailDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tvshow")
public class PublicTVShowController {

  private final TVShowService tvShowService;
  private final SeasonService seasonService;

  @Autowired
  public PublicTVShowController(TVShowService tvShowService, SeasonService seasonService) {
    this.tvShowService = tvShowService;
    this.seasonService = seasonService;
  }

  @GetMapping
  public ResponseEntity<Page<com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowDTO>> listTVShows(
      @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(tvShowService.listTVShows(pageable));
  }

  @GetMapping("/{tvShowId}")
  public ResponseEntity<TVShowDetailDTO> getTVShow(@PathVariable String tvShowId) {
    return ResponseEntity.ok(tvShowService.getTVShowDetail(tvShowId));
  }

  @GetMapping("/{tvShowId}/season")
  public ResponseEntity<List<SeasonDTO>> listSeasons(@PathVariable String tvShowId) {
    return ResponseEntity.ok(seasonService.listSeasons(tvShowId));
  }

  @GetMapping("/{tvShowId}/season/{seasonId}")
  public ResponseEntity<SeasonDetailDTO> getSeason(@PathVariable String tvShowId,
                                                    @PathVariable String seasonId) {
    return ResponseEntity.ok(seasonService.getSeason(tvShowId, seasonId));
  }
}
