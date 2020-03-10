package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

/**
 * A DTO for the ContactDetails entity.
 */
@Data
public class ContactDetailsDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class,
      Create.class}, message = "Id must not be negative")
  private Long id;

  @NotNull(message = "Surname is required to create or update the record", groups = {Update.class,
      Create.class})
  @Pattern(regexp = "^$|^[A-Za-z0-9\\-\\\\' ]+",
      message = "No special characters, with the exception of apostrophes, hyphens and spaces",
      groups = {Update.class, Create.class})
  private String surname;

  @Pattern(regexp = "^$|^[A-Za-z0-9\\-\\\\' ]+",
      message = "No special characters, with the exception of apostrophes, hyphens and spaces",
      groups = {Update.class, Create.class})
  private String legalSurname;

  @NotNull(message = "Forenames is required to create or update the record", groups = {Update.class,
      Create.class})
  @Pattern(regexp = "^$|^[A-Za-z0-9\\-\\\\' ]+",
      message = "No special characters, with the exception of apostrophes, hyphens and spaces",
      groups = {Update.class, Create.class})
  private String forenames;

  @Pattern(regexp = "^$|^[A-Za-z0-9\\-\\\\' ]+",
      message = "No special characters, with the exception of apostrophes, hyphens and spaces",
      groups = {Update.class, Create.class})
  private String legalForenames;

  @Pattern(regexp = "^$|^[A-Za-z0-9\\-\\\\' ]+",
      message = "No special characters, with the exception of apostrophes, hyphens and spaces",
      groups = {Update.class, Create.class})
  private String knownAs;

  @Pattern(regexp = "^$|^[A-Za-z0-9\\-\\\\' ]+",
      message = "No special characters, with the exception of apostrophes, hyphens and spaces",
      groups = {Update.class, Create.class})
  private String maidenName;

  @Pattern(regexp = "^$|^[A-Za-z0-9\\-\\\\' ]+",
      message = "No special characters, with the exception of apostrophes, hyphens and spaces",
      groups = {Update.class, Create.class})
  private String initials;

  private String title;

  @Pattern(regexp = "^$|^[0-9\\\\+\\- ]+",
      message = "Only numerical values allowed for TelephoneNumber, no special characters, with the exception of plus, minus and spaces",
      groups = {Update.class, Create.class})
  private String telephoneNumber;

  @Pattern(regexp = "^$|^[0-9\\\\+\\- ]+",
      message = "Only numerical values allowed for MobileNumber, no special characters, with the exception of plus, minus and spaces",
      groups = {Update.class, Create.class})
  private String mobileNumber;

  @Email(message = "Valid email format required", groups = {Update.class, Create.class})
  private String email;

  private String address1;

  private String address2;

  private String address3;

  private String address4;

  private String postCode;

  private LocalDateTime amendedDate;

  @Email(message = "Valid email format required", groups = {Update.class, Create.class})
  private String workEmail;

  @Pattern(regexp = "^$|^[A-Za-z0-9\\-\\\\' ]+",
      message = "No special characters, with the exception of apostrophes, hyphens and spaces",
      groups = {Update.class, Create.class})
  private String country;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ContactDetailsDTO contactDetailsDTO = (ContactDetailsDTO) o;
    if (contactDetailsDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), contactDetailsDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
