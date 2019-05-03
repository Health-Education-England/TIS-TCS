package com.transformuk.hee.tis.tcs.service.model;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A SpecialtyGroup.
 */

@Entity
@Table(name = "SpecialtyGroup")
public class SpecialtyGroup implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String intrepidId;

  private String name;

  @OneToMany(mappedBy = "specialtyGroup")
  private Set<Specialty> specialties = new HashSet<>();

  public Set<Specialty> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(Set<Specialty> specialties) {
    this.specialties = specialties;
  }

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

  public SpecialtyGroup name(String name) {
    this.name = name;
    return this;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
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

  @Override
  public String toString() {
    return "SpecialtyGroup{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
