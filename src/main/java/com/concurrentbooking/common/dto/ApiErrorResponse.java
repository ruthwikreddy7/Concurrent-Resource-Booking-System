package com.concurrentbooking.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API error response.")
public class ApiErrorResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Integer code;
  private String message;
  private String actionRequired;
  private String category;
  private String path;
  private Instant timestamp;
  private List<String> details;
}
