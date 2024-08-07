package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonLiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonV2DTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.repository.RightToWorkRepository;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Person.
 */
public interface PersonService {

  /**
   * Save a person.
   *
   * @param personDto the entity to save
   * @return the persisted entity
   */
  PersonDTO save(PersonDTO personDto);

  /**
   * Create a person.
   * <p>
   * Person is one of those entities that share the ID with the joining tables Save the person
   * object and ensure we copy the generated id to the linked entities
   *
   * @param personDto the entity to save
   * @return the persisted entity
   */
  PersonDTO create(PersonDTO personDto);

  /**
   * Save a list of persons
   *
   * @param personDtos the list of entities to save
   * @return a list of persisted entities
   */
  List<PersonDTO> save(List<PersonDTO> personDtos);

  /**
   * Patch a list of persons.
   *
   * @param personDtos the list of entities to patch.
   * @return The patched entities.
   */
  List<PersonDTO> patch(List<PersonDTO> personDtos);

  /**
   * Get all the people.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<PersonViewDTO> findAll(Pageable pageable);

  /**
   * Get all the people using the given smart search string and filters.
   *
   * @param searchString the search string to match, can be null
   * @param columnFilers the exact key value filters to apply, can be null
   * @param pageable     the pagination information
   * @return the list of entities
   */
  Page<PersonViewDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilers,
      Pageable pageable);


  /**
   * Looks for person basic details with support of only smart search and automatically limited to
   * 100 results.
   *
   * @param searchString the smart search string to compare basic details contents to
   * @return the list of basic details found
   */
  List<PersonBasicDetailsDTO> basicDetailsSearch(String searchString);

  /**
   * Get the "id" person.
   *
   * @param id the id of the entity
   * @return the entity
   */
  PersonDTO findOne(Long id);


  PersonDTO findPersonWithProgrammeMembershipsSorted(Long id);

  /**
   * Alternate version of the find person method that returns the same person but with no
   * qualification data. Implemented for the Trust Access work
   *
   * @param id the ID of the trainee
   * @return The found person.
   */
  PersonV2DTO findPersonV2WithProgrammeMembershipsSorted(Long id);

  /**
   * Get a person's ID by Gmc Id
   *
   * @param gmcId the GMC Id of the entity
   * @return the tcs ID if found
   */
  Long findIdByGmcId(String gmcId);

  /**
   * Retrieve the persons based on public health numbers
   *
   * @param publicHealthNumbers the persons public Health Numbers
   * @return the persons if found
   */
  List<PersonDTO> findPersonsByPublicHealthNumbersIn(List<String> publicHealthNumbers);

  Page<PersonLiteDTO> searchByRoleCategory(String query, Long categoryId, final Pageable pageable,
      final boolean filterByTrainerApprovalStatus);

  /**
   * Retrieve the basic details of persons
   *
   * @param ids the person IDs
   * @return the basic details if found
   */
  List<PersonBasicDetailsDTO> findBasicDetailsByIdIn(Set<Long> ids);

  /**
   * Retrieve the persons
   *
   * @param ids the person IDs
   * @return the persons if found
   */
  List<PersonDTO> findByIdIn(Set<Long> ids);

  /**
   * Retrieve the basic details of one person
   *
   * @param id the person ID
   * @return the basic details if found
   */
  PersonBasicDetailsDTO getBasicDetails(Long id);

  /**
   * Delete the "id" person.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  /**
   * build person ownership.
   */
  CompletableFuture<Void> buildPersonView();

  void setRightToWorkRepository(RightToWorkRepository rightToWorkRepository);

  /**
   * Method that will throw a not authorized exception if the current logged in user cannot view or
   * modify the person record
   *
   * @param personId the db managed id of the person record
   */
  void canLoggedInUserViewOrAmend(Long personId);
}
