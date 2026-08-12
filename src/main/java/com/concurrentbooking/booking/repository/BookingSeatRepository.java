package com.concurrentbooking.booking.repository;

import com.concurrentbooking.booking.entity.BookingSeat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, String> {

  List<BookingSeat> findByBookingBookingId(String bookingId);
}
