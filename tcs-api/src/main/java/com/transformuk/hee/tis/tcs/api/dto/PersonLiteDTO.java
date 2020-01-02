package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Data
public class PersonLiteDTO implements Serializable {

  private static final long serialVersionUID = -3539117098212465210L;

  private Long id;
  private String name;
  private String publicHealthNumber;
  private String gmcNumber;
  private String gdcNumber;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final PersonLiteDTO person = (PersonLiteDTO) o;
    if (person.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), person.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
