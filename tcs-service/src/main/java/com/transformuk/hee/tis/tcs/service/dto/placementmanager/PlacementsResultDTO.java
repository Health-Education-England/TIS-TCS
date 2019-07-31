package com.transformuk.hee.tis.tcs.service.dto.placementmanager;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PlacementsResultDTO implements Serializable {

  private List<SpecialtyDTO> specialties;
  private Integer totalSpecialties;
  private Integer totalSites;
  private Integer totalPlacements;

  public List<SpecialtyDTO> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(List<SpecialtyDTO> specialties) {
    this.specialties = specialties;
  }

  public Integer getTotalSpecialties() {
    return totalSpecialties;
  }

  public void setTotalSpecialties(Integer totalSpecialties) {
    this.totalSpecialties = totalSpecialties;
  }

  public Integer getTotalSites() {
    return totalSites;
  }

  public void setTotalSites(Integer totalSites) {
    this.totalSites = totalSites;
  }

  public Integer getTotalPlacements() {
    return totalPlacements;
  }

  public void setTotalPlacements(Integer totalPlacements) {
    this.totalPlacements = totalPlacements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementsResultDTO that = (PlacementsResultDTO) o;
    return Objects.equals(specialties, that.specialties) &&
        Objects.equals(totalSpecialties, that.totalSpecialties) &&
        Objects.equals(totalSites, that.totalSites) &&
        Objects.equals(totalPlacements, that.totalPlacements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(specialties, totalSpecialties, totalSites, totalPlacements);
  }
}
