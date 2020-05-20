package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

@Data
public class PlacementDetailsDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a placement")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new placement")
  private Long id;

  private String intrepidId;

  @NotNull(message = "TraineeId is required", groups = {Update.class, Create.class})
  private Long traineeId;

  private String traineeFirstName;

  private String traineeLastName;

  private String traineeGmcNumber;

  private String nationalPostNumber;

  @NotNull(message = "Date from is required", groups = {Update.class, Create.class})
  private LocalDate dateFrom;

  @NotNull(message = "Date to is required", groups = {Update.class, Create.class})
  private LocalDate dateTo;

  private Float wholeTimeEquivalent;

  private String siteCode;

  private Set<PlacementSiteDTO> sites = new HashSet<>();

  @NotNull(message = "SiteId is required", groups = {Update.class, Create.class})
  private Long siteId;

  private String siteName;

  private String gradeAbbreviation;

  @NotNull(message = "GradeId is required", groups = {Update.class, Create.class})
  private Long gradeId;

  private String gradeName;

  //TODO to add clinical supervisor and specialties

  @NotNull(message = "PlacementType is required", groups = {Update.class, Create.class})
  private String placementType;

  private String owner;

  private String trainingDescription;

  private PlacementStatus status;

  @NotNull(message = "PostId is required", groups = {Update.class, Create.class})
  private Long postId;

  private String localPostNumber;

  private Set<PlacementSpecialtyDTO> specialties;

  private LocalDateTime addedDate;

  private LocalDateTime amendedDate;

  private Set<PlacementSupervisorDTO> supervisors = new HashSet<>();

  private Set<PlacementCommentDTO> comments = new HashSet<>();

  private LifecycleState lifecycleState;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final PlacementDetailsDTO that = (PlacementDetailsDTO) o;

    return Objects.equals(id, that.id) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(traineeId, that.traineeId) &&
        Objects.equals(traineeFirstName, that.traineeFirstName) &&
        Objects.equals(traineeLastName, that.traineeLastName) &&
        Objects.equals(traineeGmcNumber, that.traineeGmcNumber) &&
        Objects.equals(nationalPostNumber, that.nationalPostNumber) &&
        Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        Objects.equals(wholeTimeEquivalent, that.wholeTimeEquivalent) &&
        Objects.equals(siteCode, that.siteCode) &&
        Objects.equals(siteName, that.siteName) &&
        Objects.equals(gradeAbbreviation, that.gradeAbbreviation) &&
        Objects.equals(gradeName, that.gradeName) &&
        Objects.equals(placementType, that.placementType) &&
        Objects.equals(owner, that.owner) &&
        Objects.equals(trainingDescription, that.trainingDescription) &&
        Objects.equals(status, that.status) &&
        Objects.equals(postId, that.postId) &&
        Objects.equals(addedDate, that.addedDate) &&
        Objects.equals(amendedDate, that.amendedDate) &&
        Objects.equals(localPostNumber, that.localPostNumber) &&
        Objects.equals(lifecycleState, that.lifecycleState);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, intrepidId, traineeId, traineeFirstName, traineeLastName, traineeGmcNumber,
            nationalPostNumber, dateFrom, dateTo, wholeTimeEquivalent, siteCode, siteName,
            gradeAbbreviation, gradeName, placementType, owner, trainingDescription,
            status, postId, addedDate, amendedDate, localPostNumber, lifecycleState);
  }
}
