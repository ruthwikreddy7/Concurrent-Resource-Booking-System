package com.concurrentbooking.movie.controller;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.movie.dto.CreateMovieRequest;
import com.concurrentbooking.movie.dto.MovieResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/movies")
@Tag(name = "Movie Controller", description = "APIs to manage movie metadata.")
public interface IMovieController {

  @PostMapping
  @Operation(summary = "Create movie", description = "Creates one movie record.")
  ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody CreateMovieRequest request);

  @GetMapping("/{movieId}")
  @Operation(summary = "Get movie", description = "Returns one movie by ID.")
  MovieResponse getMovie(@PathVariable String movieId);

  @GetMapping
  @Operation(summary = "List movies", description = "Returns all movies.")
  Page<MovieResponse> listMovies(@Valid @ParameterObject PaginationRequest pagination);
}
