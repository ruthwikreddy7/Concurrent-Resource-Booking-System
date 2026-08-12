package com.concurrentbooking.common.error;

import com.concurrentbooking.common.dto.ApiError;
import java.util.List;

public class ApiException extends RuntimeException {

  private final ApiError error;
  private final List<String> details;

  public ApiException(ApiError error) {
    this(error, error.getMessage(), List.of());
  }

  public ApiException(ApiError error, String message) {
    this(error, message, List.of());
  }

  public ApiException(ApiError error, List<String> details) {
    this(error, error.getMessage(), details);
  }

  public ApiException(ApiError error, String message, List<String> details) {
    super(message);
    this.error = error;
    this.details = details;
  }

  public ApiError getError() {
    return error;
  }

  public List<String> getDetails() {
    return details;
  }
}
