package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeCurriculumDTO;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {ProgrammeMapper.class, CurriculumMapper.class})
public abstract class ProgrammeCurriculumMapper
    implements EntityMapper<ProgrammeCurriculumDTO, ProgrammeCurriculum> {

  @Autowired
  CurriculumMapper curriculumMapper;

  public ProgrammeCurriculumDTO toDto(ProgrammeCurriculum entity) {
    ProgrammeCurriculumDTO dto = new ProgrammeCurriculumDTO(entity.getId(), null,
        curriculumMapper.curriculumToCurriculumDTO(entity.getCurriculum()),
        entity.getGmcProgrammeCode());
    return dto;
  }

}
