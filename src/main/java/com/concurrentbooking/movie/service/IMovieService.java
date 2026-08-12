package com.concurrentbooking.movie.service;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.movie.dto.CreateMovieRequest;
import com.concurrentbooking.movie.dto.MovieResponse;
import com.concurrentbooking.movie.entity.Movie;
import org.springframework.data.domain.Page;

public interface IMovieService {

  MovieResponse create(CreateMovieRequest request);

  Movie getEntity(String movieId);

  MovieResponse get(String movieId);

  Page<MovieResponse> list(PaginationRequest pagination);
}
