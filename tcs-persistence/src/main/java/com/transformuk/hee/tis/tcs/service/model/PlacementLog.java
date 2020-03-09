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
        lifecycleState == that.lifecycleState &&
        Objects.equals(placementId, that.placementId) &&
        logType == that.logType &&
        Objects.equals(validDateFrom, that.validDateFrom) &&
        Objects.equals(validDateTo, that.validDateTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateFrom, dateTo, lifecycleState, placementId, logType,
        validDateFrom, validDateTo);
  }

  @Override
  public String toString() {
    return "PlacementLog{" +
        "id=" + id +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        ", lifecycleState=" + lifecycleState +
        ", placementId=" + placementId +
        ", logType=" + logType +
        ", validDateFrom=" + validDateFrom +
        ", validDateTo=" + validDateTo +
        '}';
  }
}
