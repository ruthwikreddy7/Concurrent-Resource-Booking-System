package com.concurrentbooking.show.service.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.common.error.ApiException;
import com.concurrentbooking.common.error.BookingErrors;
import com.concurrentbooking.common.error.ResourceNotFoundException;
import com.concurrentbooking.common.util.PageUtils;
import com.concurrentbooking.movie.entity.Movie;
import com.concurrentbooking.movie.service.IMovieService;
import com.concurrentbooking.show.dto.CreateShowRequest;
import com.concurrentbooking.show.dto.ShowResponse;
import com.concurrentbooking.show.dto.ShowSeatResponse;
import com.concurrentbooking.show.entity.Show;
import com.concurrentbooking.show.entity.ShowSeat;
import com.concurrentbooking.show.enums.ShowSeatStatus;
import com.concurrentbooking.show.repository.ShowRepository;
import com.concurrentbooking.show.repository.ShowSeatRepository;
import com.concurrentbooking.show.service.IShowService;
import com.concurrentbooking.theatre.entity.Screen;
import com.concurrentbooking.theatre.entity.Seat;
import com.concurrentbooking.theatre.service.IScreenService;
import com.concurrentbooking.theatre.service.ISeatService;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements IShowService {

  private final ShowRepository showRepository;
  private final ShowSeatRepository showSeatRepository;
  private final IMovieService movieService;
  private final IScreenService screenService;
  private final ISeatService seatService;
  private final PageUtils pageUtils;

  @Override
  @Transactional
  public ShowResponse create(CreateShowRequest request) {
    Movie movie = movieService.getEntity(request.getMovieId());
    Screen screen = screenService.getEntity(request.getScreenId());
    validateStartTime(request.getStartTime());

    Show show = new Show();
    show.setMovie(movie);
    show.setScreen(screen);
    show.setStartTime(request.getStartTime());
    show.setEndTime(request.getStartTime().plusSeconds(movie.getDurationMinutes() * 60L));
    Show savedShow = showRepository.save(show);

    List<Seat> seats = seatService.listEntitiesByScreen(screen.getScreenId());
    List<ShowSeat> showSeats = seats.stream()
        .map(seat -> toShowSeat(savedShow, seat))
        .toList();
    showSeatRepository.saveAll(showSeats);
    return ShowResponse.from(savedShow);
  }

  @Override
  @Transactional(readOnly = true)
  public Show getEntity(String showId) {
    return showRepository.findById(showId)
        .orElseThrow(() -> new ResourceNotFoundException("Show", showId));
  }

  @Override
  @Transactional(readOnly = true)
  public ShowResponse get(String showId) {
    return ShowResponse.from(getEntity(showId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ShowSeat> getShowSeatEntities(String showId, List<String> showSeatIds) {
    getEntity(showId);
    List<ShowSeat> seats = showSeatRepository.findByShowShowIdAndShowSeatIdIn(showId, showSeatIds);
    if (seats.size() != showSeatIds.size()) {
      throw new ApiException(BookingErrors.INVALID_REQUEST,
          "All showSeatIds must belong to the requested show");
    }
    return seats;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ShowSeatResponse> listSeats(String showId, PaginationRequest pagination) {
    getEntity(showId);
    return showSeatRepository.findByShowShowId(showId, pageUtils.toPageable(pagination,
        Set.of("showSeatId", "status", "version", "createdAt", "updatedAt")))
        .map(ShowSeatResponse::from);
  }

  private ShowSeat toShowSeat(Show show, Seat seat) {
    ShowSeat showSeat = new ShowSeat();
    showSeat.setShow(show);
    showSeat.setSeat(seat);
    showSeat.setStatus(ShowSeatStatus.AVAILABLE);
    showSeat.setVersion(0L);
    return showSeat;
  }

  private void validateStartTime(Instant startTime) {
    if (startTime.isBefore(Instant.now())) {
      throw new ApiException(BookingErrors.INVALID_REQUEST, "startTime must be in the future");
    }
  }
}
