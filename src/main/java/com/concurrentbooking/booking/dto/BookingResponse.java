package com.concurrentbooking.booking.dto;

import com.concurrentbooking.booking.entity.Booking;
import com.concurrentbooking.booking.entity.BookingSeat;
import com.concurrentbooking.booking.enums.BookingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Booking details.")
public class BookingResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String bookingId;
  private String userId;
  private String showId;
  private BookingStatus status;
  private Instant expiresAt;
  private String idempotencyKey;
  private Instant createdAt;
  private Instant updatedAt;
  private List<BookingSeatResponse> seats;

  public static BookingResponse from(Booking booking, List<BookingSeat> bookingSeats) {
    return new BookingResponse(booking.getBookingId(), booking.getUser().getUserId(),
        booking.getShow().getShowId(), booking.getStatus(), booking.getExpiresAt(),
        booking.getIdempotencyKey(), booking.getCreatedAt(), booking.getUpdatedAt(),
        bookingSeats.stream().map(BookingSeatResponse::from).toList());
  }
}
