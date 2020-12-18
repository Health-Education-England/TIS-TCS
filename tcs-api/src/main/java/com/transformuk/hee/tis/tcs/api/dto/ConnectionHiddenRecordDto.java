package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionHiddenRecordDto {

  private String gmcReferenceNumber;
  private String doctorFirstName;
  private String doctorLastName;
  private String designatedBody;
  private String connectionStatus;
  private LocalDate programmeMembershipEndDate;
  private LocalDate programmeMembershipStartDate;
  private String programmeMembershipType;
  private String programmeName;
  private String programmeOwner;
  private LocalDate submissionDate;
}
