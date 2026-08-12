package com.concurrentbooking.booking.repository;

import com.concurrentbooking.booking.entity.BookingIdempotencyRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingIdempotencyRecordRepository
    extends JpaRepository<BookingIdempotencyRecord, String> {

  Optional<BookingIdempotencyRecord> findByIdempotencyKey(String idempotencyKey);
}
