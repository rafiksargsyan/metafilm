package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.TVShowService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.TVShowDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping
  public ResponseEntity<TVShowDTO> createTVShow(@RequestBody TVShowCreationDTO dto) {
    return new ResponseEntity<>(tvShowService.createTVShow(dto), HttpStatus.CREATED);
  }
}
