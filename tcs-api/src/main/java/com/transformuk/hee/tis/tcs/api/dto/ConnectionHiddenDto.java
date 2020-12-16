package com.transformuk.hee.tis.tcs.api.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionHiddenDto {

  private long totalCounts;
  private long totalPages;
  private List<ConnectionHiddenRecordDto> connections;
}
