package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Placement entity.
 */
public class PlacementViewDTO implements Serializable {

  private Long id;

  private String intrepidId;

  private Status status;

  private PersonDTO trainee;

  private PersonDTO clinicalSupervisor;

  private PostDTO post;

  private Long siteId;

  private String managingLocalOffice;

  private Long gradeId;

  private Set<PlacementSpecialtyDTO> specialties;

  private LocalDate dateFrom;

  private LocalDate dateTo;

  private PlacementType placementType;

  private Float placementWholeTimeEquivalent;

  private String trainingDescription;

  private String localPostNumber;

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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public PersonDTO getTrainee() {
    return trainee;
  }

  public void setTrainee(PersonDTO trainee) {
    this.trainee = trainee;
  }

  public PersonDTO getClinicalSupervisor() {
    return clinicalSupervisor;
  }

  public void setClinicalSupervisor(PersonDTO clinicalSupervisor) {
    this.clinicalSupervisor = clinicalSupervisor;
  }

  public PostDTO getPost() {
    return post;
  }

  public void setPost(PostDTO post) {
    this.post = post;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public String getManagingLocalOffice() {
    return managingLocalOffice;
  }

  public void setManagingLocalOffice(String managingLocalOffice) {
    this.managingLocalOffice = managingLocalOffice;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
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

  public PlacementType getPlacementType() {
    return placementType;
  }

  public void setPlacementType(PlacementType placementType) {
    this.placementType = placementType;
  }

  public Float getPlacementWholeTimeEquivalent() {
    return placementWholeTimeEquivalent;
  }

  public void setPlacementWholeTimeEquivalent(Float placementWholeTimeEquivalent) {
    this.placementWholeTimeEquivalent = placementWholeTimeEquivalent;
  }

  public Set<PlacementSpecialtyDTO> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(Set<PlacementSpecialtyDTO> specialties) {
    this.specialties = specialties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementViewDTO placementViewDTO = (PlacementViewDTO) o;

    if (!Objects.equals(id, placementViewDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "PlacementViewDTO{" +
        "id=" + id +
        ", status='" + status + "'" +
        ", intrepidId='" + intrepidId + "'" +
        ", trainee='" + trainee + "'" +
        ", clinicalSupervisor='" + clinicalSupervisor + "'" +
        ", post='" + post + "'" +
        ", siteId='" + siteId + "'" +
        ", gradeId='" + gradeId + "'" +
        ", specialties='" + specialties + "'" +
        ", managingLocalOffice='" + managingLocalOffice + "'" +
        ", dateFrom='" + dateFrom + "'" +
        ", dateTo='" + dateTo + "'" +
        ", placementType='" + placementType + "'" +
        ", placementWholeTimeEquivalent='" + placementWholeTimeEquivalent + "'" +
        ", localPostNumber='" + localPostNumber + "'" +
        ", trainingDescription='" + trainingDescription + "'" +
        '}';
  }
}
