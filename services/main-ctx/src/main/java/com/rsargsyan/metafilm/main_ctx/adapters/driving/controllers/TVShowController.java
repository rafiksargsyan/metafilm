package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.TVShowService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/tvshow")
public class TVShowController {

  private final TVShowService tvShowService;

  @Autowired
  public TVShowController(TVShowService tvShowService) {
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

  @PostMapping
  public ResponseEntity<TVShowDTO> createTVShow(@RequestBody TVShowCreationDTO dto) {
    return new ResponseEntity<>(tvShowService.createTVShow(dto), HttpStatus.CREATED);
  }

  @PatchMapping("/{tvShowId}/tmdb-id")
  public ResponseEntity<TVShowDTO> setTmdbId(@PathVariable String tvShowId, @RequestBody Long tmdbId) {
    return ResponseEntity.ok(tvShowService.setTmdbId(tvShowId, tmdbId));
  }

  @PatchMapping("/{tvShowId}/tvdb-id")
  public ResponseEntity<TVShowDTO> setTvdbId(@PathVariable String tvShowId, @RequestBody Long tvdbId) {
    return ResponseEntity.ok(tvShowService.setTvdbId(tvShowId, tvdbId));
  }

  @PatchMapping("/{tvShowId}/use-tvdb")
  public ResponseEntity<TVShowDTO> setUseTvdb(@PathVariable String tvShowId, @RequestBody boolean useTvdb) {
    return ResponseEntity.ok(tvShowService.setUseTvdb(tvShowId, useTvdb));
  }
}
