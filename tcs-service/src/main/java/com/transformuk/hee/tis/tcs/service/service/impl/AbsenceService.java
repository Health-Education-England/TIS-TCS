package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.model.Absence;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.AbsenceRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.AbsenceMapper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.naming.ldap.PagedResultsControl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AbsenceService {

  @Autowired
  private AbsenceMapper absenceMapper;
  @Autowired
  private AbsenceRepository absenceRepository;
  @Autowired
  private PersonRepository personRepository;

  public Optional<AbsenceDTO> findById(Long id) {
    Preconditions.checkArgument(id != null, "The id cannot be null");
    Optional<Absence> optionalAbsence = absenceRepository.findById(id);
    return optionalAbsence.map(absenceMapper::toDto);
  }

  public Optional<AbsenceDTO> findAbsenceById(String id) {
    Preconditions.checkArgument(id != null, "The id cannot be null");
    Optional<Absence> optionalAbsence = absenceRepository.findByAbsenceAttendanceId(id);
    return optionalAbsence.map(absenceMapper::toDto);
  }


  public AbsenceDTO createAbsence(AbsenceDTO absenceDTO) {
    Preconditions.checkArgument(absenceDTO != null,
        "Cannot create new Absence if absence is null");
    Preconditions
        .checkArgument(absenceDTO.getId() == null,
            "The id on absence must be null to create");

    return createOrUpdateAbsence(absenceDTO);
  }

  public AbsenceDTO updateAbsence(AbsenceDTO absenceDTO) {
    Preconditions.checkArgument(absenceDTO != null,
        "Cannot create new Absence if absence is null");
    Preconditions.checkArgument(absenceDTO.getId() != null,
        "The id on absence must be null to update");

    return createOrUpdateAbsence(absenceDTO);
  }

  private AbsenceDTO createOrUpdateAbsence(AbsenceDTO absenceDTO) {
    Optional<AbsenceDTO> result = Optional.of(absenceDTO)
        .map(absenceMapper::toEntity)
        .map(absence -> {
          Optional<Person> optionalPerson = personRepository.findById(absenceDTO.getPersonId());
          if (optionalPerson.isPresent()) {
            absence.setPerson(optionalPerson.get());
          } else {
            throw new RuntimeException(
                "No person found for person id: " + absenceDTO.getPersonId());
          }
          return absence;
        })
        .map(absenceRepository::saveAndFlush)
        .map(absenceMapper::toDto);
    return result.orElseThrow(() -> new RuntimeException("Failed to create new Absence record"));
  }


  public Optional<AbsenceDTO> patchAbsence(AbsenceDTO absenceDTO) throws Exception {
    Preconditions.checkArgument(absenceDTO != null, "absenceDto cannot be null");

    Optional<Absence> optionalAbsence = Optional.empty();
    Optional<AbsenceDTO> result = Optional.empty();
    if (absenceDTO.getId() != null) {
      optionalAbsence = absenceRepository.findById(absenceDTO.getId());
    } else if (absenceDTO.getAbsenceAttendanceId() != null) {
      optionalAbsence = absenceRepository
          .findByAbsenceAttendanceId(absenceDTO.getAbsenceAttendanceId());
    }

    if (optionalAbsence.isPresent()) {
      Absence foundAbsence = optionalAbsence.get();
      List<Field> absenceFields = Lists.newArrayList(AbsenceDTO.class.getDeclaredFields());
      for (Field absenceFieldDto : absenceFields) {
        absenceFieldDto.setAccessible(true);
        String fieldName = absenceFieldDto.getName();
        Object fieldValue = absenceFieldDto.get(absenceDTO);

        if (StringUtils.equals("personId", fieldName) || fieldValue == null) {
          continue;
        }
        Field absenceField = Absence.class.getDeclaredField(fieldName);
        absenceField.setAccessible(true);
        absenceField.set(foundAbsence, fieldValue);
      }

      result = Optional.of(absenceMapper.toDto(absenceRepository.saveAndFlush(foundAbsence)));
    }

    return result;
  }
}
