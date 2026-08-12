package com.concurrentbooking.show.dto;

import com.concurrentbooking.show.entity.Show;
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
@Schema(description = "Show details.")
public class ShowResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String showId;
  private String movieId;
  private String screenId;
  private Instant startTime;
  private Instant endTime;
  private Instant createdAt;

  public static ShowResponse from(Show show) {
    return new ShowResponse(show.getShowId(), show.getMovie().getMovieId(),
        show.getScreen().getScreenId(), show.getStartTime(), show.getEndTime(),
        show.getCreatedAt());
  }
}
