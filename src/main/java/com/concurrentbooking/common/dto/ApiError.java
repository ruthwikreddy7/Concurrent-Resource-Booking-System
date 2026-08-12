package com.concurrentbooking.common.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private HttpStatus status;
  private Integer errorCode;
  private String message;
  private String actionRequired;
  private String category;
}
