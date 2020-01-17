package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.ApprovalStatus;
import java.io.Serializable;
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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TrainerApproval implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "startDate")
  private LocalDate startDate;

  @Column(name = "endDate")
  private LocalDate endDate;

  @Column(name = "trainerType")
  private String trainerType;

  @Enumerated(EnumType.STRING)
  private ApprovalStatus approvalStatus;

  @ManyToOne//(fetch = FetchType.LAZY)
  @JoinColumn(name = "personId")
  private Person person;

  public TrainerApproval id(Long id){
    this.id = id;
    return this;
  }

  public TrainerApproval startDate(LocalDate startDate){
    this.startDate = startDate;
    return this;
  }

  public TrainerApproval endDate(LocalDate endDate){
    this.endDate = endDate;
    return this;
  }

  public TrainerApproval trainerType(String trainerType){
    this.trainerType = trainerType;
    return this;
  }

  public TrainerApproval approvalStatus(ApprovalStatus approvalStatus){
    this.approvalStatus = approvalStatus;
    return this;
  }

  /*public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getTrainerType() {
    return trainerType;
  }

  public void setTrainerType(String trainerType) {
    this.trainerType = trainerType;
  }

  public String getApprovalStatus() {
    return approvalStatus;
  }

  public void setApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public String toString() {
    return "TrainerApproval{" +
      "id=" + id +
      ", startDate=" + startDate +
      ", endDate=" + endDate +
      ", trainerType='" + trainerType + '\'' +
      ", approvalStatus='" + approvalStatus + '\'' +
      ", person=" + person +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TrainerApproval)) {
      return false;
    }
    TrainerApproval that = (TrainerApproval) o;
    return Objects.equals(getId(), that.getId()) &&
      Objects.equals(getStartDate(), that.getStartDate()) &&
      Objects.equals(getEndDate(), that.getEndDate()) &&
      Objects.equals(getTrainerType(), that.getTrainerType()) &&
      Objects.equals(getApprovalStatus(), that.getApprovalStatus()) &&
      Objects.equals(getPerson(), that.getPerson());
  }

  @Override
  public int hashCode() {

    return Objects
      .hash(getId(), getStartDate(), getEndDate(), getTrainerType(), getApprovalStatus(),
        getPerson());
  }*/
  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }
}
