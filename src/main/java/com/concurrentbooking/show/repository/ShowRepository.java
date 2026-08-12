package com.concurrentbooking.show.repository;

import com.concurrentbooking.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, String> {
}
