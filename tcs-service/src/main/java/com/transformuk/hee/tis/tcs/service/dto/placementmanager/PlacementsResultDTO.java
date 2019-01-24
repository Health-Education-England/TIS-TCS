package com.transformuk.hee.tis.tcs.service.dto.placementmanager;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PlacementsResultDTO implements Serializable {
  private List<SpecialtyDTO> specialties;

  public List<SpecialtyDTO> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(List<SpecialtyDTO> specialties) {
    this.specialties = specialties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlacementsResultDTO that = (PlacementsResultDTO) o;
    return Objects.equals(specialties, that.specialties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(specialties);
  }
}
