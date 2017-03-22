package com.transformuk.hee.tis.service.mapper;

import com.transformuk.hee.tis.domain.Specialty;
import com.transformuk.hee.tis.domain.SpecialtyGroup;
import com.transformuk.hee.tis.service.dto.SpecialtyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Specialty and its DTO SpecialtyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpecialtyMapper {

	@Mapping(source = "specialtyGroup.id", target = "specialtyGroupId")
	SpecialtyDTO specialtyToSpecialtyDTO(Specialty specialty);

	List<SpecialtyDTO> specialtiesToSpecialtyDTOs(List<Specialty> specialties);

	@Mapping(target = "curricula", ignore = true)
	@Mapping(source = "specialtyGroupId", target = "specialtyGroup")
	Specialty specialtyDTOToSpecialty(SpecialtyDTO specialtyDTO);

	List<Specialty> specialtyDTOsToSpecialties(List<SpecialtyDTO> specialtyDTOs);

	default SpecialtyGroup specialtyGroupFromId(Long id) {
		if (id == null) {
			return null;
		}
		SpecialtyGroup specialtyGroup = new SpecialtyGroup();
		specialtyGroup.setId(id);
		return specialtyGroup;
	}
}
