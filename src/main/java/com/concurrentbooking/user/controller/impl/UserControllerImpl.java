package com.concurrentbooking.user.controller.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.user.controller.IUserController;
import com.concurrentbooking.user.dto.CreateUserRequest;
import com.concurrentbooking.user.dto.UserResponse;
import com.concurrentbooking.user.service.IUserService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements IUserController {

  private final IUserService userService;

  @Override
  public ResponseEntity<UserResponse> createUser(CreateUserRequest request) {
    UserResponse response = userService.create(request);
    return ResponseEntity.created(URI.create("/api/v1/users/" + response.getUserId()))
        .body(response);
  }

  @Override
  public UserResponse getUser(String userId) {
    return userService.get(userId);
  }

  @Override
  public Page<UserResponse> listUsers(PaginationRequest pagination) {
    return userService.list(pagination);
  }
}
