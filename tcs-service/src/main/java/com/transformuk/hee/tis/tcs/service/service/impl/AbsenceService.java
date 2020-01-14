package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.model.Absence;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.AbsenceRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.AbsenceMapper;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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


  public Optional<AbsenceDTO> patchAbsence(Map<String, Object> absenceDTO) throws Exception {
    Preconditions.checkArgument(absenceDTO != null, "absenceDto cannot be null");

    Optional<Absence> optionalAbsence = Optional.empty();
    Optional<AbsenceDTO> result = Optional.empty();
    if (absenceDTO.containsKey("id")) {
      optionalAbsence = absenceRepository.findById(new Long((Integer) absenceDTO.get("id")));
    } else if (absenceDTO.containsKey("absenceAttendanceId")) {
      optionalAbsence = absenceRepository
          .findByAbsenceAttendanceId((String) absenceDTO.get("absenceAttendanceId"));
    }

    if (optionalAbsence.isPresent()) {
      Absence foundAbsence = optionalAbsence.get();
      for (Entry<String, Object> entry : absenceDTO.entrySet()) {
        String fieldName = entry.getKey();
        Object fieldValue = entry.getValue();
        if(fieldValue != null) {
          if (fieldName.equals("startDate") || fieldName.equals("endDate")) {
            fieldValue = LocalDate.parse((String) entry.getValue());
          } else if (fieldName.equals("durationInDays")) {
            fieldValue = new Long((Integer) entry.getValue());
          }
        }

        if (StringUtils.equals("personId", fieldName) ||
            StringUtils.equals("id", fieldName)) {
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
