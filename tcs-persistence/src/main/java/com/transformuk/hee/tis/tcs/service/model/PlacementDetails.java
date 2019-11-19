package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

/**
 * Holds the data from the placements table necessary to populate the {@link
 * com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO}
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

  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<PlacementSite> sites = new HashSet<>();

  private Long gradeId;

  private String gradeAbbreviation;

  private String placementType;

  private String trainingDescription;

  private String localPostNumber;

  @Column(name = "placementAddedDate")
  private LocalDateTime addedDate;

  @Column(name = "placementAmendedDate")
  private LocalDateTime amendedDate;

  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<Comment> comments = new HashSet<>();

  @Enumerated(EnumType.STRING)
  private LifecycleState lifecycleState;

  @Column(name = "placementApprovedDate")
  private LocalDateTime placementApprovedDate;

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

  public Set<PlacementSite> getSites() {
    return sites;
  }

  public void setSites(Set<PlacementSite> sites) {
    this.sites = sites;
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

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  public LifecycleState getLifecycleState() {
    return lifecycleState;
  }

  public void setLifecycleState(LifecycleState lifecycleState) {
    this.lifecycleState = lifecycleState;
  }

  public LocalDateTime getPlacementApprovedDate() {
    return placementApprovedDate;
  }

  public void setPlacementApprovedDate(LocalDateTime placementApprovedDate) {
    this.placementApprovedDate = placementApprovedDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementDetails that = (PlacementDetails) o;

    return Objects.equals(id, that.id) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(traineeId, that.traineeId) &&
        Objects.equals(postId, that.postId) &&
        Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        Objects.equals(wholeTimeEquivalent, that.wholeTimeEquivalent) &&
        Objects.equals(siteId, that.siteId) &&
        Objects.equals(siteCode, that.siteCode) &&
        Objects.equals(gradeId, that.gradeId) &&
        Objects.equals(gradeAbbreviation, that.gradeAbbreviation) &&
        Objects.equals(placementType, that.placementType) &&
        Objects.equals(trainingDescription, that.trainingDescription) &&
        Objects.equals(addedDate, that.addedDate) &&
        Objects.equals(amendedDate, that.amendedDate) &&
        Objects.equals(localPostNumber, that.localPostNumber) &&
        Objects.equals(lifecycleState, that.lifecycleState) &&
        Objects.equals(placementApprovedDate, that.placementApprovedDate);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, intrepidId, traineeId, postId, dateFrom, dateTo, wholeTimeEquivalent, siteId,
            siteCode, gradeId, gradeAbbreviation, placementType, trainingDescription, localPostNumber,
            addedDate, amendedDate, lifecycleState, placementApprovedDate);
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
        ", lifecycleState='" + lifecycleState + '\'' +
        ", placementApprovedDate='" + placementApprovedDate + '\'' +
        '}';
  }
}
