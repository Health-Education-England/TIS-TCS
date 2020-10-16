package com.transformuk.hee.tis.tcs.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to display placement details for a post or person
 */
@Data
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
  private String nationalPostNumber;
  private Set<PlacementEsrEventDto> esrEvents;

  public PlacementSummaryDTO(Date dateFrom, Date dateTo, Long siteId, String primarySpecialtyName,
      Long gradeId, String placementType, String status, String forenames, String legalforenames,
      String surname, String legalsurname, Long traineeId, String email, String gradeAbbreviation,
      Long placementId, String placementSpecialtyType, BigDecimal placementWholeTimeEquivalent, String nationalPostNumber) {
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.siteId = siteId;
    this.primarySpecialtyName = primarySpecialtyName;
    this.gradeId = gradeId;
    this.placementType = placementType;
    this.status = status;
    this.forenames = forenames;
    this.legalforenames = legalforenames;
    this.surname = surname;
    this.legalsurname = legalsurname;
    this.traineeId = traineeId;
    this.email = email;
    this.gradeAbbreviation = gradeAbbreviation;
    this.placementId = placementId;
    this.placementSpecialtyType = placementSpecialtyType;
    this.placementWholeTimeEquivalent = placementWholeTimeEquivalent;
    this.nationalPostNumber = nationalPostNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementSummaryDTO that = (PlacementSummaryDTO) o;
    return Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        Objects.equals(siteId, that.siteId) &&
        Objects.equals(siteName, that.siteName) &&
        Objects.equals(primarySpecialtyName, that.primarySpecialtyName) &&
        Objects.equals(gradeId, that.gradeId) &&
        Objects.equals(gradeName, that.gradeName) &&
        Objects.equals(placementType, that.placementType) &&
        Objects.equals(status, that.status) &&
        Objects.equals(forenames, that.forenames) &&
        Objects.equals(legalforenames, that.legalforenames) &&
        Objects.equals(surname, that.surname) &&
        Objects.equals(legalsurname, that.legalsurname) &&
        Objects.equals(traineeId, that.traineeId) &&
        Objects.equals(email, that.email) &&
        Objects.equals(gradeAbbreviation, that.gradeAbbreviation) &&
        Objects.equals(placementId, that.placementId) &&
        Objects.equals(placementStatus, that.placementStatus) &&
        Objects.equals(placementSpecialtyType, that.placementSpecialtyType) &&
        Objects.equals(placementWholeTimeEquivalent, that.placementWholeTimeEquivalent) &&
        Objects.equals(nationalPostNumber, that.nationalPostNumber) &&
        Objects.equals(esrEvents, that.esrEvents);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(dateFrom, dateTo, siteId, siteName, primarySpecialtyName, gradeId, gradeName,
            placementType, status, forenames, legalforenames, surname, legalsurname, traineeId,
            email, gradeAbbreviation, placementId, placementStatus, placementSpecialtyType,
            placementWholeTimeEquivalent, nationalPostNumber, esrEvents);
  }
}
