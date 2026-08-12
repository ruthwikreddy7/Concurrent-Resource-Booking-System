package com.concurrentbooking.common.error;

public class ResourceNotFoundException extends ApiException {

  public ResourceNotFoundException(String resourceName, String id) {
    super(BookingErrors.RESOURCE_NOT_FOUND, resourceName + " not found: " + id);
  }
}
