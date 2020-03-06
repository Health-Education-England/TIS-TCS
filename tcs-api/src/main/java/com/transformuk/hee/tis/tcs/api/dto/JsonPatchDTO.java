package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the JsonPath entity.
 */
@Data
public class JsonPatchDTO implements Serializable {

  private Long id;

  @NotNull
  private String tableDtoName;

  private String patchId;

  private String patch;

  private Date dateAdded;

  private Boolean enabled;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JsonPatchDTO countryDTO = (JsonPatchDTO) o;

    if (!Objects.equals(id, countryDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
