package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.util.Arrays.asList;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RevalidationServiceImpl implements RevalidationService {

  private static final Logger LOG = LoggerFactory.getLogger(RevalidationServiceImpl.class);
  private static final List<String> placementTypes = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");
  private final ContactDetailsService contactDetailsService;
  private final GmcDetailsRepository gmcDetailsRepository;
  private final ProgrammeMembershipRepository programmeMembershipRepository;
  private final PlacementRepository placementRepository;
  private final ReferenceService referenceService;

  public RevalidationServiceImpl(ContactDetailsService contactDetailsService,
      GmcDetailsRepository gmcDetailsRepository,
      ProgrammeMembershipRepository programmeMembershipRepository,
      PlacementRepository placementRepository,
      ReferenceService referenceService) {
    this.contactDetailsService = contactDetailsService;
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.placementRepository = placementRepository;
    this.referenceService = referenceService;
  }

  @Override
  public RevalidationRecordDto findRevalidationByGmcId(final String gmcId) {
    LOG.debug("GMC No received from Revalidation application: {}", gmcId);
    GmcDetails gmcDetails = gmcDetailsRepository.findGmcDetailsByGmcNumber(gmcId);
    return buildRevalidationRecord(gmcDetails);
  }

  @Override
  public Map<String, RevalidationRecordDto> findAllRevalidationsByGmcIds(
      final List<String> gmcIds) {
    LOG.debug("GMC Nos received from Revalidation application: {}", gmcIds);
    List<GmcDetails> gmcDetails = gmcDetailsRepository.findByGmcNumberIn(gmcIds);

    return gmcDetails.stream()
        .map(this::buildRevalidationRecord)
        .collect(Collectors.toMap(RevalidationRecordDto::getGmcNumber, dto -> dto));
  }

  private RevalidationRecordDto buildRevalidationRecord(GmcDetails gmcDetails) {
    RevalidationRecordDto revalidationRecordDto = new RevalidationRecordDto();
    final long personId = gmcDetails.getId();
    LOG.debug("Person ID : {}", personId);
    revalidationRecordDto.setGmcNumber(gmcDetails.getGmcNumber());

    // Contact details.
    ContactDetailsDTO contactDetailsDto = contactDetailsService.findOne(personId);
    revalidationRecordDto.setForenames(contactDetailsDto.getForenames());
    revalidationRecordDto.setSurname(contactDetailsDto.getSurname());

    //Programme Membership
    ProgrammeMembership programmeMembership = programmeMembershipRepository
        .findLatestProgrammeMembershipByTraineeId(personId);
    LOG.debug("Programme Membership End Date : {}", programmeMembership.getProgrammeEndDate());
    revalidationRecordDto.setCctDate(programmeMembership.getProgrammeEndDate());
    revalidationRecordDto.setProgrammeMembershipType(programmeMembership
        .getProgrammeMembershipType().toString());
    revalidationRecordDto.setProgrammeName(programmeMembership.getProgramme().getProgrammeName());

    //Placement
    List<Placement> currentPlacementsForTrainee = placementRepository
        .findCurrentPlacementForTrainee(personId, LocalDate.now(), placementTypes);
    if (CollectionUtils.isNotEmpty(currentPlacementsForTrainee)
        && currentPlacementsForTrainee.get(0).getGradeId() != null) {
      LOG.debug("Placement ID : {}", currentPlacementsForTrainee.get(0).getId());
      Long gradeId = currentPlacementsForTrainee.get(0).getGradeId();
      LOG.debug("GRADE ID : {}", gradeId);
      List<GradeDTO> grades = new ArrayList<>();
      if (gradeId != null) {
        grades = referenceService.findGradesIdIn(Collections.singleton(gradeId));
      }
      grades.forEach(gradeDTO -> revalidationRecordDto.setCurrentGrade(gradeDTO.getName()));
    }

    return revalidationRecordDto;
  }
}
