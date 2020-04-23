package com.transformuk.hee.tis.tcs.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RevalidationRecordDTO {

  private String gmcId;
  private LocalDate cctDate;
  private String programmeMembershipType;
  private String programmeName;
  String currentGrade;

}
