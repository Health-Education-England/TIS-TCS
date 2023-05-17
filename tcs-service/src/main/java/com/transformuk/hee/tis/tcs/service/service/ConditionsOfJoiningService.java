package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Conditions Of Joining.
 */
public interface ConditionsOfJoiningService {

  /**
   * Save a Conditions of Joining from its DTO and related programme membership ID.
   *
   * @param programmeMembershipId the programme membership ID
   * @param dto the Conditions Of Joining DTO
   * @return the saved Conditions of Joining DTO
   */
  ConditionsOfJoiningDto save(Long programmeMembershipId, ConditionsOfJoiningDto dto);

  /**
   * Get all the Conditions Of Joining.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<ConditionsOfJoiningDto> findAll(Pageable pageable);

  /**
   * Get the "uuid" Conditions Of Joining Dto.
   *
   * @param programmeMembershipUuid the UUID of the entity
   * @return the entity
   */
  ConditionsOfJoiningDto findOne(UUID programmeMembershipUuid);
}
