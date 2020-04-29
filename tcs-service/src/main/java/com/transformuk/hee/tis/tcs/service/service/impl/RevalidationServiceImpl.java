package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.util.Arrays.asList;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RevalidationServiceImpl implements RevalidationService {

  private static final Logger LOG = LoggerFactory.getLogger(RevalidationServiceImpl.class);
  private final GmcDetailsRepository gmcDetailsRepository;
  private final ProgrammeMembershipRepository programmeMembershipRepository;
  private final PlacementRepository placementRepository;
  private final ReferenceService referenceService;

  private static final List<String> placementTypes = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");

  public RevalidationServiceImpl(GmcDetailsRepository gmcDetailsRepository,
                                 ProgrammeMembershipRepository programmeMembershipRepository,
                                 PlacementRepository placementRepository,
                                 ReferenceService referenceService) {
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.placementRepository = placementRepository;
    this.referenceService = referenceService;
  }

  @Override
  public Map<String, RevalidationRecordDTO> findAllRevalidationsByGmcIds(final List<String> gmcIds) {
    LOG.debug("GMC Nos received from Revalidation application: {}", gmcIds);
    Map<String, RevalidationRecordDTO> revalidationRecordDTOMap = new HashMap<>();
    List<GmcDetails> gmcDetails = gmcDetailsRepository.findByGmcNumberIn(gmcIds);
    gmcDetails.forEach(gmcDetail -> {
      RevalidationRecordDTO revalidationRecordDTO = new RevalidationRecordDTO();
      final long personId = gmcDetail.getId();
      LOG.debug("Person ID : {}", personId);
      revalidationRecordDTO.setGmcId(gmcDetail.getGmcNumber());

      //Programme Membership
      ProgrammeMembership programmeMembership = programmeMembershipRepository
          .findLatestProgrammeMembershipByTraineeId(personId);
      LOG.debug("Programme Membership End Date : {}", programmeMembership.getProgrammeEndDate());
      revalidationRecordDTO.setCctDate(programmeMembership.getProgrammeEndDate());
      revalidationRecordDTO.setProgrammeMembershipType(programmeMembership
          .getProgrammeMembershipType().toString());
      revalidationRecordDTO.setProgrammeName(programmeMembership.getProgramme().getProgrammeName());

      //Placement
      List<Placement> currentPlacementsForTrainee = placementRepository
          .findCurrentPlacementForTrainee(personId, LocalDate.now(), placementTypes);
      if (CollectionUtils.isNotEmpty(currentPlacementsForTrainee) && currentPlacementsForTrainee.get(0).getGradeId() != null) {
        LOG.debug("Placement ID : {}", currentPlacementsForTrainee.get(0).getId());
        Long gradeId = currentPlacementsForTrainee.get(0).getGradeId();
        LOG.debug("GRADE ID : {}", gradeId);
        List<GradeDTO> grades = new ArrayList<>();
        if (gradeId != null) {
          grades = referenceService.findGradesIdIn(Collections.singleton(gradeId));
        }
        grades.forEach(gradeDTO -> {
          revalidationRecordDTO.setCurrentGrade(gradeDTO.getName());
        });
      }
      revalidationRecordDTOMap.put(gmcDetail.getGmcNumber(), revalidationRecordDTO);
    });
    return revalidationRecordDTOMap;
  }
}
