package com.concurrentbooking.theatre.service.impl;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.common.error.ResourceNotFoundException;
import com.concurrentbooking.common.util.PageUtils;
import com.concurrentbooking.theatre.dto.CreateTheatreRequest;
import com.concurrentbooking.theatre.dto.TheatreResponse;
import com.concurrentbooking.theatre.entity.Theatre;
import com.concurrentbooking.theatre.repository.TheatreRepository;
import com.concurrentbooking.theatre.service.ITheatreService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements ITheatreService {

  private final TheatreRepository theatreRepository;
  private final PageUtils pageUtils;

  @Override
  @Transactional
  public TheatreResponse create(CreateTheatreRequest request) {
    Theatre theatre = new Theatre();
    theatre.setName(request.getName());
    theatre.setCity(request.getCity());
    theatre.setAddress(request.getAddress());
    return TheatreResponse.from(theatreRepository.save(theatre));
  }

  @Override
  @Transactional(readOnly = true)
  public Theatre getEntity(String theatreId) {
    return theatreRepository.findById(theatreId)
        .orElseThrow(() -> new ResourceNotFoundException("Theatre", theatreId));
  }

  @Override
  @Transactional(readOnly = true)
  public TheatreResponse get(String theatreId) {
    return TheatreResponse.from(getEntity(theatreId));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TheatreResponse> list(PaginationRequest pagination) {
    return theatreRepository.findAll(pageUtils.toPageable(pagination,
        Set.of("theatreId", "name", "city", "createdAt"))).map(TheatreResponse::from);
  }
}
