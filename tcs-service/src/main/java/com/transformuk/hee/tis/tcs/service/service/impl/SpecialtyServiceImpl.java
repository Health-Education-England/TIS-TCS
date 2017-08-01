package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyService;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

  private final SpecialtyMapper specialtyMapper;

  public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
    this.specialtyRepository = specialtyRepository;
    this.specialtyMapper = specialtyMapper;
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
    SpecialtyDTO result = specialtyMapper.specialtyToSpecialtyDTO(specialty);
    return result;
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
    specialty = specialtyRepository.save(specialty);
    List<SpecialtyDTO> result = specialtyMapper.specialtiesToSpecialtyDTOs(specialty);
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<SpecialtyDTO> advancedSearch(
      String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<Specialty>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("college", searchString)).
          or(containsLike("nhsSpecialtyCode", searchString)).
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
    return result.map(specialty -> specialtyMapper.specialtyToSpecialtyDTO(specialty));
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
    Specialty specialty = specialtyRepository.findOne(id);
    SpecialtyDTO specialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(specialty);
    return specialtyDTO;
  }

  /**
   * Delete the  specialty by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Specialty : {}", id);
    specialtyRepository.delete(id);
  }
}
