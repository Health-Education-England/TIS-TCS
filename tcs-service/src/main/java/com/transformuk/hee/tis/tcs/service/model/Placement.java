package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * A Placement.
 */
@Entity
public class Placement implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "traineeId")
  private Person trainee;

  @ManyToOne
  @JoinColumn(name = "clinicalSupervisorId")
  private Person clinicalSupervisor;

  @Column(name = "intrepidId")
  private String intrepidId;

  @Enumerated(EnumType.STRING)
  private Status status;

  @ManyToOne
  @JoinColumn(name = "postId")
  private Post post;

  @Column(name = "siteId")
  private Long siteId;

  @Column(name = "gradeId")
  private Long gradeId;

  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<PlacementSpecialty> specialties;

  @Column(name = "dateFrom")
  private LocalDate dateFrom;

  @Column(name = "dateTo")
  private LocalDate dateTo;

  @Enumerated(EnumType.STRING)
  @Column(name = "placementType")
  private PlacementType placementType;

  @Column(name = "placementWholeTimeEquivalent")
  private Float placementWholeTimeEquivalent;

  @Column(name = "trainingDescription")
  private String trainingDescription;

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

  public Person getTrainee() {
    return trainee;
  }

  public void setTrainee(Person trainee) {
    this.trainee = trainee;
  }

  public Person getClinicalSupervisor() {
    return clinicalSupervisor;
  }

  public void setClinicalSupervisor(Person clinicalSupervisor) {
    this.clinicalSupervisor = clinicalSupervisor;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public Set<PlacementSpecialty> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(Set<PlacementSpecialty> specialties) {
    this.specialties = specialties;
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

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(String trainingDescription) {
    this.trainingDescription = trainingDescription;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Placement placement = (Placement) o;
    if (placement.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, placement.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Placement{" +
        "id=" + id +
        ", status='" + status + "'" +
        ", post='" + post + "'" +
        ", siteId='" + siteId + "'" +
        ", gradeId='" + gradeId + "'" +
        ", specialties='" + specialties + "'" +
        ", dateFrom='" + dateFrom + "'" +
        ", dateTo='" + dateTo + "'" +
        ", placementType='" + placementType + "'" +
        ", placementWholeTimeEquivalent='" + placementWholeTimeEquivalent + "'" +
        '}';
  }
}
