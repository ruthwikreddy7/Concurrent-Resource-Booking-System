package com.concurrentbooking.movie.dto;

import com.concurrentbooking.movie.entity.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Movie details.")
public class MovieResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String movieId;
  private String title;
  private Integer durationMinutes;
  private String language;
  private Instant createdAt;

  public static MovieResponse from(Movie movie) {
    return new MovieResponse(movie.getMovieId(), movie.getTitle(), movie.getDurationMinutes(),
        movie.getLanguage(), movie.getCreatedAt());
  }
}
