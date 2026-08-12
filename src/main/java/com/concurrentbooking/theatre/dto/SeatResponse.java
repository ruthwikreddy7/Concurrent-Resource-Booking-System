package com.concurrentbooking.theatre.dto;

import com.concurrentbooking.theatre.entity.Seat;
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
@Schema(description = "Physical seat details.")
public class SeatResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String seatId;
  private String screenId;
  private String rowLabel;
  private Integer seatNumber;
  private Instant createdAt;

  public static SeatResponse from(Seat seat) {
    return new SeatResponse(seat.getSeatId(), seat.getScreen().getScreenId(), seat.getRowLabel(),
        seat.getSeatNumber(), seat.getCreatedAt());
  }
}
