package com.concurrentbooking.user.repository;

import com.concurrentbooking.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
