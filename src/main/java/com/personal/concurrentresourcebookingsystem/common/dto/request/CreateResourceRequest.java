package com.personal.concurrentresourcebookingsystem.common.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateResourceRequest(
    @NotBlank @Size(max = 120) String name,
    @NotNull @Min(1) @Max(100) Integer rowCount,
    @NotNull @Min(1) @Max(100) Integer columnCount
) {}

