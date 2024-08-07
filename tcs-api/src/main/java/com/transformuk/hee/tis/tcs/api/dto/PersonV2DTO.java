package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;

/**
 * Copy of the PersonDTO without the Qualification.
 * <p>
 * This was introduced as we dont want trust admin users to access the qualification data.
 * <p>
 * The issues was around the ger person endpoint where it provides too much information. This DTO
 * will be used in a new endpoint so have as little impact on other services on the origin get
 * person endpoint as possible
 */
@Data
public class PersonV2DTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a person")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new person")
  private Long id;

  private String intrepidId;

  private LocalDateTime addedDate;

  private LocalDateTime amendedDate;

  private String role;

  @NotNull(message = "Status is required", groups = {Update.class, Create.class})
  private Status status;

  private String comments;

  private LocalDateTime inactiveDate;

  private String inactiveNotes;

  private String publicHealthNumber;

  private String regulator;

  private ContactDetailsDTO contactDetails;

  private PersonalDetailsDTO personalDetails;

  private GmcDetailsDTO gmcDetails;

  private GdcDetailsDTO gdcDetails;

  private RightToWorkDTO rightToWork;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PersonV2DTO personDTO = (PersonV2DTO) o;

    if (id != null ? !id.equals(personDTO.id) : personDTO.id != null) {
      return false;
    }
    if (intrepidId != null ? !intrepidId.equals(personDTO.intrepidId)
        : personDTO.intrepidId != null) {
      return false;
    }
    if (addedDate != null ? !addedDate.equals(personDTO.addedDate) : personDTO.addedDate != null) {
      return false;
    }
    if (amendedDate != null ? !amendedDate.equals(personDTO.amendedDate)
        : personDTO.amendedDate != null) {
      return false;
    }
    if (role != null ? !role.equals(personDTO.role) : personDTO.role != null) {
      return false;
    }
    if (status != null ? !status.equals(personDTO.status) : personDTO.status != null) {
      return false;
    }
    if (comments != null ? !comments.equals(personDTO.comments) : personDTO.comments != null) {
      return false;
    }
    if (inactiveDate != null ? !inactiveDate.equals(personDTO.inactiveDate)
        : personDTO.inactiveDate != null) {
      return false;
    }
    if (inactiveNotes != null ? !inactiveNotes.equals(personDTO.inactiveNotes)
        : personDTO.inactiveNotes != null) {
      return false;
    }
    if (publicHealthNumber != null ? !publicHealthNumber.equals(personDTO.publicHealthNumber)
        : personDTO.publicHealthNumber != null) {
      return false;
    }
    if (regulator != null ? !regulator.equals(personDTO.regulator) : personDTO.regulator != null) {
      return false;
    }
    if (contactDetails != null ? !contactDetails.equals(personDTO.contactDetails)
        : personDTO.contactDetails != null) {
      return false;
    }
    if (personalDetails != null ? !personalDetails.equals(personDTO.personalDetails)
        : personDTO.personalDetails != null) {
      return false;
    }
    if (gmcDetails != null ? !gmcDetails.equals(personDTO.gmcDetails)
        : personDTO.gmcDetails != null) {
      return false;
    }
    if (gdcDetails != null ? !gdcDetails.equals(personDTO.gdcDetails)
        : personDTO.gdcDetails != null) {
      return false;
    }
    return rightToWork != null ? rightToWork.equals(personDTO.rightToWork)
        : personDTO.rightToWork == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (addedDate != null ? addedDate.hashCode() : 0);
    result = 31 * result + (amendedDate != null ? amendedDate.hashCode() : 0);
    result = 31 * result + (role != null ? role.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (comments != null ? comments.hashCode() : 0);
    result = 31 * result + (inactiveDate != null ? inactiveDate.hashCode() : 0);
    result = 31 * result + (inactiveNotes != null ? inactiveNotes.hashCode() : 0);
    result = 31 * result + (publicHealthNumber != null ? publicHealthNumber.hashCode() : 0);
    result = 31 * result + (regulator != null ? regulator.hashCode() : 0);
    result = 31 * result + (contactDetails != null ? contactDetails.hashCode() : 0);
    result = 31 * result + (personalDetails != null ? personalDetails.hashCode() : 0);
    result = 31 * result + (gmcDetails != null ? gmcDetails.hashCode() : 0);
    result = 31 * result + (gdcDetails != null ? gdcDetails.hashCode() : 0);
    result = 31 * result + (rightToWork != null ? rightToWork.hashCode() : 0);
    return result;
  }
}
