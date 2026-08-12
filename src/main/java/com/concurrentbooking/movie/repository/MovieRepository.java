package com.concurrentbooking.movie.repository;

import com.concurrentbooking.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String> {
}
