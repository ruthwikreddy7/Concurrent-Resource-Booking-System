package com.concurrentbooking.user.dto;

import com.concurrentbooking.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Booking user details.")
public class UserResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String userId;
  private String name;
  private String email;
  private Instant createdAt;

  public static UserResponse from(User user) {
    return new UserResponse(user.getUserId(), user.getName(), user.getEmail(),
        user.getCreatedAt());
  }
}
