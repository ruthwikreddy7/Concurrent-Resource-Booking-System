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
import java.time.Instant;

@Entity
@Table(name = "screens")
public class Screen {

  @Id
  @Column(name = "screen_id", nullable = false, length = 36)
  private String screenId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "theatre_id", nullable = false)
  private Theatre theatre;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer capacity;

  @Column(name = "seats_per_row", nullable = false, columnDefinition = "integer default 10")
  private Integer seatsPerRow;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (screenId == null) {
      screenId = IdGenerator.uuidV7();
    }
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  public String getScreenId() {
    return screenId;
  }

  public Theatre getTheatre() {
    return theatre;
  }

  public void setTheatre(Theatre theatre) {
    this.theatre = theatre;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(Integer capacity) {
    this.capacity = capacity;
  }

  public Integer getSeatsPerRow() {
    return seatsPerRow;
  }

  public void setSeatsPerRow(Integer seatsPerRow) {
    this.seatsPerRow = seatsPerRow;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
