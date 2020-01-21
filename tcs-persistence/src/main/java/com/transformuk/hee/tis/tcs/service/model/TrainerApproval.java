package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.ApprovalStatus;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
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

  @ManyToOne
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
}
