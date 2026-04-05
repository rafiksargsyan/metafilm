package com.rsargsyan.metafilm.main_ctx.adapters.driving.controllers;

import com.rsargsyan.metafilm.main_ctx.core.app.MovieService;
import com.rsargsyan.metafilm.main_ctx.core.app.MovieSyncService;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieDetailDTO;
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
  private final MovieSyncService movieSyncService;

  @Autowired
  public MovieController(MovieService movieService, MovieSyncService movieSyncService) {
    this.movieService = movieService;
    this.movieSyncService = movieSyncService;
  }

  @GetMapping
  public ResponseEntity<Page<MovieDTO>> listMovies(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(movieService.listMovies(pageable));
  }

  @GetMapping("/{movieId}")
  public ResponseEntity<MovieDetailDTO> getMovie(@PathVariable String movieId) {
    return ResponseEntity.ok(movieService.getMovieDetail(movieId));
  }

  @PostMapping
  public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieCreationDTO dto) {
    return new ResponseEntity<>(movieService.createMovie(dto), HttpStatus.CREATED);
  }

  @PatchMapping("/{movieId}/tmdb-id")
  public ResponseEntity<MovieDetailDTO> setTmdbId(@PathVariable String movieId,
                                                   @RequestBody Long tmdbId) {
    movieService.setTmdbId(movieId, tmdbId);
    return ResponseEntity.ok(movieService.getMovieDetail(movieId));
  }

  @PostMapping("/{movieId}/sync")
  public ResponseEntity<Void> syncMovie(@PathVariable String movieId) {
    movieSyncService.syncExternal(movieId);
    return ResponseEntity.noContent().build();
  }
}
