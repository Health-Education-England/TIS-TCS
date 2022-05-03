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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Mapper for the entity ProgrammeMembership and its DTO ProgrammeMembershipDTO.
 */
@Component
public class ProgrammeMembershipMapper {

  CurriculumMembershipMapper curriculumMembershipMapper;

  public ProgrammeMembershipMapper(CurriculumMembershipMapper curriculumMembershipMapper) {
    this.curriculumMembershipMapper = curriculumMembershipMapper;
  }

  public ProgrammeMembershipDTO toDto(ProgrammeMembership programmeMembership) {
    ProgrammeMembershipDTO result = null;
    if (programmeMembership != null) {
      result = programmeMembershipToProgrammeMembershipDTO(programmeMembership);
      if (CollectionUtils.isEmpty(result.getCurriculumMemberships())) {
        result.setCurriculumMemberships(Lists.newArrayList());
      }
      result.getCurriculumMemberships()
          .addAll(programmeMembershipToCurriculumMembershipDTOs(programmeMembership));
    }
    return result;
  }

  public List<ProgrammeMembershipDTO> allEntityToDto(
      List<ProgrammeMembership> programmeMemberships) {
    List<ProgrammeMembershipDTO> result = Lists.newArrayList();

    for (ProgrammeMembership programmeMembership : programmeMemberships) {
      ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipToProgrammeMembershipDTO(
          programmeMembership);
      if (CollectionUtils.isEmpty(programmeMembershipDTO.getCurriculumMemberships())) {
        programmeMembershipDTO.setCurriculumMemberships(Lists.newArrayList());
      }
      programmeMembershipDTO.getCurriculumMemberships()
          .addAll(programmeMembershipToCurriculumMembershipDTOs(programmeMembership));
      result.add(programmeMembershipDTO);
    }

    return result;
  }

  public List<ProgrammeMembershipDTO> programmeMembershipsToProgrammeMembershipDTOs(
      List<ProgrammeMembership> programmeMemberships) {
    Map<ProgrammeMembershipDTO, ProgrammeMembershipDTO> listMap = Maps.newHashMap();

    for (ProgrammeMembership programmeMembership : programmeMemberships) {
      ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipToProgrammeMembershipDTO(
          programmeMembership);
      if (listMap.containsKey(programmeMembershipDTO)) {
        programmeMembershipDTO = listMap.get(programmeMembershipDTO);
      }
      if (CollectionUtils.isEmpty(programmeMembershipDTO.getCurriculumMemberships())) {
        programmeMembershipDTO.setCurriculumMemberships(Lists.newArrayList());
      }
      programmeMembershipDTO.getCurriculumMemberships()
          .addAll(programmeMembershipToCurriculumMembershipDTOs(programmeMembership));
      listMap.put(programmeMembershipDTO, programmeMembershipDTO);
    }

    return new ArrayList<>(listMap.keySet());
  }

  public ProgrammeMembership toEntity(ProgrammeMembershipDTO programmeMembershipDTO) {
    ProgrammeMembership result = new ProgrammeMembership();

    result.setId(programmeMembershipDTO.getId());
    result.setProgrammeMembershipType(programmeMembershipDTO.getProgrammeMembershipType());
    result.setProgrammeStartDate(programmeMembershipDTO.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembershipDTO.getProgrammeEndDate());
    result.setLeavingReason(programmeMembershipDTO.getLeavingReason());
    result.setTrainingPathway(programmeMembershipDTO.getTrainingPathway());
    Programme programme = null;
    if (programmeMembershipDTO.getProgrammeId() != null) {
      programme = new Programme();
      programme.setId(programmeMembershipDTO.getProgrammeId());
      programme.setProgrammeName(programmeMembershipDTO.getProgrammeName());
      programme.setOwner(programmeMembershipDTO.getProgrammeOwner());
      programme.setProgrammeNumber(programmeMembershipDTO.getProgrammeNumber());
      result.setRotation(rotationDTOToRotation(programmeMembershipDTO.getRotation()));
    }
    result.setProgramme(programme);
    result.setTrainingNumber(trainingNumberDTOToTrainingNumber(programmeMembershipDTO.getTrainingNumber()));
    result.setPerson(personDTOToPerson(programmeMembershipDTO.getPerson()));

    if (CollectionUtils.isEmpty(programmeMembershipDTO.getCurriculumMemberships())) {
      result.setCurriculumMemberships(new HashSet<>());
    }
    result.getCurriculumMemberships()
        .addAll(curriculumMembershipMapper.toEntity(programmeMembershipDTO));
    result.getCurriculumMemberships().forEach(cm -> cm.setProgrammeMembership(result));
    return result;
  }

  public List<ProgrammeMembership> programmeMembershipDTOsToProgrammeMemberships(
      List<ProgrammeMembershipDTO> programmeMembershipDTOs) {
    List<ProgrammeMembership> result = Lists.newArrayList();

    for (ProgrammeMembershipDTO programmeMembershipDTO : programmeMembershipDTOs) {
      result.add(toEntity(programmeMembershipDTO));
    }

    return result;
  }

  private ProgrammeMembershipDTO programmeMembershipToProgrammeMembershipDTO(
      ProgrammeMembership programmeMembership) {
    ProgrammeMembershipDTO result = new ProgrammeMembershipDTO();

    result.setId(programmeMembership.getId());
    result.setProgrammeMembershipType(programmeMembership.getProgrammeMembershipType());
    result.setProgrammeStartDate(programmeMembership.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembership.getProgrammeEndDate());
    result.setLeavingReason(programmeMembership.getLeavingReason());
    Programme programme = programmeMembership.getProgramme();
    if (programme != null) {
      result.setProgrammeId(programme.getId());
      result.setProgrammeOwner(programme.getOwner());
      result.setProgrammeName(programme.getProgrammeName());
      result.setProgrammeNumber(programme.getProgrammeNumber());
      result.setRotation(rotationToRotationDTO(programmeMembership.getRotation(), programme));
    }
    result.setTrainingNumber(
        trainingNumberToTrainingNumberDTO(programmeMembership.getTrainingNumber()));

    if (programmeMembership.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personToPersonDTO(programmeMembership.getPerson()));
    }
    result.setTrainingPathway(programmeMembership.getTrainingPathway());

    return result;
  }

  private List<CurriculumMembershipDTO> programmeMembershipToCurriculumMembershipDTOs(
      ProgrammeMembership programmeMembership) {
    List<CurriculumMembershipDTO> curriculumMembershipDTOs = Lists.newArrayList();
    Set<CurriculumMembership> curriculumMemberships = programmeMembership.getCurriculumMemberships();

    curriculumMemberships.forEach(cm -> {
      CurriculumMembershipDTO cmDTO = new CurriculumMembershipDTO();

      cmDTO.setId(cm.getId());
      cmDTO.setIntrepidId(cm.getIntrepidId());
      cmDTO.setCurriculumStartDate(cm.getCurriculumStartDate());
      cmDTO.setCurriculumEndDate(cm.getCurriculumEndDate());
      cmDTO.setPeriodOfGrace(cm.getPeriodOfGrace());
      cmDTO.setCurriculumCompletionDate(cm.getCurriculumCompletionDate());
      cmDTO.setCurriculumId(cm.getCurriculumId());
      cmDTO.setAmendedDate(cm.getAmendedDate());

      curriculumMembershipDTOs.add(cmDTO);
    });

    return curriculumMembershipDTOs;
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
