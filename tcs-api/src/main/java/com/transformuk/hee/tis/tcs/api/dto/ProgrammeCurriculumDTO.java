package com.transformuk.hee.tis.tcs.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammeCurriculumDTO {

  private Long id;

  private ProgrammeDTO programme;

  private CurriculumDTO curriculum;

  private String gmcProgrammeCode;
}
