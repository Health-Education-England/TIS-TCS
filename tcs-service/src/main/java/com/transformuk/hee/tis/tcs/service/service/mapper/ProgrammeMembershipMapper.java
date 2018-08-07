package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.service.model.*;
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

    result.setId(programmeMembership.getId());
    result.setProgrammeMembershipType(programmeMembership.getProgrammeMembershipType());
    result.setProgrammeStartDate(programmeMembership.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembership.getProgrammeEndDate());
    result.setLeavingDestination(programmeMembership.getLeavingDestination());
    Programme programme = programmeMembership.getProgramme();
    if(programme != null){
      result.setProgrammeId(programme.getId());
      result.setProgrammeName(programme.getProgrammeName());
      result.setProgrammeNumber(programme.getProgrammeNumber());
      result.setRotation(rotationToRotationDTO(programmeMembership.getRotation(), programme));
    }
    result.setTrainingNumber(trainingNumberToTrainingNumberDTO(programmeMembership.getTrainingNumber()));

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

    result.setId(programmeMembershipDTO.getId());
    result.setProgrammeMembershipType(programmeMembershipDTO.getProgrammeMembershipType());
    result.setRotation(rotationDTOToRotation(programmeMembershipDTO.getRotation()));
    result.setProgrammeStartDate(programmeMembershipDTO.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembershipDTO.getProgrammeEndDate());
    result.setLeavingDestination(programmeMembershipDTO.getLeavingDestination());
    if(programmeMembershipDTO.getProgrammeId() != null){
      Programme programme = new Programme();
      programme.setId(programmeMembershipDTO.getProgrammeId());
      programme.setProgrammeName(programmeMembershipDTO.getProgrammeName());
      programme.setProgrammeNumber(programmeMembershipDTO.getProgrammeNumber());
      result.setProgramme(programme);
    }
    result.setTrainingNumber(trainingNumberDTOToTrainingNumber(programmeMembershipDTO.getTrainingNumber()));

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

  private TrainingNumberDTO trainingNumberToTrainingNumberDTO(TrainingNumber trainingNumber) {
    TrainingNumberDTO result = null;
    if (trainingNumber != null) {
      result = new TrainingNumberDTO();
      result.setId(trainingNumber.getId());
      result.setIntrepidId(trainingNumber.getIntrepidId());
      result.setTrainingNumber(trainingNumber.getTrainingNumber());
    }
    return result;
  }

  private TrainingNumber trainingNumberDTOToTrainingNumber(TrainingNumberDTO trainingNumberDTO) {
    TrainingNumber result = null;
    if (trainingNumberDTO != null) {
      result = new TrainingNumber();
      result.setId(trainingNumberDTO.getId());
      result.setIntrepidId(trainingNumberDTO.getIntrepidId());
      result.setTrainingNumber(trainingNumberDTO.getTrainingNumber());
    }
    return result;
  }

  private RotationDTO rotationToRotationDTO(Rotation rotation, Programme programme) {
    RotationDTO result = null;
    if (rotation != null) {
      result = new RotationDTO();
      result.setId(rotation.getId());
      result.setProgrammeId(rotation.getProgrammeId());
      result.setName(rotation.getName());
      result.setProgrammeName(programme.getProgrammeName());
      result.setProgrammeNumber(programme.getProgrammeNumber());
      result.setStatus(rotation.getStatus());
    }
    return result;
  }

  private Rotation rotationDTOToRotation(RotationDTO rotationDTO) {
    Rotation result = null;
    if (rotationDTO != null) {
      result = new Rotation();
      result.setId(rotationDTO.getId());
      result.setProgrammeId(rotationDTO.getProgrammeId());
      result.setName(rotationDTO.getName());
      result.setStatus(rotationDTO.getStatus());
    }
    return result;
  }
}
