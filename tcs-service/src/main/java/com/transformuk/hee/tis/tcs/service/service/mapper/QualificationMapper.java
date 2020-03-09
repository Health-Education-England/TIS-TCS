package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity Qualification and its DTO QualificationDTO.
 */
@Component
public class QualificationMapper {

  public QualificationDTO toDto(Qualification qualification) {
    QualificationDTO result = null;
    if (qualification != null) {
      result = qualificationToQualificationDTO(qualification);
    }
    return result;
  }

  public Qualification toEntity(QualificationDTO qualificationDTO) {
    return qualificationDTOToQualification(qualificationDTO);
  }

  public List<Qualification> toEntities(List<QualificationDTO> qualificationDTOs) {
    List<Qualification> result = Lists.newArrayList();

    for (QualificationDTO qualificationDTO : qualificationDTOs) {
      result.add(toEntity(qualificationDTO));
    }

    return result;
  }

  public List<QualificationDTO> toDTOs(List<Qualification> qualifications) {
    List<QualificationDTO> result = Lists.newArrayList();

    for (Qualification qualification : qualifications) {
      result.add(toDto(qualification));
    }

    return result;
  }

  private QualificationDTO qualificationToQualificationDTO(Qualification qualification) {
    QualificationDTO result = new QualificationDTO();

    result.setId(qualification.getId());
    result.setIntrepidId(qualification.getIntrepidId());
    result.setQualification(qualification.getQualification());
    result.setQualificationType(qualification.getQualificationType());
    result.setQualificationAttainedDate(qualification.getQualificationAttainedDate());
    result.setCountryOfQualification(qualification.getCountryOfQualification());
    result.setMedicalSchool(qualification.getMedicalSchool());
    result.setAmendedDate(qualification.getAmendedDate());
    if (qualification.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personToPersonDTO(qualification.getPerson()));
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

  private Qualification qualificationDTOToQualification(QualificationDTO qualificationDTO) {
    Qualification result = new Qualification();
    result.setId(qualificationDTO.getId());
    result.setIntrepidId(qualificationDTO.getIntrepidId());
    result.setQualification(qualificationDTO.getQualification());
    result.setQualificationType(qualificationDTO.getQualificationType());
    result.setQualificationAttainedDate(qualificationDTO.getQualificationAttainedDate());
    result.setCountryOfQualification(qualificationDTO.getCountryOfQualification());
    result.setMedicalSchool(qualificationDTO.getMedicalSchool());
    result.setAmendedDate(qualificationDTO.getAmendedDate());
    if (qualificationDTO.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personDTOToPerson(qualificationDTO.getPerson()));
    }
    return result;
  }
}
