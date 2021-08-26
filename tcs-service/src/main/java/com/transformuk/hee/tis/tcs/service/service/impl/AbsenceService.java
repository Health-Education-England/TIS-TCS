package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.model.Absence;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.AbsenceRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.AbsenceMapper;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AbsenceService {

  private static final String ABSENCE_ID_KEY = "id";
  private static final String ABSENCE_ATTENDANCE_ID_KEY = "absenceAttendanceId";

  @Autowired
  private AbsenceMapper absenceMapper;
  @Autowired
  private AbsenceRepository absenceRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private EntityManager entityManager;

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

  public Optional<AbsenceDTO> patchAbsence(Map<String, Object> absenceDtoPatchMap) {
    Preconditions.checkArgument(absenceDtoPatchMap != null, "absenceDto cannot be null");

    Optional<Absence> optionalAbsence = Optional.empty();
    if (absenceDtoPatchMap.containsKey(ABSENCE_ID_KEY)) {
      long id = ((Integer) absenceDtoPatchMap.get(ABSENCE_ID_KEY)).longValue();
      optionalAbsence = absenceRepository.findById(id);
    } else if (absenceDtoPatchMap.containsKey(ABSENCE_ATTENDANCE_ID_KEY)) {
      optionalAbsence = absenceRepository
          .findByAbsenceAttendanceId((String) absenceDtoPatchMap.get(ABSENCE_ATTENDANCE_ID_KEY));
    }

    if (!optionalAbsence.isPresent()) {
      return Optional.empty();
    }
    Absence foundAbsence = optionalAbsence.get();
    entityManager.detach(foundAbsence); //must detach as changes to the entity while attached wont
    //trigger a version check, even if we change the version
    foundAbsence.setAmendedDate(null);

    absenceMapper.patch(absenceDtoPatchMap, foundAbsence);
    return Optional.of(absenceMapper.toDto(absenceRepository.saveAndFlush(foundAbsence)));
  }
}
