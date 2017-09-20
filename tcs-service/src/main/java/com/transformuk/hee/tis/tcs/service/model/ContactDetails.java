package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

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

  private String contactPhoneNr1;

  private String contactPhoneNr2;

  private String email;

  private String workEmail;

  private String address;

  private String postCode;

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

  public String getContactPhoneNr1() {
    return contactPhoneNr1;
  }

  public void setContactPhoneNr1(String contactPhoneNr1) {
    this.contactPhoneNr1 = contactPhoneNr1;
  }

  public ContactDetails contactPhoneNr1(String contactPhoneNr1) {
    this.contactPhoneNr1 = contactPhoneNr1;
    return this;
  }

  public String getContactPhoneNr2() {
    return contactPhoneNr2;
  }

  public void setContactPhoneNr2(String contactPhoneNr2) {
    this.contactPhoneNr2 = contactPhoneNr2;
  }

  public ContactDetails contactPhoneNr2(String contactPhoneNr2) {
    this.contactPhoneNr2 = contactPhoneNr2;
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

  public String getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(String workEmail) {
    this.workEmail = workEmail;
  }

  public ContactDetails workEmail(String workEmail) {
    this.workEmail = workEmail;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public ContactDetails address(String address) {
    this.address = address;
    return this;
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
        ", contactPhoneNr1='" + contactPhoneNr1 + '\'' +
        ", contactPhoneNr2='" + contactPhoneNr2 + '\'' +
        ", email='" + email + '\'' +
        ", workEmail='" + workEmail + '\'' +
        ", address='" + address + '\'' +
        ", postCode='" + postCode + '\'' +
        '}';
  }
}
