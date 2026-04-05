package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.TVShowService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tvshow")
public class PublicTVShowController {

  private final TVShowService tvShowService;

  @Autowired
  public PublicTVShowController(TVShowService tvShowService) {
    this.tvShowService = tvShowService;
  }

  @GetMapping
  public ResponseEntity<Page<TVShowDTO>> listTVShows(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(tvShowService.listTVShows(pageable));
  }

  @GetMapping("/{tvShowId}")
  public ResponseEntity<TVShowDTO> getTVShow(@PathVariable String tvShowId) {
    return ResponseEntity.ok(tvShowService.getTVShow(tvShowId));
  }
}
