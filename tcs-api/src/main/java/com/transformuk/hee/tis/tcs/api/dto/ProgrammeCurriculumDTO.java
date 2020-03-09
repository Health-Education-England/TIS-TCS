package com.transformuk.hee.tis.tcs.api.dto;

public class ProgrammeCurriculumDTO {

  private Long id;
  
  private ProgrammeDTO programme;

  private CurriculumDTO curriculum;

  private String gmcProgrammeCode;

  public ProgrammeCurriculumDTO() {}

  public ProgrammeCurriculumDTO(Long id,  ProgrammeDTO programme,  CurriculumDTO curriculum,
      String gmcProgrammeCode) {
    this.id = id;
    this.programme = programme;
    this.curriculum = curriculum;
    this.gmcProgrammeCode = gmcProgrammeCode;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProgrammeDTO getProgramme() {
    return programme;
  }

  public void setProgramme(ProgrammeDTO programme) {
    this.programme = programme;
  }

  public CurriculumDTO getCurriculum() {
    return curriculum;
  }

  public void setCurriculum(CurriculumDTO curriculum) {
    this.curriculum = curriculum;
  }

  public String getGmcProgrammeCode() {
    return gmcProgrammeCode;
  }

  public void setGmcProgrammeCode(String gmcProgrammeCode) {
    this.gmcProgrammeCode = gmcProgrammeCode;
  }



}
