package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeCurriculumDTO;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProgrammeMapper.class, CurriculumMapper.class})
public interface ProgrammeCurriculumMapper
    extends EntityMapper<ProgrammeCurriculumDTO, ProgrammeCurriculum> {

}
