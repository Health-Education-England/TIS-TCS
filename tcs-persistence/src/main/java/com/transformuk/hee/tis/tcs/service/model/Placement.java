package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
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
  private static final long serialVersionUID = 42021597714335688L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "traineeId")
  private Person trainee;
  @Column(name = "intrepidId")
  private String intrepidId;
  @ManyToOne(fetch = FetchType.LAZY)
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
  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL)
  private Set<PlacementSpecialty> specialties = new HashSet<>();
  @Column(name = "dateFrom")
  private LocalDate dateFrom;
  @Column(name = "dateTo")
  private LocalDate dateTo;
  @Column(name = "placementType")
  private String placementType;
  @Column(name = "placementWholeTimeEquivalent")
  private BigDecimal placementWholeTimeEquivalent;
  @Column(name = "trainingDescription")
  private String trainingDescription;
  @Column(name = "localPostNumber")
  private String localPostNumber;
  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Comment> comments = new HashSet<>();
  @Enumerated(EnumType.STRING)
  private Status status;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(final String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Person getTrainee() {
    return trainee;
  }

  public void setTrainee(final Person trainee) {
    this.trainee = trainee;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(final Post post) {
    this.post = post;
  }

  public Set<PlacementSpecialty> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(final Set<PlacementSpecialty> specialties) {
    this.specialties = specialties;
  }

  public String getSiteCode() {
    return siteCode;
  }

  public void setSiteCode(final String siteCode) {
    this.siteCode = siteCode;
  }

  public String getGradeAbbreviation() {
    return gradeAbbreviation;
  }

  public void setGradeAbbreviation(final String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(final String trainingDescription) {
    this.trainingDescription = trainingDescription;
  }

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(final String localPostNumber) {
    this.localPostNumber = localPostNumber;
  }

  public LocalDate getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(final LocalDate dateFrom) {
    this.dateFrom = dateFrom;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }

  public void setDateTo(final LocalDate dateTo) {
    this.dateTo = dateTo;
  }

  public String getPlacementType() {
    return placementType;
  }

  public void setPlacementType(final String placementType) {
    this.placementType = placementType;
  }

  public BigDecimal getPlacementWholeTimeEquivalent() {
    return placementWholeTimeEquivalent;
  }

  public void setPlacementWholeTimeEquivalent(final BigDecimal placementWholeTimeEquivalent) {
    this.placementWholeTimeEquivalent = placementWholeTimeEquivalent;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(final Long siteId) {
    this.siteId = siteId;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(final Long gradeId) {
    this.gradeId = gradeId;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
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
    Placement placement = (Placement) o;
    return Objects.equals(id, placement.id) &&
        Objects.equals(intrepidId, placement.intrepidId) &&
        Objects.equals(siteCode, placement.siteCode) &&
        Objects.equals(gradeAbbreviation, placement.gradeAbbreviation) &&
        Objects.equals(siteId, placement.siteId) &&
        Objects.equals(gradeId, placement.gradeId) &&
        Objects.equals(dateFrom, placement.dateFrom) &&
        Objects.equals(dateTo, placement.dateTo) &&
        Objects.equals(placementType, placement.placementType) &&
        Objects.equals(placementWholeTimeEquivalent, placement.placementWholeTimeEquivalent) &&
        Objects.equals(trainingDescription, placement.trainingDescription) &&
        Objects.equals(localPostNumber, placement.localPostNumber) &&
        status == placement.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, intrepidId, siteCode, gradeAbbreviation, siteId, gradeId, dateFrom, dateTo, placementType, placementWholeTimeEquivalent, trainingDescription, localPostNumber, status);
  }

  @Override
  public String toString() {
    return "Placement{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", siteCode='" + siteCode + '\'' +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", siteId=" + siteId +
        ", gradeId=" + gradeId +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        ", placementType='" + placementType + '\'' +
        ", placementWholeTimeEquivalent=" + placementWholeTimeEquivalent +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", localPostNumber='" + localPostNumber + '\'' +
        ", status=" + status +
        '}';
  }
}