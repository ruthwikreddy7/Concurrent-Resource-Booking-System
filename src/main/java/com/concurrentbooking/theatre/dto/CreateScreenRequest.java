package com.concurrentbooking.theatre.dto;

import com.concurrentbooking.common.util.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a screen inside a theatre.")
public class CreateScreenRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotBlank
  private String name;

  @NotNull
  @Min(1)
  private Integer capacity;

  @NotNull
  @Min(1)
  private Integer seatsPerRow;
}
