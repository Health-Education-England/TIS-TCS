package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtySimpleDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.event.SpecialtyDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.SpecialtySavedEvent;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtySimple;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtySimpleRepository;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyService;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtySimpleMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;

/**
 * Service Implementation for managing Specialty.
 */
@Service
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService {

  private final Logger log = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

  private final SpecialtyRepository specialtyRepository;
  private final SpecialtySimpleRepository specialtySimpleRepository;
  private final SpecialtyMapper specialtyMapper;
  private final SpecialtySimpleMapper specialtySimpleMapper;
  private final ApplicationEventPublisher applicationEventPublisher;

  public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper,
                              SpecialtySimpleRepository specialtySimpleRepository,
                              SpecialtySimpleMapper specialtySimpleMapper, ApplicationEventPublisher applicationEventPublisher) {
    this.specialtyRepository = specialtyRepository;
    this.specialtyMapper = specialtyMapper;
    this.specialtySimpleRepository = specialtySimpleRepository;
    this.specialtySimpleMapper = specialtySimpleMapper;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  /**
   * Save a specialty.
   * <p>
   * Specialties have a default status value of @see com.transformuk.hee.tis.tcs.api.enumeration.Status#CURRENT
   *
   * @param specialtyDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public SpecialtyDTO save(SpecialtyDTO specialtyDTO) {
    log.debug("Request to save Specialty : {}", specialtyDTO);
    Specialty specialty = specialtyMapper.specialtyDTOToSpecialty(specialtyDTO);
    if (specialty.getStatus() == null) {
      specialty.setStatus(Status.CURRENT);
    }
    specialty = specialtyRepository.save(specialty);
    SpecialtyDTO specialtyDTO1 = specialtyMapper.specialtyToSpecialtyDTO(specialty);

    applicationEventPublisher.publishEvent(new SpecialtySavedEvent(specialtyDTO1));

    return specialtyDTO1;
  }

  /**
   * Save a list of specialties.
   *
   * @param specialtyDTO the entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<SpecialtyDTO> save(List<SpecialtyDTO> specialtyDTO) {
    log.debug("Request to save Specialties : {}", specialtyDTO);
    List<Specialty> specialty = specialtyMapper.specialtyDTOsToSpecialties(specialtyDTO);
    specialty = specialtyRepository.saveAll(specialty);
    List<SpecialtyDTO> specialtyDTOS = specialtyMapper.specialtiesToSpecialtyDTOs(specialty);

    specialtyDTO.stream()
        .map(SpecialtySavedEvent::new)
        .forEach(applicationEventPublisher::publishEvent);

    return specialtyDTOS;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SpecialtyDTO> advancedSearch(
      String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<Specialty>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("college", searchString)).
          or(containsLike("specialtyCode", searchString)).
          or(containsLike("name", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }
    Specifications<Specialty> fullSpec = Specifications.where(specs.get(0));
    //add the rest of the specs that made it in
    for (int i = 1; i < specs.size(); i++) {
      fullSpec = fullSpec.and(specs.get(i));
    }
    Page<Specialty> result = specialtyRepository.findAll(fullSpec, pageable);

    return result.map(specialty -> specialtyMapper.specialtyToSpecialtyDTO(specialty));
  }

  /**
   * Get all the specialties.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<SpecialtyDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Specialties");
    Page<Specialty> result = specialtyRepository.findAll(pageable);
    return result.map(specialtyMapper::specialtyToSpecialtyDTO);
  }

  /**
   * Looks for specialties given their ID's
   *
   * @param ids the id's to look for
   * @return the list of specialties found
   */
  @Override
  @Transactional(readOnly = true)
  public List<SpecialtySimpleDTO> findByIdIn(List<Long> ids) {
    log.debug("Request to get all Specialties");
    List<SpecialtySimple> result = specialtySimpleRepository.findByIdIn(ids);
    return specialtySimpleMapper.specialtiesSimpleToSpecialtyDTOs(result);
  }

  /**
   * Get one specialty by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public SpecialtyDTO findOne(Long id) {
    log.debug("Request to get Specialty : {}", id);
    Specialty specialty = specialtyRepository.findById(id).orElse(null);
    return specialtyMapper.specialtyToSpecialtyDTO(specialty);
  }

  /**
   * Delete the  specialty by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Specialty : {}", id);
    specialtyRepository.deleteById(id);
    applicationEventPublisher.publishEvent(new SpecialtyDeletedEvent(id));
  }

  public Page<SpecialtyDTO> getPagedSpecialtiesForProgrammeId(Long programmeId, String searchQuery, Pageable pageable) {
    Preconditions.checkNotNull(programmeId, "programmeId cannot be null");
    Preconditions.checkNotNull(pageable, "pageable cannot be null");

    Page<Specialty> foundSpecialties;
    if(StringUtils.isEmpty(searchQuery)) {
      foundSpecialties = specialtyRepository.findSpecialtyDistinctByCurriculaProgrammesIdAndStatusIs(programmeId, Status.CURRENT, pageable);
    } else {
      foundSpecialties = specialtyRepository.findSpecialtyDistinctByCurriculaProgrammesIdAndNameContainingIgnoreCaseAndStatusIs(programmeId, searchQuery, Status.CURRENT, pageable);
    }
    List<SpecialtyDTO> specialtyDTOS = specialtyMapper.specialtiesToSpecialtyDTOs(Lists.newArrayList(foundSpecialties));
    return new PageImpl<>(specialtyDTOS, pageable, foundSpecialties.getTotalElements());
  }

  @Override
  public List<SpecialtyDTO> getSpecialtiesForProgrammeAndPerson(Long programmeId, Long personId) {
    Preconditions.checkNotNull(programmeId, "Programme id cannot be null");
    Preconditions.checkNotNull(personId, "Person id cannot be null");

    List<Specialty> specialties = specialtyRepository.findDistinctByProgrammeIdAndPersonId(programmeId, personId);
    return specialtyMapper.specialtiesToSpecialtyDTOs(specialties);
  }
}
