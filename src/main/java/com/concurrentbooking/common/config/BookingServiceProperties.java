package com.concurrentbooking.common.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "booking.service", ignoreInvalidFields = true)
public class BookingServiceProperties {

  private Duration holdDuration = Duration.ofMinutes(5);
  private Integer defaultPageSize = 20;
  private Integer maxPageSize = 100;

  public Duration getHoldDuration() {
    return holdDuration;
  }

  public void setHoldDuration(Duration holdDuration) {
    this.holdDuration = holdDuration;
  }

  public Integer getDefaultPageSize() {
    return defaultPageSize;
  }

  public void setDefaultPageSize(Integer defaultPageSize) {
    this.defaultPageSize = defaultPageSize;
  }

  public Integer getMaxPageSize() {
    return maxPageSize;
  }

  public void setMaxPageSize(Integer maxPageSize) {
    this.maxPageSize = maxPageSize;
  }
}
