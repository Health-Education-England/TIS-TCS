package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;

/**
 * A ContactDetails.
 */
@Data
@Entity
public class ContactDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private String surname;

  private String legalSurname;

  private String forenames;

  private String legalForenames;

  private String knownAs;

  private String maidenName;

  private String initials;

  private String title;

  private String telephoneNumber;

  private String mobileNumber;

  private String email;

  private String address1;

  private String address2;

  private String address3;

  private String address4;

  private String postCode;

  @Version
  private LocalDateTime amendedDate;

  private String workEmail;

  private String country;

  public ContactDetails id(Long id) {
    this.id = id;
    return this;
  }

  public ContactDetails surname(String surname) {
    this.surname = surname;
    return this;
  }

  public ContactDetails forenames(String forenames) {
    this.forenames = forenames;
    return this;
  }

  public ContactDetails knownAs(String knownAs) {
    this.knownAs = knownAs;
    return this;
  }

  public ContactDetails maidenName(String maidenName) {
    this.maidenName = maidenName;
    return this;
  }

  public ContactDetails initials(String initials) {
    this.initials = initials;
    return this;
  }

  public ContactDetails title(String title) {
    this.title = title;
    return this;
  }

  public ContactDetails telephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
    return this;
  }

  public ContactDetails mobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
    return this;
  }

  public ContactDetails email(String email) {
    this.email = email;
    return this;
  }

  public ContactDetails address1(String address) {
    this.address1 = address;
    return this;
  }

  public ContactDetails postCode(String postCode) {
    this.postCode = postCode;
    return this;
  }

  public ContactDetails legalSurname(String legalSurname) {
    this.legalSurname = legalSurname;
    return this;
  }

  public ContactDetails legalForenames(String legalForenames) {
    this.legalForenames = legalForenames;
    return this;
  }

  public ContactDetails workEmail(String workEmail) {
    this.workEmail = workEmail;
    return this;
  }

  public ContactDetails country(String country) {
    this.country = country;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContactDetails contactDetails = (ContactDetails) o;
    if (contactDetails.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), contactDetails.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
