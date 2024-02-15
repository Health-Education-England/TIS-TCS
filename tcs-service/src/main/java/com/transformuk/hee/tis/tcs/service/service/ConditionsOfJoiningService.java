package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;

/**
 * Service Interface for managing Conditions Of Joining.
 */
public interface ConditionsOfJoiningService {

  /**
   * Save a Conditions of Joining from its DTO and related programme membership UUID.
   *
   * @param id  the programme membership UUID as a string
   * @param dto the Conditions Of Joining DTO
   * @return the saved Conditions of Joining DTO
   */
  ConditionsOfJoiningDto save(String id, ConditionsOfJoiningDto dto);
}
