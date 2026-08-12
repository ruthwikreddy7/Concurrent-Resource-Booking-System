package com.concurrentbooking.theatre.controller.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.theatre.controller.ITheatreController;
import com.concurrentbooking.theatre.dto.CreateScreenRequest;
import com.concurrentbooking.theatre.dto.CreateTheatreRequest;
import com.concurrentbooking.theatre.dto.ScreenResponse;
import com.concurrentbooking.theatre.dto.TheatreResponse;
import com.concurrentbooking.theatre.service.IScreenService;
import com.concurrentbooking.theatre.service.ITheatreService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class TheatreControllerImpl implements ITheatreController {

  private final ITheatreService theatreService;
  private final IScreenService screenService;

  @Override
  public ResponseEntity<TheatreResponse> createTheatre(CreateTheatreRequest request) {
    TheatreResponse response = theatreService.create(request);
    return ResponseEntity.created(URI.create("/api/v1/theatres/" + response.getTheatreId()))
        .body(response);
  }

  @Override
  public TheatreResponse getTheatre(String theatreId) {
    return theatreService.get(theatreId);
  }

  @Override
  public Page<TheatreResponse> listTheatres(PaginationRequest pagination) {
    return theatreService.list(pagination);
  }

  @Override
  public ResponseEntity<ScreenResponse> createScreen(String theatreId, CreateScreenRequest request) {
    ScreenResponse response = screenService.create(theatreId, request);
    return ResponseEntity.created(URI.create("/api/v1/screens/" + response.getScreenId()))
        .body(response);
  }

  @Override
  public Page<ScreenResponse> listScreens(String theatreId, PaginationRequest pagination) {
    return screenService.listByTheatre(theatreId, pagination);
  }
}
