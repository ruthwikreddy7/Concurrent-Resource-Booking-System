package com.concurrentbooking.show.dto;

import com.concurrentbooking.show.entity.ShowSeat;
import com.concurrentbooking.show.enums.ShowSeatStatus;
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
@Schema(description = "Availability state for one physical seat in one show.")
public class ShowSeatResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String showSeatId;
  private String showId;
  private String seatId;
  private String rowLabel;
  private Integer seatNumber;
  private ShowSeatStatus status;
  private Long version;
  private Instant createdAt;
  private Instant updatedAt;

  public static ShowSeatResponse from(ShowSeat showSeat) {
    return new ShowSeatResponse(showSeat.getShowSeatId(), showSeat.getShow().getShowId(),
        showSeat.getSeat().getSeatId(), showSeat.getSeat().getRowLabel(),
        showSeat.getSeat().getSeatNumber(), showSeat.getStatus(), showSeat.getVersion(),
        showSeat.getCreatedAt(), showSeat.getUpdatedAt());
  }
}
