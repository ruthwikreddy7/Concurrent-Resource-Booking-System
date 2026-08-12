package com.concurrentbooking.user.service.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.common.error.ResourceNotFoundException;
import com.concurrentbooking.common.util.PageUtils;
import com.concurrentbooking.user.dto.CreateUserRequest;
import com.concurrentbooking.user.dto.UserResponse;
import com.concurrentbooking.user.entity.User;
import com.concurrentbooking.user.repository.UserRepository;
import com.concurrentbooking.user.service.IUserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

  private final UserRepository userRepository;
  private final PageUtils pageUtils;

  @Override
  @Transactional
  public UserResponse create(CreateUserRequest request) {
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    return UserResponse.from(userRepository.save(user));
  }

  @Override
  @Transactional(readOnly = true)
  public User getEntity(String userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", userId));
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse get(String userId) {
    return UserResponse.from(getEntity(userId));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserResponse> list(PaginationRequest pagination) {
    return userRepository.findAll(pageUtils.toPageable(pagination,
        Set.of("userId", "name", "email", "createdAt"))).map(UserResponse::from);
  }
}
