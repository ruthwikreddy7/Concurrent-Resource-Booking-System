package com.concurrentbooking.show.repository;

import com.concurrentbooking.show.entity.ShowSeat;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, String> {

  Page<ShowSeat> findByShowShowId(String showId, Pageable pageable);

  List<ShowSeat> findByShowShowIdAndShowSeatIdIn(String showId, Collection<String> showSeatIds);
}
