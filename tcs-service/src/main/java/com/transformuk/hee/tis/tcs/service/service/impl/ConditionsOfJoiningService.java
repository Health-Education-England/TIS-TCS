package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
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
public class ConditionsOfJoiningService {

  private static final Logger LOG = LoggerFactory.getLogger(ConditionsOfJoiningService.class);

  private final ConditionsOfJoiningRepository repository;
  private final ConditionsOfJoiningMapper mapper;
  private final ProgrammeMembershipService programmeMembershipService;

  @Autowired
  public ConditionsOfJoiningService(ConditionsOfJoiningRepository repository,
      ConditionsOfJoiningMapper mapper, ProgrammeMembershipService programmeMembershipService) {
    this.repository = repository;
    this.mapper = mapper;
    this.programmeMembershipService = programmeMembershipService;
  }

  public ConditionsOfJoiningDto save(Long programmeMembershipId, ConditionsOfJoiningDto dto) {
    LOG.info("Request received to save Conditions of Joining for Programme Membership {}.",
        programmeMembershipId);

    ProgrammeMembershipDTO programmeMembership = programmeMembershipService.findOne(
        programmeMembershipId);

    if (programmeMembership == null) {
      throw new IllegalArgumentException(
          String.format("Programme Membership %s not found.", programmeMembershipId));
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
