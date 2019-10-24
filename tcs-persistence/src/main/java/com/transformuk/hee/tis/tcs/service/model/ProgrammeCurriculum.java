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
  
  // TODO Generic obj methods
}
