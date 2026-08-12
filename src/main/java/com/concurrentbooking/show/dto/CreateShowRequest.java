package com.concurrentbooking.show.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create one movie show and initialize its show-seat inventory.")
public class CreateShowRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotBlank
  private String movieId;

  @NotBlank
  private String screenId;

  @NotNull
  private Instant startTime;
}
