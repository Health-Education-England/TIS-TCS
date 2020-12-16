package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ConnectionHiddenRecordDto {

  public String surname;
  public String forenames;
  private String gmcNumber;
  private String programmeName;
  private String programmeMembershipType;
  private LocalDate programmeStartDate;
  private LocalDate programmeEndDate;
}
