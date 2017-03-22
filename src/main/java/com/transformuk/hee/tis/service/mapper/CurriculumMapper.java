package com.transformuk.hee.tis.service.mapper;

import com.transformuk.hee.tis.domain.Curriculum;
import com.transformuk.hee.tis.domain.Grade;
import com.transformuk.hee.tis.domain.Specialty;
import com.transformuk.hee.tis.service.dto.CurriculumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Curriculum and its DTO CurriculumDTO.
 */
@Mapper(componentModel = "spring", uses = {GradeMapper.class,})
public interface CurriculumMapper {

	@Mapping(source = "specialty.id", target = "specialtyId")
	CurriculumDTO curriculumToCurriculumDTO(Curriculum curriculum);

	List<CurriculumDTO> curriculaToCurriculumDTOs(List<Curriculum> curricula);

	@Mapping(target = "programmeMemberships", ignore = true)
	@Mapping(source = "specialtyId", target = "specialty")
	Curriculum curriculumDTOToCurriculum(CurriculumDTO curriculumDTO);

	List<Curriculum> curriculumDTOsToCurricula(List<CurriculumDTO> curriculumDTOs);

	default Grade gradeFromId(Long id) {
		if (id == null) {
			return null;
		}
		Grade grade = new Grade();
		grade.setId(id);
		return grade;
	}

	default Specialty specialtyFromId(Long id) {
		if (id == null) {
			return null;
		}
		Specialty specialty = new Specialty();
		specialty.setId(id);
		return specialty;
	}
}
