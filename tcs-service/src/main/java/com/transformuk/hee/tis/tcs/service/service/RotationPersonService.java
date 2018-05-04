package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.RotationPersonDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Interface for managing RotationPerson.
 */
public interface RotationPersonService {

    /**
     * Save a rotationPerson.
     *
     * @param rotationPersonDTO the entity to save
     * @return the persisted entity
     */
    RotationPersonDTO save(RotationPersonDTO rotationPersonDTO);

    /**
     * Get all the rotationPeople.
     *
     * @return the list of entities
     */
    List<RotationPersonDTO> findAll();

    /**
     * Get all the rotationPeople by person
     *
     * @return the list of entities
     */
    List<RotationPersonDTO> findByPersonId(Long personId);

    /**
     * Get the "id" rotationPerson.
     *
     * @param id the id of the entity
     * @return the entity
     */
    RotationPersonDTO findOne(Long id);

    /**
     * Delete the "id" rotationPerson.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
