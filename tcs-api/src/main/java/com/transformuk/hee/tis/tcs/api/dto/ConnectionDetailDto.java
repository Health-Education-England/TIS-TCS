package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class ConnectionDetailDto {

  private String gmcNumber;
  private String forenames;
  private String surname;
  private LocalDate curriculumEndDate;
  private String programmeMembershipType;
  private String programmeName;
  private String currentGrade;
  private List<ConnectionRecordDto> programmeHistory;

}
