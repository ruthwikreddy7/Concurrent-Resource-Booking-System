package com.concurrentbooking.user.controller;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.user.dto.CreateUserRequest;
import com.concurrentbooking.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "APIs to manage booking users.")
public interface IUserController {

  @PostMapping
  @Operation(summary = "Create user", description = "Creates one booking user.")
  ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request);

  @GetMapping("/{userId}")
  @Operation(summary = "Get user", description = "Returns one user by ID.")
  UserResponse getUser(@PathVariable String userId);

  @GetMapping
  @Operation(summary = "List users", description = "Returns all users.")
  Page<UserResponse> listUsers(@Valid @ParameterObject PaginationRequest pagination);
}
