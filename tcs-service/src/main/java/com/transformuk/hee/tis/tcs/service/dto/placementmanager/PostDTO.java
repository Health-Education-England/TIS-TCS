package com.transformuk.hee.tis.tcs.service.dto.placementmanager;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PostDTO implements Serializable {
  private Long id;
  private String nationalPostNumber;
  private List<PlacementDTO> placements;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
  }

  public List<PlacementDTO> getPlacements() {
    return placements;
  }

  public void setPlacements(List<PlacementDTO> placements) {
    this.placements = placements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PostDTO postDTO = (PostDTO) o;
    return Objects.equals(id, postDTO.id) &&
        Objects.equals(nationalPostNumber, postDTO.nationalPostNumber) &&
        Objects.equals(placements, postDTO.placements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nationalPostNumber, placements);
  }
}
