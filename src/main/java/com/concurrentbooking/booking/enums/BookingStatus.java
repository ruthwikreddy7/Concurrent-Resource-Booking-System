package com.concurrentbooking.booking.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BookingStatus {
  HELD("HELD"),
  CONFIRMED("CONFIRMED"),
  EXPIRED("EXPIRED"),
  CANCELLED("CANCELLED");

  private final String value;

  BookingStatus(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
