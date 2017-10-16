package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
  private ProgrammeMembershipMapper programmeMembershipMapper;

  @Autowired
  private PlacementViewMapper placementViewMapper;

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
      person.setQualifications(qualificationMapper.toEntities(
          dto.getQualifications().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet())
      );
    }
    if (dto.getProgrammeMemberships() != null) {
      person.setProgrammeMemberships(programmeMembershipMapper.programmeMembershipDTOsToProgrammeMemberships(
          dto.getProgrammeMemberships().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet())
      );
    }
    person.setRightToWork(rightToWorkMapper.toEntity(dto.getRightToWork()));
    person.setRegulator(dto.getRegulator());

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
      personDTO.setQualifications(qualificationMapper.toDTOs(
          entity.getQualifications().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet())
      );
    }
    personDTO.setRightToWork(rightToWorkMapper.toDto(entity.getRightToWork()));
    personDTO.setRegulator(entity.getRegulator());
    if (entity.getProgrammeMemberships() != null) {
      personDTO.setProgrammeMemberships(programmeMembershipMapper.programmeMembershipsToProgrammeMembershipDTOs(
          entity.getProgrammeMemberships().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet())
      );
    }
    if (entity.getPlacements() != null && includePlacements) {
      personDTO.setPlacements(placementViewMapper.placementsToPlacementViewDTOs(
          entity.getPlacements().stream().collect(Collectors.toList()), true, false).stream().collect(Collectors.toSet())
      );
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
