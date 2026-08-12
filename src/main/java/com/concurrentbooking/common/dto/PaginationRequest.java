package com.concurrentbooking.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Schema(description = "Pagination and sorting query parameters.")
public class PaginationRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Min(0)
  @Schema(description = "Page number for pagination, zero-indexed.", example = "0")
  private Integer page = 0;

  @Min(1)
  @Max(100)
  @Schema(description = "Number of records per page.", example = "20")
  private Integer size = 20;

  @Schema(description = "Field name used for sorting.", example = "createdAt")
  private String sortBy = "createdAt";

  @Schema(description = "Sort direction. Supported values are asc and desc.", example = "desc")
  private String sortOrder = "desc";
}
