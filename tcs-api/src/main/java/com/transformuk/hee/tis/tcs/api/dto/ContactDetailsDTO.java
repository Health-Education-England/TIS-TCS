package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ContactDetails entity.
 */
public class ContactDetailsDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class, Create.class}, message = "Id must not be negative")
  private Long id;

  @NotNull(message = "Surname is required", groups = {Update.class, Create.class})
  private String surname;

  @NotNull(message = "Forenames is required", groups = {Update.class, Create.class})
  private String forenames;

  private String knownAs;

  private String maidenName;

  @NotNull(message = "Initials is required", groups = {Update.class, Create.class})
  private String initials;

  @NotNull(message = "Title is required", groups = {Update.class, Create.class})
  private String title;

  private String contactPhoneNr1;

  private String contactPhoneNr2;

  @NotNull(message = "Email is required", groups = {Update.class, Create.class})
  @Email(message = "Valid email is required", groups = {Update.class, Create.class})
  private String email;

  @Email(message = "Valid email is required", groups = {Update.class, Create.class})
  private String workEmail;

  @NotNull(message = "Address is required", groups = {Update.class, Create.class})
  private String address;

  @NotNull(message = "PostCode is required", groups = {Update.class, Create.class})
  private String postCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getForenames() {
    return forenames;
  }

  public void setForenames(String forenames) {
    this.forenames = forenames;
  }

  public String getKnownAs() {
    return knownAs;
  }

  public void setKnownAs(String knownAs) {
    this.knownAs = knownAs;
  }

  public String getMaidenName() {
    return maidenName;
  }

  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }

  public String getInitials() {
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContactPhoneNr1() {
    return contactPhoneNr1;
  }

  public void setContactPhoneNr1(String contactPhoneNr1) {
    this.contactPhoneNr1 = contactPhoneNr1;
  }

  public String getContactPhoneNr2() {
    return contactPhoneNr2;
  }

  public void setContactPhoneNr2(String contactPhoneNr2) {
    this.contactPhoneNr2 = contactPhoneNr2;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(String workEmail) {
    this.workEmail = workEmail;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

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

  @Override
  public String toString() {
    return "ContactDetailsDTO{" +
        "id=" + getId() +
        ", surname='" + getSurname() + "'" +
        ", forenames='" + getForenames() + "'" +
        ", knownAs='" + getKnownAs() + "'" +
        ", maidenName='" + getMaidenName() + "'" +
        ", initials='" + getInitials() + "'" +
        ", title='" + getTitle() + "'" +
        ", contactPhoneNr1='" + getContactPhoneNr1() + "'" +
        ", contactPhoneNr2='" + getContactPhoneNr2() + "'" +
        ", email='" + getEmail() + "'" +
        ", workEmail='" + getWorkEmail() + "'" +
        ", address='" + getAddress() + "'" +
        ", postCode='" + getPostCode() + "'" +
        "}";
  }
}
