package com.concurrentbooking.theatre.controller.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.theatre.controller.IScreenController;
import com.concurrentbooking.theatre.dto.ScreenResponse;
import com.concurrentbooking.theatre.dto.SeatResponse;
import com.concurrentbooking.theatre.service.IScreenService;
import com.concurrentbooking.theatre.service.ISeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class ScreenControllerImpl implements IScreenController {

  private final IScreenService screenService;
  private final ISeatService seatService;

  @Override
  public ScreenResponse getScreen(String screenId) {
    return screenService.get(screenId);
  }

  @Override
  public Page<SeatResponse> listSeats(String screenId, PaginationRequest pagination) {
    return seatService.listByScreen(screenId, pagination);
  }
}
