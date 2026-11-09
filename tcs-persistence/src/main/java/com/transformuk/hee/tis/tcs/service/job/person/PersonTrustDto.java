package com.transformuk.hee.tis.tcs.service.job.person;

import com.transformuk.hee.tis.tcs.api.enumeration.PersonTrustStatus;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO to hold the personId, trustId and status of a person-trust relationship.
 */
@Setter
@Getter
public class PersonTrustDto implements Serializable {

  private Long personId;
  private Long trustId;
  private PersonTrustStatus status;

  public PersonTrustDto() {
  }

  public PersonTrustDto(Long personId, Long trustId) {
    this.personId = personId;
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
    return Objects.equals(personId, that.personId)
        && Objects.equals(trustId, that.trustId)
        && Objects.equals(status, that.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personId, trustId, status);
  }
}
