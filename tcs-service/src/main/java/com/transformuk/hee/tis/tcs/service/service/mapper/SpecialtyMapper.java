package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Specialty and its DTO SpecialtyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public class SpecialtyMapper {

  public SpecialtyDTO specialtyToSpecialtyDTO(Specialty specialty) {
    SpecialtyDTO result = null;

    if (specialty != null) {
      result = new SpecialtyDTO();
      result.setId(specialty.getId());
      result.setUuid(specialty.getUuid());
      result.setName(specialty.getName());
      result.setCollege(specialty.getCollege());
      result.setIntrepidId(specialty.getIntrepidId());
      result.setSpecialtyCode(specialty.getSpecialtyCode());
      result.setSpecialtyTypes(specialty.getSpecialtyTypes());
      result.setStatus(specialty.getStatus());
      result.setSpecialtyGroup(specialtyGroupToSpecialtyGroupDTO(specialty.getSpecialtyGroup()));
      result.setBlockIndemnity(specialty.isBlockIndemnity());
    }
    return result;
  }

  public List<SpecialtyDTO> specialtiesToSpecialtyDTOs(List<Specialty> specialties) {
    List<SpecialtyDTO> result = new ArrayList<>();
    for (Specialty specialty : specialties) {
      result.add(specialtyToSpecialtyDTO(specialty));
    }
    return result;
  }

  public Specialty specialtyDTOToSpecialty(SpecialtyDTO specialtyDTO) {
    Specialty result = null;

    if (specialtyDTO != null) {
      result = new Specialty();
      result.setId(specialtyDTO.getId());
      result.setUuid(specialtyDTO.getUuid());
      result.setName(specialtyDTO.getName());
      result.setCollege(specialtyDTO.getCollege());
      result.setIntrepidId(specialtyDTO.getIntrepidId());
      result.setSpecialtyCode(specialtyDTO.getSpecialtyCode());
      result.setSpecialtyTypes(specialtyDTO.getSpecialtyTypes());
      result.setStatus(specialtyDTO.getStatus());
      result.setSpecialtyGroup(specialtyGroupDTOToSpecialtyGroup(specialtyDTO.getSpecialtyGroup()));
      result.setBlockIndemnity(specialtyDTO.isBlockIndemnity());
    }
    return result;
  }

  public List<Specialty> specialtyDTOsToSpecialties(List<SpecialtyDTO> specialtyDTOs) {
    List<Specialty> result = new ArrayList<>();
    for (SpecialtyDTO specialtyDTO : specialtyDTOs) {
      result.add(specialtyDTOToSpecialty(specialtyDTO));
    }

    return result;
  }

  private SpecialtyGroupDTO specialtyGroupToSpecialtyGroupDTO(SpecialtyGroup specialtyGroup) {
    SpecialtyGroupDTO result = null;
    if (specialtyGroup != null) {
      result = new SpecialtyGroupDTO();
      result.setId(specialtyGroup.getId());
      result.setUuid(specialtyGroup.getUuid());
      result.setName(specialtyGroup.getName());
      result.setIntrepidId(specialtyGroup.getIntrepidId());
    }
    return result;
  }

  private SpecialtyGroup specialtyGroupDTOToSpecialtyGroup(SpecialtyGroupDTO specialtyGroupDTO) {
    SpecialtyGroup result = null;
    if (specialtyGroupDTO != null) {
      result = new SpecialtyGroup();
      result.setId(specialtyGroupDTO.getId());
      result.setUuid(specialtyGroupDTO.getUuid());
      result.setName(specialtyGroupDTO.getName());
      result.setIntrepidId(specialtyGroupDTO.getIntrepidId());
    }
    return result;
  }
}
