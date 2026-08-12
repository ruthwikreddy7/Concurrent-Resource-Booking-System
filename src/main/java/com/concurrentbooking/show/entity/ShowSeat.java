package com.concurrentbooking.show.entity;

import com.concurrentbooking.common.util.IdGenerator;
import com.concurrentbooking.show.enums.ShowSeatStatus;
import com.concurrentbooking.theatre.entity.Seat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(name = "show_seats", uniqueConstraints = {
    @UniqueConstraint(name = "uk_show_seat_show_physical_seat", columnNames = {"show_id", "seat_id"})
})
public class ShowSeat {

  @Id
  @Column(name = "show_seat_id", nullable = false, length = 36)
  private String showSeatId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "show_id", nullable = false)
  private Show show;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "seat_id", nullable = false)
  private Seat seat;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ShowSeatStatus status;

  @Column(nullable = false)
  private Long version;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  void prePersist() {
    if (showSeatId == null) {
      showSeatId = IdGenerator.uuidV7();
    }
    if (status == null) {
      status = ShowSeatStatus.AVAILABLE;
    }
    if (version == null) {
      version = 0L;
    }
    Instant now = Instant.now();
    if (createdAt == null) {
      createdAt = now;
    }
    if (updatedAt == null) {
      updatedAt = now;
    }
  }

  @PreUpdate
  void preUpdate() {
    updatedAt = Instant.now();
  }

  public String getShowSeatId() {
    return showSeatId;
  }

  public Show getShow() {
    return show;
  }

  public void setShow(Show show) {
    this.show = show;
  }

  public Seat getSeat() {
    return seat;
  }

  public void setSeat(Seat seat) {
    this.seat = seat;
  }

  public ShowSeatStatus getStatus() {
    return status;
  }

  public void setStatus(ShowSeatStatus status) {
    this.status = status;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
