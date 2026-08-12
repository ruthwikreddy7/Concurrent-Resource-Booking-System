package com.concurrentbooking.show.controller.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.show.controller.IShowController;
import com.concurrentbooking.show.dto.CreateShowRequest;
import com.concurrentbooking.show.dto.ShowResponse;
import com.concurrentbooking.show.dto.ShowSeatResponse;
import com.concurrentbooking.show.service.IShowService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class ShowControllerImpl implements IShowController {

  private final IShowService showService;

  @Override
  public ResponseEntity<ShowResponse> createShow(CreateShowRequest request) {
    ShowResponse response = showService.create(request);
    return ResponseEntity.created(URI.create("/api/v1/shows/" + response.getShowId()))
        .body(response);
  }

  @Override
  public ShowResponse getShow(String showId) {
    return showService.get(showId);
  }

  @Override
  public Page<ShowSeatResponse> listShowSeats(String showId, PaginationRequest pagination) {
    return showService.listSeats(showId, pagination);
  }
}
