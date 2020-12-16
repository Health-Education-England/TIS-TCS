package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

/**
 * A Specialty.
 */
@Data
@Entity
public class Specialty implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "uuid")
  private UUID uuid;

  private String intrepidId;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "college")
  private String college;

  @Column(name = "specialtyCode")
  private String specialtyCode;

  @ElementCollection(targetClass = SpecialtyType.class, fetch = FetchType.LAZY)
  @CollectionTable(name = "SpecialtyTypes", joinColumns = {@JoinColumn(name = "specialtyId")})
  @Column(name = "specialtyType")
  @Enumerated(EnumType.STRING)
  private Set<SpecialtyType> specialtyTypes = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "specialtyGroupId", referencedColumnName = "id")
  private SpecialtyGroup specialtyGroup;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "specialty", fetch = FetchType.LAZY)
  private List<Curriculum> curricula;

  @OneToMany(mappedBy = "specialty", cascade = {CascadeType.REMOVE,
      CascadeType.REFRESH}, orphanRemoval = true)
  private Set<PostSpecialty> posts = new HashSet<>();

  @OneToMany(mappedBy = "specialty", fetch = FetchType.LAZY)
  private Set<PlacementSpecialty> placementSpecialty;

  public Specialty status(Status status) {
    this.status = status;
    return this;
  }

  public Specialty college(String college) {
    this.college = college;
    return this;
  }

  public Specialty specialtyCode(String specialtyCode) {
    this.specialtyCode = specialtyCode;
    return this;
  }

  public Specialty specialtyTypes(Set<SpecialtyType> specialtyTypes) {
    this.specialtyTypes = specialtyTypes;
    return this;
  }

  public Specialty intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public Specialty specialtyGroup(SpecialtyGroup specialtyGroup) {
    this.specialtyGroup = specialtyGroup;
    return this;
  }

  public Specialty name(String name) {
    this.name = name;
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
    Specialty specialty = (Specialty) o;
    return Objects.equals(id, specialty.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
