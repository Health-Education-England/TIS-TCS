package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipSummaryDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.validation.ProgrammeMembershipValidator;
import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.CurriculumMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.TrainingNumberService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipSummaryDtoMapper;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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
  private final ApplicationEventPublisher applicationEventPublisher;
  private final PersonRepository personRepository;
  private final ProgrammeMembershipValidator programmeMembershipValidator;
  private final ProgrammeMembershipDtoMapper programmeMembershipDtoMapper;
  private final TrainingNumberService trainingNumberService;

  /**
   * Initialise the ProgrammeMembershipService.
   *
   * @param programmeMembershipRepository  the ProgrammeMembershipRepository
   * @param curriculumMembershipRepository the CurriculumMembershipRepository
   * @param programmeMembershipMapper      the ProgrammeMembershipMapper
   * @param curriculumMembershipMapper     the CurriculumMembershipMapper
   * @param curriculumRepository           the CurriculumRepository
   * @param curriculumMapper               the CurriculumMapper
   * @param applicationEventPublisher      the ApplicationEventPublisher
   * @param personRepository               the PersonRepository
   * @param programmeMembershipValidator   the ProgrammeMembershipValidator
   * @param programmeMembershipDtoMapper   the ProgrammeMembershipDtoMapper
   */
  public ProgrammeMembershipServiceImpl(
      ProgrammeMembershipRepository programmeMembershipRepository,
      CurriculumMembershipRepository curriculumMembershipRepository,
      ProgrammeMembershipMapper programmeMembershipMapper,
      CurriculumMembershipMapper curriculumMembershipMapper,
      CurriculumRepository curriculumRepository, CurriculumMapper curriculumMapper,
      ApplicationEventPublisher applicationEventPublisher,
      PersonRepository personRepository,
      ProgrammeMembershipValidator programmeMembershipValidator,
      ProgrammeMembershipDtoMapper programmeMembershipDtoMapper,
      TrainingNumberService trainingNumberService) {
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.curriculumMembershipRepository = curriculumMembershipRepository;
    this.programmeMembershipMapper = programmeMembershipMapper;
    this.curriculumMembershipMapper = curriculumMembershipMapper;
    this.curriculumRepository = curriculumRepository;
    this.curriculumMapper = curriculumMapper;
    this.applicationEventPublisher = applicationEventPublisher;
    this.personRepository = personRepository;
    this.programmeMembershipValidator = programmeMembershipValidator;
    this.programmeMembershipDtoMapper = programmeMembershipDtoMapper;
    this.trainingNumberService = trainingNumberService;
  }

  /**
   * Save a programmeMembership and its curriculum memberships.
   *
   * @param programmeMembershipDto the entity to save
   * @return the persisted entity
   */
  @Override
  public ProgrammeMembershipDTO save(ProgrammeMembershipDTO programmeMembershipDto) {
    log.debug("Request to save ProgrammeMembership : {}", programmeMembershipDto);

    ProgrammeMembership programmeMembership
        = programmeMembershipMapper.toEntity(programmeMembershipDto);

    if (programmeMembershipDto.getCurriculumMemberships() != null) {
      for (CurriculumMembershipDTO cmDto : programmeMembershipDto.getCurriculumMemberships()) {
        if (cmDto.getId() != null) {
          CurriculumMembership existingCm = curriculumMembershipRepository.findById(cmDto.getId())
              .orElseThrow(() -> new RuntimeException("Curriculum membership not found: " + cmDto.getId()));

          // check if this CM is already attached to another ProgrammeMembership
          Optional<ProgrammeMembership> existingPmOpt = programmeMembershipRepository.findByCurriculumMembershipId(existingCm.getId());
          if (existingPmOpt.isPresent()) {
            ProgrammeMembership existingPm = existingPmOpt.get();

            // if the existing PM is different from the one we're trying to save, throw error
            if (programmeMembershipDto.getUuid() == null ||
                !programmeMembershipDto.getUuid().equals(existingPm.getUuid())) {
              throw new RuntimeException("Curriculum membership already assigned. Please reload the page.");
            }
          }

          // attach the managed CM entity to the programme membership
          programmeMembership.getCurriculumMemberships().add(existingCm);
        }
      }
    }

    programmeMembership = programmeMembershipRepository.save(programmeMembership);

    ProgrammeMembershipDTO programmeMembershipSavedDto
        = programmeMembershipMapper.toDto(programmeMembership);

    // update Person status
    updatePersonWhenStatusIsStale(programmeMembershipSavedDto.getPerson().getId());
    //emit events
    emitProgrammeMembershipSavedEvents(Collections.singletonList(programmeMembershipSavedDto));

    return programmeMembershipSavedDto;
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

    List<ProgrammeMembership> programmeMemberships =
        programmeMembershipMapper
            .programmeMembershipDTOsToProgrammeMemberships(programmeMembershipDto);
    programmeMemberships.forEach(programmeMembership -> {
      programmeMembership = programmeMembershipRepository.save(programmeMembership);
      updatePersonWhenStatusIsStale(programmeMembership.getPerson().getId());
    });

    //emit events
    List<ProgrammeMembershipDTO> resultDtos =
        programmeMembershipMapper
            .programmeMembershipsToProgrammeMembershipDTOs(programmeMemberships);
    emitProgrammeMembershipSavedEvents(resultDtos);
    return resultDtos;
  }

  private void emitProgrammeMembershipSavedEvents(
      List<ProgrammeMembershipDTO> programmeMembershipDtos) {
    if (CollectionUtils.isNotEmpty(programmeMembershipDtos)) {
      programmeMembershipDtos.stream()
          .distinct()
          .map(CurriculumMembershipSavedEvent::new)
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
   * Get one programmeMembership by uuid.
   *
   * @param uuid the uuid of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public ProgrammeMembershipDTO findOne(UUID uuid) {
    log.debug("Request to get ProgrammeMembership : {}", uuid);
    ProgrammeMembership programmeMembership = programmeMembershipRepository.findByUuid(uuid)
        .orElse(null);
    return programmeMembershipMapper.toDto(programmeMembership);
  }

  /**
   * Get programmeMemberships for a set of UUIDs.
   *
   * @param uuids the list of uuids for the programme memberships.
   * @return the list of programme membership summary DTOs
   */
  @Transactional(readOnly = true)
  @Override
  public List<ProgrammeMembershipSummaryDTO> findProgrammeMembershipSummariesByUuid(
      Set<UUID> uuids) {
    List<ProgrammeMembershipDTO> programmeMembershipDtos = programmeMembershipMapper.allEntityToDto(
        programmeMembershipRepository.findByUuidIn(uuids));

    return programmeMembershipDtos.stream()
        .map(dto -> ProgrammeMembershipSummaryDtoMapper.INSTANCE
            .toSummaryDTO(dto.getUuid().toString(), dto))
        .collect(Collectors.toList());
  }

  /**
   * Delete the curriculumMembership by id. NOTE: this is the curriculumMembership, not the
   * containing programmeMembership
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete CurriculumMembership : {}", id);

    //Get the person id from the programme membership before deleting it
    ProgrammeMembership programmeMembership = curriculumMembershipRepository.getOne(id)
        .getProgrammeMembership();
    Long personId = programmeMembership.getPerson().getId();

    programmeMembership.getCurriculumMemberships().removeIf(cm -> cm.getId().equals(id));

    if (!programmeMembership.getCurriculumMemberships().isEmpty()) {
      programmeMembershipRepository.save(programmeMembership);
    } else {
      programmeMembershipRepository.delete(programmeMembership);
    }

    updatePersonWhenStatusIsStale(personId);
    applicationEventPublisher.publishEvent(
        new CurriculumMembershipDeletedEvent(programmeMembershipMapper.toDto(programmeMembership)));
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

    List<ProgrammeMembership> foundProgrammeMemberships = programmeMembershipRepository
        .findByTraineeId(traineeId);

    if (CollectionUtils.isNotEmpty(foundProgrammeMemberships)) {
      trainingNumberService.populateTrainingNumbers(foundProgrammeMemberships);
      List<ProgrammeMembershipDTO> programmeMembershipDtos = programmeMembershipMapper
          .allEntityToDto(foundProgrammeMemberships);
      return attachCurricula(programmeMembershipDtos);
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

    return programmeMembershipMapper.allEntityToDto(foundProgrammeMemberships);
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
   * patch a programme membership.
   *
   * @param programmeMembershipDto the dto to patch
   * @return the patched dto
   */
  public ProgrammeMembershipDTO patch(ProgrammeMembershipDTO programmeMembershipDto) {
    ProgrammeMembershipDTO programmeMembershipDtoFromDb = findOne(programmeMembershipDto.getUuid());
    if (programmeMembershipDtoFromDb == null) {
      programmeMembershipDto.addMessage("Programme membership id not found.");
      return programmeMembershipDto;
    }
    programmeMembershipDtoMapper.copyIfNotNull(programmeMembershipDto,
        programmeMembershipDtoFromDb);
    programmeMembershipValidator.validateForBulk(programmeMembershipDtoFromDb);

    ProgrammeMembershipDTO returnDto;
    if (programmeMembershipDtoFromDb.getMessageList().isEmpty()) {
      returnDto = save(programmeMembershipDtoFromDb);
    } else {
      programmeMembershipDto.setMessageList(programmeMembershipDtoFromDb.getMessageList());
      returnDto = programmeMembershipDto;
    }
    return returnDto;
  }
}
