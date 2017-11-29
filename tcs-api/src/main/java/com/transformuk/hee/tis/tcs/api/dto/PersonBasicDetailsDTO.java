package com.transformuk.hee.tis.tcs.api.dto;


/**
 * Holds basic details for a person. This is useful when needing to display name and gmc number in the UI
 */
public class PersonBasicDetailsDTO {

  private Long id;
  private String firstName;
  private String lastName;
  private String gmcNumber;

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

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
  }
}
