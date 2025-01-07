package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.CurriculumMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurriculumMembershipServiceImpl implements CurriculumMembershipService {

  private final Logger log = LoggerFactory.getLogger(CurriculumMembershipServiceImpl.class);

  private final CurriculumMembershipRepository cmRepository;
  private final CurriculumMembershipMapper cmMapper;

  public CurriculumMembershipServiceImpl(CurriculumMembershipRepository cmRepository,
      CurriculumMembershipMapper cmMapper) {
    this.cmRepository = cmRepository;
    this.cmMapper = cmMapper;
  }
  @Transactional
  public CurriculumMembershipDTO save(CurriculumMembershipDTO cmDto) {
    log.debug("Request to save CurriculumMembership : {}", cmDto);
    CurriculumMembership cm = cmMapper.toEntity(cmDto);
    CurriculumMembership returnedCm = cmRepository.save(cm);
    return cmMapper.curriculumMembershipToCurriculumMembershipDto(returnedCm);
  }
}
