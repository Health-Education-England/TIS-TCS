package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ConnectionRecordDto {

  private String programmeMembershipType;
  private String programmeName;
  private String programmeOwner;
  private String tisConnectionStatus;
  private String designatedBodyCode;
  private LocalDate programmeMembershipStartDate;
  private LocalDate programmeMembershipEndDate;

}
