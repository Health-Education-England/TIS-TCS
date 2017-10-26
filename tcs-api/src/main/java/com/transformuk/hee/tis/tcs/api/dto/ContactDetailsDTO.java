package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
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

  private String legalSurname;

  @NotNull(message = "Forenames is required", groups = {Update.class, Create.class})
  private String forenames;

  private String legalForenames;

  private String knownAs;

  private String maidenName;

  @NotNull(message = "Initials is required", groups = {Update.class, Create.class})
  private String initials;

  @NotNull(message = "Title is required", groups = {Update.class, Create.class})
  private String title;

  private String telephoneNumber;

  private String mobileNumber;

  @NotNull(message = "Email is required", groups = {Update.class, Create.class})
  @Email(message = "Valid email is required", groups = {Update.class, Create.class})
  private String email;

  @Email(message = "Valid email is required", groups = {Update.class, Create.class})
  private String workEmail;

  @NotNull(message = "Address is required", groups = {Update.class, Create.class})
  private String address1;

  private String address2;

  private String address3;

  private String address4;

  @NotNull(message = "PostCode is required", groups = {Update.class, Create.class})
  private String postCode;

  private LocalDateTime amendedDate;

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

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
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

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getAddress3() {
    return address3;
  }

  public void setAddress3(String address3) {
    this.address3 = address3;
  }

  public String getAddress4() {
    return address4;
  }

  public void setAddress4(String address4) {
    this.address4 = address4;
  }

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public String getLegalSurname() {
    return legalSurname;
  }

  public void setLegalSurname(String legalSurname) {
    this.legalSurname = legalSurname;
  }

  public String getLegalForenames() {
    return legalForenames;
  }

  public void setLegalForenames(String legalForenames) {
    this.legalForenames = legalForenames;
  }

  public LocalDateTime getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
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
        "id=" + id +
        ", surname='" + surname + '\'' +
        ", legalSurname='" + legalSurname + '\'' +
        ", forenames='" + forenames + '\'' +
        ", legalForenames='" + legalForenames + '\'' +
        ", knownAs='" + knownAs + '\'' +
        ", maidenName='" + maidenName + '\'' +
        ", initials='" + initials + '\'' +
        ", title='" + title + '\'' +
        ", telephoneNumber='" + telephoneNumber + '\'' +
        ", mobileNumber='" + mobileNumber + '\'' +
        ", email='" + email + '\'' +
        ", workEmail='" + workEmail + '\'' +
        ", address1='" + address1 + '\'' +
        ", address2='" + address2 + '\'' +
        ", address3='" + address3 + '\'' +
        ", address4='" + address4 + '\'' +
        ", postCode='" + postCode + '\'' +
        ", amendedDate='" + amendedDate + '\'' +
        '}';
  }
}
