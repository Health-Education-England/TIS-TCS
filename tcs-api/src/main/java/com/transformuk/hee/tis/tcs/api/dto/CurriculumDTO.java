package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Curriculum entity.
 */
public class CurriculumDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a curriculum")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new curriculum")
  private Long id;

  private String intrepidId;

  @NotNull(groups = {Create.class, Update.class}, message = "name must not be null")
  private String name;

  private CurriculumSubType curriculumSubType;

  @NotNull(groups = {Create.class, Update.class}, message = "assessmentType must not be null")
  private AssessmentType assessmentType;

  @NotNull(groups = {Create.class, Update.class}, message = "doesThisCurriculumLeadToCct must not be null")
  private Boolean doesThisCurriculumLeadToCct;

  @DecimalMin(value = "0", groups = {Create.class, Update.class}, message = "periodOfGrace must not be negative")
  @NotNull(groups = {Create.class, Update.class}, message = "periodOfGrace must not be null")
  private Integer periodOfGrace;

  @NotNull(groups = {Create.class, Update.class}, message = "specialty must not be null")
  private SpecialtyDTO specialty;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CurriculumSubType getCurriculumSubType() {
    return curriculumSubType;
  }

  public void setCurriculumSubType(CurriculumSubType curriculumSubType) {
    this.curriculumSubType = curriculumSubType;
  }

  public AssessmentType getAssessmentType() {
    return assessmentType;
  }

  public void setAssessmentType(AssessmentType assessmentType) {
    this.assessmentType = assessmentType;
  }

  public Boolean getDoesThisCurriculumLeadToCct() {
    return doesThisCurriculumLeadToCct;
  }

  public void setDoesThisCurriculumLeadToCct(Boolean doesThisCurriculumLeadToCct) {
    this.doesThisCurriculumLeadToCct = doesThisCurriculumLeadToCct;
  }

  public Integer getPeriodOfGrace() {
    return periodOfGrace;
  }

  public void setPeriodOfGrace(Integer periodOfGrace) {
    this.periodOfGrace = periodOfGrace;
  }

  public SpecialtyDTO getSpecialty() {
    return specialty;
  }

  public void setSpecialty(SpecialtyDTO specialty) {
    this.specialty = specialty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CurriculumDTO curriculumDTO = (CurriculumDTO) o;

    if (!Objects.equals(id, curriculumDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "CurriculumDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", name='" + name + '\'' +
        ", curriculumSubType=" + curriculumSubType +
        ", assessmentType=" + assessmentType +
        ", doesThisCurriculumLeadToCct=" + doesThisCurriculumLeadToCct +
        ", periodOfGrace=" + periodOfGrace +
        ", specialty=" + specialty +
        '}';
  }
}
