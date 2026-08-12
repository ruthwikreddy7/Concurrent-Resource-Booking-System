package com.concurrentbooking.common.error;

import com.concurrentbooking.common.dto.ApiError;
import org.springframework.http.HttpStatus;

public final class BookingErrors {

  public static final String VALIDATION = "VALIDATION";
  public static final String NOT_FOUND = "NOT_FOUND";
  public static final String CONFLICT = "CONFLICT";

  public static final ApiError INVALID_REQUEST = new ApiError(HttpStatus.BAD_REQUEST, 400000,
      "Invalid request", "Please verify the request payload and query parameters.", VALIDATION);

  public static final ApiError INVALID_QUERY_PARAM = new ApiError(HttpStatus.BAD_REQUEST, 400100,
      "Invalid query parameter", "Please verify pagination and sorting parameters.", VALIDATION);

  public static final ApiError RESOURCE_NOT_FOUND = new ApiError(HttpStatus.NOT_FOUND, 404000,
      "Resource not found", "Please provide a valid resource ID.", NOT_FOUND);

  public static final ApiError SEAT_NOT_AVAILABLE = new ApiError(HttpStatus.CONFLICT, 409000,
      "Requested seat is not available", "Please select an available show seat.", CONFLICT);

  public static final ApiError INVALID_BOOKING_STATE = new ApiError(HttpStatus.CONFLICT, 409001,
      "Invalid booking state transition", "Please verify the booking status before this operation.",
      CONFLICT);

  public static final ApiError DATA_INTEGRITY_VIOLATION = new ApiError(HttpStatus.CONFLICT, 409002,
      "Request conflicts with an existing record or database constraint",
      "Please provide unique values where required.", CONFLICT);

  private BookingErrors() {
  }
}
