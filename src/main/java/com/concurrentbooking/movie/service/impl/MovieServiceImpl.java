package com.concurrentbooking.movie.service.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.common.error.ResourceNotFoundException;
import com.concurrentbooking.common.util.PageUtils;
import com.concurrentbooking.movie.dto.CreateMovieRequest;
import com.concurrentbooking.movie.dto.MovieResponse;
import com.concurrentbooking.movie.entity.Movie;
import com.concurrentbooking.movie.repository.MovieRepository;
import com.concurrentbooking.movie.service.IMovieService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements IMovieService {

  private final MovieRepository movieRepository;
  private final PageUtils pageUtils;

  @Override
  @Transactional
  public MovieResponse create(CreateMovieRequest request) {
    Movie movie = new Movie();
    movie.setTitle(request.getTitle());
    movie.setDurationMinutes(request.getDurationMinutes());
    movie.setLanguage(request.getLanguage());
    return MovieResponse.from(movieRepository.save(movie));
  }

  @Override
  @Transactional(readOnly = true)
  public Movie getEntity(String movieId) {
    return movieRepository.findById(movieId)
        .orElseThrow(() -> new ResourceNotFoundException("Movie", movieId));
  }

  @Override
  @Transactional(readOnly = true)
  public MovieResponse get(String movieId) {
    return MovieResponse.from(getEntity(movieId));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MovieResponse> list(PaginationRequest pagination) {
    return movieRepository.findAll(pageUtils.toPageable(pagination,
        Set.of("movieId", "title", "durationMinutes", "language", "createdAt")))
        .map(MovieResponse::from);
  }
}
