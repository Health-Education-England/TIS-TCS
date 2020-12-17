package com.transformuk.hee.tis.tcs.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConnectionHiddenDto {

  private long totalCounts;
  private long totalPages;
  private List<ConnectionHiddenRecordDto> connections;
}
