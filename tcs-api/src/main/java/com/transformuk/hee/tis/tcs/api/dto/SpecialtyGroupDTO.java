package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the SpecialtyGroup entity.
 */
public class SpecialtyGroupDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id cannot be null when updating")
  @Null(groups = Create.class, message = "Id must be null when creating")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  private Long id;

  private String intrepidId;

  @NotBlank(groups = {Create.class, Update.class}, message = "Specialty group name cannot be blank")
  @Length(min = 1, max = 100, groups = {Create.class, Update.class},
      message = "Specialty group name must be at most 100 characters long")
  @Pattern(regexp = "^$|^[A-Za-z0-9 ]+",
      message = "Specialty group name must be alphanumeric",
      groups = {Update.class, Create.class})
  private String name;

  private Set<SpecialtyDTO> specialties;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Set<SpecialtyDTO> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(Set<SpecialtyDTO> specialties) {
    this.specialties = specialties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SpecialtyGroupDTO specialtyGroupDTO = (SpecialtyGroupDTO) o;

    if (!Objects.equals(id, specialtyGroupDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "SpecialtyGroupDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", name='" + name + '\'' +
        ", specialties=" + specialties +
        '}';
  }
}
