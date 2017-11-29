package com.transformuk.hee.tis.tcs.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ContactDetails")
public class PersonBasicDetails {

  @Id
  private Long id;

  @Column(name = "forenames")
  private String firstName;

  @Column(name = "surname")
  private String lastName;

  @OneToOne
  @JoinColumn(name="id", unique=true, nullable=false, updatable=false)
  private  GmcDetails gmcDetails;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public GmcDetails getGmcDetails() {
    return gmcDetails;
  }

  public void setGmcDetails(GmcDetails gmcDetails) {
    this.gmcDetails = gmcDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonBasicDetails that = (PersonBasicDetails) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
    if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
    return gmcDetails != null ? gmcDetails.equals(that.gmcDetails) : that.gmcDetails == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (gmcDetails != null ? gmcDetails.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PersonBasicDetails{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", gmcDetails=" + gmcDetails +
        '}';
  }
}
