package com.concurrentbooking.booking.service.impl;

import com.concurrentbooking.booking.dto.BookingResponse;
import com.concurrentbooking.booking.dto.CreateBookingRequest;
import com.concurrentbooking.booking.entity.Booking;
import com.concurrentbooking.booking.entity.BookingIdempotencyRecord;
import com.concurrentbooking.booking.entity.BookingSeat;
import com.concurrentbooking.booking.enums.BookingStatus;
import com.concurrentbooking.booking.repository.BookingIdempotencyRecordRepository;
import com.concurrentbooking.booking.repository.BookingRepository;
import com.concurrentbooking.booking.repository.BookingSeatRepository;
import com.concurrentbooking.booking.service.IBookingService;
import com.concurrentbooking.common.config.BookingServiceProperties;
import com.concurrentbooking.common.error.ApiException;
import com.concurrentbooking.common.error.BookingErrors;
import com.concurrentbooking.common.error.BookingConflictException;
import com.concurrentbooking.common.error.ResourceNotFoundException;
import com.concurrentbooking.show.entity.Show;
import com.concurrentbooking.show.entity.ShowSeat;
import com.concurrentbooking.show.enums.ShowSeatStatus;
import com.concurrentbooking.show.service.IShowService;
import com.concurrentbooking.user.entity.User;
import com.concurrentbooking.user.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {

  private final BookingRepository bookingRepository;
  private final BookingIdempotencyRecordRepository idempotencyRecordRepository;
  private final BookingSeatRepository bookingSeatRepository;
  private final IUserService userService;
  private final IShowService showService;
  private final BookingServiceProperties properties;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public BookingResponse createHold(String idempotencyKey, CreateBookingRequest request) {
    String normalizedKey = normalizeIdempotencyKey(idempotencyKey);
    String requestHash = hashRequest(request);
    BookingIdempotencyRecord existingRecord = idempotencyRecordRepository
        .findByIdempotencyKey(normalizedKey)
        .orElse(null);
    if (existingRecord != null) {
      validateSameRequest(existingRecord, requestHash);
      return readStoredResponse(existingRecord);
    }

    User user = userService.getEntity(request.getUserId());
    Show show = showService.getEntity(request.getShowId());
    List<ShowSeat> showSeats = showService.getShowSeatEntities(request.getShowId(),
        request.getShowSeatIds());

    List<String> unavailableSeatIds = showSeats.stream()
        .filter(seat -> seat.getStatus() != ShowSeatStatus.AVAILABLE)
        .map(ShowSeat::getShowSeatId)
        .toList();
    if (!unavailableSeatIds.isEmpty()) {
      throw new BookingConflictException("Seats are not available: " + unavailableSeatIds, true);
    }

    Booking booking = new Booking();
    booking.setUser(user);
    booking.setShow(show);
    booking.setStatus(BookingStatus.HELD);
    booking.setExpiresAt(Instant.now().plus(properties.getHoldDuration()));
    booking.setIdempotencyKey(normalizedKey);
    Booking savedBooking = bookingRepository.save(booking);

    List<BookingSeat> bookingSeats = showSeats.stream()
        .map(showSeat -> holdSeat(savedBooking, showSeat))
        .toList();
    bookingSeatRepository.saveAll(bookingSeats);
    BookingResponse response = BookingResponse.from(savedBooking, bookingSeats);
    saveIdempotencyRecord(normalizedKey, requestHash, response);
    return response;
  }

  @Override
  @Transactional
  public BookingResponse confirm(String bookingId) {
    Booking booking = getEntity(bookingId);
    if (booking.getStatus() != BookingStatus.HELD) {
      throw new BookingConflictException("Only HELD bookings can be confirmed");
    }
    if (!booking.getExpiresAt().isAfter(Instant.now())) {
      booking.setStatus(BookingStatus.EXPIRED);
      releaseSeats(bookingId);
      throw new BookingConflictException("Booking hold has expired");
    }

    List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingBookingId(bookingId);
    bookingSeats.forEach(bookingSeat -> bookingSeat.getShowSeat().setStatus(ShowSeatStatus.BOOKED));
    booking.setStatus(BookingStatus.CONFIRMED);
    return BookingResponse.from(booking, bookingSeats);
  }

  @Override
  @Transactional
  public BookingResponse cancel(String bookingId) {
    Booking booking = getEntity(bookingId);
    if (booking.getStatus() == BookingStatus.CONFIRMED) {
      throw new BookingConflictException("Confirmed bookings cannot be cancelled in phase 0");
    }
    if (booking.getStatus() == BookingStatus.HELD) {
      releaseSeats(bookingId);
    }
    booking.setStatus(BookingStatus.CANCELLED);
    List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingBookingId(bookingId);
    return BookingResponse.from(booking, bookingSeats);
  }

  @Override
  @Transactional(readOnly = true)
  public BookingResponse get(String bookingId) {
    Booking booking = getEntity(bookingId);
    return BookingResponse.from(booking, bookingSeatRepository.findByBookingBookingId(bookingId));
  }

  private Booking getEntity(String bookingId) {
    return bookingRepository.findById(bookingId)
        .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId));
  }

  private BookingSeat holdSeat(Booking booking, ShowSeat showSeat) {
    showSeat.setStatus(ShowSeatStatus.HELD);
    BookingSeat bookingSeat = new BookingSeat();
    bookingSeat.setBooking(booking);
    bookingSeat.setShowSeat(showSeat);
    return bookingSeat;
  }

  private void releaseSeats(String bookingId) {
    bookingSeatRepository.findByBookingBookingId(bookingId)
        .forEach(bookingSeat -> bookingSeat.getShowSeat().setStatus(ShowSeatStatus.AVAILABLE));
  }

  private String normalizeIdempotencyKey(String idempotencyKey) {
    String normalizedKey = idempotencyKey == null ? "" : idempotencyKey.trim();
    if (normalizedKey.isBlank()) {
      throw new ApiException(BookingErrors.INVALID_REQUEST,
          "X-Idempotency-Key header is required");
    }
    return normalizedKey;
  }

  private void validateSameRequest(BookingIdempotencyRecord existingRecord, String requestHash) {
    if (!existingRecord.getRequestHash().equals(requestHash)) {
      throw new BookingConflictException(
          "X-Idempotency-Key was already used with a different booking request");
    }
  }

  private BookingResponse readStoredResponse(BookingIdempotencyRecord existingRecord) {
    try {
      return objectMapper.readValue(existingRecord.getResponseBody(), BookingResponse.class);
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException("Stored idempotent booking response is unreadable", ex);
    }
  }

  private void saveIdempotencyRecord(String idempotencyKey, String requestHash,
      BookingResponse response) {
    try {
      BookingIdempotencyRecord record = new BookingIdempotencyRecord();
      record.setIdempotencyKey(idempotencyKey);
      record.setRequestHash(requestHash);
      record.setResponseStatus(201);
      record.setResponseBody(objectMapper.writeValueAsString(response));
      idempotencyRecordRepository.save(record);
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException("Booking response cannot be stored for idempotency", ex);
    }
  }

  private String hashRequest(CreateBookingRequest request) {
    String payload = request.getUserId() + "|" + request.getShowId() + "|"
        + String.join(",", request.getShowSeatIds());
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(payload.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("SHA-256 is not available", ex);
    }
  }
}
