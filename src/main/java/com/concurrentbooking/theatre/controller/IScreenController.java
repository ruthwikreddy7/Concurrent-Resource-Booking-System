package com.concurrentbooking.theatre.controller;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.theatre.dto.ScreenResponse;
import com.concurrentbooking.theatre.dto.SeatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/screens")
@Tag(name = "Screen Controller", description = "APIs to manage screens and physical seats.")
public interface IScreenController {

  @GetMapping("/{screenId}")
  @Operation(summary = "Get screen", description = "Returns one screen by ID.")
  ScreenResponse getScreen(@PathVariable String screenId);

  @GetMapping("/{screenId}/seats")
  @Operation(summary = "List screen seats", description = "Returns physical seats under a screen.")
  Page<SeatResponse> listSeats(@PathVariable String screenId,
      @Valid @ParameterObject PaginationRequest pagination);
}
