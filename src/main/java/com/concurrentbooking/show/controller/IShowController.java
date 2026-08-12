package com.concurrentbooking.show.controller;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.show.dto.CreateShowRequest;
import com.concurrentbooking.show.dto.ShowResponse;
import com.concurrentbooking.show.dto.ShowSeatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/shows")
@Tag(name = "Show Controller", description = "APIs to manage shows and show-seat inventory.")
public interface IShowController {

  @PostMapping
  @Operation(summary = "Create show", description = "Creates a show and initializes show-seat inventory from the screen seats.")
  ResponseEntity<ShowResponse> createShow(@Valid @RequestBody CreateShowRequest request);

  @GetMapping("/{showId}")
  @Operation(summary = "Get show", description = "Returns one show by ID.")
  ShowResponse getShow(@PathVariable String showId);

  @GetMapping("/{showId}/seats")
  @Operation(summary = "List show seats", description = "Returns seat availability for one show.")
  Page<ShowSeatResponse> listShowSeats(@PathVariable String showId,
      @Valid @ParameterObject PaginationRequest pagination);
}
