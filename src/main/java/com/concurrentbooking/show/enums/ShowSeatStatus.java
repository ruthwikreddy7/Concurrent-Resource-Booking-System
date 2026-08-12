package com.concurrentbooking.show.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ShowSeatStatus {
  AVAILABLE("AVAILABLE"),
  HELD("HELD"),
  BOOKED("BOOKED");

  private final String value;

  ShowSeatStatus(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
