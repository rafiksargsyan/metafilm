package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.MovieService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class PublicMovieController {

  private final MovieService movieService;

  @Autowired
  public PublicMovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping
  public ResponseEntity<Page<MovieDTO>> listMovies(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(movieService.listMovies(pageable));
  }

  @GetMapping("/{movieId}")
  public ResponseEntity<MovieDetailDTO> getMovie(@PathVariable String movieId) {
    return ResponseEntity.ok(movieService.getMovieDetail(movieId));
  }
}
