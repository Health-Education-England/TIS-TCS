package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ConnectionRecordDto {

  private String gmcNumber;
  private String connectionStatus;
  private LocalDate pmStartDate;
  private LocalDate pmEndDate;

}
