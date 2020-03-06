package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * A Placement. Contains the fields necessary for presenting an item in a placement list.
 */
@Data
@Entity
@Table(name = "Placement")
public class PlacementView implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "traineeId")
  private Long traineeId;

  @Column(name = "intrepidId")
  private String intrepidId;

  @Column(name = "postId")
  private Long postId;

  @Column(name = "siteId")
  private Long siteId;

  @Column(name = "siteCode")
  private String siteCode;

  @Column(name = "gradeId")
  private Long gradeId;

  @Column(name = "gradeAbbreviation")
  private String gradeAbbreviation;

  @Column(name = "dateFrom")
  private LocalDate dateFrom;

  @Column(name = "dateTo")
  private LocalDate dateTo;

  @Column(name = "placementType")
  private String placementType;

  /**
   * @return the placement status based on dateFrom and dateTo
   */
  public PlacementStatus getStatus() {
    if (this.dateFrom == null || this.dateTo == null) {
      return null;
    }

    LocalDate today = LocalDate.now();
    if (today.isBefore(this.dateFrom)) {
      return PlacementStatus.FUTURE;
    } else if (today.isAfter(this.dateTo)) {
      return PlacementStatus.PAST;
    }
    return PlacementStatus.CURRENT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementView that = (PlacementView) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (traineeId != null ? !traineeId.equals(that.traineeId) : that.traineeId != null) {
      return false;
    }
    if (intrepidId != null ? !intrepidId.equals(that.intrepidId) : that.intrepidId != null) {
      return false;
    }
    if (postId != null ? !postId.equals(that.postId) : that.postId != null) {
      return false;
    }
    if (siteId != null ? !siteId.equals(that.siteId) : that.siteId != null) {
      return false;
    }
    if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) {
      return false;
    }
    if (gradeId != null ? !gradeId.equals(that.gradeId) : that.gradeId != null) {
      return false;
    }
    if (gradeAbbreviation != null ? !gradeAbbreviation.equals(that.gradeAbbreviation)
        : that.gradeAbbreviation != null) {
      return false;
    }
    if (dateFrom != null ? !dateFrom.equals(that.dateFrom) : that.dateFrom != null) {
      return false;
    }
    if (dateTo != null ? !dateTo.equals(that.dateTo) : that.dateTo != null) {
      return false;
    }
    return placementType != null ? placementType.equals(that.placementType)
        : that.placementType == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (traineeId != null ? traineeId.hashCode() : 0);
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (postId != null ? postId.hashCode() : 0);
    result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
    result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
    result = 31 * result + (gradeId != null ? gradeId.hashCode() : 0);
    result = 31 * result + (gradeAbbreviation != null ? gradeAbbreviation.hashCode() : 0);
    result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
    result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
    result = 31 * result + (placementType != null ? placementType.hashCode() : 0);
    return result;
  }
}
