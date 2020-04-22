package com.transformuk.hee.tis.tcs.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RevalidationRecordDTO {

  private String gmcId;
  private LocalDate cctDate;
  private String programmeMembershipType;
  private String programmeName;
  String currentGrade;

}
