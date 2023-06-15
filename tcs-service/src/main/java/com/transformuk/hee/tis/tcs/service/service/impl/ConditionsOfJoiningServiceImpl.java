package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
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
  private final ProgrammeMembershipService programmeMembershipService;

  /**
   * Initialise the Conditions of Joining service.
   *
   * @param repository the Conditions of Joining repository
   * @param mapper the Conditions of Joining mapper
   * @param programmeMembershipService the Programme Membership service
   */
  @Autowired
  public ConditionsOfJoiningServiceImpl(ConditionsOfJoiningRepository repository,
      ConditionsOfJoiningMapper mapper, ProgrammeMembershipService programmeMembershipService) {
    this.repository = repository;
    this.mapper = mapper;
    this.programmeMembershipService = programmeMembershipService;
  }

  @Override
  public ConditionsOfJoiningDto save(Object id, ConditionsOfJoiningDto dto) {
    UUID realProgrammeMembershipId = null;
    Long curriculumMembershipId = null;
    ProgrammeMembershipDTO programmeMembership;
    try {
      realProgrammeMembershipId = UUID.fromString(id.toString());
    } catch (IllegalArgumentException e) {
      try {
        curriculumMembershipId = (Long) id;
      } catch (ClassCastException castException) {
        throw new IllegalArgumentException(id + " is neither Long nor UUID");
      }
    }
    if (realProgrammeMembershipId == null) {
      LOG.info("Request received to save Conditions of Joining for Curriculum Membership {}.",
          curriculumMembershipId);
      programmeMembership = programmeMembershipService.findOne(curriculumMembershipId);
    } else {
      LOG.info("Request received to save Conditions of Joining for Programme Membership {}.",
          realProgrammeMembershipId);
      programmeMembership = programmeMembershipService.findOne(realProgrammeMembershipId);
    }

    if (programmeMembership == null) {
      throw new IllegalArgumentException(
          String.format("Programme Membership %s not found.", id));
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
