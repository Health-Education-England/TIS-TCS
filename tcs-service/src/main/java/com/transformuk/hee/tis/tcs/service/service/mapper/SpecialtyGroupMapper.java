package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity SpecialtyGroup and its DTO SpecialtyGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpecialtyGroupMapper {

  SpecialtyGroupDTO specialtyGroupToSpecialtyGroupDTO(SpecialtyGroup specialtyGroup);

  List<SpecialtyGroupDTO> specialtyGroupsToSpecialtyGroupDTOs(List<SpecialtyGroup> specialtyGroups);

  SpecialtyGroup specialtyGroupDTOToSpecialtyGroup(SpecialtyGroupDTO specialtyGroupDTO);

  List<SpecialtyGroup> specialtyGroupDTOsToSpecialtyGroups(List<SpecialtyGroupDTO> specialtyGroupDTOs);

  Set<SpecialtyDTO> map(Set<Specialty> specialties);

  SpecialtyDTO map(Specialty specialty);

  Specialty specialtyDtoToSpecialty(SpecialtyDTO specialtyDTO);

  Set<Specialty> specialtyDtostoSpecialty(Set<Specialty> specialtyDTOs);

}