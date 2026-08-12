package com.concurrentbooking.theatre.service.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.common.error.ResourceNotFoundException;
import com.concurrentbooking.common.util.PageUtils;
import com.concurrentbooking.theatre.dto.SeatResponse;
import com.concurrentbooking.theatre.entity.Seat;
import com.concurrentbooking.theatre.repository.SeatRepository;
import com.concurrentbooking.theatre.service.IScreenService;
import com.concurrentbooking.theatre.service.ISeatService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements ISeatService {

  private final SeatRepository seatRepository;
  private final IScreenService screenService;
  private final PageUtils pageUtils;

  @Override
  @Transactional(readOnly = true)
  public Seat getEntity(String seatId) {
    return seatRepository.findById(seatId)
        .orElseThrow(() -> new ResourceNotFoundException("Seat", seatId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Seat> listEntitiesByScreen(String screenId) {
    screenService.getEntity(screenId);
    return seatRepository.findByScreenScreenIdOrderByRowLabelAscSeatNumberAsc(screenId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SeatResponse> listByScreen(String screenId, PaginationRequest pagination) {
    screenService.getEntity(screenId);
    return seatRepository.findByScreenScreenId(screenId, pageUtils.toPageable(pagination,
        Set.of("seatId", "rowLabel", "seatNumber", "createdAt"))).map(SeatResponse::from);
  }
}
