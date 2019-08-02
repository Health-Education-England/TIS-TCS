package com.transformuk.hee.tis.tcs.service.job.person;

import java.io.Serializable;
import java.util.Objects;

public class PersonTrustDto implements Serializable {

  private Long personId;
  private Long trustId;

  public PersonTrustDto() {
  }

  public PersonTrustDto(Long personId, Long trustId) {
    this.personId = personId;
    this.trustId = trustId;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonTrustDto that = (PersonTrustDto) o;
    return Objects.equals(personId, that.personId) &&
        Objects.equals(trustId, that.trustId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personId, trustId);
  }
}
