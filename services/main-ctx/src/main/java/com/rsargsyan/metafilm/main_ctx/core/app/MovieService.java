package com.rsargsyan.metafilm.main_ctx.core.app;

import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieCreationDTO;
import com.rsargsyan.metafilm.main_ctx.core.app.dto.MovieDTO;
import com.rsargsyan.metafilm.main_ctx.core.domain.aggregate.Movie;
import com.rsargsyan.metafilm.main_ctx.core.domain.event.MovieTmdbIdSetEvent;
import com.rsargsyan.metafilm.main_ctx.core.exception.ResourceNotFoundException;
import com.rsargsyan.metafilm.main_ctx.core.ports.repository.MovieRepository;
import com.rsargsyan.metafilm.main_ctx.core.Util;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  private final MovieRepository movieRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Autowired
  public MovieService(MovieRepository movieRepository, ApplicationEventPublisher eventPublisher) {
    this.movieRepository = movieRepository;
    this.eventPublisher = eventPublisher;
  }

  public Page<MovieDTO> listMovies(Pageable pageable) {
    return movieRepository.findAll(pageable).map(MovieDTO::from);
  }

  public MovieDTO getMovie(String movieIdStr) {
    Long movieId = Util.validateTSID(movieIdStr);
    return MovieDTO.from(movieRepository.findById(movieId).orElseThrow(ResourceNotFoundException::new));
  }

  @Transactional
  public MovieDTO createMovie(MovieCreationDTO dto) {
    Movie movie = new Movie(dto.originalTitle(), dto.originalLocale(), dto.releaseDate(),
        null, null, null);
    movieRepository.save(movie);
    return MovieDTO.from(movie);
  }

  @Transactional
  public MovieDTO setTmdbId(String movieIdStr, Long tmdbId) {
    Long movieId = Util.validateTSID(movieIdStr);
    Movie movie = movieRepository.findById(movieId).orElseThrow(ResourceNotFoundException::new);
    movie.setTmdbId(tmdbId);
    movieRepository.save(movie);
    eventPublisher.publishEvent(new MovieTmdbIdSetEvent(movieIdStr));
    return MovieDTO.from(movie);
  }
}
