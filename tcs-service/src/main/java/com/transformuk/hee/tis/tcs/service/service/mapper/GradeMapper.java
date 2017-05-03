package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.service.model.Grade;
import com.transformuk.hee.tis.tcs.api.dto.GradeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Grade and its DTO GradeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GradeMapper {

	GradeDTO gradeToGradeDTO(Grade grade);

	List<GradeDTO> gradesToGradeDTOs(List<Grade> grades);

	@Mapping(target = "curriculumIds", ignore = true)
	Grade gradeDTOToGrade(GradeDTO gradeDTO);

	List<Grade> gradeDTOsToGrades(List<GradeDTO> gradeDTOs);
}
