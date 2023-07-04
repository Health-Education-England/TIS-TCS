package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;

/**
 * Service Interface for managing Conditions Of Joining.
 */
public interface ConditionsOfJoiningService {

  /**
   * Save a Conditions of Joining from its DTO and related programme membership ID/UUID.
   *
   * @param id  the programme membership ID or UUID
   * @param dto the Conditions Of Joining DTO
   * @return the saved Conditions of Joining DTO
   */
  ConditionsOfJoiningDto save(Object id, ConditionsOfJoiningDto dto);
}
