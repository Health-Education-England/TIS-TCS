package com.transformuk.hee.tis.tcs.service.dto.placementmanager;

import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PlacementDTO implements Serializable {

  private Long id;
  private LocalDate dateFrom;
  private LocalDate dateTo;
  private Float wte;
  private String type;
  private PersonDTO trainee;
  private LifecycleState lifecycleState;

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

  public Float getWte() {
    return wte;
  }

  public void setWte(Float wte) {
    this.wte = wte;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public PersonDTO getTrainee() {
    return trainee;
  }

  public void setTrainee(PersonDTO trainee) {
    this.trainee = trainee;
  }

  public LifecycleState getLifecycleState() {
    return lifecycleState;
  }

  public void setLifecycleState(LifecycleState lifecycleState) {
    this.lifecycleState = lifecycleState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementDTO that = (PlacementDTO) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        Objects.equals(wte, that.wte) &&
        Objects.equals(type, that.type) &&
        Objects.equals(trainee, that.trainee) &&
        Objects.equals(lifecycleState, that.lifecycleState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateFrom, dateTo, wte, type, trainee, lifecycleState);
  }
}
