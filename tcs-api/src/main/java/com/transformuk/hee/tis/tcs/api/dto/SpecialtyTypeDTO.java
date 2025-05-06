package com.transformuk.hee.tis.tcs.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the SpecialtyType entity
 */
@Data
public class SpecialtyTypeDTO {

  private String name;

  public SpecialtyTypeDTO() {
  }

  public SpecialtyTypeDTO(String name) {
    this.name = name;
  }

  @JsonCreator
  public static SpecialtyTypeDTO fromString(String name) {
    return new SpecialtyTypeDTO(name);
  }

  @JsonValue
  public String toValue() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpecialtyTypeDTO that = (SpecialtyTypeDTO) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
