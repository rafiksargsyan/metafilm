package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.MovieService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

  @GetMapping
  public ResponseEntity<Page<MovieDTO>> listMovies(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(movieService.listMovies(pageable));
  }

  @GetMapping("/{movieId}")
  public ResponseEntity<MovieDTO> getMovie(@PathVariable String movieId) {
    return ResponseEntity.ok(movieService.getMovie(movieId));
  }

  @PostMapping
  public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieCreationDTO dto) {
    return new ResponseEntity<>(movieService.createMovie(dto), HttpStatus.CREATED);
  }

  @PatchMapping("/{movieId}/tmdb-id")
  public ResponseEntity<MovieDTO> setTmdbId(@PathVariable String movieId,
                                            @RequestBody Long tmdbId) {
    return ResponseEntity.ok(movieService.setTmdbId(movieId, tmdbId));
  }
}
