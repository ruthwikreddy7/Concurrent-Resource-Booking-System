package com.concurrentbooking.theatre.service;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.theatre.dto.CreateScreenRequest;
import com.concurrentbooking.theatre.dto.ScreenResponse;
import com.concurrentbooking.theatre.entity.Screen;
import org.springframework.data.domain.Page;

public interface IScreenService {

  ScreenResponse create(String theatreId, CreateScreenRequest request);

  Screen getEntity(String screenId);

  ScreenResponse get(String screenId);

  Page<ScreenResponse> listByTheatre(String theatreId, PaginationRequest pagination);
}
