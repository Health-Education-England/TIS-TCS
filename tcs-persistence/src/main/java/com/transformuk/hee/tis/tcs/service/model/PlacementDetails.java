package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 * Holds the data from the placements table necessary to populate the {@link
 * com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO}
 */
@Data
@Entity
@Table(name = "Placement")
public class PlacementDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String intrepidId;

  private Long traineeId;

  private Long postId;

  private LocalDate dateFrom;

  private LocalDate dateTo;

  @Column(name = "placementWholeTimeEquivalent")
  private Float wholeTimeEquivalent;

  private Long siteId;

  private String siteCode;

  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<PlacementSite> sites = new HashSet<>();

  private Long gradeId;

  private String gradeAbbreviation;

  private String placementType;

  private String trainingDescription;

  private String localPostNumber;

  @Column(name = "placementAddedDate")
  private LocalDateTime addedDate;

  @Column(name = "placementAmendedDate")
  private LocalDateTime amendedDate;

  @OneToMany(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Set<Comment> comments = new HashSet<>();

  @Enumerated(EnumType.STRING)
  private LifecycleState lifecycleState;

  /**
   * @return the placement status based on dateFrom and dateTo
   */
  public PlacementStatus getStatus() {
    if (this.dateFrom == null || this.dateTo == null) {
      return null;
    }

    LocalDate today = LocalDate.now();
    if (today.isBefore(this.dateFrom)) {
      return PlacementStatus.FUTURE;
    } else if (today.isAfter(this.dateTo)) {
      return PlacementStatus.PAST;
    }
    return PlacementStatus.CURRENT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementDetails that = (PlacementDetails) o;

    return Objects.equals(id, that.id) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(traineeId, that.traineeId) &&
        Objects.equals(postId, that.postId) &&
        Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        Objects.equals(wholeTimeEquivalent, that.wholeTimeEquivalent) &&
        Objects.equals(siteId, that.siteId) &&
        Objects.equals(siteCode, that.siteCode) &&
        Objects.equals(gradeId, that.gradeId) &&
        Objects.equals(gradeAbbreviation, that.gradeAbbreviation) &&
        Objects.equals(placementType, that.placementType) &&
        Objects.equals(trainingDescription, that.trainingDescription) &&
        Objects.equals(addedDate, that.addedDate) &&
        Objects.equals(amendedDate, that.amendedDate) &&
        Objects.equals(localPostNumber, that.localPostNumber) &&
        Objects.equals(lifecycleState, that.lifecycleState);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, intrepidId, traineeId, postId, dateFrom, dateTo, wholeTimeEquivalent, siteId,
            siteCode, gradeId, gradeAbbreviation, placementType, trainingDescription, localPostNumber,
            addedDate, amendedDate, lifecycleState);
  }
}
