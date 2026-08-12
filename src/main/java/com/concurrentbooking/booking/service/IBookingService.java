package com.concurrentbooking.booking.service;

import com.concurrentbooking.booking.dto.BookingResponse;
import com.concurrentbooking.booking.dto.CreateBookingRequest;

public interface IBookingService {

  BookingResponse createHold(String idempotencyKey, CreateBookingRequest request);

  BookingResponse confirm(String bookingId);

  BookingResponse cancel(String bookingId);

  BookingResponse get(String bookingId);
}
