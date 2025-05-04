package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyTypeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity SpecialtyGroup and its DTO SpecialtyGroupDTO. Created this mapper as
 * mapstruct was having problems mapping between entities and DTOs Problem was the circular link
 * between SpecialtyGroup to Specialty to SpecialtyGroup etc
 */

@Component
public class SpecialtyGroupMapper {

  public SpecialtyGroupDTO specialtyGroupToSpecialtyGroupDTO(SpecialtyGroup specialtyGroup) {
    SpecialtyGroupDTO result = null;

    if (specialtyGroup != null) {
      result = new SpecialtyGroupDTO();
      result.setId(specialtyGroup.getId());
      result.setUuid(specialtyGroup.getUuid());
      result.setIntrepidId(specialtyGroup.getIntrepidId());
      result.setName(specialtyGroup.getName());

      if (CollectionUtils.isNotEmpty(specialtyGroup.getSpecialties())) {
        Set<SpecialtyDTO> specialtyDTOs = new HashSet<>();

        for (Specialty specialtyGroupSpecialty : specialtyGroup.getSpecialties()) {
          SpecialtyDTO specialtyDTO = specialtyToSpecialtyDTO(specialtyGroupSpecialty);
          specialtyDTOs.add(specialtyDTO);
        }
        result.setSpecialties(specialtyDTOs);
      }
    }
    return result;
  }

  public List<SpecialtyGroupDTO> specialtyGroupsToSpecialtyGroupDTOs(
      List<SpecialtyGroup> specialtyGroupList) {
    List<SpecialtyGroupDTO> result = new ArrayList<>();
    for (SpecialtyGroup specialtyGroup : specialtyGroupList) {
      result.add(specialtyGroupToSpecialtyGroupDTO(specialtyGroup));
    }
    return result;
  }

  public SpecialtyGroup specialtyGroupDTOToSpecialtyGroup(SpecialtyGroupDTO specialtyGroupDTO) {
    SpecialtyGroup result = null;

    if (specialtyGroupDTO != null) {
      result = new SpecialtyGroup();
      result.setId(specialtyGroupDTO.getId());
      result.setUuid(specialtyGroupDTO.getUuid());
      result.setName(specialtyGroupDTO.getName());
      result.setIntrepidId(specialtyGroupDTO.getIntrepidId());

      if (CollectionUtils.isNotEmpty(specialtyGroupDTO.getSpecialties())) {
        Set<Specialty> specialties = new HashSet<>();

        for (SpecialtyDTO specialtyDTO : specialtyGroupDTO.getSpecialties()) {
          Specialty specialty = specialtyDTOToSpecialty(specialtyDTO);
          specialties.add(specialty);
        }
        result.setSpecialties(specialties);
      }
    }
    return result;
  }

  public List<SpecialtyGroup> specialtyGroupDTOsToSpecialtyGroups(
      List<SpecialtyGroupDTO> specialtyGroupDTOList) {
    List<SpecialtyGroup> result = new ArrayList<>();
    for (SpecialtyGroupDTO specialtyGroupDTO : specialtyGroupDTOList) {
      result.add(specialtyGroupDTOToSpecialtyGroup(specialtyGroupDTO));
    }
    return result;
  }

  private Specialty specialtyDTOToSpecialty(SpecialtyDTO specialtyDTO) {
    Specialty result = null;
    if (specialtyDTO != null) {
      result = new Specialty();
      result.setId(specialtyDTO.getId());
      result.setUuid(specialtyDTO.getUuid());
      result.setName(specialtyDTO.getName());
      if (CollectionUtils.isNotEmpty(specialtyDTO.getSpecialtyTypes())) {
        Set<SpecialtyType> types = specialtyDTO.getSpecialtyTypes().stream()
            .filter(Objects::nonNull)
            .map(dto -> SpecialtyType.valueOf(dto.getName()))
            .collect(Collectors.toSet());
        result.setSpecialtyTypes(types);
      }
      result.setSpecialtyCode(specialtyDTO.getSpecialtyCode());
      result.setStatus(specialtyDTO.getStatus());
      result.setIntrepidId(specialtyDTO.getIntrepidId());
      result.setCollege(specialtyDTO.getCollege());
    }
    return result;
  }

  private SpecialtyDTO specialtyToSpecialtyDTO(Specialty specialty) {
    SpecialtyDTO result = null;
    if (specialty != null) {
      result = new SpecialtyDTO();
      result.setId(specialty.getId());
      result.setUuid(specialty.getUuid());
      result.setName(specialty.getName());

      if (CollectionUtils.isNotEmpty(specialty.getSpecialtyTypes())) {
        Set<SpecialtyTypeDTO> typeDTOs = specialty.getSpecialtyTypes().stream()
            .map(type -> {
              SpecialtyTypeDTO dto = new SpecialtyTypeDTO();
              dto.setName(type.getName());
              return dto;
            }).collect(Collectors.toSet());
        result.setSpecialtyTypes(typeDTOs);
      }
      result.setSpecialtyCode(specialty.getSpecialtyCode());
      result.setStatus(specialty.getStatus());
      result.setIntrepidId(specialty.getIntrepidId());
      result.setCollege(specialty.getCollege());
    }
    return result;
  }
}
