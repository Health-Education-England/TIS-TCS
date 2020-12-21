package com.transformuk.hee.tis.tcs.api.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RevalidationRecordDto {

  private String gmcNumber;
  private String forenames;
  private String surname;
  private LocalDate cctDate;
  private String programmeMembershipType;
  private String programmeName;
  private String currentGrade;
  private Long tisPersonId;
}
