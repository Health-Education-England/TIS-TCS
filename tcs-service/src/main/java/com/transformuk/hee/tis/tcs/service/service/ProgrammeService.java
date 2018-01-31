package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing Programme.
 */
public interface ProgrammeService {

  /**
   * Save a programme.
   *
   * @param programmeDTO the entity to save
   * @return the persisted entity
   */
  ProgrammeDTO save(ProgrammeDTO programmeDTO);

  /**
   * Update a programme.
   *
   * @param programmeDTO the entity to update
   * @return the persisted entity
   */
  ProgrammeDTO update(ProgrammeDTO programmeDTO);

  /**
   * Save a list of programmes.
   *
   * @param programmeDTO the list of entities to save
   * @return the list of persisted entities
   */
  List<ProgrammeDTO> save(List<ProgrammeDTO> programmeDTO);

  /**
   * Get all the programmes.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<ProgrammeDTO> findAll(Pageable pageable);

  /**
   * Get all the programmes.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<ProgrammeDTO> findAllCurrent(Pageable pageable);

  /**
   * Get all the programmes within the given designated body codes using the
   * given smart search string.
   *
   * @param searchString the search string to match, can be null
   * @param columnFilers the exact key value filters to apply, can be null
   * @param pageable     the pagination information
   * @return the list of entities
   */
  Page<ProgrammeDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilers, Pageable pageable, boolean currentStatus);

  /**
   * Get the "id" programme.
   *
   * @param id the id of the entity
   * @return the entity
   */
  ProgrammeDTO findOne(Long id);

  /**
   * Find a list of programmes that a trainee has enrolled to
   * @param traineeId
   * @return
   */
  List<ProgrammeDTO> findTraineeProgrammes(Long traineeId);

  /**
   * Delete the "id" programme.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
