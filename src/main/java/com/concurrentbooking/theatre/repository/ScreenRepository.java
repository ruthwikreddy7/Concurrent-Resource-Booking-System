package com.concurrentbooking.theatre.repository;

import com.concurrentbooking.theatre.entity.Screen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenRepository extends JpaRepository<Screen, String> {

  Page<Screen> findByTheatreTheatreId(String theatreId, Pageable pageable);
}
