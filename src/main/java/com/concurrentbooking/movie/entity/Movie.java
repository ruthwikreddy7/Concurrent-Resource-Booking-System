package com.concurrentbooking.movie.entity;

import com.concurrentbooking.common.util.IdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "movies")
public class Movie {

  @Id
  @Column(name = "movie_id", nullable = false, length = 36)
  private String movieId;

  @Column(nullable = false)
  private String title;

  @Column(name = "duration_minutes", nullable = false)
  private Integer durationMinutes;

  @Column(nullable = false)
  private String language;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (movieId == null) {
      movieId = IdGenerator.uuidV7();
    }
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  public String getMovieId() {
    return movieId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
