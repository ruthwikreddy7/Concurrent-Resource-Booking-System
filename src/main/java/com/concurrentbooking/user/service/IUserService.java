package com.concurrentbooking.user.service;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.user.dto.CreateUserRequest;
import com.concurrentbooking.user.dto.UserResponse;
import com.concurrentbooking.user.entity.User;
import org.springframework.data.domain.Page;

public interface IUserService {

  UserResponse create(CreateUserRequest request);

  User getEntity(String userId);

  UserResponse get(String userId);

  Page<UserResponse> list(PaginationRequest pagination);
}
