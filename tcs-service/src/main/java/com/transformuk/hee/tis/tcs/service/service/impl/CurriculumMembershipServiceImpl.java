package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.CurriculumMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Service Implementation for managing CurriculumMembership.
 */
@Service
public class CurriculumMembershipServiceImpl implements CurriculumMembershipService {

  private final Logger log = LoggerFactory.getLogger(CurriculumMembershipServiceImpl.class);

  private final CurriculumMembershipRepository cmRepository;
  private final ProgrammeMembershipRepository pmRepository;
  private final CurriculumMembershipMapper cmMapper;
  private final CurriculumMembershipValidator cmValidator;

  /**
   * Initialise the CurriculumMembershipServiceImpl.
   *
   * @param cmRepository Curriculum Membership repository
   * @param cmMapper     Curriculum Membership mapper
   * @param pmRepository Programme Membership repository
   */
  public CurriculumMembershipServiceImpl(CurriculumMembershipRepository cmRepository,
      CurriculumMembershipMapper cmMapper, ProgrammeMembershipRepository pmRepository,
      CurriculumMembershipValidator cmValidator) {
    this.cmRepository = cmRepository;
    this.cmMapper = cmMapper;
    this.pmRepository = pmRepository;
    this.cmValidator = cmValidator;
  }

  /**
   * Save a curriculumMembership.
   *
   * @param cmDto the dto to save
   * @return the persisted object
   */
  @Transactional
  public CurriculumMembershipDTO save(CurriculumMembershipDTO cmDto) {
    log.debug("Request to save CurriculumMembership : {}", cmDto);
    CurriculumMembership cm = cmMapper.toEntity(cmDto);
    // As we have done the validation before save, so it's very unlikely that pm is not found.
    ProgrammeMembership pm =
        pmRepository.findByUuid(cmDto.getProgrammeMembershipUuid())
            .orElseThrow(() -> new NoSuchElementException("No value present"));
    cm.setProgrammeMembership(pm);
    CurriculumMembership returnedCm = cmRepository.save(cm);
    return cmMapper.curriculumMembershipToCurriculumMembershipDto(returnedCm);
  }

  /**
   * Patch a curriculumMembership.
   *
   * @param cmDto the dto to patch
   * @return the persisted object
   */
  @Transactional
  public CurriculumMembershipDTO patch(CurriculumMembershipDTO cmDto)
      throws MethodArgumentNotValidException, NoSuchMethodException {
    log.debug("Request to patch CurriculumMembership : {}", cmDto);
    CurriculumMembership curriculumMembershipDb = cmRepository.findById(cmDto.getId())
        .orElse(null);
    if (curriculumMembershipDb == null) {
      cmDto.addMessage("Curriculum membership id not found.");
      return cmDto;
    }
    if (cmDto.getCurriculumStartDate() == null) {
      cmDto.setCurriculumStartDate(curriculumMembershipDb.getCurriculumStartDate());
    }
    if (cmDto.getCurriculumEndDate() == null) {
      cmDto.setCurriculumEndDate(curriculumMembershipDb.getCurriculumEndDate());
    }
    cmValidator.validateForBulkUploadPatch(cmDto);

    curriculumMembershipDb.setCurriculumStartDate(cmDto.getCurriculumStartDate());
    curriculumMembershipDb.curriculumEndDate(cmDto.getCurriculumEndDate());
    CurriculumMembership returnedCm = cmRepository.save(curriculumMembershipDb);

    return cmMapper.curriculumMembershipToCurriculumMembershipDto(returnedCm);
  }
}
