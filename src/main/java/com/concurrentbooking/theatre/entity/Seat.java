package com.concurrentbooking.theatre.entity;

import com.concurrentbooking.common.util.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(name = "seats", uniqueConstraints = {
    @UniqueConstraint(name = "uk_seat_screen_row_number", columnNames = {"screen_id", "row_label",
        "seat_number"})
})
public class Seat {

  @Id
  @Column(name = "seat_id", nullable = false, length = 36)
  private String seatId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "screen_id", nullable = false)
  private Screen screen;

  @Column(name = "row_label", nullable = false)
  private String rowLabel;

  @Column(name = "seat_number", nullable = false)
  private Integer seatNumber;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (seatId == null) {
      seatId = IdGenerator.uuidV7();
    }
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  public String getSeatId() {
    return seatId;
  }

  public Screen getScreen() {
    return screen;
  }

  public void setScreen(Screen screen) {
    this.screen = screen;
  }

  public String getRowLabel() {
    return rowLabel;
  }

  public void setRowLabel(String rowLabel) {
    this.rowLabel = rowLabel;
  }

  public Integer getSeatNumber() {
    return seatNumber;
  }

  public void setSeatNumber(Integer seatNumber) {
    this.seatNumber = seatNumber;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
