package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Override
  @Transactional(readOnly = true)
  public Page<ConditionsOfJoiningDto> findAll(Pageable pageable) {
    LOG.debug("Request to get all ConditionsOfJoining");
    return repository.findAll(pageable)
        .map(mapper::toDto);
  }

  /**
   * Get one ConditionsOfJoiningDto by uuid.
   *
   * @param programmeMembershipUuid the uuid of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public ConditionsOfJoiningDto findOne(UUID programmeMembershipUuid) {
    LOG.debug("Request to get ConditionsOfJoiningDto : {}", programmeMembershipUuid);
    ConditionsOfJoining conditionsOfJoining
        = repository.findById(programmeMembershipUuid).orElse(null);
    return mapper.toDto(conditionsOfJoining);
  }

  /**
   * Get a list of Conditions Of Joining for a trainee.
   *
   * @param traineeId the TIS id of the trainee
   * @return a list of Conditions of Joining for the trainee
   */
  @Override
  @Transactional(readOnly = true)
  public List<ConditionsOfJoiningDto> findConditionsOfJoiningsForTrainee(Long traineeId) {
    LOG.debug("Request to get ConditionsOfJoining for trainee : {}", traineeId);
    List<ConditionsOfJoining> foundConditionsOfJoinings = repository
        .findByTraineeId(traineeId);

    if (CollectionUtils.isNotEmpty(foundConditionsOfJoinings)) {
      return mapper.allEntityToDto(foundConditionsOfJoinings);
    }
    return Collections.emptyList();
  }
}
