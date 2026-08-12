package com.concurrentbooking.common.util;

import com.github.f4b6a3.uuid.UuidCreator;

public final class IdGenerator {

  private IdGenerator() {
  }

  public static String uuidV7() {
    return UuidCreator.getTimeOrderedEpoch().toString();
  }
}
