package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Mapper for the entity CurriculumMembership and its DTO ProgrammeMembershipDTO.
 */
@Component
public class CurriculumMembershipMapper {

  public ProgrammeMembershipDTO toDto(CurriculumMembership curriculumMembership) {
    ProgrammeMembershipDTO result = null;
    if (curriculumMembership != null) {
      result = curriculumMembershipToProgrammeMembershipDTO(curriculumMembership);
      if (CollectionUtils.isEmpty(result.getCurriculumMemberships())) {
        result.setCurriculumMemberships(Lists.newArrayList());
      }
      result.getCurriculumMemberships()
          .add(curriculumMembershipToCurriculumMembershipDTO(curriculumMembership));
    }
    return result;
  }

  public List<ProgrammeMembershipDTO> allEntityToDto(
      List<CurriculumMembership> curriculumMemberships) {
    List<ProgrammeMembershipDTO> result = Lists.newArrayList();

    for (CurriculumMembership curriculumMembership : curriculumMemberships) {
      ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipToProgrammeMembershipDTO(
          curriculumMembership);
      if (CollectionUtils.isEmpty(programmeMembershipDTO.getCurriculumMemberships())) {
        programmeMembershipDTO.setCurriculumMemberships(Lists.newArrayList());
      }
      programmeMembershipDTO.getCurriculumMemberships()
          .add(curriculumMembershipToCurriculumMembershipDTO(curriculumMembership));
      result.add(programmeMembershipDTO);
    }

    return result;
  }

  public List<ProgrammeMembershipDTO> curriculumMembershipsToProgrammeMembershipDTOs(
      List<CurriculumMembership> curriculumMemberships) {
    Map<ProgrammeMembershipDTO, ProgrammeMembershipDTO> listMap = Maps.newHashMap();

    for (CurriculumMembership curriculumMembership : curriculumMemberships) {
      ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipToProgrammeMembershipDTO(
          curriculumMembership);
      if (listMap.containsKey(programmeMembershipDTO)) {
        programmeMembershipDTO = listMap.get(programmeMembershipDTO);
      }
      if (CollectionUtils.isEmpty(programmeMembershipDTO.getCurriculumMemberships())) {
        programmeMembershipDTO.setCurriculumMemberships(Lists.newArrayList());
      }
      programmeMembershipDTO.getCurriculumMemberships()
          .add(curriculumMembershipToCurriculumMembershipDTO(curriculumMembership));
      listMap.put(programmeMembershipDTO, programmeMembershipDTO);
    }

    return listMap.keySet().stream().collect(Collectors.toList());
  }

  public List<CurriculumMembership> toEntity(ProgrammeMembershipDTO programmeMembershipDTO) {
    List<CurriculumMembership> curriculumMembershipList = Lists.newArrayList();
    for (CurriculumMembershipDTO curriculumMembershipDTO : programmeMembershipDTO
        .getCurriculumMemberships()) {
      CurriculumMembership curriculumMembership = programmeMembershipDTOToCurriculumMembership(
          programmeMembershipDTO);
      curriculumMembership = curriculumMembershipDTOToCurriculumMembership(curriculumMembershipDTO,
          curriculumMembership);
      curriculumMembershipList.add(curriculumMembership);
    }
    return curriculumMembershipList;
  }

  public List<CurriculumMembership> programmeMembershipDTOsToCurriculumMemberships(
      List<ProgrammeMembershipDTO> programmeMembershipDTOs) {
    List<CurriculumMembership> result = Lists.newArrayList();

    for (ProgrammeMembershipDTO programmeMembershipDTO : programmeMembershipDTOs) {
      result.addAll(toEntity(programmeMembershipDTO));
    }

    return result;
  }

  private ProgrammeMembershipDTO curriculumMembershipToProgrammeMembershipDTO(
      CurriculumMembership curriculumMembership) {
    ProgrammeMembershipDTO result = new ProgrammeMembershipDTO();

    result.setId(curriculumMembership.getId());
    result.setProgrammeMembershipType(curriculumMembership.getProgrammeMembershipType());
    result.setProgrammeStartDate(curriculumMembership.getProgrammeStartDate());
    result.setProgrammeEndDate(curriculumMembership.getProgrammeEndDate());
    result.setLeavingDestination(curriculumMembership.getLeavingDestination());
    result.setLeavingReason(curriculumMembership.getLeavingReason());
    Programme programme = curriculumMembership.getProgramme();
    if (programme != null) {
      result.setProgrammeId(programme.getId());
      result.setProgrammeOwner(programme.getOwner());
      result.setProgrammeName(programme.getProgrammeName());
      result.setProgrammeNumber(programme.getProgrammeNumber());
      result.setRotation(rotationToRotationDTO(curriculumMembership.getRotation(), programme));
    }
    result.setTrainingNumber(
        trainingNumberToTrainingNumberDTO(curriculumMembership.getTrainingNumber()));

    if (curriculumMembership.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personToPersonDTO(curriculumMembership.getPerson()));
    }
    result.setTrainingPathway(curriculumMembership.getTrainingPathway());
    return result;
  }

  private CurriculumMembershipDTO curriculumMembershipToCurriculumMembershipDTO(
      CurriculumMembership curriculumMembership) {
    CurriculumMembershipDTO result = new CurriculumMembershipDTO();

    //Note: this is done as the ProgrammeMembershipDTO didn't have an id but it was needed so that the FE can send delete by id requests
    result.setId(curriculumMembership.getId());
    result.setIntrepidId(curriculumMembership.getIntrepidId());
    result.setCurriculumStartDate(curriculumMembership.getCurriculumStartDate());
    result.setCurriculumEndDate(curriculumMembership.getCurriculumEndDate());
    result.setPeriodOfGrace(curriculumMembership.getPeriodOfGrace());
    result.setCurriculumCompletionDate(curriculumMembership.getCurriculumCompletionDate());
    result.setCurriculumId(curriculumMembership.getCurriculumId());
    result.setAmendedDate(curriculumMembership.getAmendedDate());
    return result;
  }

  private CurriculumMembership curriculumMembershipDTOToCurriculumMembership(
      CurriculumMembershipDTO curriculumMembershipDTO, CurriculumMembership curriculumMembership) {

    curriculumMembership.setId(curriculumMembershipDTO.getId());
    curriculumMembership.setIntrepidId(curriculumMembershipDTO.getIntrepidId());
    curriculumMembership.setCurriculumStartDate(curriculumMembershipDTO.getCurriculumStartDate());
    curriculumMembership.setCurriculumEndDate(curriculumMembershipDTO.getCurriculumEndDate());
    curriculumMembership.setPeriodOfGrace(curriculumMembershipDTO.getPeriodOfGrace());
    curriculumMembership
        .setCurriculumCompletionDate(curriculumMembershipDTO.getCurriculumCompletionDate());
    curriculumMembership.setCurriculumId(curriculumMembershipDTO.getCurriculumId());
    curriculumMembership.setAmendedDate(curriculumMembershipDTO.getAmendedDate());
    return curriculumMembership;
  }

  private CurriculumMembership programmeMembershipDTOToCurriculumMembership(
      ProgrammeMembershipDTO programmeMembershipDTO) {
    CurriculumMembership result = new CurriculumMembership();

    result.setId(programmeMembershipDTO.getId());
    result.setProgrammeMembershipType(programmeMembershipDTO.getProgrammeMembershipType());
    result.setRotation(rotationDTOToRotation(programmeMembershipDTO.getRotation()));
    result.setProgrammeStartDate(programmeMembershipDTO.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembershipDTO.getProgrammeEndDate());
    result.setLeavingDestination(programmeMembershipDTO.getLeavingDestination());
    result.setLeavingReason(programmeMembershipDTO.getLeavingReason());
    result.setTrainingPathway(programmeMembershipDTO.getTrainingPathway());
    if (programmeMembershipDTO.getProgrammeId() != null) {
      Programme programme = new Programme();
      programme.setId(programmeMembershipDTO.getProgrammeId());
      programme.setProgrammeName(programmeMembershipDTO.getProgrammeName());
      programme.setProgrammeNumber(programmeMembershipDTO.getProgrammeNumber());
      result.setProgramme(programme);
    }
    result.setTrainingNumber(
        trainingNumberDTOToTrainingNumber(programmeMembershipDTO.getTrainingNumber()));

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
