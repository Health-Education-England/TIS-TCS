package com.transformuk.hee.tis.tcs.service.service;


import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TrainingNumber.
 */
public interface TrainingNumberService {

  /**
   * Save a trainingNumber.
   *
   * @param trainingNumberDTO the entity to save
   * @return the persisted entity
   */
  TrainingNumberDTO save(TrainingNumberDTO trainingNumberDTO);

  /**
   * Save a list of trainingNumbers.
   *
   * @param trainingNumberDTO the entities to save
   * @return the list of persisted entities
   */
  List<TrainingNumberDTO> save(List<TrainingNumberDTO> trainingNumberDTO);

  /**
   * Get all the trainingNumbers.
   *
   * @return the list of entities
   */
  Page<TrainingNumberDTO> findAll(Pageable pageable);

  /**
   * Get the "id" trainingNumber.
   *
   * @param id the id of the entity
   * @return the entity
   */
  TrainingNumberDTO findOne(Long id);

  /**
   * Delete the "id" trainingNumber.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  /**
   * Populate the trainingNumbers for the given programme memberships.
   *
   * @param programmeMemberships The programme memberships to generate the training numbers for.
   */
  void populateTrainingNumbers(List<ProgrammeMembership> programmeMemberships);
}
