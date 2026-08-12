package com.concurrentbooking.theatre.controller;

import com.concurrentbooking.common.dto.PaginationRequest;
import com.concurrentbooking.theatre.dto.CreateScreenRequest;
import com.concurrentbooking.theatre.dto.CreateTheatreRequest;
import com.concurrentbooking.theatre.dto.ScreenResponse;
import com.concurrentbooking.theatre.dto.TheatreResponse;
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

@RequestMapping("/api/v1/theatres")
@Tag(name = "Theatre Controller", description = "APIs to manage cinema theatres.")
public interface ITheatreController {

  @PostMapping
  @Operation(summary = "Create theatre", description = "Creates one physical theatre location.")
  ResponseEntity<TheatreResponse> createTheatre(@Valid @RequestBody CreateTheatreRequest request);

  @GetMapping("/{theatreId}")
  @Operation(summary = "Get theatre", description = "Returns one theatre by ID.")
  TheatreResponse getTheatre(@PathVariable String theatreId);

  @GetMapping
  @Operation(summary = "List theatres", description = "Returns all theatres.")
  Page<TheatreResponse> listTheatres(@Valid @ParameterObject PaginationRequest pagination);

  @PostMapping("/{theatreId}/screens")
  @Operation(summary = "Create screen", description = "Creates one screen under a theatre.")
  ResponseEntity<ScreenResponse> createScreen(@PathVariable String theatreId,
      @Valid @RequestBody CreateScreenRequest request);

  @GetMapping("/{theatreId}/screens")
  @Operation(summary = "List theatre screens", description = "Returns screens under one theatre.")
  Page<ScreenResponse> listScreens(@PathVariable String theatreId,
      @Valid @ParameterObject PaginationRequest pagination);
}
