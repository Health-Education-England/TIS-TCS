package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.LifecycleState;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * A DTO for the Placement entity.
 */
@Data
public class PlacementDTO implements Serializable {

  private static final long serialVersionUID = 2794590706651836140L;

  @NotNull(groups = Update.class, message = "Id must not be null when updating a placement")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new placement")
  private Long id;
  private String intrepidId;
  private PlacementStatus status;
  @NotNull(message = "TraineeId is required", groups = {Update.class, Create.class})
  private Long traineeId;
  @NotNull(message = "PostId is required", groups = {Update.class, Create.class})
  private Long postId;
  @NotNull(message = "SiteId is required", groups = {Update.class, Create.class})
  private Long siteId;
  private Set<PlacementSiteDTO> sites = new HashSet<>();
  private String siteCode;
  //@NotNull(message = "GradeId is required", groups = {Update.class, Create.class})
  private Long gradeId;
  private String gradeAbbreviation;
  private Set<PlacementSpecialtyDTO> specialties;
  @NotNull(message = "Date from is required", groups = {Update.class, Create.class})
  private LocalDate dateFrom;
  @NotNull(message = "Date to is required", groups = {Update.class, Create.class})
  private LocalDate dateTo;
  @NotNull(message = "PlacementType is required", groups = {Update.class, Create.class})
  private String placementType;
  @Range(message = "WholeTimeEquivalent should be between 0 and 1", min = 0, max = 1,
      groups = {Update.class, Create.class})
  @Digits(message = "Format of wholeTimeEquivalent is not correct", integer = 1, fraction = 2,
      groups = {Update.class, Create.class})
  private BigDecimal placementWholeTimeEquivalent;
  private String trainingDescription;
  private String localPostNumber;
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

    final PlacementDTO placementDTO = (PlacementDTO) o;

    return Objects.equals(id, placementDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
