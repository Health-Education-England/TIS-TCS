package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds the data from the placements table necessary to populate the
 * {@link com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO}
 */

@Entity
@Table(name = "Placement")
public class PlacementDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String intrepidId;

  private Long traineeId;

  private Long postId;

  private LocalDate dateFrom;

  private LocalDate dateTo;

  @Column(name = "placementWholeTimeEquivalent")
  private BigDecimal wholeTimeEquivalent;

  private Long siteId;

  private String siteCode;

  private Long gradeId;

  private String gradeAbbreviation;

  private String placementType;

  private String trainingDescription;

  private String localPostNumber;

  private LocalDateTime addedDate;

  private LocalDateTime amendedDate;

  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<Comment> comments = new HashSet<>();

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Long getTraineeId() {
    return traineeId;
  }

  public void setTraineeId(Long traineeId) {
    this.traineeId = traineeId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
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

  public BigDecimal getWholeTimeEquivalent() {
    return wholeTimeEquivalent;
  }

  public void setWholeTimeEquivalent(BigDecimal wholeTimeEquivalent) {
    this.wholeTimeEquivalent = wholeTimeEquivalent;
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

  public String getPlacementType() {
    return placementType;
  }

  public void setPlacementType(String placementType) {
    this.placementType = placementType;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(String trainingDescription) {
    this.trainingDescription = trainingDescription;
  }

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(String localPostNumber) {
    this.localPostNumber = localPostNumber;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public LocalDateTime getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(LocalDateTime addedDate) {
    this.addedDate = addedDate;
  }

  public LocalDateTime getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
  }

  public Set<Comment> getComments() { return comments; }

  public void setComments(Set<Comment> comments) { this.comments = comments; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlacementDetails that = (PlacementDetails) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (intrepidId != null ? !intrepidId.equals(that.intrepidId) : that.intrepidId != null) return false;
    if (traineeId != null ? !traineeId.equals(that.traineeId) : that.traineeId != null) return false;
    if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
    if (dateFrom != null ? !dateFrom.equals(that.dateFrom) : that.dateFrom != null) return false;
    if (dateTo != null ? !dateTo.equals(that.dateTo) : that.dateTo != null) return false;
    if (wholeTimeEquivalent != null ? !wholeTimeEquivalent.equals(that.wholeTimeEquivalent) : that.wholeTimeEquivalent != null)
      return false;
    if (siteId != null ? !siteId.equals(that.siteId) : that.siteId != null) return false;
    if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
    if (gradeId != null ? !gradeId.equals(that.gradeId) : that.gradeId != null) return false;
    if (gradeAbbreviation != null ? !gradeAbbreviation.equals(that.gradeAbbreviation) : that.gradeAbbreviation != null)
      return false;
    if (placementType != null ? !placementType.equals(that.placementType) : that.placementType != null) return false;
    if (trainingDescription != null ? !trainingDescription.equals(that.trainingDescription) : that.trainingDescription != null)
      return false;
    if (addedDate != null ? !addedDate.equals(that.addedDate) : that.addedDate != null) return false;
    if (amendedDate != null ? !amendedDate.equals(that.amendedDate) : that.amendedDate != null) return false;
    return localPostNumber != null ? localPostNumber.equals(that.localPostNumber) : that.localPostNumber == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (traineeId != null ? traineeId.hashCode() : 0);
    result = 31 * result + (postId != null ? postId.hashCode() : 0);
    result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
    result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
    result = 31 * result + (wholeTimeEquivalent != null ? wholeTimeEquivalent.hashCode() : 0);
    result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
    result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
    result = 31 * result + (gradeId != null ? gradeId.hashCode() : 0);
    result = 31 * result + (gradeAbbreviation != null ? gradeAbbreviation.hashCode() : 0);
    result = 31 * result + (placementType != null ? placementType.hashCode() : 0);
    result = 31 * result + (trainingDescription != null ? trainingDescription.hashCode() : 0);
    result = 31 * result + (localPostNumber != null ? localPostNumber.hashCode() : 0);
    result = 31 * result + (addedDate != null ? addedDate.hashCode() : 0);
    result = 31 * result + (amendedDate != null ? amendedDate.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PlacementDetails{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", traineeId=" + traineeId +
        ", postId=" + postId +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        ", wholeTimeEquivalent=" + wholeTimeEquivalent +
        ", siteId=" + siteId +
        ", siteCode='" + siteCode + '\'' +
        ", gradeId=" + gradeId +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", placementType='" + placementType + '\'' +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", localPostNumber='" + localPostNumber + '\'' +
        ", addedDate='" + addedDate + '\'' +
        ", amendedDate='" + amendedDate + '\'' +
        '}';
  }
}
