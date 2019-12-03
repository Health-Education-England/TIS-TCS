package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementLogOperation;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

  private PlacementLogOperation operation;

  private LocalDateTime validDateFrom;

  private LocalDateTime validDateTo;
}
