package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
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
    Optional<ProgrammeMembership> programmeMembershipOptional = Optional.empty();
    LOG.info("Request received to save Conditions of Joining for id {}.", id);

    // Deprecated structure and will be removed. JIRA - TIS21-2446: ProgrammeMembership refactor
    if (StringUtils.isNumeric(id.toString())) {
      Optional<CurriculumMembership> curriculumMembershipOptional
          = curriculumMembershipRepository.findById(Long.parseLong(id.toString()));
      if (curriculumMembershipOptional.isPresent()) {
        programmeMembershipOptional = Optional.of(
            curriculumMembershipOptional.get().getProgrammeMembership());
      }
    } else {
      programmeMembershipOptional = programmeMembershipRepository.findByUuid(
          UUID.fromString(id.toString()));
    }

    if (!programmeMembershipOptional.isPresent()) {
      throw new IllegalArgumentException(String.format("No Programme Membership for %s.", id));
    }
    LOG.debug("Found ProgrammeMembership {}", programmeMembershipOptional.get());

    UUID programmeMembershipUuid = programmeMembershipOptional.get().getUuid();
    dto.setProgrammeMembershipUuid(programmeMembershipUuid);
    LOG.info("Saving Conditions of Joining for Programme Membership with UUID {}.",
        programmeMembershipUuid);
    ConditionsOfJoining entity;
    try {
      entity = repository.save(mapper.toEntity(dto));
    } catch (NonTransientDataAccessException e) {
      throw new IllegalArgumentException(
          String.format("Unable to save Conditions of Joining %s.", id), e);
    }
    LOG.info("Saved Conditions of Joining for Programme Membership with UUID {}.",
        programmeMembershipUuid);

    return mapper.toDto(entity);
  }
}
