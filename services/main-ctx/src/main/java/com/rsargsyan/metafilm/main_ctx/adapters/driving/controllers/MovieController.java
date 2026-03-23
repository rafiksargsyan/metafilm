package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.MovieService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/movie")
public class MovieController {

  private final MovieService movieService;

  @Autowired
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @PostMapping
  public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieCreationDTO dto) {
    return new ResponseEntity<>(movieService.createMovie(dto), HttpStatus.CREATED);
  }
}
