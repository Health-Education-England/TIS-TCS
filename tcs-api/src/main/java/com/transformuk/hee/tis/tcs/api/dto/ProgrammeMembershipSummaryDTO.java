package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 * A DTO for the ProgrammeMembershipSummary entity.
 */
@Data
public class ProgrammeMembershipSummaryDTO implements Serializable {
  private String programmeMembershipUuid;
  private String programmeName;
  private String programmeStartDate;
}
