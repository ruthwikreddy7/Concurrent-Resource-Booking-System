package com.concurrentbooking.theatre.repository;

import com.concurrentbooking.theatre.entity.Seat;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, String> {

  List<Seat> findByScreenScreenIdOrderByRowLabelAscSeatNumberAsc(String screenId);

  Page<Seat> findByScreenScreenId(String screenId, Pageable pageable);
}
