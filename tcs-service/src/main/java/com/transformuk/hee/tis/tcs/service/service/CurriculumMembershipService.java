package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Service Interface for managing CurriculumMembership.
 */
public interface CurriculumMembershipService {

  /**
   * Save a curriculumMembership.
   *
   * @param cmDto the dto to save
   * @return the persisted object
   */
  CurriculumMembershipDTO save(CurriculumMembershipDTO cmDto);

  CurriculumMembershipDTO patch(CurriculumMembershipDTO cmDto)
      throws MethodArgumentNotValidException, NoSuchMethodException;
}
