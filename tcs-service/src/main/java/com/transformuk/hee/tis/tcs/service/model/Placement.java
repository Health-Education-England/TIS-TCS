package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Placement.
 */
@SqlResultSetMapping(name = "PlacementsSummary", classes = {
    @ConstructorResult(targetClass = PlacementSummaryDTO.class,
        columns = {
            @ColumnResult(name = "dateFrom"),
            @ColumnResult(name = "dateTo"),
            @ColumnResult(name = "siteId"),
            @ColumnResult(name = "primarySpecialtyName"),
            @ColumnResult(name = "gradeId"),
            @ColumnResult(name = "placementType"),
            @ColumnResult(name = "status"),
            @ColumnResult(name = "forenames"),
            @ColumnResult(name = "surname"),
            @ColumnResult(name = "traineeId"),
            @ColumnResult(name = "placementId"),
            @ColumnResult(name = "placementSpecialtyType")
        })
})
@Entity
public class Placement implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "traineeId")
  private Person trainee;

  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<PlacementSupervisor> clinicalSupervisors = new HashSet<>();

  @Column(name = "intrepidId")
  private String intrepidId;

  @ManyToOne
  @JoinColumn(name = "postId")
  private Post post;

  //Please use site id, site codes from intrepid are NOT unique
  @Column(name = "siteCode")
  private String siteCode;

  //Please use grade id
  @Column(name = "gradeAbbreviation")
  private String gradeAbbreviation;

  @Column(name = "siteId")
  private Long siteId;

  @Column(name = "gradeId")
  private Long gradeId;

  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<PlacementSpecialty> specialties = new HashSet<>();

  @Column(name = "dateFrom")
  private LocalDate dateFrom;

  @Column(name = "dateTo")
  private LocalDate dateTo;

  @Column(name = "placementType")
  private String placementType;

  @Column(name = "placementWholeTimeEquivalent")
  private Float placementWholeTimeEquivalent;

  @Column(name = "trainingDescription")
  private String trainingDescription;

  @Column(name = "localPostNumber")
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

  public Person getTrainee() {
    return trainee;
  }

  public void setTrainee(Person trainee) {
    this.trainee = trainee;
  }

  public Set<PlacementSupervisor> getClinicalSupervisors() {
    return clinicalSupervisors;
  }

  public void setClinicalSupervisors(Set<PlacementSupervisor> clinicalSupervisors) {
    this.clinicalSupervisors = clinicalSupervisors;
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

  public String getPlacementType() {
    return placementType;
  }

  public void setPlacementType(String placementType) {
    this.placementType = placementType;
  }

  public Float getPlacementWholeTimeEquivalent() {
    return placementWholeTimeEquivalent;
  }

  public void setPlacementWholeTimeEquivalent(Float placementWholeTimeEquivalent) {
    this.placementWholeTimeEquivalent = placementWholeTimeEquivalent;
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
        ", trainee=" + trainee +
        ", clinicalSupervisors=" + clinicalSupervisors +
        ", intrepidId='" + intrepidId + '\'' +
        ", post=" + post +
        ", siteCode='" + siteCode + '\'' +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", siteId='" + siteId + '\'' +
        ", gradeId='" + gradeId + '\'' +
        ", specialties=" + specialties +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        ", placementType='" + placementType + '\'' +
        ", placementWholeTimeEquivalent=" + placementWholeTimeEquivalent +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", localPostNumber='" + localPostNumber + '\'' +
        '}';
  }
}
