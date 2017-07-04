package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Curriculum and its DTO CurriculumDTO.
 */
@Mapper(componentModel = "spring")
public interface CurriculumMapper {

    CurriculumDTO curriculumToCurriculumDTO(Curriculum curriculum);

	List<CurriculumDTO> curriculaToCurriculumDTOs(List<Curriculum> curricula);

	Curriculum curriculumDTOToCurriculum(CurriculumDTO curriculumDTO);

	List<Curriculum> curriculumDTOsToCurricula(List<CurriculumDTO> curriculumDTOs);

	default Specialty specialtyFromId(Long id) {
		if (id == null) {
			return null;
		}
		Specialty specialty = new Specialty();
		specialty.setId(id);
		return specialty;
	}
}
