package com.transformuk.hee.tis.service.mapper;

import com.transformuk.hee.tis.domain.*;
import com.transformuk.hee.tis.service.dto.GradeDTO;

import org.mapstruct.*;
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
