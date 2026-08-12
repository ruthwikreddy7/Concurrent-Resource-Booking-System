package com.concurrentbooking.common.util;

import com.concurrentbooking.common.config.BookingServiceProperties;
import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.common.error.ApiException;
import com.concurrentbooking.common.error.BookingErrors;
import java.util.Locale;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageUtils {

  private final BookingServiceProperties properties;

  public Pageable toPageable(PaginationRequest request, Set<String> allowedSortFields) {
    int page = request.getPage() == null ? 0 : request.getPage();
    int requestedSize = request.getSize() == null ? properties.getDefaultPageSize() : request.getSize();
    int size = Math.min(requestedSize, properties.getMaxPageSize());
    String sortBy = request.getSortBy() == null || request.getSortBy().isBlank()
        ? "createdAt" : request.getSortBy();
    if (!allowedSortFields.contains(sortBy)) {
      throw new ApiException(BookingErrors.INVALID_QUERY_PARAM, "Unsupported sortBy field: " + sortBy);
    }
    Sort.Direction direction = resolveDirection(request.getSortOrder());
    return PageRequest.of(page, size, Sort.by(direction, sortBy));
  }

  private Sort.Direction resolveDirection(String sortOrder) {
    String value = sortOrder == null ? "desc" : sortOrder.toLowerCase(Locale.ROOT);
    if ("asc".equals(value)) {
      return Sort.Direction.ASC;
    }
    if ("desc".equals(value)) {
      return Sort.Direction.DESC;
    }
    throw new ApiException(BookingErrors.INVALID_QUERY_PARAM,
        "Unsupported sortOrder value: " + sortOrder);
  }
}
