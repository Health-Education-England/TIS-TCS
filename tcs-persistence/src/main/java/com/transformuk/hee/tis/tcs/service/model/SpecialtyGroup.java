package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * A SpecialtyGroup.
 */
@Data
@Entity
@Table(name = "SpecialtyGroup")
public class SpecialtyGroup implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid")
  private UUID uuid;

  private String intrepidId;

  private String name;

  @OneToMany(mappedBy = "specialtyGroup")
  private Set<Specialty> specialties = new HashSet<>();

  public SpecialtyGroup specialties(Set<Specialty> specialties) {
    this.specialties = specialties;
    return this;
  }

  public SpecialtyGroup addSpecialty(Specialty specialty) {
    this.specialties.add(specialty);
    return this;
  }

  public SpecialtyGroup removeSpecialty(Specialty specialty) {
    this.specialties.remove(specialty);
    return this;
  }

  public SpecialtyGroup name(String name) {
    this.name = name;
    return this;
  }

  public SpecialtyGroup intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpecialtyGroup specialtyGroup = (SpecialtyGroup) o;
    if (specialtyGroup.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, specialtyGroup.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
