package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the Specialty entity. It does not contain it's relationships to types and groups. Use
 * this when you want a faster response that does not hit any other data table except Specialties.
 */
@Data
public class SpecialtySimpleDTO implements Serializable {

  private Long id;

  private String intrepidId;

  private Status status;

  private String college;

  private String specialtyCode;

  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SpecialtySimpleDTO specialtyDTO = (SpecialtySimpleDTO) o;

    if (!Objects.equals(id, specialtyDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
