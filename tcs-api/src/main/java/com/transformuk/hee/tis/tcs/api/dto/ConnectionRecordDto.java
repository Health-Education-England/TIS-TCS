package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ConnectionRecordDto {

  private String programmeOwner;
  private String connectionStatus;
  private LocalDate programmeMembershipStartDate;
  private LocalDate programmeMembershipEndDate;

}
