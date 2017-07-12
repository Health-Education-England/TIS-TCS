package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Specialty and its DTO SpecialtyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpecialtyMapper {

    SpecialtyDTO specialtyToSpecialtyDTO(Specialty specialty);

	List<SpecialtyDTO> specialtiesToSpecialtyDTOs(List<Specialty> specialties);

	Specialty specialtyDTOToSpecialty(SpecialtyDTO specialtyDTO);

	List<Specialty> specialtyDTOsToSpecialties(List<SpecialtyDTO> specialtyDTOs);

	CurriculumDTO map(Curriculum curriculum);

	Curriculum map(CurriculumDTO curriculumDTO);
}
