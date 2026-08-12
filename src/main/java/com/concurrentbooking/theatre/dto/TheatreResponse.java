package com.concurrentbooking.theatre.dto;

import com.concurrentbooking.theatre.entity.Theatre;
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
@Schema(description = "Theatre details.")
public class TheatreResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String theatreId;
  private String name;
  private String city;
  private String address;
  private Instant createdAt;

  public static TheatreResponse from(Theatre theatre) {
    return new TheatreResponse(theatre.getTheatreId(), theatre.getName(), theatre.getCity(),
        theatre.getAddress(), theatre.getCreatedAt());
  }
}
