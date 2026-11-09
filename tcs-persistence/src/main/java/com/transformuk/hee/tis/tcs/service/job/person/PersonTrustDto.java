package com.transformuk.hee.tis.tcs.service.job.person;

import java.io.Serializable;
import java.util.Objects;

public class PersonTrustDto implements Serializable {

  private Long personId;
  private Long trustId;
  private String status;

  public PersonTrustDto() {
  }

  public PersonTrustDto(Long personId, Long trustId) {
    this(personId, trustId, null);
  }

  public PersonTrustDto(Long personId, Long trustId, String status) {
    this.personId = personId;
    this.trustId = trustId;
    this.status = status;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public Long getTrustId() {
    return trustId;
  }

  public void setTrustId(Long trustId) {
    this.trustId = trustId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonTrustDto that = (PersonTrustDto) o;
    return Objects.equals(personId, that.personId)
        && Objects.equals(trustId, that.trustId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personId, trustId);
  }
}
