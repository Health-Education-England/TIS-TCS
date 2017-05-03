package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity SpecialtyGroup and its DTO SpecialtyGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpecialtyGroupMapper {

	SpecialtyGroupDTO specialtyGroupToSpecialtyGroupDTO(SpecialtyGroup specialtyGroup);

	List<SpecialtyGroupDTO> specialtyGroupsToSpecialtyGroupDTOs(List<SpecialtyGroup> specialtyGroups);

	SpecialtyGroup specialtyGroupDTOToSpecialtyGroup(SpecialtyGroupDTO specialtyGroupDTO);

	List<SpecialtyGroup> specialtyGroupDTOsToSpecialtyGroups(List<SpecialtyGroupDTO> specialtyGroupDTOs);
}
