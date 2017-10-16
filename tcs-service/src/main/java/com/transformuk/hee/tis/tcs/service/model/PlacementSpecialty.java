package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PlacementSpecialty")
public class PlacementSpecialty implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(targetEntity = Placement.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "placementId")
  private Placement placement;

  @ManyToOne
  @JoinColumn(name = "specialtyId")
  private Specialty specialty;

  @Enumerated(EnumType.STRING)
  @Column(name = "placementSpecialtyType")
  private PostSpecialtyType placementSpecialtyType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Placement getPlacement() {
    return placement;
  }

  public void setPlacement(Placement placement) {
    this.placement = placement;
  }

  public Specialty getSpecialty() {
    return specialty;
  }

  public void setSpecialty(Specialty specialty) {
    this.specialty = specialty;
  }

  public PostSpecialtyType getPlacementSpecialtyType() {
    return placementSpecialtyType;
  }

  public void setPlacementSpecialtyType(PostSpecialtyType placementSpecialtyType) {
    this.placementSpecialtyType = placementSpecialtyType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlacementSpecialty that = (PlacementSpecialty) o;

    if (placement != null ? !placement.equals(that.placement) : that.placement != null) return false;
    if (specialty != null ? !specialty.equals(that.specialty) : that.specialty != null) return false;
    return placementSpecialtyType == that.placementSpecialtyType;
  }

  @Override
  public int hashCode() {
    int result = placement != null ? placement.hashCode() : 0;
    result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
    result = 31 * result + (placementSpecialtyType != null ? placementSpecialtyType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PlacementSpecialty{" +
        "id=" + id +
        ", specialty=" + specialty +
        ", placementSpecialtyType=" + placementSpecialtyType +
        '}';
  }
}
