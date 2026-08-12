package com.concurrentbooking.booking.controller;

import com.concurrentbooking.booking.dto.BookingResponse;
import com.concurrentbooking.booking.dto.CreateBookingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RequestMapping("/api/v1/bookings")
@Tag(name = "Booking Controller", description = "Naive phase 0 APIs to hold, confirm, cancel, and read bookings.")
public interface IBookingController {

  @PostMapping
  @Operation(summary = "Create booking hold", description = "Naively checks requested show seats, creates a HELD booking, and marks seats HELD without concurrency optimization. Reusing the same X-Idempotency-Key with the same request returns the first create response.")
  ResponseEntity<BookingResponse> createBooking(
      @Parameter(description = "Required idempotency key. Reuse the same value to safely retry the same create-booking request.", required = true)
      @RequestHeader("X-Idempotency-Key") @NotBlank String idempotencyKey,
      @Valid @RequestBody CreateBookingRequest request);

  @PostMapping("/{bookingId}/confirm")
  @Operation(summary = "Confirm booking", description = "Confirms an unexpired HELD booking and marks its seats BOOKED.")
  BookingResponse confirmBooking(@PathVariable String bookingId);

  @PostMapping("/{bookingId}/cancel")
  @Operation(summary = "Cancel booking", description = "Cancels a HELD booking and releases its seats.")
  BookingResponse cancelBooking(@PathVariable String bookingId);

  @GetMapping("/{bookingId}")
  @Operation(summary = "Get booking", description = "Returns one booking by ID.")
  BookingResponse getBooking(@PathVariable String bookingId);
}
