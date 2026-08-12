package com.concurrentbooking.show.service;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.show.dto.CreateShowRequest;
import com.concurrentbooking.show.dto.ShowResponse;
import com.concurrentbooking.show.dto.ShowSeatResponse;
import com.concurrentbooking.show.entity.Show;
import com.concurrentbooking.show.entity.ShowSeat;
import java.util.List;
import org.springframework.data.domain.Page;

public interface IShowService {

  ShowResponse create(CreateShowRequest request);

  Show getEntity(String showId);

  ShowResponse get(String showId);

  List<ShowSeat> getShowSeatEntities(String showId, List<String> showSeatIds);

  Page<ShowSeatResponse> listSeats(String showId, PaginationRequest pagination);
}
