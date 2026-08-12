package com.concurrentbooking.movie.controller.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.movie.controller.IMovieController;
import com.concurrentbooking.movie.dto.CreateMovieRequest;
import com.concurrentbooking.movie.dto.MovieResponse;
import com.concurrentbooking.movie.service.IMovieService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class MovieControllerImpl implements IMovieController {

  private final IMovieService movieService;

  @Override
  public ResponseEntity<MovieResponse> createMovie(CreateMovieRequest request) {
    MovieResponse response = movieService.create(request);
    return ResponseEntity.created(URI.create("/api/v1/movies/" + response.getMovieId()))
        .body(response);
  }

  @Override
  public MovieResponse getMovie(String movieId) {
    return movieService.get(movieId);
  }

  @Override
  public Page<MovieResponse> listMovies(PaginationRequest pagination) {
    return movieService.list(pagination);
  }
}
