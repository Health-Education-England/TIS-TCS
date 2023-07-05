package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A service providing Conditions of Joining functionality.
 */
@Service
@Transactional
public class ConditionsOfJoiningServiceImpl implements ConditionsOfJoiningService {

  private static final Logger LOG = LoggerFactory.getLogger(ConditionsOfJoiningServiceImpl.class);

  private final ConditionsOfJoiningRepository repository;
  private final ConditionsOfJoiningMapper mapper;
  private final ProgrammeMembershipRepository programmeMembershipRepository;
  private final CurriculumMembershipRepository curriculumMembershipRepository;

  /**
   * Initialise the Conditions of Joining service.
   *
   * @param repository                     the Conditions of Joining repository
   * @param mapper                         the Conditions of Joining mapper
   * @param programmeMembershipRepository  the Programme Membership repository
   * @param curriculumMembershipRepository the Curriculum Membership repository
   */
  @Autowired
  public ConditionsOfJoiningServiceImpl(ConditionsOfJoiningRepository repository,
      ConditionsOfJoiningMapper mapper, ProgrammeMembershipRepository programmeMembershipRepository,
      CurriculumMembershipRepository curriculumMembershipRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.curriculumMembershipRepository = curriculumMembershipRepository;
  }

  @Override
  public ConditionsOfJoiningDto save(Object id, ConditionsOfJoiningDto dto) {
    ProgrammeMembership programmeMembership;
    LOG.info("Request received to save Conditions of Joining for id {}.", id);
    try {
      // Deprecated structure and will be removed. JIRA - TIS21-2446: ProgrammeMembership refactor
      programmeMembership = curriculumMembershipRepository.getOne(Long.parseLong(id.toString()))
          .getProgrammeMembership();
    } catch (NumberFormatException e) {
      programmeMembership = programmeMembershipRepository.getOne(UUID.fromString(id.toString()));
    }

    if (programmeMembership == null) {
      throw new IllegalArgumentException(String.format("Programme Membership %s not found.", id));
    }

    UUID programmeMembershipUuid = programmeMembership.getUuid();
    dto.setProgrammeMembershipUuid(programmeMembershipUuid);
    LOG.info("Saving Conditions of Joining for Programme Membership with UUID {}.",
        programmeMembershipUuid);
    ConditionsOfJoining entity = repository.save(mapper.toEntity(dto));
    LOG.info("Saved Conditions of Joining for Programme Membership with UUID {}.",
        programmeMembershipUuid);

    return mapper.toDto(entity);
  }
}
