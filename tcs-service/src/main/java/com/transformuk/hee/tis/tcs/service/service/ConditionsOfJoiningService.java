package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import java.util.UUID;

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
   * Get the "uuid" Conditions Of Joining Dto.
   *
   * @param programmeMembershipUuid the UUID of the entity
   * @return the entity
   */
  ConditionsOfJoiningDto findOne(UUID programmeMembershipUuid);
}
