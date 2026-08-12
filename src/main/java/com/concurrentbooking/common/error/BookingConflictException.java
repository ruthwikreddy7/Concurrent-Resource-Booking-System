package com.concurrentbooking.common.error;

public class BookingConflictException extends ApiException {

  public BookingConflictException(String message) {
    super(BookingErrors.INVALID_BOOKING_STATE, message);
  }

  public BookingConflictException(String message, boolean seatConflict) {
    super(seatConflict ? BookingErrors.SEAT_NOT_AVAILABLE : BookingErrors.INVALID_BOOKING_STATE,
        message);
  }
}
