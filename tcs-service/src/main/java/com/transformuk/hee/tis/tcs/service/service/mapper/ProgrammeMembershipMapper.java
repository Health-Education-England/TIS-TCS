package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity ProgrammeMembership and its DTO ProgrammeMembershipDTO.
 */
@Component
public class ProgrammeMembershipMapper {

  CurriculumMembershipMapper curriculumMembershipMapper;
  ConditionsOfJoiningMapper conditionsOfJoiningMapper;
  TrainingNumberMapper trainingNumberMapper;
  RotationMapper rotationMapper;

  /**
   * Initialise the ProgrammeMembership mapper.
   *
   * @param curriculumMembershipMapper the Curriculum Membership mapper
   * @param conditionsOfJoiningMapper  the Conditions of Joining mapper
   */
  public ProgrammeMembershipMapper(CurriculumMembershipMapper curriculumMembershipMapper,
      ConditionsOfJoiningMapper conditionsOfJoiningMapper,
      TrainingNumberMapper trainingNumberMapper,
      RotationMapper rotationMapper) {
    this.curriculumMembershipMapper = curriculumMembershipMapper;
    this.conditionsOfJoiningMapper = conditionsOfJoiningMapper;
    this.trainingNumberMapper = trainingNumberMapper;
    this.rotationMapper = rotationMapper;
  }

  /**
   * Convert a programmeMembership entity to DTO with all currculumMemberships.
   *
   * @param pm programmeMembership entity
   * @return pmDto programmeMembership dto
   */
  public ProgrammeMembershipDTO toDto(ProgrammeMembership pm) {
    ProgrammeMembershipDTO pmDto = null;
    if (pm != null) {
      pmDto = programmeMembershipToProgrammeMembershipDTO(pm);
      pmDto.getCurriculumMemberships()
          .addAll(programmeMembershipToCurriculumMembershipDtos(pm));
    }
    return pmDto;
  }

  /**
   * Convert programmeMembership from entities to DTOs, and flatten every curriculumMembership in
   * programmeMemberships.
   *
   * <p>So the returned ProgrammeMembershipDTO list can contain the same programmeMembershipDTOs
   * under each of ProgrammeMembershipDTO, there will be only one CurriculumMembershipDTO.
   *
   * @param programmeMemberships list of programmeMembership entities to convert
   * @return ProgrammeMembershipDTO list
   */
  public List<ProgrammeMembershipDTO> allEntityToDto(
      List<ProgrammeMembership> programmeMemberships) {
    List<ProgrammeMembershipDTO> result = new ArrayList<>();

    programmeMemberships.forEach(
        pm -> pm.getCurriculumMemberships().forEach(cm -> {
          ProgrammeMembershipDTO pmDto = programmeMembershipToProgrammeMembershipDTO(pm);
          CurriculumMembershipDTO cmDto
              = curriculumMembershipMapper.curriculumMembershipToCurriculumMembershipDto(cm);
          pmDto.getCurriculumMemberships().add(cmDto);
          result.add(pmDto);
        }));

    return result;
  }

  /**
   * Convert programmeMembership from entities to DTOS, and roll up curriculumMembership for the
   * same programmeMembership.
   *
   * <p>So the returned ProgrammeMembershipDTO list contains unique ProgrammeMembershipDTOs
   * under each of ProgrammeMembershipDTO, there can be multiple CurriculumMembershipDTOs.
   *
   * @param programmeMemberships list of programmeMembership entities to convert
   * @return ProgrammeMembershipDTO list
   */
  public List<ProgrammeMembershipDTO> programmeMembershipsToProgrammeMembershipDTOs(
      List<ProgrammeMembership> programmeMemberships) {
    Map<UUID, ProgrammeMembershipDTO> listMap = Maps.newHashMap();

    programmeMemberships.forEach(pm -> {
      ProgrammeMembershipDTO pmDto = programmeMembershipToProgrammeMembershipDTO(pm);
      if (listMap.containsKey(pmDto.getUuid())) {
        pmDto = listMap.get(pmDto.getUuid());
      }
      pmDto.getCurriculumMemberships().addAll(programmeMembershipToCurriculumMembershipDtos(pm));
      listMap.put(pmDto.getUuid(), pmDto);
    });

    return new ArrayList<>(listMap.values());
  }

  /**
   * Covert a ProgrammeMembershipDTO to a ProgrammeMembership, including its CurriculumMemberships.
   *
   * @param dto the ProgrammeMembershipDTO to convert
   * @return the ProgrammeMembership entity
   */
  public ProgrammeMembership toEntity(ProgrammeMembershipDTO dto) {
    ProgrammeMembership entity = new ProgrammeMembership();

    entity.setUuid(dto.getUuid()); //use real (database) ID
    entity.setProgrammeMembershipType(dto.getProgrammeMembershipType());
    entity.setProgrammeStartDate(dto.getProgrammeStartDate());
    entity.setProgrammeEndDate(dto.getProgrammeEndDate());
    entity.setLeavingReason(dto.getLeavingReason());
    entity.setTrainingPathway(dto.getTrainingPathway());
    entity.setAmendedDate(dto.getAmendedDate());

    Programme programme = null;
    if (dto.getProgrammeId() != null) {
      programme = new Programme();
      programme.setId(dto.getProgrammeId());
      programme.setProgrammeName(dto.getProgrammeName());
      programme.setOwner(dto.getProgrammeOwner());
      programme.setProgrammeNumber(dto.getProgrammeNumber());
      entity.setRotation(rotationMapper.toEntity(dto.getRotation()));
    }
    entity.setProgramme(programme);

    entity.setTrainingNumber(
        trainingNumberMapper.trainingNumberDTOToTrainingNumber(dto.getTrainingNumber()));
    entity.setPerson(personDTOToPerson(dto.getPerson()));

    entity.setCurriculumMemberships(new HashSet<>());
    entity.getCurriculumMemberships()
        .addAll(curriculumMembershipMapper.toEntity(dto));
    entity.getCurriculumMemberships().forEach(cm -> cm.setProgrammeMembership(entity));
    return entity;
  }

  /**
   * Convert ProgrammeMembership DTOs to entities by using {@link #toEntity(ProgrammeMembershipDTO)}
   * for each DTO.
   *
   * @param pmDtos list of ProgrammeMembershipDTO
   * @return the ProgrammeMembership entity list
   */
  public List<ProgrammeMembership> programmeMembershipDTOsToProgrammeMemberships(
      List<ProgrammeMembershipDTO> pmDtos) {
    return pmDtos.stream().map(this::toEntity).collect(Collectors.toList());
  }

  // convert programmeMembership entity to DTO with an empty curriculumMembership list
  private ProgrammeMembershipDTO programmeMembershipToProgrammeMembershipDTO(
      ProgrammeMembership entity) {
    ProgrammeMembershipDTO dto = new ProgrammeMembershipDTO();

    if (!entity.getCurriculumMemberships().isEmpty()) {
      //Preserve backward-compatibility, set ID in DTO to the ID of the first curriculumMembership
      dto.setId(entity.getCurriculumMemberships().iterator().next().getId());
    }

    dto.setUuid(entity.getUuid());
    dto.setProgrammeMembershipType(entity.getProgrammeMembershipType());
    dto.setProgrammeStartDate(entity.getProgrammeStartDate());
    dto.setProgrammeEndDate(entity.getProgrammeEndDate());
    dto.setLeavingReason(entity.getLeavingReason());
    dto.setAmendedDate(entity.getAmendedDate());
    dto.setTrainingPathway(entity.getTrainingPathway());

    Programme programme = entity.getProgramme();
    if (programme != null) {
      dto.setProgrammeId(programme.getId());
      dto.setProgrammeOwner(programme.getOwner());
      dto.setProgrammeName(programme.getProgrammeName());
      dto.setProgrammeNumber(programme.getProgrammeNumber());
      dto.setRotation(rotationToRotationDTO(entity.getRotation(), programme));
    }

    dto.setTrainingNumber(
        trainingNumberMapper.trainingNumberToTrainingNumberDTO(entity.getTrainingNumber()));
    dto.setPerson(personToPersonDTO(entity.getPerson()));

    dto.setCurriculumMemberships(new ArrayList<>());

    dto.setConditionsOfJoining(
        conditionsOfJoiningMapper.toDto(entity.getConditionsOfJoining()));
    return dto;
  }

  private List<CurriculumMembershipDTO> programmeMembershipToCurriculumMembershipDtos(
      ProgrammeMembership programmeMembership) {
    return programmeMembership.getCurriculumMemberships().stream()
        .map(curriculumMembershipMapper::curriculumMembershipToCurriculumMembershipDto)
        .collect(Collectors.toList());
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

  // map rotation entity to DTO with programme details
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
}
