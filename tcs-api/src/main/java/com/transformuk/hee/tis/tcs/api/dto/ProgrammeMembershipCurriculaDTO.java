package com.transformuk.hee.tis.tcs.api.dto;

/**
 * A DTO that extends the ProgrammeMembership entity and adds curricula data.
 */
public class ProgrammeMembershipCurriculaDTO extends ProgrammeMembershipDTO {

  private CurriculumDTO curriculumDTO;

  public CurriculumDTO getCurriculumDTO() {
    return curriculumDTO;
  }

  public void setCurriculumDTO(CurriculumDTO curriculumDTO) {
    this.curriculumDTO = curriculumDTO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    ProgrammeMembershipCurriculaDTO that = (ProgrammeMembershipCurriculaDTO) o;

    return curriculumDTO != null ? curriculumDTO.equals(that.curriculumDTO) : that.curriculumDTO == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (curriculumDTO != null ? curriculumDTO.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ProgrammeMembershipCurriculaDTO{" +
        "curriculumDTO=" + curriculumDTO +
        '}';
  }
}
