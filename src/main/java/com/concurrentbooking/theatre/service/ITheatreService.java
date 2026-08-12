package com.concurrentbooking.theatre.service;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.theatre.dto.CreateTheatreRequest;
import com.concurrentbooking.theatre.dto.TheatreResponse;
import com.concurrentbooking.theatre.entity.Theatre;
import org.springframework.data.domain.Page;

public interface ITheatreService {

  TheatreResponse create(CreateTheatreRequest request);

  Theatre getEntity(String theatreId);

  TheatreResponse get(String theatreId);

  Page<TheatreResponse> list(PaginationRequest pagination);
}
