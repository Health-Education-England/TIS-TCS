package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the Curriculum entity.
 */
@Data
public class CurriculumDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a curriculum")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new curriculum")
  private Long id;

  private String intrepidId;

  @NotNull(message = "Status is required", groups = {Update.class, Create.class})
  // mandatory and must be a valid ENUM value
  private Status status;

  @NotNull(groups = {Create.class, Update.class}, message = "name must not be null")
  private String name;

  private CurriculumSubType curriculumSubType;

  @DecimalMin(value = "0", groups = {Create.class, Update.class},
      message = "length must not be negative")
  @NotNull(groups = {Create.class, Update.class}, message = "length must not be null")
  private Integer length;

  @NotNull(groups = {Create.class, Update.class}, message = "assessmentType must not be null")
  private AssessmentType assessmentType;

  @NotNull(groups = {Create.class, Update.class},
      message = "doesThisCurriculumLeadToCct must not be null")
  private Boolean doesThisCurriculumLeadToCct;

  @DecimalMin(value = "0", groups = {Create.class, Update.class},
      message = "periodOfGrace must not be negative")
  @NotNull(groups = {Create.class, Update.class}, message = "periodOfGrace must not be null")
  private Integer periodOfGrace;

  @NotNull(groups = {Create.class, Update.class}, message = "specialty must not be null")
  private SpecialtyDTO specialty;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CurriculumDTO curriculumDTO = (CurriculumDTO) o;

    if (!Objects.equals(id, curriculumDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
