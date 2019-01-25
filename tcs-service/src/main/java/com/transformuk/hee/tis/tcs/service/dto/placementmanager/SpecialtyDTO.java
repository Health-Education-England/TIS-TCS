package com.transformuk.hee.tis.tcs.service.dto.placementmanager;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SpecialtyDTO implements Serializable {

  private Long id;
  private String name;
  private String college;
  private List<SiteDTO> sites;

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

  public String getCollege() {
    return college;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public List<SiteDTO> getSites() {
    return sites;
  }

  public void setSites(List<SiteDTO> sites) {
    this.sites = sites;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SpecialtyDTO that = (SpecialtyDTO) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        Objects.equals(college, that.college) &&
        Objects.equals(sites, that.sites);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, college, sites);
  }
}
