package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Mapper for the entity CurriculumMembership and its DTO ProgrammeMembershipDTO.
 */
@Component
public class CurriculumMembershipMapper {

  ConditionsOfJoiningMapper conditionsOfJoiningMapper;

  /**
   * Initialise the CurriculumMembership mapper.
   *
   * @param conditionsOfJoiningMapper the Conditions of Joining mapper
   */
  public CurriculumMembershipMapper(ConditionsOfJoiningMapper conditionsOfJoiningMapper) {
    this.conditionsOfJoiningMapper = conditionsOfJoiningMapper;
  }

  /**
   * Convert a CurriculumMembership to a ProgrammeMembershipDTO, including its
   * curriculumMemberships.
   *
   * @param curriculumMembership the CurriculumMembership object to convert
   * @return a ProgrammeMembershipDTO object
   */
  public ProgrammeMembershipDTO toDto(CurriculumMembership curriculumMembership) {
    ProgrammeMembershipDTO result = null;
    if (curriculumMembership != null) {
      result = curriculumMembershipToProgrammeMembershipDto(curriculumMembership);
      if (CollectionUtils.isEmpty(result.getCurriculumMemberships())) {
        result.setCurriculumMemberships(Lists.newArrayList());
      }
      result.getCurriculumMemberships()
          .add(curriculumMembershipToCurriculumMembershipDto(curriculumMembership));
    }
    return result;
  }

  /**
   * Convert a list of CurriculumMemberships to a list of ProgrammeMembershipDTOs.
   *
   * @param curriculumMemberships the list of CurriculumMembership objects to convert
   * @return a list of ProgrammeMembershipDTO objects
   */
  public List<ProgrammeMembershipDTO> allEntityToDto(
      List<CurriculumMembership> curriculumMemberships) {
    List<ProgrammeMembershipDTO> result = Lists.newArrayList();

    for (CurriculumMembership curriculumMembership : curriculumMemberships) {
      ProgrammeMembershipDTO programmeMembershipDto = toDto(curriculumMembership);
      result.add(programmeMembershipDto);
    }

    return result;
  }

  /**
   * Convert a list of CurriculumMemberships to a list of distinct ProgrammeMembershipDTOs.
   *
   * @param curriculumMemberships the list of CurriculumMembership objects to convert
   * @return a list of distinct ProgrammeMembershipDTO objects
   */
  public List<ProgrammeMembershipDTO> curriculumMembershipsToProgrammeMembershipDtos(
      List<CurriculumMembership> curriculumMemberships) {
    Map<UUID, ProgrammeMembershipDTO> listMap = Maps.newHashMap();

    for (CurriculumMembership curriculumMembership : curriculumMemberships) {
      ProgrammeMembershipDTO programmeMembershipDto = curriculumMembershipToProgrammeMembershipDto(
          curriculumMembership);
      if (listMap.containsKey(programmeMembershipDto.getUuid())) {
        programmeMembershipDto = listMap.get(programmeMembershipDto.getUuid());
      }
      if (CollectionUtils.isEmpty(programmeMembershipDto.getCurriculumMemberships())) {
        programmeMembershipDto.setCurriculumMemberships(Lists.newArrayList());
      }
      programmeMembershipDto.getCurriculumMemberships()
          .add(curriculumMembershipToCurriculumMembershipDto(curriculumMembership));

      listMap.put(programmeMembershipDto.getUuid(), programmeMembershipDto);
    }

    return listMap.values().stream().collect(Collectors.toList());
  }

  /**
   * Convert a ProgrammeMembershipDTO to a CurriculumMembership object, enriched with all curriculum
   * membership details from the ProgrammeMembershipDTO.
   *
   * @param programmeMembershipDto the ProgrammeMembershipDTO object to convert
   * @return a CurriculumMembership object
   */
  public List<CurriculumMembership> toEntity(ProgrammeMembershipDTO programmeMembershipDto) {
    List<CurriculumMembership> curriculumMembershipList = Lists.newArrayList();
    for (CurriculumMembershipDTO curriculumMembershipDto :
        programmeMembershipDto.getCurriculumMemberships()) {
      CurriculumMembership curriculumMembership = new CurriculumMembership();
      curriculumMembership = curriculumMembershipDtoToCurriculumMembership(curriculumMembershipDto,
          curriculumMembership);
      curriculumMembershipList.add(curriculumMembership);
    }
    return curriculumMembershipList;
  }

  /**
   * Convert a CurriculumMembershipDTO to a CurriculumMembership entity.
   *
   * @param cmDto the CurriculumMembershipDTO to convert
   * @return the converted CurriculumMembership entity
   */
  public CurriculumMembership toEntity(CurriculumMembershipDTO cmDto) {
    if (cmDto == null) {
      return null;
    }
    CurriculumMembership cm = new CurriculumMembership();
    cm.setId(cmDto.getId());
    cm.setIntrepidId(cmDto.getIntrepidId());
    cm.setCurriculumStartDate(cmDto.getCurriculumStartDate());
    cm.setCurriculumEndDate(cmDto.getCurriculumEndDate());
    cm.setPeriodOfGrace(cmDto.getPeriodOfGrace());
    cm.setCurriculumCompletionDate(cmDto.getCurriculumCompletionDate());
    cm.setCurriculumId(cmDto.getCurriculumId());
    cm.setAmendedDate(cmDto.getAmendedDate());
    return cm;
  }

  /**
   * Convert a CurriculumMembership to a ProgrammeMembershipDTO, without its curriculumMemberships.
   *
   * @param curriculumMembership the CurriculumMembership object to convert
   * @return a ProgrammeMembershipDTO object
   */
  private ProgrammeMembershipDTO curriculumMembershipToProgrammeMembershipDto(
      CurriculumMembership curriculumMembership) {
    ProgrammeMembershipDTO result = new ProgrammeMembershipDTO();
    ProgrammeMembership programmeMembership = curriculumMembership.getProgrammeMembership();

    result.setId(curriculumMembership.getId()); //use CM ID
    result.setUuid(programmeMembership.getUuid());
    result.setProgrammeMembershipType(programmeMembership.getProgrammeMembershipType());
    result.setProgrammeStartDate(programmeMembership.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembership.getProgrammeEndDate());
    result.setLeavingDestination(programmeMembership.getLeavingDestination());
    result.setLeavingReason(programmeMembership.getLeavingReason());
    Programme programme = programmeMembership.getProgramme();
    if (programme != null) {
      result.setProgrammeId(programme.getId());
      result.setProgrammeOwner(programme.getOwner());
      result.setProgrammeName(programme.getProgrammeName());
      result.setProgrammeNumber(programme.getProgrammeNumber());
      if (programmeMembership.getRotation() == null) {
        result.setRotation(null);
      } else {
        result.setRotation(rotationToRotationDto(programmeMembership.getRotation(), programme));
      }
    }
    result.setTrainingNumber(
        trainingNumberToTrainingNumberDto(programmeMembership.getTrainingNumber()));

    if (programmeMembership.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personToPersonDto(programmeMembership.getPerson()));
    }
    result.setConditionsOfJoining(
        conditionsOfJoiningMapper.toDto(programmeMembership.getConditionsOfJoining()));

    result.setTrainingPathway(programmeMembership.getTrainingPathway());
    return result;
  }

  /**
   * Convert a CurriculumMembership to a CurriculumMembershipDTO.
   *
   * @param curriculumMembership the CurriculumMembership object to convert
   * @return a CurriculumMembershipDTO object
   */
  public CurriculumMembershipDTO curriculumMembershipToCurriculumMembershipDto(
      CurriculumMembership curriculumMembership) {
    CurriculumMembershipDTO result = new CurriculumMembershipDTO();

    //Note: this is done as the ProgrammeMembershipDTO didn't have an id but it was needed
    //so that the FE can send delete by id requests
    result.setId(curriculumMembership.getId());
    result.setIntrepidId(curriculumMembership.getIntrepidId());
    result.setCurriculumStartDate(curriculumMembership.getCurriculumStartDate());
    result.setCurriculumEndDate(curriculumMembership.getCurriculumEndDate());
    result.setPeriodOfGrace(curriculumMembership.getPeriodOfGrace());
    result.setCurriculumCompletionDate(curriculumMembership.getCurriculumCompletionDate());
    result.setCurriculumId(curriculumMembership.getCurriculumId());
    result.setAmendedDate(curriculumMembership.getAmendedDate());
    if (curriculumMembership.getProgrammeMembership() != null) {
      result.setProgrammeMembershipUuid(curriculumMembership.getProgrammeMembership().getUuid());
    }
    return result;
  }

  /**
   * Amend a CurriculumMembership object with details from a CurriculumMembershipDTO.
   *
   * @param curriculumMembershipDto the CurriculumMembershipDTO to use
   * @param curriculumMembership    the CurriculumMembership object to amend
   * @return the amended (enriched) CurriculumMembership object
   */
  private CurriculumMembership curriculumMembershipDtoToCurriculumMembership(
      CurriculumMembershipDTO curriculumMembershipDto, CurriculumMembership curriculumMembership) {

    curriculumMembership.setId(curriculumMembershipDto.getId());
    curriculumMembership.setIntrepidId(curriculumMembershipDto.getIntrepidId());
    curriculumMembership.setCurriculumStartDate(curriculumMembershipDto.getCurriculumStartDate());
    curriculumMembership.setCurriculumEndDate(curriculumMembershipDto.getCurriculumEndDate());
    curriculumMembership.setPeriodOfGrace(curriculumMembershipDto.getPeriodOfGrace());
    curriculumMembership
        .setCurriculumCompletionDate(curriculumMembershipDto.getCurriculumCompletionDate());
    curriculumMembership.setCurriculumId(curriculumMembershipDto.getCurriculumId());
    curriculumMembership.setAmendedDate(curriculumMembershipDto.getAmendedDate());
    return curriculumMembership;
  }

  /**
   * Convert a Person to a PersonDTO object.
   *
   * @param person the Person object to convert
   * @return a PersonDTO object
   */
  private PersonDTO personToPersonDto(Person person) {
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

  /**
   * Convert a TrainingNumber to a TrainingNumberDTO object.
   *
   * @param trainingNumber the TrainingNumber object to convert
   * @return a TrainingNumberDTO object
   */
  private TrainingNumberDTO trainingNumberToTrainingNumberDto(TrainingNumber trainingNumber) {
    TrainingNumberDTO result = null;
    if (trainingNumber != null) {
      result = new TrainingNumberDTO();
      result.setId(trainingNumber.getId());
      result.setIntrepidId(trainingNumber.getIntrepidId());
      result.setTrainingNumber(trainingNumber.getTrainingNumber());
    }
    return result;
  }

  /**
   * Convert a Rotation to a RotationDTO object.
   *
   * @param rotation  the Rotation object to convert
   * @param programme the rotation Programme
   * @return a RotationDTO object
   */
  private RotationDTO rotationToRotationDto(Rotation rotation, Programme programme) {
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
}
