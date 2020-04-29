package com.transformuk.hee.tis.tcs.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private String currentGrade;

}
