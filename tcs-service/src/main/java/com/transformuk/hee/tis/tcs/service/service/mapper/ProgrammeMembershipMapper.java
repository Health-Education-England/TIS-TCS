package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper for the entity ProgrammeMembership and its DTO ProgrammeMembershipDTO.
 */
@Component
public class ProgrammeMembershipMapper {

  public ProgrammeMembershipDTO toDto(ProgrammeMembership programmeMembership) {
    ProgrammeMembershipDTO result = null;
    if (programmeMembership != null) {
      result = programmeMembershipToProgrammeMembershipDTO(programmeMembership);
      if (CollectionUtils.isEmpty(result.getCurriculumMemberships())) {
        result.setCurriculumMemberships(Lists.newArrayList());
      }
      result.getCurriculumMemberships().add(curriculumMembershipToCurriculumMembershipDTO(programmeMembership));
    }
    return result;
  }

  public List<ProgrammeMembershipDTO> programmeMembershipsToProgrammeMembershipDTOs(List<ProgrammeMembership> programmeMemberships) {
    Map<ProgrammeMembershipDTO, ProgrammeMembershipDTO> listMap = Maps.newHashMap();

    for (ProgrammeMembership programmeMembership : programmeMemberships) {
      ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipToProgrammeMembershipDTO(programmeMembership);
      if (listMap.containsKey(programmeMembershipDTO)) {
        programmeMembershipDTO = listMap.get(programmeMembershipDTO);
      }
      if (CollectionUtils.isEmpty(programmeMembershipDTO.getCurriculumMemberships())) {
        programmeMembershipDTO.setCurriculumMemberships(Lists.newArrayList());
      }
      programmeMembershipDTO.getCurriculumMemberships().add(curriculumMembershipToCurriculumMembershipDTO(programmeMembership));
      listMap.put(programmeMembershipDTO, programmeMembershipDTO);
    }

    return listMap.keySet().stream().collect(Collectors.toList());
  }

  public List<ProgrammeMembership> toEntity(ProgrammeMembershipDTO programmeMembershipDTO) {
    List<ProgrammeMembership> programmeMembershipList = Lists.newArrayList();
    for (CurriculumMembershipDTO curriculumMembershipDTO : programmeMembershipDTO.getCurriculumMemberships()) {
      ProgrammeMembership programmeMembership = programmeMembershipDTOToProgrammeMembership(programmeMembershipDTO);
      programmeMembership = curriculumMembershipDTOToCurriculumMembership(curriculumMembershipDTO, programmeMembership);
      programmeMembershipList.add(programmeMembership);
    }
    return programmeMembershipList;

  }

  public List<ProgrammeMembership> programmeMembershipDTOsToProgrammeMemberships(List<ProgrammeMembershipDTO> programmeMembershipDTOs) {
    List<ProgrammeMembership> result = Lists.newArrayList();

    for (ProgrammeMembershipDTO programmeMembershipDTO : programmeMembershipDTOs) {
      result.addAll(toEntity(programmeMembershipDTO));
    }

    return result;
  }

  private ProgrammeMembershipDTO programmeMembershipToProgrammeMembershipDTO(ProgrammeMembership programmeMembership) {
    ProgrammeMembershipDTO result = new ProgrammeMembershipDTO();

    result.setProgrammeMembershipType(programmeMembership.getProgrammeMembershipType());
    result.setRotation(programmeMembership.getRotation());
    result.setProgrammeStartDate(programmeMembership.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembership.getProgrammeEndDate());
    result.setLeavingDestination(programmeMembership.getLeavingDestination());
    result.setProgrammeId(programmeMembership.getProgrammeId());
    result.setTrainingNumberId(programmeMembership.getTrainingNumberId());

    if (programmeMembership.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personToPersonDTO(programmeMembership.getPerson()));
    }
    return result;
  }

  private CurriculumMembershipDTO curriculumMembershipToCurriculumMembershipDTO(ProgrammeMembership programmeMembership) {
    CurriculumMembershipDTO result = new CurriculumMembershipDTO();

    result.setId(programmeMembership.getId());
    result.setIntrepidId(programmeMembership.getIntrepidId());
    result.setCurriculumStartDate(programmeMembership.getCurriculumStartDate());
    result.setCurriculumEndDate(programmeMembership.getCurriculumEndDate());
    result.setPeriodOfGrace(programmeMembership.getPeriodOfGrace());
    result.setCurriculumCompletionDate(programmeMembership.getCurriculumCompletionDate());
    result.setCurriculumId(programmeMembership.getCurriculumId());
    result.setAmendedDate(programmeMembership.getAmendedDate());
    return result;
  }

  private ProgrammeMembership curriculumMembershipDTOToCurriculumMembership(CurriculumMembershipDTO curriculumMembershipDTO, ProgrammeMembership programmeMembership) {

    programmeMembership.setId(curriculumMembershipDTO.getId());
    programmeMembership.setIntrepidId(curriculumMembershipDTO.getIntrepidId());
    programmeMembership.setCurriculumStartDate(curriculumMembershipDTO.getCurriculumStartDate());
    programmeMembership.setCurriculumEndDate(curriculumMembershipDTO.getCurriculumEndDate());
    programmeMembership.setPeriodOfGrace(curriculumMembershipDTO.getPeriodOfGrace());
    programmeMembership.setCurriculumCompletionDate(curriculumMembershipDTO.getCurriculumCompletionDate());
    programmeMembership.setCurriculumId(curriculumMembershipDTO.getCurriculumId());
    programmeMembership.setAmendedDate(curriculumMembershipDTO.getAmendedDate());
    return programmeMembership;
  }

  private ProgrammeMembership programmeMembershipDTOToProgrammeMembership(ProgrammeMembershipDTO programmeMembershipDTO) {
    ProgrammeMembership result = new ProgrammeMembership();

    result.setProgrammeMembershipType(programmeMembershipDTO.getProgrammeMembershipType());
    result.setRotation(programmeMembershipDTO.getRotation());
    result.setProgrammeStartDate(programmeMembershipDTO.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembershipDTO.getProgrammeEndDate());
    result.setLeavingDestination(programmeMembershipDTO.getLeavingDestination());
    result.setProgrammeId(programmeMembershipDTO.getProgrammeId());
    result.setTrainingNumberId(programmeMembershipDTO.getTrainingNumberId());

    if (programmeMembershipDTO.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personDTOToPerson(programmeMembershipDTO.getPerson()));
    }
    return result;
  }

  private PersonDTO personToPersonDTO(Person person) {
    PersonDTO result = null;
    if (person != null) {
      result = new PersonDTO();
      result.setId(person.getId());
      result.setIntrepidId(person.getIntrepidId());
      result.setAddedDate(person.getAddedDate());
      result.setAmendedDate(person.getAmendedDate());
      result.setRole(person.getRole());
      result.setStatus(person.getStatus());
      result.setComments(person.getComments());
      result.setInactiveDate(person.getInactiveDate());
      result.setInactiveNotes(person.getInactiveNotes());
      result.setPublicHealthNumber(person.getPublicHealthNumber());
      result.setRegulator(person.getRegulator());
    }
    return result;
  }

  private Person personDTOToPerson(PersonDTO personDTO) {
    Person result = null;
    if (personDTO != null) {
      result = new Person();
      result.setId(personDTO.getId());
      result.setIntrepidId(personDTO.getIntrepidId());
      result.setAddedDate(personDTO.getAddedDate());
      result.setAmendedDate(personDTO.getAmendedDate());
      result.setRole(personDTO.getRole());
      result.setStatus(personDTO.getStatus());
      result.setComments(personDTO.getComments());
      result.setInactiveDate(personDTO.getInactiveDate());
      result.setInactiveNotes(personDTO.getInactiveNotes());
      result.setPublicHealthNumber(personDTO.getPublicHealthNumber());
      result.setRegulator(personDTO.getRegulator());

    }
    return result;
  }
}
