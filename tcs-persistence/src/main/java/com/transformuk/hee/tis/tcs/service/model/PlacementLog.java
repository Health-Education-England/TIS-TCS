package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementLogType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PlacementLog")
public class PlacementLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate dateFrom;

  private LocalDate dateTo;

  @Column(name = "placementWholeTimeEquivalent")
  private BigDecimal wholeTimeEquivalent;

  private String intrepidId;

  private Long traineeId;

  private Long postId;

  private String localPostNumber;

  private String siteCode;

  private String gradeAbbreviation;

  private String placementType;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String trainingDescription;

  private Long siteId;

  private Long gradeId;

  @Column(name = "placementAddedDate")
  private LocalDateTime addedDate;

  @Column(name = "placementAmendedDate")
  private LocalDateTime amendedDate;

  @Enumerated(EnumType.STRING)
  private LifecycleState lifecycleState;

  private Long placementId;

  @Enumerated(EnumType.STRING)
  private PlacementLogType logType;

  private LocalDateTime validDateFrom;

  private LocalDateTime validDateTo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(String localPostNumber) {
    this.localPostNumber = localPostNumber;
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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(String trainingDescription) {
    this.trainingDescription = trainingDescription;
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

  public LifecycleState getLifecycleState() {
    return lifecycleState;
  }

  public void setLifecycleState(LifecycleState lifecycleState) {
    this.lifecycleState = lifecycleState;
  }

  public Long getPlacementId() {
    return placementId;
  }

  public void setPlacementId(Long placementId) {
    this.placementId = placementId;
  }

  public PlacementLogType getLogType() {
    return logType;
  }

  public void setLogType(PlacementLogType logType) {
    this.logType = logType;
  }

  public LocalDateTime getValidDateFrom() {
    return validDateFrom;
  }

  public void setValidDateFrom(LocalDateTime validDateFrom) {
    this.validDateFrom = validDateFrom;
  }

  public LocalDateTime getValidDateTo() {
    return validDateTo;
  }

  public void setValidDateTo(LocalDateTime validDateTo) {
    this.validDateTo = validDateTo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlacementLog that = (PlacementLog) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        Objects.equals(wholeTimeEquivalent, that.wholeTimeEquivalent) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(traineeId, that.traineeId) &&
        Objects.equals(postId, that.postId) &&
        Objects.equals(localPostNumber, that.localPostNumber) &&
        Objects.equals(siteCode, that.siteCode) &&
        Objects.equals(gradeAbbreviation, that.gradeAbbreviation) &&
        Objects.equals(placementType, that.placementType) &&
        status == that.status &&
        Objects.equals(trainingDescription, that.trainingDescription) &&
        Objects.equals(siteId, that.siteId) &&
        Objects.equals(gradeId, that.gradeId) &&
        Objects.equals(addedDate, that.addedDate) &&
        Objects.equals(amendedDate, that.amendedDate) &&
        lifecycleState == that.lifecycleState &&
        Objects.equals(placementId, that.placementId) &&
        logType == that.logType &&
        Objects.equals(validDateFrom, that.validDateFrom) &&
        Objects.equals(validDateTo, that.validDateTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateFrom, dateTo, wholeTimeEquivalent, intrepidId, traineeId, postId,
        localPostNumber, siteCode, gradeAbbreviation, placementType, status, trainingDescription,
        siteId, gradeId, addedDate, amendedDate, lifecycleState, placementId, logType,
        validDateFrom, validDateTo);
  }

  @Override
  public String toString() {
    return "PlacementLog{" +
        "id=" + id +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        ", wholeTimeEquivalent=" + wholeTimeEquivalent +
        ", intrepidId='" + intrepidId + '\'' +
        ", traineeId=" + traineeId +
        ", postId=" + postId +
        ", localPostNumber='" + localPostNumber + '\'' +
        ", siteCode='" + siteCode + '\'' +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", placementType='" + placementType + '\'' +
        ", status=" + status +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", siteId=" + siteId +
        ", gradeId=" + gradeId +
        ", addedDate=" + addedDate +
        ", amendedDate=" + amendedDate +
        ", lifecycleState=" + lifecycleState +
        ", placementId=" + placementId +
        ", logType=" + logType +
        ", validDateFrom=" + validDateFrom +
        ", validDateTo=" + validDateTo +
        '}';
  }
}
