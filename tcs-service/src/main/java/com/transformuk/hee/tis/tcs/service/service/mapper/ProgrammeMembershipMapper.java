package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Mapper for the entity ProgrammeMembership and its DTO ProgrammeMembershipDTO.
 */
@Component
public class ProgrammeMembershipMapper {

  CurriculumMembershipMapper curriculumMembershipMapper;
  ConditionsOfJoiningMapper conditionsOfJoiningMapper;
  ConditionsOfJoiningRepository conditionsOfJoiningRepository;

  public ProgrammeMembershipMapper(CurriculumMembershipMapper curriculumMembershipMapper,
      ConditionsOfJoiningMapper conditionsOfJoiningMapper,
      ConditionsOfJoiningRepository conditionsOfJoiningRepository) {
    this.curriculumMembershipMapper = curriculumMembershipMapper;
    this.conditionsOfJoiningMapper = conditionsOfJoiningMapper;
    this.conditionsOfJoiningRepository = conditionsOfJoiningRepository;
  }

  public ProgrammeMembershipDTO toDto(ProgrammeMembership programmeMembership) {
    ProgrammeMembershipDTO result = null;
    if (programmeMembership != null) {
      result = programmeMembershipToProgrammeMembershipDTO(programmeMembership);
      if (CollectionUtils.isEmpty(result.getCurriculumMemberships())) {
        result.setCurriculumMemberships(Lists.newArrayList());
      }
      result.getCurriculumMemberships()
          .addAll(programmeMembershipToCurriculumMembershipDtos(programmeMembership));
    }
    return result;
  }

  public List<ProgrammeMembershipDTO> allEntityToDto(
      List<ProgrammeMembership> programmeMemberships) {
    List<ProgrammeMembershipDTO> result = Lists.newArrayList();

    for (ProgrammeMembership programmeMembership : programmeMemberships) {
      for (CurriculumMembership curriculumMembership
          : programmeMembership.getCurriculumMemberships()) {
        ProgrammeMembershipDTO programmeMembershipDto
            = programmeMembershipToProgrammeMembershipDTO(programmeMembership);
        programmeMembershipDto.setCurriculumMemberships(Lists.newArrayList());
        CurriculumMembershipDTO curriculumMembershipDto
            = curriculumMembershipMapper
            .curriculumMembershipToCurriculumMembershipDto(curriculumMembership);
        programmeMembershipDto.getCurriculumMemberships()
            .add(curriculumMembershipDto);
        if (programmeMembershipDto.getUuid() != null) {
          Optional<ConditionsOfJoining> conditionsOfJoiningOptional
              = conditionsOfJoiningRepository.findById(programmeMembershipDto.getUuid());
          if (conditionsOfJoiningOptional.isPresent()) {
            programmeMembershipDto.setConditionsOfJoining(
                conditionsOfJoiningMapper.toDto(conditionsOfJoiningOptional.get()));
          } else {
            programmeMembershipDto.setConditionsOfJoining(null);
          }
        }
        result.add(programmeMembershipDto);
      }
    }

    return result;
  }

  public List<ProgrammeMembershipDTO> programmeMembershipsToProgrammeMembershipDTOs(
      List<ProgrammeMembership> programmeMemberships) {
    Map<ProgrammeMembershipDTO, ProgrammeMembershipDTO> listMap = Maps.newHashMap();

    for (ProgrammeMembership programmeMembership : programmeMemberships) {
      ProgrammeMembershipDTO programmeMembershipDto
          = programmeMembershipToProgrammeMembershipDTO(programmeMembership);
      if (listMap.containsKey(programmeMembershipDto)) {
        programmeMembershipDto = listMap.get(programmeMembershipDto);
      }
      if (CollectionUtils.isEmpty(programmeMembershipDto.getCurriculumMemberships())) {
        programmeMembershipDto.setCurriculumMemberships(Lists.newArrayList());
      }
      programmeMembershipDto.getCurriculumMemberships()
          .addAll(programmeMembershipToCurriculumMembershipDtos(programmeMembership));
      listMap.put(programmeMembershipDto, programmeMembershipDto);
    }

    return new ArrayList<>(listMap.keySet());
  }

  /**
   * Covert a ProgrammeMembershipDTO to a ProgrammeMembership, including its CurriculumMemberships.
   *
   * @param programmeMembershipDto the ProgrammeMembershipDTO to convert
   * @return the ProgrammeMembership entity
   */
  public ProgrammeMembership toEntity(ProgrammeMembershipDTO programmeMembershipDto) {
    ProgrammeMembership result = new ProgrammeMembership();

    result.setUuid(programmeMembershipDto.getUuid()); //use real (database) ID
    result.setProgrammeMembershipType(programmeMembershipDto.getProgrammeMembershipType());
    result.setProgrammeStartDate(programmeMembershipDto.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembershipDto.getProgrammeEndDate());
    result.setLeavingReason(programmeMembershipDto.getLeavingReason());
    result.setTrainingPathway(programmeMembershipDto.getTrainingPathway());
    result.setAmendedDate(programmeMembershipDto.getAmendedDate());
    Programme programme = null;
    if (programmeMembershipDto.getProgrammeId() != null) {
      programme = new Programme();
      programme.setId(programmeMembershipDto.getProgrammeId());
      programme.setProgrammeName(programmeMembershipDto.getProgrammeName());
      programme.setOwner(programmeMembershipDto.getProgrammeOwner());
      programme.setProgrammeNumber(programmeMembershipDto.getProgrammeNumber());
      result.setRotation(rotationDTOToRotation(programmeMembershipDto.getRotation()));
    }
    result.setProgramme(programme);
    result.setTrainingNumber(
        trainingNumberDTOToTrainingNumber(programmeMembershipDto.getTrainingNumber()));
    result.setPerson(personDTOToPerson(programmeMembershipDto.getPerson()));

    if (programmeMembershipDto.getCurriculumMemberships() == null) {
      result.setCurriculumMemberships(new HashSet<>());
    }
    result.getCurriculumMemberships()
        .addAll(curriculumMembershipMapper.toEntity(programmeMembershipDto));
    result.getCurriculumMemberships().forEach(cm -> cm.setProgrammeMembership(result));
    return result;
  }

  public List<ProgrammeMembership> programmeMembershipDTOsToProgrammeMemberships(
      List<ProgrammeMembershipDTO> programmeMembershipDTOs) {
    List<ProgrammeMembership> result = Lists.newArrayList();

    for (ProgrammeMembershipDTO programmeMembershipDto : programmeMembershipDTOs) {
      result.add(toEntity(programmeMembershipDto));
    }

    return result;
  }

  private ProgrammeMembershipDTO programmeMembershipToProgrammeMembershipDTO(
      ProgrammeMembership programmeMembership) {
    ProgrammeMembershipDTO result = new ProgrammeMembershipDTO();

    if (!programmeMembership.getCurriculumMemberships().isEmpty()) {
      //Preserve backward-compatibility
      result.setId(programmeMembership.getCurriculumMemberships().iterator().next().getId());
    }
    result.setUuid(programmeMembership.getUuid());
    result.setProgrammeMembershipType(programmeMembership.getProgrammeMembershipType());
    result.setProgrammeStartDate(programmeMembership.getProgrammeStartDate());
    result.setProgrammeEndDate(programmeMembership.getProgrammeEndDate());
    result.setLeavingReason(programmeMembership.getLeavingReason());
    result.setAmendedDate(programmeMembership.getAmendedDate());
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
    if (result.getUuid() != null) {
      Optional<ConditionsOfJoining> conditionsOfJoiningOptional
          = conditionsOfJoiningRepository.findById(result.getUuid());
      if (conditionsOfJoiningOptional.isPresent()) {
        result.setConditionsOfJoining(
            conditionsOfJoiningMapper.toDto(conditionsOfJoiningOptional.get()));
      } else {
        result.setConditionsOfJoining(null);
      }
    }
    return result;
  }

  private List<CurriculumMembershipDTO> programmeMembershipToCurriculumMembershipDtos(
      ProgrammeMembership programmeMembership) {
    List<CurriculumMembershipDTO> curriculumMembershipDtos = Lists.newArrayList();
    Set<CurriculumMembership> curriculumMemberships
        = programmeMembership.getCurriculumMemberships();

    curriculumMemberships.forEach(cm -> {
      CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();

      cmDto.setId(cm.getId());
      cmDto.setIntrepidId(cm.getIntrepidId());
      cmDto.setCurriculumStartDate(cm.getCurriculumStartDate());
      cmDto.setCurriculumEndDate(cm.getCurriculumEndDate());
      cmDto.setPeriodOfGrace(cm.getPeriodOfGrace());
      cmDto.setCurriculumCompletionDate(cm.getCurriculumCompletionDate());
      cmDto.setCurriculumId(cm.getCurriculumId());
      cmDto.setAmendedDate(cm.getAmendedDate());

      curriculumMembershipDtos.add(cmDto);
    });

    return curriculumMembershipDtos;
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
