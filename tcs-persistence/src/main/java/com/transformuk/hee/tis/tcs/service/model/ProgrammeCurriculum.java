package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProgrammeCurriculum implements Serializable {

  public ProgrammeCurriculum() {}

  public ProgrammeCurriculum(Programme programme, Curriculum curriculum, String gmcProgrammeCode) {
    this.programme = programme;
    this.curriculum = curriculum;
    this.gmcProgrammeCode = gmcProgrammeCode;
  }

  private static final long serialVersionUID = -831481163179219626L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(targetEntity = Programme.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "programmeId")
  private Programme programme;

  @ManyToOne(targetEntity = Curriculum.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "curriculumId")
  private Curriculum curriculum;

  private String gmcProgrammeCode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Programme getProgramme() {
    return programme;
  }

  public void setProgramme(Programme programme) {
    this.programme = programme;
  }

  public Curriculum getCurriculum() {
    return curriculum;
  }

  public void setCurriculum(Curriculum curriculum) {
    this.curriculum = curriculum;
  }

  public String getGmcProgrammeCode() {
    return gmcProgrammeCode;
  }

  public void setGmcProgrammeCode(String gmcProgrammeCode) {
    this.gmcProgrammeCode = gmcProgrammeCode;
  }
  
  public ProgrammeCurriculum id(Long id) {
    setId(id);
    return this;
  }

  public ProgrammeCurriculum programme(Programme programme) {
    setProgramme(programme);
    return this;
  }

  public ProgrammeCurriculum curriculum(Curriculum curriculum) {
    setCurriculum(curriculum);
    return this;
  }

  public ProgrammeCurriculum gmcProgrammeCode(String gmcProgrammeCode) {
    setGmcProgrammeCode(gmcProgrammeCode);
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final ProgrammeCurriculum pc = (ProgrammeCurriculum) o;

    if (id != null ? !id.equals(pc.id) : pc.id != null) {
      return false;
    }

    return id.equals(pc.id) && programme.equals(pc.programme) && curriculum.equals(pc.curriculum)
        && gmcProgrammeCode.equals(pc.gmcProgrammeCode);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (programme != null ? programme.hashCode() : 0);
    result = 31 * result + (curriculum != null ? curriculum.hashCode() : 0);
    result = 31 * result + (gmcProgrammeCode != null ? gmcProgrammeCode.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Programme{" +
        "id=" + id +
        ", programme=" + programme +
        ", curriculum='" + curriculum + "'" +
        ", gmcProgrammeCode='" + gmcProgrammeCode + "'" +
        '}';
  }
}
