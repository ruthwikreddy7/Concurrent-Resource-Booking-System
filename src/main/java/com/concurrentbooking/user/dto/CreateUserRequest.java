package com.concurrentbooking.user.dto;

import com.concurrentbooking.common.util.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Request to create a booking user.")
public class CreateUserRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotBlank
  @Pattern(regexp = ValidationPatterns.ALPHANUMERIC_NO_SPACES,
      message = "must contain only alphanumeric characters with no spaces")
  private String name;

  @Email
  @NotBlank
  private String email;
}
