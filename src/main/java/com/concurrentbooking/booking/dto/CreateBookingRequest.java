package com.concurrentbooking.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to hold one or more seats for a show.")
public class CreateBookingRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotBlank
  private String userId;

  @NotBlank
  private String showId;

  @NotEmpty
  private List<String> showSeatIds;
}
