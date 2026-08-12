package com.concurrentbooking.booking.dto;

import com.concurrentbooking.booking.entity.BookingSeat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Seat attached to a booking.")
public class BookingSeatResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String bookingSeatId;
  private String showSeatId;
  private String rowLabel;
  private Integer seatNumber;

  public static BookingSeatResponse from(BookingSeat bookingSeat) {
    return new BookingSeatResponse(bookingSeat.getBookingSeatId(),
        bookingSeat.getShowSeat().getShowSeatId(), bookingSeat.getShowSeat().getSeat().getRowLabel(),
        bookingSeat.getShowSeat().getSeat().getSeatNumber());
  }
}
