package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtySimpleDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Specialty.
 */
public interface SpecialtyService {

  /**
   * Save a specialty.
   *
   * @param specialtyDTO the entity to save
   * @return the persisted entity
   */
  SpecialtyDTO save(SpecialtyDTO specialtyDTO);

  /**
   * Save a list of specialties.
   *
   * @param specialtyDTO the entities to save
   * @return the list of persisted entities
   */
  List<SpecialtyDTO> save(List<SpecialtyDTO> specialtyDTO);

  /**
   * Get all the specialties within the given smart search string.
   *
   * @param searchString the search string to match, can be null
   * @param columnFilers the exact key value filters to apply, can be null
   * @param pageable     the pagination information
   * @return the list of entities
   */
  Page<SpecialtyDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilers,
      Pageable pageable);

  /**
   * Get all the specialties.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<SpecialtyDTO> findAll(Pageable pageable);


  /**
   * Looks for specialties given their ID's
   *
   * @param ids the id's to look for
   * @return the list of specialties found
   */
  List<SpecialtySimpleDTO> findByIdIn(List<Long> ids);

  /**
   * Get the "id" specialty.
   *
   * @param id the id of the entity
   * @return the entity
   */
  SpecialtyDTO findOne(Long id);

  /**
   * Delete the "id" specialty.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  /**
   * Find Specialties that are linked to a Programe via the attached Curricula, find by Specialty
   * name if a search criteria is provided
   *
   * @param programmeId The programme id to search for
   * @param searchQuery the possible name of the Specialty
   * @param pageable    The page in which we want
   * @return Paginated list of found specialties
   */
  Page<SpecialtyDTO> getPagedSpecialtiesForProgrammeId(Long programmeId, String searchQuery,
      Pageable pageable);

  /**
   * Find specialties for a Programme that a Person is a member of
   *
   * @param programmeId The Programme id
   * @param personId    The id of the Person
   * @return List of all the specialties linked to the person and programme
   */
  List<SpecialtyDTO> getSpecialtiesForProgrammeAndPerson(Long programmeId, Long personId);
}
