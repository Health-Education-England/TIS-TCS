package com.transformuk.hee.tis.tcs.service.model;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * A ContactDetails.
 */
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


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ContactDetails id(Long id) {
    this.id = id;
    return this;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public ContactDetails surname(String surname) {
    this.surname = surname;
    return this;
  }

  public String getForenames() {
    return forenames;
  }

  public void setForenames(String forenames) {
    this.forenames = forenames;
  }

  public ContactDetails forenames(String forenames) {
    this.forenames = forenames;
    return this;
  }

  public String getKnownAs() {
    return knownAs;
  }

  public void setKnownAs(String knownAs) {
    this.knownAs = knownAs;
  }

  public ContactDetails knownAs(String knownAs) {
    this.knownAs = knownAs;
    return this;
  }

  public String getMaidenName() {
    return maidenName;
  }

  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }

  public ContactDetails maidenName(String maidenName) {
    this.maidenName = maidenName;
    return this;
  }

  public String getInitials() {
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public ContactDetails initials(String initials) {
    this.initials = initials;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ContactDetails title(String title) {
    this.title = title;
    return this;
  }

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public ContactDetails telephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
    return this;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public ContactDetails mobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ContactDetails email(String email) {
    this.email = email;
    return this;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address) {
    this.address1 = address;
  }

  public ContactDetails address1(String address) {
    this.address1 = address;
    return this;
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

  public ContactDetails postCode(String postCode) {
    this.postCode = postCode;
    return this;
  }

  public String getLegalSurname() {
    return legalSurname;
  }

  public void setLegalSurname(String legalSurname) {
    this.legalSurname = legalSurname;
  }

  public ContactDetails legalSurname(String legalSurname) {
    this.legalSurname = legalSurname;
    return this;
  }

  public String getLegalForenames() {
    return legalForenames;
  }

  public void setLegalForenames(String legalForenames) {
    this.legalForenames = legalForenames;
  }

  public ContactDetails legalForenames(String legalForenames) {
    this.legalForenames = legalForenames;
    return this;
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

  @Override
  public String toString() {
    return "ContactDetails{" +
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
        ", address1='" + address1 + '\'' +
        ", address2='" + address2 + '\'' +
        ", address3='" + address3 + '\'' +
        ", address4='" + address4 + '\'' +
        ", postCode='" + postCode + '\'' +
        ", amendedDate='" + amendedDate + '\'' +
        '}';
  }
}
