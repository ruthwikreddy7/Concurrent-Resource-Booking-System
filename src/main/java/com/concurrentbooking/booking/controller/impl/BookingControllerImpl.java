package com.concurrentbooking.booking.controller.impl;

import com.concurrentbooking.booking.controller.IBookingController;
import com.concurrentbooking.booking.dto.BookingResponse;
import com.concurrentbooking.booking.dto.CreateBookingRequest;
import com.concurrentbooking.booking.service.IBookingService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class BookingControllerImpl implements IBookingController {

  private final IBookingService bookingService;

  @Override
  public ResponseEntity<BookingResponse> createBooking(String idempotencyKey,
      CreateBookingRequest request) {
    BookingResponse response = bookingService.createHold(idempotencyKey, request);
    return ResponseEntity.created(URI.create("/api/v1/bookings/" + response.getBookingId()))
        .body(response);
  }

  @Override
  public BookingResponse confirmBooking(String bookingId) {
    return bookingService.confirm(bookingId);
  }

  @Override
  public BookingResponse cancelBooking(String bookingId) {
    return bookingService.cancel(bookingId);
  }

  @Override
  public BookingResponse getBooking(String bookingId) {
    return bookingService.get(bookingId);
  }
}
