package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for the entity ProgrammeMembership and its DTO ProgrammeMembershipDTO.
 */
@Component
public class ProgrammeMembershipMapper {

  public ProgrammeMembershipDTO toDto(ProgrammeMembership programmeMembership) {
    ProgrammeMembershipDTO result = null;
    if (programmeMembership != null) {
      result = programmeMembershipToProgrammeMembershipDTO(programmeMembership);
    }
    return result;
  }

  public List<ProgrammeMembershipDTO> programmeMembershipsToProgrammeMembershipDTOs(List<ProgrammeMembership> programmeMemberships) {
    List<ProgrammeMembershipDTO> result = Lists.newArrayList();

    for (ProgrammeMembership programmeMembership : programmeMemberships) {
      result.add(programmeMembershipToProgrammeMembershipDTO(programmeMembership));
    }

    return result;
  }

  public ProgrammeMembership toEntity(ProgrammeMembershipDTO programmeMembershipDTO) {
    return programmeMembershipDTOToProgrammeMembership(programmeMembershipDTO);

  }

  public List<ProgrammeMembership> programmeMembershipDTOsToProgrammeMemberships(List<ProgrammeMembershipDTO> programmeMembershipDTOs) {
    List<ProgrammeMembership> result = Lists.newArrayList();

    for (ProgrammeMembershipDTO programmeMembershipDTO : programmeMembershipDTOs) {
      result.add(programmeMembershipDTOToProgrammeMembership(programmeMembershipDTO));
    }

    return result;
  }

  private ProgrammeMembershipDTO programmeMembershipToProgrammeMembershipDTO(ProgrammeMembership programmeMembership) {
    ProgrammeMembershipDTO result = new ProgrammeMembershipDTO();

    result.setId(programmeMembership.getId());
    result.setIntrepidId(programmeMembership.getIntrepidId());
    result.setProgrammeMembershipType(programmeMembership.getProgrammeMembershipType());
    result.setRotation(programmeMembership.getRotation());
    result.setCurriculumStartDate(programmeMembership.getCurriculumStartDate());
    result.setCurriculumEndDate(programmeMembership.getCurriculumEndDate());
    result.setPeriodOfGrace(programmeMembership.getPeriodOfGrace());
    result.setProgrammeStartDate(programmeMembership.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembership.getProgrammeEndDate());
    result.setCurriculumCompletionDate(programmeMembership.getCurriculumCompletionDate());
    result.setLeavingDestination(programmeMembership.getLeavingDestination());
    result.setProgrammeId(programmeMembership.getProgrammeId());
    result.setCurriculumId(programmeMembership.getCurriculumId());

    if (programmeMembership.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personToPersonDTO(programmeMembership.getPerson()));
    }
    return result;
  }

  private ProgrammeMembership programmeMembershipDTOToProgrammeMembership(ProgrammeMembershipDTO programmeMembershipDTO) {
    ProgrammeMembership result = new ProgrammeMembership();
    result.setId(programmeMembershipDTO.getId());
    result.setIntrepidId(programmeMembershipDTO.getIntrepidId());
    result.setProgrammeMembershipType(programmeMembershipDTO.getProgrammeMembershipType());
    result.setRotation(programmeMembershipDTO.getRotation());
    result.setCurriculumStartDate(programmeMembershipDTO.getCurriculumStartDate());
    result.setCurriculumEndDate(programmeMembershipDTO.getCurriculumEndDate());
    result.setPeriodOfGrace(programmeMembershipDTO.getPeriodOfGrace());
    result.setProgrammeStartDate(programmeMembershipDTO.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembershipDTO.getProgrammeEndDate());
    result.setCurriculumCompletionDate(programmeMembershipDTO.getCurriculumCompletionDate());
    result.setLeavingDestination(programmeMembershipDTO.getLeavingDestination());
    result.setProgrammeId(programmeMembershipDTO.getProgrammeId());
    result.setCurriculumId(programmeMembershipDTO.getCurriculumId());

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
