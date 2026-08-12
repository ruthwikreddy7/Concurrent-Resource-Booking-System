package com.concurrentbooking.theatre.dto;

import com.concurrentbooking.theatre.entity.Screen;
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
@Schema(description = "Screen details.")
public class ScreenResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String screenId;
  private String theatreId;
  private String name;
  private Integer capacity;
  private Integer seatsPerRow;
  private Instant createdAt;

  public static ScreenResponse from(Screen screen) {
    return new ScreenResponse(screen.getScreenId(), screen.getTheatre().getTheatreId(),
        screen.getName(), screen.getCapacity(), screen.getSeatsPerRow(), screen.getCreatedAt());
  }
}
