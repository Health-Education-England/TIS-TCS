package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Curriculum and its DTO CurriculumDTO.
 */
@Mapper(componentModel = "spring", uses = {SpecialtyMapper.class})
public interface CurriculumMapper {

  CurriculumDTO curriculumToCurriculumDTO(Curriculum curriculum);

  List<CurriculumDTO> curriculaToCurriculumDTOs(List<Curriculum> curricula);

  Curriculum curriculumDTOToCurriculum(CurriculumDTO curriculumDTO);

  List<Curriculum> curriculumDTOsToCurricula(List<CurriculumDTO> curriculumDTOs);

}
