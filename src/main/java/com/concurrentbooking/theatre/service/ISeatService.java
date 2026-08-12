package com.concurrentbooking.theatre.service;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.theatre.dto.SeatResponse;
import com.concurrentbooking.theatre.entity.Seat;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ISeatService {

  Seat getEntity(String seatId);

  List<Seat> listEntitiesByScreen(String screenId);

  Page<SeatResponse> listByScreen(String screenId, PaginationRequest pagination);
}
