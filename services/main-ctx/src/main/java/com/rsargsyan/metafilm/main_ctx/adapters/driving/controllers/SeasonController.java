package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.SeasonService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.SeasonCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.SeasonDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.SeasonDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tvshow/{tvShowId}/season")
public class SeasonController {

  private final SeasonService seasonService;

  @Autowired
  public SeasonController(SeasonService seasonService) {
    this.seasonService = seasonService;
  }

  @GetMapping
  public ResponseEntity<List<SeasonDTO>> listSeasons(@PathVariable String tvShowId) {
    return ResponseEntity.ok(seasonService.listSeasons(tvShowId));
  }

  @GetMapping("/{seasonId}")
  public ResponseEntity<SeasonDetailDTO> getSeason(@PathVariable String tvShowId,
                                                   @PathVariable String seasonId) {
    return ResponseEntity.ok(seasonService.getSeason(tvShowId, seasonId));
  }

  @PostMapping
  public ResponseEntity<SeasonDTO> createSeason(@PathVariable String tvShowId,
                                                @RequestBody SeasonCreationDTO dto) {
    return new ResponseEntity<>(seasonService.createSeason(tvShowId, dto), HttpStatus.CREATED);
  }
}
