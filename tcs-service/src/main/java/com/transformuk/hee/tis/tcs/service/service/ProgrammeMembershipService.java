package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ProgrammeMembership.
 */
public interface ProgrammeMembershipService {

  /**
   * Save a programmeMembership.
   *
   * @param programmeMembershipDTO the entity to save
   * @return the persisted entity
   */
  ProgrammeMembershipDTO save(ProgrammeMembershipDTO programmeMembershipDTO);

  /**
   * Save a list of programmeMembership.
   *
   * @param programmeMembershipDTO the list of entities to save
   * @return the list of persisted entities
   */
  List<ProgrammeMembershipDTO> save(List<ProgrammeMembershipDTO> programmeMembershipDTO);

  /**
   * Get all the programmeMemberships.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<ProgrammeMembershipDTO> findAll(Pageable pageable);

  /**
   * Get the "id" programmeMembership.
   *
   * @param id the id of the entity
   * @return the entity
   */
  ProgrammeMembershipDTO findOne(Long id);

  /**
   * Delete the "id" programmeMembership.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  /**
   * Get all the programmeMemberships for a trainee id and programme id.
   *
   * @return the list of entities
   */
  List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTraineeAndProgramme(Long traineeId, Long programmeId);

  /**
   * Get a list of programme memberships for a trainee
   *
   * @param traineeId the tis id of the trainee
   * @return a list of programme curricula memberships for the trainee
   */
  List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTrainee(Long traineeId);

  List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTraineeRolledUp(Long traineeId);

}
