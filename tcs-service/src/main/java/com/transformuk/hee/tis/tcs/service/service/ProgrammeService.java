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
     * Save a list of programmes.
     *
     * @param programmeDTO the list of entities to save
     * @return the list of persisted entities
     */
    List<ProgrammeDTO> save(List<ProgrammeDTO> programmeDTO);

	/**
	 * Get all the programmes within the given designated body codes.
	 *
	 * @param dbcs the designated body codes to search through
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<ProgrammeDTO> findAll(Set<String> dbcs, Pageable pageable);

	/**
	 * Get all the programmes within the given designated body codes using the
	 * given smart search string.
	 *
	 * @param dbcs the designated body codes to search through not null
	 * @param searchString the search string to match, can be null
	 * @param columnFilers the exact key value filters to apply, can be null
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<ProgrammeDTO> advancedSearch(
			Set<String> dbcs, String searchString, List<ColumnFilter> columnFilers, Pageable pageable);

	/**
	 * Get the "id" programme.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	ProgrammeDTO findOne(Long id);

	/**
	 * Delete the "id" programme.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
