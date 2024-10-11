package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipCurriculaDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
   * Get the "uuid" programmeMembership.
   *
   * @param uuid the uuid of the entity
   * @return the entity
   */
  ProgrammeMembershipDTO findOne(UUID uuid);

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
  List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTraineeAndProgramme(
      Long traineeId, Long programmeId);

  /**
   * Get a list of programme memberships for a trainee
   *
   * @param traineeId the tis id of the trainee
   * @return a list of programme curricula memberships for the trainee
   */
  List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTrainee(Long traineeId);

  /**
   * Get a list of programmeMembershipsDto for a set of UUIDs.
   *
   * @param uuids a list of programme membership UUIDs.
   * @return a list of programme memberships.
   */
  List<ProgrammeMembershipDTO> findProgrammeMembershipsByUuid(Set<UUID> uuids);

  /**
   * Get a list of programme memberships with curricula.
   *
   * @param ids set of all the programme memberships.
   * @return a list of programme memberships with curricula for all the ids.
   */
  List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipDetailsByIds(
      Set<Long> ids);

  List<ProgrammeMembershipCurriculaDTO> findProgrammeMembershipsForTraineeRolledUp(Long traineeId);

  /**
   * Get a list of programme memberships for a programme.
   *
   * @param programmeId the id of the programme
   * @return a list of programme memberships of the programme
   */
  List<ProgrammeMembershipDTO> findProgrammeMembershipsByProgramme(Long programmeId);

  /**
   * patch a programme membership.
   *
   * @param programmeMembershipDto the dto to patch
   * @return the patched dto
   */
  ProgrammeMembershipDTO patch(ProgrammeMembershipDTO programmeMembershipDto);
}
