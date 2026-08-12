package com.concurrentbooking.booking.entity;

import com.concurrentbooking.common.util.IdGenerator;
import com.concurrentbooking.show.entity.ShowSeat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "booking_seats")
public class BookingSeat {

  @Id
  @Column(name = "booking_seat_id", nullable = false, length = 36)
  private String bookingSeatId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "booking_id", nullable = false)
  private Booking booking;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "show_seat_id", nullable = false)
  private ShowSeat showSeat;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (bookingSeatId == null) {
      bookingSeatId = IdGenerator.uuidV7();
    }
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  public String getBookingSeatId() {
    return bookingSeatId;
  }

  public Booking getBooking() {
    return booking;
  }

  public void setBooking(Booking booking) {
    this.booking = booking;
  }

  public ShowSeat getShowSeat() {
    return showSeat;
  }

  public void setShowSeat(ShowSeat showSeat) {
    this.showSeat = showSeat;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
