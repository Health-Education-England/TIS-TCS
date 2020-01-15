package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
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

  private static final String ABSENCE_ID_KEY = "id";
  private static final String ABSENCE_ATTENDANCE_ID_KEY = "absenceAttendanceId";
  private static final String PERSON_ID_KEY = "personId";
  private static final String DURATION_IN_DAYS_KEY = "durationInDays";
  private static final String START_DATE_KEY = "startDate";
  private static final String END_DATE_KEY = "endDate";

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

  public Optional<AbsenceDTO> findAbsenceByAbsenceAttendanceId(String absenceAttendanceId) {
    Preconditions.checkArgument(StringUtils.isNotBlank(absenceAttendanceId),
        "The absenceAttendanceId cannot be null or blank");
    Optional<Absence> optionalAbsence = absenceRepository
        .findByAbsenceAttendanceId(absenceAttendanceId);
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


  public Optional<AbsenceDTO> patchAbsence(Map<String, Object> absenceDtoPatchMap)
      throws Exception {
    Preconditions.checkArgument(absenceDtoPatchMap != null, "absenceDto cannot be null");

    Map<String, Object> params = fixPatchParams(absenceDtoPatchMap);
    Optional<Absence> optionalAbsence = Optional.empty();
    if (absenceDtoPatchMap.containsKey(ABSENCE_ID_KEY)) {
      optionalAbsence = absenceRepository.findById((Long) params.get(ABSENCE_ID_KEY));
    } else if (absenceDtoPatchMap.containsKey(ABSENCE_ATTENDANCE_ID_KEY)) {
      optionalAbsence = absenceRepository
          .findByAbsenceAttendanceId((String) params.get(ABSENCE_ATTENDANCE_ID_KEY));
    }

    if (!optionalAbsence.isPresent()) {
      return Optional.empty();
    }
    Absence foundAbsence = optionalAbsence.get();
    for (Entry<String, Object> entry : params.entrySet()) {
      String fieldName = entry.getKey();
      Object fieldValue = entry.getValue();

      if (StringUtils.equals(PERSON_ID_KEY, fieldName) ||
          StringUtils.equals(ABSENCE_ID_KEY, fieldName)) {
        continue;
      }

      Field absenceField = Absence.class.getDeclaredField(fieldName);
      absenceField.setAccessible(true);
      absenceField.set(foundAbsence, fieldValue);
    }

    return Optional.of(absenceMapper.toDto(absenceRepository.saveAndFlush(foundAbsence)));
  }

  //because the endpoint converts json to a maps, it only knows about simple types and not the types
  //of the absenceDTO, so this is to fix the types so that they will match the Absence entity
  private Map<String, Object> fixPatchParams(Map<String, Object> params) {
    Map<String, Object> result = Maps.newHashMap();

    for (Entry<String, Object> entry : params.entrySet()) {
      String key = entry.getKey();
      if (entry.getValue() == null) {
        result.put(key, entry.getValue());
      } else {
        if (StringUtils.equals(key, ABSENCE_ID_KEY) || StringUtils
            .equals(key, DURATION_IN_DAYS_KEY)) {
          result.put(key, new Long((Integer) entry.getValue()));
        } else if (StringUtils.equals(key, START_DATE_KEY) || StringUtils
            .equals(key, END_DATE_KEY)) {
          result.put(key, LocalDate.parse((String) entry.getValue()));
        } else {
          result.put(key, entry.getValue());
        }
      }
    }

    return result;
  }
}
