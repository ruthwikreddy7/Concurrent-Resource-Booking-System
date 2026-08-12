package com.concurrentbooking.theatre.repository;

import com.concurrentbooking.theatre.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheatreRepository extends JpaRepository<Theatre, String> {
}
