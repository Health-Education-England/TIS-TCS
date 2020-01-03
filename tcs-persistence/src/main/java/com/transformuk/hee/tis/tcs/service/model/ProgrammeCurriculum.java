package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ProgrammeCurriculum implements Serializable {

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

  public ProgrammeCurriculum(Programme programme, Curriculum curriculum, String gmcProgrammeCode) {
    this.programme = programme;
    this.curriculum = curriculum;
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
}
