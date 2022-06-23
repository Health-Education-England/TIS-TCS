package com.transformuk.hee.tis.tcs.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to display placement details for a post or person
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlacementSummaryDTO {

  private Date dateFrom;
  private Date dateTo;
  private Long siteId;
  private String siteName;
  private String primarySpecialtyName;
  private Long gradeId;
  private String gradeName;
  private String placementType;
  private String status;
  private String forenames;
  private String legalforenames;
  private String surname;
  private String legalsurname;
  private Long traineeId;
  private String email;
  private String gradeAbbreviation;
  private Long placementId;
  private String placementStatus;
  private String placementSpecialtyType;
  private BigDecimal placementWholeTimeEquivalent;
  private Long postId;
  private String nationalPostNumber;
  private Set<PlacementEsrEventDto> esrEvents;
}
