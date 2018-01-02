package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMapper;
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
 * Service Implementation for managing Programme.
 */
@Service
@Transactional
public class ProgrammeServiceImpl implements ProgrammeService {

  private final Logger log = LoggerFactory.getLogger(ProgrammeServiceImpl.class);

  private final ProgrammeRepository programmeRepository;

  private final ProgrammeMapper programmeMapper;

  public ProgrammeServiceImpl(ProgrammeRepository programmeRepository, ProgrammeMapper programmeMapper) {
    this.programmeRepository = programmeRepository;
    this.programmeMapper = programmeMapper;
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
    return programmeMapper.programmeToProgrammeDTO(programme);
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
    return programmeMapper.programmeToProgrammeDTO(programme);
  }

  /**
   * Save a list of programmes.
   *
   * @param programmeDTO the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<ProgrammeDTO> save(List<ProgrammeDTO> programmeDTO) {
    log.debug("Request to save Programme : {}", programmeDTO);
    List<Programme> programme = programmeMapper.programmeDTOsToProgrammes(programmeDTO);
    programme = programmeRepository.save(programme);
    return programmeMapper.programmesToProgrammeDTOs(programme);
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

    Page<Programme> result = programmeRepository.findAll(pageable);
    return result.map(programmeMapper::programmeToProgrammeDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProgrammeDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<Programme>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("programmeName", searchString)).
          or(containsLike("owner", searchString)).
          or(containsLike("programmeNumber", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }
    Page<Programme> result;
    if (!specs.isEmpty()) {
      Specifications<Programme> fullSpec = Specifications.where(specs.get(0));
      //add the rest of the specs that made it in
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
    Programme programme = programmeRepository.findOne(id);
    return programmeMapper.programmeToProgrammeDTO(programme);
  }

  /**
   * Delete the  programme by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Programme : {}", id);
    programmeRepository.delete(id);
  }
}
