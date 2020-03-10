package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.QualificationType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * A DTO for the Qualification entity.
 */
@Data
public class QualificationDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a qualification")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new qualification")
  private Long id;

  private String intrepidId;

  @NotNull(message = "Qualification is required", groups = {Update.class, Create.class})
  private String qualification;

  private PersonDTO person;

  private QualificationType qualificationType;

  private LocalDate qualificationAttainedDate;

  @NotNull(message = "Medical School is required", groups = {Update.class, Create.class})
  private String medicalSchool;

  @NotNull(message = "Country Of Qualification is required", groups = {Update.class, Create.class})
  private String countryOfQualification;

  private LocalDateTime amendedDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QualificationDTO that = (QualificationDTO) o;
    return Objects.equals(qualification, that.qualification) &&
        Objects.equals(getPersonIdOrNull(), that.getPersonIdOrNull()) &&
        qualificationType == that.qualificationType &&
        Objects.equals(qualificationAttainedDate, that.qualificationAttainedDate) &&
        Objects.equals(medicalSchool, that.medicalSchool) &&
        Objects.equals(countryOfQualification, that.countryOfQualification);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(qualification, getPersonIdOrNull(), qualificationType, qualificationAttainedDate,
            medicalSchool, countryOfQualification);
  }

  private Long getPersonIdOrNull() {
    return person != null ? person.getId() : null;
  }
}
