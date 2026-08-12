package com.concurrentbooking.common.error;

import com.concurrentbooking.common.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MissingRequestHeaderException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiErrorResponse> handleApiException(
      ApiException ex, HttpServletRequest request) {
    return build(ex.getError().getStatus(), ex.getError().getErrorCode(), ex.getMessage(),
        ex.getError().getActionRequired(), ex.getError().getCategory(), request, ex.getDetails());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> details = ex.getBindingResult().getFieldErrors().stream()
        .map(this::formatFieldError)
        .toList();
    return build(HttpStatus.BAD_REQUEST, BookingErrors.INVALID_REQUEST.getErrorCode(),
        "Request validation failed", BookingErrors.INVALID_REQUEST.getActionRequired(),
        BookingErrors.INVALID_REQUEST.getCategory(), request, details);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest request) {
    List<String> details = ex.getConstraintViolations().stream()
        .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
        .toList();
    return build(HttpStatus.BAD_REQUEST, BookingErrors.INVALID_REQUEST.getErrorCode(),
        "Request validation failed", BookingErrors.INVALID_REQUEST.getActionRequired(),
        BookingErrors.INVALID_REQUEST.getCategory(), request, details);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ApiErrorResponse> handleMissingRequestHeader(
      MissingRequestHeaderException ex, HttpServletRequest request) {
    return build(HttpStatus.BAD_REQUEST, BookingErrors.INVALID_REQUEST.getErrorCode(),
        ex.getHeaderName() + " header is required",
        BookingErrors.INVALID_REQUEST.getActionRequired(),
        BookingErrors.INVALID_REQUEST.getCategory(), request, List.of());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
      IllegalArgumentException ex, HttpServletRequest request) {
    return build(HttpStatus.BAD_REQUEST, BookingErrors.INVALID_REQUEST.getErrorCode(), ex.getMessage(),
        BookingErrors.INVALID_REQUEST.getActionRequired(), BookingErrors.INVALID_REQUEST.getCategory(),
        request, List.of());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException ex, HttpServletRequest request) {
    return build(HttpStatus.CONFLICT, BookingErrors.DATA_INTEGRITY_VIOLATION.getErrorCode(),
        BookingErrors.DATA_INTEGRITY_VIOLATION.getMessage(),
        BookingErrors.DATA_INTEGRITY_VIOLATION.getActionRequired(),
        BookingErrors.DATA_INTEGRITY_VIOLATION.getCategory(), request, List.of());
  }

  private ResponseEntity<ApiErrorResponse> build(HttpStatus status, Integer code, String message,
      String actionRequired, String category, HttpServletRequest request, List<String> details) {
    ApiErrorResponse body = new ApiErrorResponse(code, message, actionRequired, category,
        request.getRequestURI(), Instant.now(), details);
    return ResponseEntity.status(status).body(body);
  }

  private String formatFieldError(FieldError error) {
    return error.getField() + " " + error.getDefaultMessage();
  }
}
