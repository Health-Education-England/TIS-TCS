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

  private long totalResults;
  private long totalPages;
  private long countTotal;
  private List<ConnectionHiddenRecordDto> connections;
}
