package com.concurrentbooking.theatre.service.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.common.error.ApiException;
import com.concurrentbooking.common.error.BookingErrors;
import com.concurrentbooking.common.error.ResourceNotFoundException;
import com.concurrentbooking.common.util.PageUtils;
import com.concurrentbooking.theatre.dto.CreateScreenRequest;
import com.concurrentbooking.theatre.dto.ScreenResponse;
import com.concurrentbooking.theatre.entity.Screen;
import com.concurrentbooking.theatre.entity.Seat;
import com.concurrentbooking.theatre.entity.Theatre;
import com.concurrentbooking.theatre.repository.ScreenRepository;
import com.concurrentbooking.theatre.repository.SeatRepository;
import com.concurrentbooking.theatre.service.IScreenService;
import com.concurrentbooking.theatre.service.ITheatreService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements IScreenService {

  private final ScreenRepository screenRepository;
  private final SeatRepository seatRepository;
  private final ITheatreService theatreService;
  private final PageUtils pageUtils;

  @Override
  @Transactional
  public ScreenResponse create(String theatreId, CreateScreenRequest request) {
    validateSeatMatrix(request);
    Theatre theatre = theatreService.getEntity(theatreId);
    Screen screen = new Screen();
    screen.setTheatre(theatre);
    screen.setName(request.getName());
    screen.setCapacity(request.getCapacity());
    screen.setSeatsPerRow(request.getSeatsPerRow());
    Screen savedScreen = screenRepository.save(screen);
    seatRepository.saveAll(buildSeats(savedScreen));
    return ScreenResponse.from(savedScreen);
  }

  @Override
  @Transactional(readOnly = true)
  public Screen getEntity(String screenId) {
    return screenRepository.findById(screenId)
        .orElseThrow(() -> new ResourceNotFoundException("Screen", screenId));
  }

  @Override
  @Transactional(readOnly = true)
  public ScreenResponse get(String screenId) {
    return ScreenResponse.from(getEntity(screenId));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ScreenResponse> listByTheatre(String theatreId, PaginationRequest pagination) {
    theatreService.getEntity(theatreId);
    return screenRepository.findByTheatreTheatreId(theatreId, pageUtils.toPageable(pagination,
        Set.of("screenId", "name", "capacity", "seatsPerRow", "createdAt")))
        .map(ScreenResponse::from);
  }

  private void validateSeatMatrix(CreateScreenRequest request) {
    if (request.getSeatsPerRow() > request.getCapacity()) {
      throw new ApiException(BookingErrors.INVALID_REQUEST,
          "seatsPerRow must be less than or equal to capacity");
    }
    int rowCount = (int) Math.ceil((double) request.getCapacity() / request.getSeatsPerRow());
    if (rowCount > 26) {
      throw new ApiException(BookingErrors.INVALID_REQUEST,
          "capacity and seatsPerRow require " + rowCount
              + " rows, but only A to Z row labels are supported");
    }
  }

  private List<Seat> buildSeats(Screen screen) {
    List<Seat> seats = new ArrayList<>(screen.getCapacity());
    for (int index = 0; index < screen.getCapacity(); index++) {
      Seat seat = new Seat();
      seat.setScreen(screen);
      seat.setRowLabel(String.valueOf((char) ('A' + index / screen.getSeatsPerRow())));
      seat.setSeatNumber(index % screen.getSeatsPerRow() + 1);
      seats.add(seat);
    }
    return seats;
  }
}
