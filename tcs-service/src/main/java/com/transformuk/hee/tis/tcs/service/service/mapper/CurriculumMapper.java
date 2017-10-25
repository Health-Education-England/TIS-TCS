package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import org.mapstruct.Mapper;

import java.util.List;

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
