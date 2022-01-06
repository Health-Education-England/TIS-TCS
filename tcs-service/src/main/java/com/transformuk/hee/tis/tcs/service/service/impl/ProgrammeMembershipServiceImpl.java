package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing ProgrammeMembership.
 */
@Service
@Transactional
public class ProgrammeMembershipServiceImpl implements ProgrammeMembershipService {

  private final Logger log = LoggerFactory.getLogger(ProgrammeMembershipServiceImpl.class);

  private final ProgrammeMembershipRepository programmeMembershipRepository;
  private final CurriculumMembershipRepository curriculumMembershipRepository;
  private final ProgrammeMembershipMapper programmeMembershipMapper;
  private final CurriculumMembershipMapper curriculumMembershipMapper;
  private final CurriculumRepository curriculumRepository;
  private final CurriculumMapper curriculumMapper;
  private final ProgrammeRepository programmeRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final PersonRepository personRepository;

  public ProgrammeMembershipServiceImpl(ProgrammeMembershipRepository programmeMembershipRepository,
      CurriculumMembershipRepository curriculumMembershipRepository,
      ProgrammeMembershipMapper programmeMembershipMapper,
      CurriculumMembershipMapper curriculumMembershipMapper,
      CurriculumRepository curriculumRepository, CurriculumMapper curriculumMapper,
      ProgrammeRepository programmeRepository, ApplicationEventPublisher applicationEventPublisher,
      PersonRepository personRepository) {
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.curriculumMembershipRepository = curriculumMembershipRepository;
    this.programmeMembershipMapper = programmeMembershipMapper;
    this.curriculumMembershipMapper = curriculumMembershipMapper;
    this.curriculumRepository = curriculumRepository;
    this.curriculumMapper = curriculumMapper;
    this.programmeRepository = programmeRepository;
    this.applicationEventPublisher = applicationEventPublisher;
    this.personRepository = personRepository;
  }

  /**
   * Save a programmeMembership.
   *
   * @param programmeMembershipDto the entity to save
   * @return the persisted entity
   */
  @Override
  public ProgrammeMembershipDTO save(ProgrammeMembershipDTO programmeMembershipDto) {
    log.debug("Request to save ProgrammeMembership : {}", programmeMembershipDto);

    //new
    List<CurriculumMembership> curriculumMembershipList = curriculumMembershipMapper
        .toEntity(programmeMembershipDto);
    curriculumMembershipList = curriculumMembershipRepository.saveAll(curriculumMembershipList);

    //below is deprecated
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipMapper
        .toEntity(programmeMembershipDto);
    programmeMembershipList.forEach(this::setProgrammeMembershipAmendedDateFromDbRecord);
    programmeMembershipRepository.saveAll(programmeMembershipList);

    //emit events
    updatePersonWhenStatusIsStale(curriculumMembershipList.get(0).getPerson().getId());
    List<ProgrammeMembershipDTO> resultDtos = curriculumMembershipMapper
        .curriculumMembershipsToProgrammeMembershipDtos(curriculumMembershipList);

    emitProgrammeMembershipSavedEvents(resultDtos);
    return CollectionUtils.isNotEmpty(resultDtos) ? resultDtos.get(0) : null;
  }

  /**
   * Save a list of programmeMembership.
   *
   * @param programmeMembershipDto the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<ProgrammeMembershipDTO> save(List<ProgrammeMembershipDTO> programmeMembershipDto) {
    log.debug("Request to save ProgrammeMembership : {}", programmeMembershipDto);

    //new
    List<CurriculumMembership> curriculumMemberships = curriculumMembershipMapper
        .programmeMembershipDtosToCurriculumMemberships(programmeMembershipDto);
    curriculumMemberships = curriculumMembershipRepository.saveAll(curriculumMemberships);

    //below is deprecated
    List<ProgrammeMembership> programmeMemberships = programmeMembershipMapper
        .programmeMembershipDTOsToProgrammeMemberships(programmeMembershipDto);
    programmeMemberships.forEach(this::setProgrammeMembershipAmendedDateFromDbRecord);
    programmeMembershipRepository.saveAll(programmeMemberships);

    //emit events
    updatePersonWhenStatusIsStale(curriculumMemberships.get(0).getPerson().getId());
    List<ProgrammeMembershipDTO> programmeMembershipDtos = curriculumMembershipMapper
        .curriculumMembershipsToProgrammeMembershipDtos(curriculumMemberships);
    emitProgrammeMembershipSavedEvents(programmeMembershipDtos);
    return programmeMembershipDtos;
  }

  private void emitProgrammeMembershipSavedEvents(
      List<ProgrammeMembershipDTO> programmeMembershipDtos) {
    if (CollectionUtils.isNotEmpty(programmeMembershipDtos)) {
      programmeMembershipDtos.stream()
          .distinct()
          .map(ProgrammeMembershipSavedEvent::new)
          .forEach(applicationEventPublisher::publishEvent);
    }
  }

  /**
   * Get all the programmeMemberships.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<ProgrammeMembershipDTO> findAll(Pageable pageable) {
    log.debug("Request to get all ProgrammeMemberships");
    Page<CurriculumMembership> result = curriculumMembershipRepository.findAll(pageable);
    return result.map(curriculumMembershipMapper::toDto);
  }

  /**
   * Get one programmeMembership by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public ProgrammeMembershipDTO findOne(Long id) {
    log.debug("Request to get ProgrammeMembership : {}", id);
    CurriculumMembership curriculumMembership = curriculumMembershipRepository.findById(id)
        .orElse(null);
    return curriculumMembershipMapper.toDto(curriculumMembership);
  }

  /**
   * Delete the  programmeMembership by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete ProgrammeMembership : {}", id);
    //below is deprecated
    programmeMembershipRepository.deleteById(id);
    programmeRepository.flush();

    //Get the person id from the programme membership before deleting it
    Long personId = curriculumMembershipRepository.getOne(id).getPerson().getId();
    //new
    curriculumMembershipRepository.deleteById(id);
    curriculumRepository.flush();

    updatePersonWhenStatusIsStale(personId);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTraineeAndProgramme(
      Long traineeId, Long programmeId) {
    Preconditions.checkNotNull(traineeId);
    Preconditions.checkNotNull(programmeId);

    List<CurriculumMembership> foundCurriculumMemberships = curriculumMembershipRepository
        .findByTraineeIdAndProgrammeId(traineeId, programmeId);
    List<ProgrammeMembershipDTO> programmeMembershipDtos = curriculumMembershipMapper
        .curriculumMembershipsToProgrammeMembershipDtos(foundCurriculumMemberships);

    //get all curriculum ids
    return attachCurricula(programmeMembershipDtos);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipDetailsByIds(
      Set<Long> ids) {
    List<CurriculumMembership> curriculumMemberships
        = curriculumMembershipRepository.findByIdIn(ids);
    List<ProgrammeMembershipDTO> programmeMembershipDtos
        = curriculumMembershipMapper.allEntityToDto(curriculumMemberships);

    return attachCurricula(programmeMembershipDtos);
  }

  /**
   * Method just like the find programme memberships for trainee but rolls up the programme (group
   * by) and also attaches all the curricula on those rolled up programmes into a single programme
   * <p>
   * The initial thoughts around the attachment of curricula was that we use a Set collection as
   * this would then remove all duplicates. This was found to be an issue as the CurriculaDTO
   * equals/hashcode methods are scenario specific code. So we're just using a list now
   *
   * @param traineeId Trainee id
   */
  @Transactional(readOnly = true)
  @Override
  public List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTraineeRolledUp(
      Long traineeId) {
    Preconditions.checkNotNull(traineeId);

    List<ProgrammeMembershipCurriculaDTO> programmeMembershipsForTrainee
        = findProgrammeMembershipsForTrainee(traineeId);

    List<ProgrammeMembershipCurriculaDTO> result = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(programmeMembershipsForTrainee)) {
      for (ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDto
          : programmeMembershipsForTrainee) {
        Optional<ProgrammeMembershipCurriculaDTO> foundPmcOptional
            = getSameProgrammeMembershipForDates(result, programmeMembershipCurriculaDto);
        if (foundPmcOptional.isPresent()) {
          ProgrammeMembershipCurriculaDTO foundPmc = foundPmcOptional.get();

          List<CurriculumMembershipDTO> curriculumMemberships = Lists.newArrayList();
          if (foundPmc.getCurriculumMemberships() != null) {
            curriculumMemberships.addAll(foundPmc.getCurriculumMemberships());
          }
          //merge the existing curricula memberships with the new ones
          if (programmeMembershipCurriculaDto.getCurriculumMemberships() != null) {
            curriculumMemberships
                .addAll(programmeMembershipCurriculaDto.getCurriculumMemberships());
          }

          foundPmc.setCurriculumMemberships(curriculumMemberships);
        } else {
          result.add(programmeMembershipCurriculaDto);
        }
      }
    }
    return result;
  }

  //Loop through a collection of PMC DTO and if theres one already that matches
  // the programme id, dates and type then return that one
  private Optional<ProgrammeMembershipCurriculaDTO> getSameProgrammeMembershipForDates(
      List<ProgrammeMembershipCurriculaDTO> result,
      ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDto) {
    if (CollectionUtils.isNotEmpty(result)) {
      return result.stream()
          .filter(isProgrammeMembershipEffectivelyTheSame(programmeMembershipCurriculaDto))
          .findAny();
    }
    return Optional.empty();
  }

  private Predicate<ProgrammeMembershipCurriculaDTO> isProgrammeMembershipEffectivelyTheSame(
      ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDto) {
    return pmc ->
        Objects.equals(pmc.getProgrammeId(), programmeMembershipCurriculaDto.getProgrammeId())
            && Objects.equals(pmc.getProgrammeMembershipType(),
                programmeMembershipCurriculaDto.getProgrammeMembershipType())
            && Objects.equals(pmc.getProgrammeStartDate(),
                programmeMembershipCurriculaDto.getProgrammeStartDate())
            && Objects.equals(pmc.getProgrammeEndDate(),
                programmeMembershipCurriculaDto.getProgrammeEndDate());
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTrainee(Long traineeId) {
    Preconditions.checkNotNull(traineeId);

    List<CurriculumMembership> foundCurriculumMemberships = curriculumMembershipRepository
        .findByTraineeId(traineeId);

    if (CollectionUtils.isNotEmpty(foundCurriculumMemberships)) {
      List<ProgrammeMembershipDTO> programmeMembershipDtos = curriculumMembershipMapper
          .allEntityToDto(foundCurriculumMemberships);
      List<ProgrammeMembershipCurriculaDTO> result = attachCurricula(programmeMembershipDtos);

      //get the programme names and numbers
      Set<Long> programmeIds = foundCurriculumMemberships.stream()
          .filter(pm -> Objects.nonNull(pm.getProgramme()))
          .map(pm -> pm.getProgramme().getId())
          .collect(Collectors.toSet());

      List<Programme> programmesById = programmeRepository.findByIdIn(programmeIds);
      Map<Long, Programme> programmeIdToProgramme = Maps.newHashMap();
      if (CollectionUtils.isNotEmpty(programmesById)) {
        programmeIdToProgramme = programmesById.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Programme::getId, p -> p));
      }
      for (ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDto : result) {
        Programme programme = programmeIdToProgramme
            .get(programmeMembershipCurriculaDto.getProgrammeId());
        programmeMembershipCurriculaDto.setProgrammeName(programme.getProgrammeName());
        programmeMembershipCurriculaDto.setProgrammeNumber(programme.getProgrammeNumber());
      }
      return result;
    }
    return Collections.emptyList();
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProgrammeMembershipDTO> findProgrammeMembershipsByProgramme(
      Long programmeId) {
    Preconditions.checkNotNull(programmeId);

    List<CurriculumMembership> foundCurriculumMemberships = curriculumMembershipRepository
        .findByProgrammeId(programmeId);
    return curriculumMembershipMapper
        .curriculumMembershipsToProgrammeMembershipDtos(foundCurriculumMemberships);
  }

  private List<ProgrammeMembershipCurriculaDTO> attachCurricula(
      List<ProgrammeMembershipDTO> programmeMembershipDtos) {
    List<ProgrammeMembershipCurriculaDTO> result = Lists.newArrayList();

    //get all curriculum ids
    Set<Long> curriculumIds = programmeMembershipDtos.stream().
        map(ProgrammeMembershipDTO::getCurriculumMemberships).
        flatMap(Collection::stream).
        map(CurriculumMembershipDTO::getCurriculumId).
        collect(Collectors.toSet());

    Map<Long, CurriculumDTO> curriculumDTOMap = Maps.newHashMap();
    if (CollectionUtils.isNotEmpty(curriculumIds)) {
      List<Curriculum> all = curriculumRepository.findAllById(curriculumIds);
      curriculumDTOMap = all.stream().collect(
          Collectors.toMap(Curriculum::getId, curriculumMapper::curriculumToCurriculumDTO));
    }

    //attach the curriculum data to the programme membership
    for (ProgrammeMembershipDTO pm : programmeMembershipDtos) {
      ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDto
          = new ProgrammeMembershipCurriculaDTO();
      BeanUtils.copyProperties(pm, programmeMembershipCurriculaDto);
      for (CurriculumMembershipDTO cm : pm.getCurriculumMemberships()) {
        programmeMembershipCurriculaDto
            .setCurriculumDTO(curriculumDTOMap.get(cm.getCurriculumId()));
      }
      result.add(programmeMembershipCurriculaDto);
    }
    return result;
  }

  private void updatePersonWhenStatusIsStale(Long personId) {
    Person person = personRepository.getOne(personId);
    Status newStatus = person.programmeMembershipsStatus();
    log.debug("person id:{} was {} and re-evaluated is {}.", personId, person.getStatus(),
        newStatus);
    if (!newStatus.equals(person.getStatus())) {
      person.setStatus(newStatus);
      personRepository.save(person);
    }
  }

  /**
   * Set a ProgrammeMembership AmendedDate from its database record.
   *
   * <p>The amendedDate of the programmeMembershipDto which is being saved is based on the entry
   * in the CurriculumMembership table, which will have a slightly different AmendedDate value
   * from the record in the ProgrammeMembership table. Since this field is used for @Version
   * optimistic locking, we need the actual value from the ProgrammeMembership table otherwise we
   * will get 'You are acting on stale data, please refresh' concurrency failures.
   *
   * <p>Caution: by overwriting the AmendedDate, it is possible to handle concurrent updates to the
   * same ProgrammeMembership incorrectly: for example, if record A is updated to A' and this is
   * committed, and then update B (which was based on A not A') is committed, this should fail as
   * B needs to be based on A', otherwise the A' update is lost. To avoid this, apply the
   * ProgrammeMembership record Save after the parallel CurriculumMembership Save in the same
   * code block: in the event of stale data (the B update in the example above) the
   * CurriculumMembership Save will fail and throw an exception before the incorrect
   * ProgrammeMembership Save is applied.
   *
   * @param pm the ProgrammeMembership to set the amended date for
   * @return the updated ProgrammeMembership
   */
  private ProgrammeMembership setProgrammeMembershipAmendedDateFromDbRecord(
      ProgrammeMembership pm) {
    if (pm.getId() != null) {
      Optional<ProgrammeMembership> programmeMembership
          = programmeMembershipRepository.findById(pm.getId());
      if (programmeMembership.isPresent()) {
        pm.setAmendedDate(programmeMembership.get().getAmendedDate());
      } else {
        log.error("ProgrammeMembership record missing: {}", pm.getId());
      }
    }
    return pm;
  }
}
