package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.CurriculumMembershipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing CurriculumMembership.
 */
@Service
@Transactional
public class CurriculumMembershipServiceImpl implements CurriculumMembershipService {

  private final Logger log = LoggerFactory.getLogger(CurriculumMembershipServiceImpl.class);

  private final CurriculumMembershipRepository curriculumMembershipRepository;

  private final ProgrammeMembershipRepository programmeMembershipRepository;

  private final ProgrammeMembershipServiceImpl programmeMembershipServiceImpl;

  /**
   * Initialise the CurriculumMembershipService.
   *
   * @param curriculumMembershipRepository the CurriculumMembershipRepository
   * @param programmeMembershipRepository  the ProgrammeMembershipRepository
   */
  public CurriculumMembershipServiceImpl(
      CurriculumMembershipRepository curriculumMembershipRepository,
      ProgrammeMembershipRepository programmeMembershipRepository,
      ProgrammeMembershipServiceImpl programmeMembershipServiceImpl) {
    this.curriculumMembershipRepository = curriculumMembershipRepository;
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.programmeMembershipServiceImpl = programmeMembershipServiceImpl;
  }

  /**
   * Delete the curriculumMembership by id.
   *
   * <p>If the curriculumMembership is the last in the programmeMembership to delete, delete the
   * programmeMembership too.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete CurriculumMembership : {}", id);

    //Get the person id from the programme membership before deleting it
    ProgrammeMembership programmeMembership = curriculumMembershipRepository.getOne(id)
        .getProgrammeMembership();
    Long personId = programmeMembership.getPerson().getId();

    programmeMembership.getCurriculumMemberships().removeIf(cm -> cm.getId().equals(id));

    if (!programmeMembership.getCurriculumMemberships().isEmpty()) {
      programmeMembershipRepository.save(programmeMembership);
    } else {
      programmeMembershipRepository.delete(programmeMembership);
    }

    programmeMembershipServiceImpl.updatePersonWhenStatusIsStale(personId);
  }
}
