package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionDto {
  public String surname;
  public String forenames;
  private String gmcNumber;
  private String programmeOwner;
  private String programmeName;
  private ProgrammeMembershipType programmeMembershipType;
  private LocalDate programmeStartDate;
  private LocalDate programmeEndDate;
}
