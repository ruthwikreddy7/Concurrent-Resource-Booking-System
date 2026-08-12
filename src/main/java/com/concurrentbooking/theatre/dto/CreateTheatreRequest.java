package com.concurrentbooking.theatre.dto;

import com.concurrentbooking.common.util.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a theatre location.")
public class CreateTheatreRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotBlank
  private String name;

  @NotBlank
  @Pattern(regexp = ValidationPatterns.ALPHANUMERIC_NO_SPACES,
      message = "must contain only alphanumeric characters with no spaces")
  private String city;

  @NotBlank
  private String address;
}
