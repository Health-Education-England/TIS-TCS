package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;

import com.google.common.base.Preconditions;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeCurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Programme.
 */
@Service
@Transactional
public class ProgrammeServiceImpl implements ProgrammeService {

  private final Logger log = LoggerFactory.getLogger(ProgrammeServiceImpl.class);

  private final ProgrammeRepository programmeRepository;
  private final ProgrammeMapper programmeMapper;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final PermissionService permissionService;

  public ProgrammeServiceImpl(ProgrammeRepository programmeRepository,
      ProgrammeCurriculumRepository programmeCurriculumRepository,
      CurriculumRepository curriculumRepository, ProgrammeMapper programmeMapper,
      ApplicationEventPublisher applicationEventPublisher, PermissionService permissionService) {
    this.programmeRepository = programmeRepository;
    this.programmeMapper = programmeMapper;
    this.applicationEventPublisher = applicationEventPublisher;
    this.permissionService = permissionService;
  }

  /**
   * Save a programme
   *
   * @param programmeDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public ProgrammeDTO save(ProgrammeDTO programmeDTO) {
    log.debug("Request to save Programme : {}", programmeDTO);
    Programme programme = programmeMapper.programmeDTOToProgramme(programmeDTO);
    programme = programmeRepository.save(programme);
    ProgrammeDTO programmeDTO1 = programmeMapper.programmeToProgrammeDTO(programme);
    applicationEventPublisher.publishEvent(new ProgrammeCreatedEvent(programmeDTO1));
    return programmeDTO1;
  }

  /**
   * Update a programme
   *
   * @param programmeDTO the entity to update
   * @return the persisted entity
   */
  @Override
  public ProgrammeDTO update(ProgrammeDTO programmeDTO) {
    log.debug("Request to update Programme : {}", programmeDTO);

    Programme programme = programmeMapper.programmeDTOToProgramme(programmeDTO);
    programme = programmeRepository.save(programme);

    ProgrammeDTO programmeDTO1 = programmeMapper.programmeToProgrammeDTO(programme);
    applicationEventPublisher.publishEvent(new ProgrammeSavedEvent(programmeDTO1));
    return programmeDTO1;
  }

  /**
   * Save a list of programmes.
   *
   * @param programmeDtos the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<ProgrammeDTO> save(List<ProgrammeDTO> programmeDtos) {
    log.debug("Request to save {} programmes.", programmeDtos.size());
    List<Programme> programmes = programmeMapper.programmeDTOsToProgrammes(programmeDtos);

    programmes = programmeRepository.saveAll(programmes);
    List<ProgrammeDTO> programmeDTOs = programmeMapper.programmesToProgrammeDTOs(programmes);
    programmeDTOs.stream().distinct().map(ProgrammeSavedEvent::new)
        .forEach(applicationEventPublisher::publishEvent);
    return programmeDTOs;
  }

  /**
   * Get all the programmes.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<ProgrammeDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Programmes");
    Page<Programme> result = null;
    if (permissionService.isProgrammeObserver()) {
      Specification spec = in("id", new ArrayList<>(permissionService.getUsersProgrammeIds()));
      result = programmeRepository.findAll(Specification.where(spec), pageable);
    } else {
      result = programmeRepository.findAll(pageable);
    }
    return result.map(programmeMapper::programmeToProgrammeDTO);
  }

  @Override
  public List<ProgrammeDTO> findByIdIn(Set<Long> ids) {
    log.debug("Request to get Programmes by ID");
    List<Programme> programmes = programmeRepository.findByIdIn(ids);
    return programmeMapper.programmesToProgrammeDTOs(programmes);
  }

  @Override
  public Page<ProgrammeDTO> findAllCurrent(Pageable pageable) {
    log.debug("Request to get all current Programmes");
    Programme example = new Programme().status(Status.CURRENT);
    Page<Programme> result = programmeRepository.findAll(Example.of(example), pageable);
    return result.map(programmeMapper::programmeToProgrammeDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProgrammeDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters,
      Pageable pageable) {

    List<Specification<Programme>> specs = new ArrayList<>();
    // add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("programmeName", searchString))
          .or(containsLike("programmeNumber", searchString)));
    }

    if (permissionService.isProgrammeObserver()) {
      specs.add(in("id", new ArrayList<>(permissionService.getUsersProgrammeIds())));
    }

    // add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }
    Page<Programme> result;
    if (!specs.isEmpty()) {
      Specification<Programme> fullSpec = Specification.where(specs.get(0));
      // add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = programmeRepository.findAll(fullSpec, pageable);
    } else {
      result = programmeRepository.findAll(pageable);
    }

    return result.map(programmeMapper::programmeToProgrammeDTO);
  }

  /**
   * Get one programme by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public ProgrammeDTO findOne(Long id) {
    log.debug("Request to get Programme : {}", id);
    Programme programme = programmeRepository.findProgrammeByIdEagerFetch(id).orElse(null);
    return programmeMapper.programmeToProgrammeDTO(programme);
  }


  @Override
  public List<ProgrammeDTO> findTraineeProgrammes(Long traineeId) {
    Preconditions.checkNotNull(traineeId);
    List<Programme> traineeProgrammes =
        programmeRepository.findByCurriculumMembershipPersonId(traineeId);
    return programmeMapper.programmesToProgrammeDTOs(traineeProgrammes);
  }

  /**
   * Delete the programme by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Programme : {}", id);
    programmeRepository.deleteById(id);
    applicationEventPublisher.publishEvent(new ProgrammeDeletedEvent(id));
  }
}
