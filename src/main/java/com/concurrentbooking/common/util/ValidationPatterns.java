package com.concurrentbooking.common.util;

public final class ValidationPatterns {

  public static final String ALPHANUMERIC_NO_SPACES = "^[A-Za-z0-9]+$";
  public static final String ALPHABETIC_NO_SPACES = "^[A-Za-z]+$";
  public static final String ALPHANUMERIC_WITH_SPACES = "^[A-Za-z0-9 ]+$";

  private ValidationPatterns() {
  }
}
