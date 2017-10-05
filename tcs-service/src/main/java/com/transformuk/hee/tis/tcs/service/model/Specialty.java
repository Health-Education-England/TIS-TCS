package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Specialty.
 */
@Entity
public class Specialty implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String intrepidId;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "college")
  private String college;

  @Column(name = "nhsSpecialtyCode")
  private String nhsSpecialtyCode;

  @ElementCollection(targetClass = SpecialtyType.class)
  @CollectionTable(name = "SpecialtyTypes", joinColumns = {@JoinColumn(name = "specialtyId")})
  @Column(name = "specialtyType")
  @Enumerated(EnumType.STRING)
  private Set<SpecialtyType> specialtyTypes = new HashSet<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "specialtyGroupId", referencedColumnName = "id")
  private SpecialtyGroup specialtyGroup;

  @Column(name = "name")
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Specialty status(Status status) {
    this.status = status;
    return this;
  }

  public String getCollege() {
    return college;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public Specialty college(String college) {
    this.college = college;
    return this;
  }

  public String getNhsSpecialtyCode() {
    return nhsSpecialtyCode;
  }

  public void setNhsSpecialtyCode(String nhsSpecialtyCode) {
    this.nhsSpecialtyCode = nhsSpecialtyCode;
  }

  public Specialty nhsSpecialtyCode(String nhsSpecialtyCode) {
    this.nhsSpecialtyCode = nhsSpecialtyCode;
    return this;
  }

  public Set<SpecialtyType> getSpecialtyTypes() {
    return specialtyTypes;
  }

  public void setSpecialtyTypes(Set<SpecialtyType> specialtyTypes) {
    this.specialtyTypes = specialtyTypes;
  }

  public Specialty specialtyTypes(Set<SpecialtyType> specialtyTypes) {
    this.specialtyTypes = specialtyTypes;
    return this;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Specialty intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public SpecialtyGroup getSpecialtyGroup() {
    return specialtyGroup;
  }

  public void setSpecialtyGroup(SpecialtyGroup specialtyGroup) {
    this.specialtyGroup = specialtyGroup;
  }

  public Specialty specialtyGroup(SpecialtyGroup specialtyGroup) {
    this.specialtyGroup = specialtyGroup;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
    if (specialty.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, specialty.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Specialty{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", status=" + status +
        ", college='" + college + '\'' +
        ", nhsSpecialtyCode='" + nhsSpecialtyCode + '\'' +
        ", specialtyTypes=" + specialtyTypes +
        ", specialtyGroup=" + specialtyGroup +
        ", name=" + name +
        '}';
  }
}
