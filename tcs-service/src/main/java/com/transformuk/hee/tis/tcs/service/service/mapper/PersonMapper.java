package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity Person and its DTO PersonDTO.
 */
@Component
public class PersonMapper {

  @Autowired
  private ContactDetailsMapper contactDetailsMapper;

  @Autowired
  private PersonalDetailsMapper personalDetailsMapper;

  @Autowired
  private GmcDetailsMapper gmcDetailsMapper;

  @Autowired
  private GdcDetailsMapper gdcDetailsMapper;

  @Autowired
  private QualificationMapper qualificationMapper;

  @Autowired
  private RightToWorkMapper rightToWorkMapper;

  @Autowired
  private CurriculumMembershipMapper curriculumMembershipMapper;

  @Autowired
  private TrainerApprovalMapper trainerApprovalMapper;

  public Person toEntity(PersonDTO dto) {
    if (dto == null) {
      return null;
    }
    Person person = new Person();
    person.setId(dto.getId());
    person.setIntrepidId(dto.getIntrepidId());
    person.setAddedDate(dto.getAddedDate());
    person.setAmendedDate(dto.getAmendedDate());
    person.setRole(dto.getRole());
    person.setStatus(dto.getStatus());
    person.setComments(dto.getComments());
    person.setInactiveDate(dto.getInactiveDate());
    person.setInactiveNotes(dto.getInactiveNotes());
    person.setPublicHealthNumber(dto.getPublicHealthNumber());
    person.setContactDetails(contactDetailsMapper.toEntity(dto.getContactDetails()));
    person.setPersonalDetails(personalDetailsMapper.toEntity(dto.getPersonalDetails()));
    person.setGmcDetails(gmcDetailsMapper.toEntity(dto.getGmcDetails()));
    person.setGdcDetails(gdcDetailsMapper.toEntity(dto.getGdcDetails()));

    if (dto.getQualifications() != null) {
      person.setQualifications(
          new HashSet<>(qualificationMapper.toEntities(new ArrayList<>(dto.getQualifications()))));
    }

    if (dto.getProgrammeMemberships() != null) {
      person.setCurriculumMemberships(new HashSet<>(curriculumMembershipMapper
          .programmeMembershipDtosToCurriculumMemberships(
              new ArrayList<>(dto.getProgrammeMemberships()))));
    }

    person.setRightToWork(rightToWorkMapper.toEntity(dto.getRightToWork()));
    person.setRegulator(dto.getRegulator());

    if (dto.getTrainerApprovals() != null) {
      person.setTrainerApprovals(new HashSet<>(
          trainerApprovalMapper.toEntities(new ArrayList<>(dto.getTrainerApprovals()))));
    }

    return person;
  }

  public PersonDTO toDto(Person entity) {
    return toDto(entity, true);
  }

  public PersonDTO toDto(Person entity, Boolean includePlacements) {
    if (entity == null) {
      return null;
    }
    PersonDTO personDTO = new PersonDTO();
    personDTO.setId(entity.getId());
    personDTO.setIntrepidId(entity.getIntrepidId());
    personDTO.setAddedDate(entity.getAddedDate());
    personDTO.setAmendedDate(entity.getAmendedDate());
    personDTO.setRole(entity.getRole());
    personDTO.setStatus(entity.getStatus());
    personDTO.setComments(entity.getComments());
    personDTO.setInactiveDate(entity.getInactiveDate());
    personDTO.setInactiveNotes(entity.getInactiveNotes());
    personDTO.setPublicHealthNumber(entity.getPublicHealthNumber());
    personDTO.setContactDetails(contactDetailsMapper.toDto(entity.getContactDetails()));
    personDTO.setPersonalDetails(personalDetailsMapper.toDto(entity.getPersonalDetails()));
    personDTO.setGmcDetails(gmcDetailsMapper.toDto(entity.getGmcDetails()));
    personDTO.setGdcDetails(gdcDetailsMapper.toDto(entity.getGdcDetails()));

    if (entity.getQualifications() != null) {
      personDTO.setQualifications(
          new HashSet<>(qualificationMapper.toDTOs(new ArrayList<>(entity.getQualifications()))));
    }

    personDTO.setRightToWork(rightToWorkMapper.toDto(entity.getRightToWork()));
    personDTO.setRegulator(entity.getRegulator());

    if (entity.getCurriculumMemberships() != null) {
      personDTO.setProgrammeMemberships(new HashSet<>(curriculumMembershipMapper
          .curriculumMembershipsToProgrammeMembershipDtos(
              new ArrayList<>(entity.getCurriculumMemberships()))));
    }

    if (entity.getTrainerApprovals() != null) {
      personDTO.setTrainerApprovals(new HashSet<>(
          trainerApprovalMapper.toDTOs(new ArrayList<>(entity.getTrainerApprovals()))));
    }

    return personDTO;
  }

  public List<Person> toEntity(List<PersonDTO> dtoList) {
    if (dtoList == null) {
      return null;
    }
    return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
  }

  public List<PersonDTO> toDto(List<Person> entityList) {
    if (entityList == null) {
      return null;
    }
    return entityList.stream().map(p -> this.toDto(p, true)).collect(Collectors.toList());
  }
}
