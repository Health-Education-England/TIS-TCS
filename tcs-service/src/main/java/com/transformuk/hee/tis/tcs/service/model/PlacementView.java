package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Placement. Contains the fields necessary for presenting an item in a placement list.
 */
@Entity
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

  @Column(name = "siteCode")
  private String siteCode;

  @Column(name = "gradeAbbreviation")
  private String gradeAbbreviation;

  @Column(name = "dateFrom")
  private LocalDate dateFrom;

  @Column(name = "dateTo")
  private LocalDate dateTo;

  @Column(name = "placementType")
  private String placementType;

  @Enumerated(EnumType.STRING)
  private Status status;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTraineeId() {
    return traineeId;
  }

  public void setTraineeId(Long traineeId) {
    this.traineeId = traineeId;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getSiteCode() {
    return siteCode;
  }

  public void setSiteCode(String siteCode) {
    this.siteCode = siteCode;
  }

  public String getGradeAbbreviation() {
    return gradeAbbreviation;
  }

  public void setGradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
  }

  public LocalDate getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(LocalDate dateFrom) {
    this.dateFrom = dateFrom;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }

  public void setDateTo(LocalDate dateTo) {
    this.dateTo = dateTo;
  }

  public String getPlacementType() {
    return placementType;
  }

  public void setPlacementType(String placementType) {
    this.placementType = placementType;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlacementView that = (PlacementView) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (traineeId != null ? !traineeId.equals(that.traineeId) : that.traineeId != null) return false;
    if (intrepidId != null ? !intrepidId.equals(that.intrepidId) : that.intrepidId != null) return false;
    if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
    if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
    if (gradeAbbreviation != null ? !gradeAbbreviation.equals(that.gradeAbbreviation) : that.gradeAbbreviation != null)
      return false;
    if (dateFrom != null ? !dateFrom.equals(that.dateFrom) : that.dateFrom != null) return false;
    if (dateTo != null ? !dateTo.equals(that.dateTo) : that.dateTo != null) return false;
    if (placementType != null ? !placementType.equals(that.placementType) : that.placementType != null) return false;
    return status == that.status;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (traineeId != null ? traineeId.hashCode() : 0);
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (postId != null ? postId.hashCode() : 0);
    result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
    result = 31 * result + (gradeAbbreviation != null ? gradeAbbreviation.hashCode() : 0);
    result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
    result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
    result = 31 * result + (placementType != null ? placementType.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PlacementView{" +
        "id=" + id +
        ", traineeId=" + traineeId +
        ", intrepidId='" + intrepidId + '\'' +
        ", postId=" + postId +
        ", siteCode='" + siteCode + '\'' +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        ", placementType='" + placementType + '\'' +
        ", status=" + status +
        '}';
  }
}
