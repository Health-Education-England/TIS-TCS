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
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMapper;
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
  private final ProgrammeMembershipMapper programmeMembershipMapper;
  private final CurriculumRepository curriculumRepository;
  private final CurriculumMapper curriculumMapper;
  private final ProgrammeRepository programmeRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final PersonRepository personRepository;

  public ProgrammeMembershipServiceImpl(ProgrammeMembershipRepository programmeMembershipRepository,
      ProgrammeMembershipMapper programmeMembershipMapper,
      CurriculumRepository curriculumRepository, CurriculumMapper curriculumMapper,
      ProgrammeRepository programmeRepository, ApplicationEventPublisher applicationEventPublisher,
      PersonRepository personRepository) {
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.programmeMembershipMapper = programmeMembershipMapper;
    this.curriculumRepository = curriculumRepository;
    this.curriculumMapper = curriculumMapper;
    this.programmeRepository = programmeRepository;
    this.applicationEventPublisher = applicationEventPublisher;
    this.personRepository = personRepository;
  }

  /**
   * Save a programmeMembership.
   *
   * @param programmeMembershipDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public ProgrammeMembershipDTO save(ProgrammeMembershipDTO programmeMembershipDTO) {
    log.debug("Request to save ProgrammeMembership : {}", programmeMembershipDTO);
    List<ProgrammeMembership> programmeMembershipList = programmeMembershipMapper
        .toEntity(programmeMembershipDTO);
    programmeMembershipList = programmeMembershipRepository.saveAll(programmeMembershipList);
    updatePersonWhenStatusIsStale(programmeMembershipList.get(0).getPerson().getId());
    List<ProgrammeMembershipDTO> resultDtos = programmeMembershipMapper
        .programmeMembershipsToProgrammeMembershipDTOs(programmeMembershipList);

    emitProgrammeMembershipSavedEvents(resultDtos);
    return CollectionUtils.isNotEmpty(resultDtos) ? resultDtos.get(0) : null;
  }

  /**
   * Save a list of programmeMembership.
   *
   * @param programmeMembershipDTO the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<ProgrammeMembershipDTO> save(List<ProgrammeMembershipDTO> programmeMembershipDTO) {
    log.debug("Request to save ProgrammeMembership : {}", programmeMembershipDTO);
    List<ProgrammeMembership> programmeMemberships = programmeMembershipMapper
        .programmeMembershipDTOsToProgrammeMemberships(programmeMembershipDTO);
    programmeMemberships = programmeMembershipRepository.saveAll(programmeMemberships);
    updatePersonWhenStatusIsStale(programmeMemberships.get(0).getPerson().getId());
    List<ProgrammeMembershipDTO> programmeMembershipDTOS = programmeMembershipMapper
        .programmeMembershipsToProgrammeMembershipDTOs(programmeMemberships);
    emitProgrammeMembershipSavedEvents(programmeMembershipDTOS);
    return programmeMembershipDTOS;
  }

  private void emitProgrammeMembershipSavedEvents(
      List<ProgrammeMembershipDTO> programmeMembershipDTOS) {
    if (CollectionUtils.isNotEmpty(programmeMembershipDTOS)) {
      programmeMembershipDTOS.stream()
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
    Page<ProgrammeMembership> result = programmeMembershipRepository.findAll(pageable);
    return result.map(programmeMembershipMapper::toDto);
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
    ProgrammeMembership programmeMembership = programmeMembershipRepository.findById(id)
        .orElse(null);
    return programmeMembershipMapper.toDto(programmeMembership);
  }

  /**
   * Delete the  programmeMembership by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete ProgrammeMembership : {}", id);
    //Get the person id from the programme membership before deleting it
    Long personId = programmeMembershipRepository.getOne(id).getPerson().getId();
    programmeMembershipRepository.deleteById(id);
    programmeRepository.flush();
    updatePersonWhenStatusIsStale(personId);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTraineeAndProgramme(
      Long traineeId, Long programmeId) {
    Preconditions.checkNotNull(traineeId);
    Preconditions.checkNotNull(programmeId);

    List<ProgrammeMembership> foundProgrammeMemberships = programmeMembershipRepository
        .findByTraineeIdAndProgrammeId(traineeId, programmeId);
    List<ProgrammeMembershipDTO> programmeMembershipDTOS = programmeMembershipMapper
        .programmeMembershipsToProgrammeMembershipDTOs(foundProgrammeMemberships);

    //get all curriculum ids
    return attachCurricula(programmeMembershipDTOS);
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

    List<ProgrammeMembershipCurriculaDTO> programmeMembershipsForTrainee = findProgrammeMembershipsForTrainee(
        traineeId);

    List<ProgrammeMembershipCurriculaDTO> result = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(programmeMembershipsForTrainee)) {
      for (ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO : programmeMembershipsForTrainee) {
        Optional<ProgrammeMembershipCurriculaDTO> foundPMCOptional = getSameProgrammeMembershipForDates(
            result, programmeMembershipCurriculaDTO);
        if (foundPMCOptional.isPresent()) {
          ProgrammeMembershipCurriculaDTO foundPMC = foundPMCOptional.get();

          List<CurriculumMembershipDTO> curriculumMemberships = Lists.newArrayList();
          if (foundPMC.getCurriculumMemberships() != null) {
            curriculumMemberships.addAll(foundPMC.getCurriculumMemberships());
          }
          //merge the existing curricula memberships with the new ones
          if (programmeMembershipCurriculaDTO.getCurriculumMemberships() != null) {
            curriculumMemberships
                .addAll(programmeMembershipCurriculaDTO.getCurriculumMemberships());
          }

          foundPMC.setCurriculumMemberships(curriculumMemberships);
        } else {
          result.add(programmeMembershipCurriculaDTO);
        }
      }
    }
    return result;
  }

  //Loop through a collection of PMC DTO and if theres one already that matches the programme id, dates and type then return that one
  private Optional<ProgrammeMembershipCurriculaDTO> getSameProgrammeMembershipForDates(
      List<ProgrammeMembershipCurriculaDTO> result,
      ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO) {
    if (CollectionUtils.isNotEmpty(result)) {
      return result.stream()
          .filter(isProgrammeMembershipEffectivelyTheSame(programmeMembershipCurriculaDTO))
          .findAny();
    }
    return Optional.empty();
  }

  private Predicate<ProgrammeMembershipCurriculaDTO> isProgrammeMembershipEffectivelyTheSame(
      ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO) {
    return pmc ->
        Objects.equals(pmc.getProgrammeId(), programmeMembershipCurriculaDTO.getProgrammeId()) &&
            Objects.equals(pmc.getProgrammeMembershipType(),
                programmeMembershipCurriculaDTO.getProgrammeMembershipType()) &&
            Objects.equals(pmc.getProgrammeStartDate(),
                programmeMembershipCurriculaDTO.getProgrammeStartDate()) &&
            Objects.equals(pmc.getProgrammeEndDate(),
                programmeMembershipCurriculaDTO.getProgrammeEndDate());
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTrainee(Long traineeId) {
    Preconditions.checkNotNull(traineeId);

    List<ProgrammeMembership> foundProgrammeMemberships = programmeMembershipRepository
        .findByTraineeId(traineeId);

    if (CollectionUtils.isNotEmpty(foundProgrammeMemberships)) {
      List<ProgrammeMembershipDTO> programmeMembershipDTOS = programmeMembershipMapper
          .allEntityToDto(foundProgrammeMemberships);
      List<ProgrammeMembershipCurriculaDTO> result = attachCurricula(programmeMembershipDTOS);

      //get the programme names and numbers
      Set<Long> programmeIds = foundProgrammeMemberships.stream()
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
      for (ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO : result) {
        Programme programme = programmeIdToProgramme
            .get(programmeMembershipCurriculaDTO.getProgrammeId());
        programmeMembershipCurriculaDTO.setProgrammeName(programme.getProgrammeName());
        programmeMembershipCurriculaDTO.setProgrammeNumber(programme.getProgrammeNumber());
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

    List<ProgrammeMembership> foundProgrammeMemberships = programmeMembershipRepository
        .findByProgrammeId(programmeId);
    return programmeMembershipMapper
        .programmeMembershipsToProgrammeMembershipDTOs(foundProgrammeMemberships);
  }


  private List<ProgrammeMembershipCurriculaDTO> attachCurricula(
      List<ProgrammeMembershipDTO> programmeMembershipDTOS) {
    List<ProgrammeMembershipCurriculaDTO> result = Lists.newArrayList();

    //get all curriculum ids
    Set<Long> curriculumIds = programmeMembershipDTOS.stream().
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
    for (ProgrammeMembershipDTO pm : programmeMembershipDTOS) {
      ProgrammeMembershipCurriculaDTO programmeMembershipCurriculaDTO = new ProgrammeMembershipCurriculaDTO();
      BeanUtils.copyProperties(pm, programmeMembershipCurriculaDTO);
      for (CurriculumMembershipDTO cm : pm.getCurriculumMemberships()) {
        programmeMembershipCurriculaDTO
            .setCurriculumDTO(curriculumDTOMap.get(cm.getCurriculumId()));
      }
      result.add(programmeMembershipCurriculaDTO);
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
}
