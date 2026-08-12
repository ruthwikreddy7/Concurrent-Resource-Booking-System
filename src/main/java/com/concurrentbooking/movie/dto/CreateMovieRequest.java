package com.concurrentbooking.movie.dto;

import com.concurrentbooking.common.util.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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
@Schema(description = "Request to create a movie.")
public class CreateMovieRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotBlank
  @Pattern(regexp = ValidationPatterns.ALPHANUMERIC_WITH_SPACES,
      message = "must contain only alphanumeric characters and spaces")
  private String title;

  @Min(1)
  private Integer durationMinutes;

  @NotBlank
  @Pattern(regexp = ValidationPatterns.ALPHABETIC_NO_SPACES,
      message = "must contain only alphabetic characters with no spaces")
  private String language;
}
