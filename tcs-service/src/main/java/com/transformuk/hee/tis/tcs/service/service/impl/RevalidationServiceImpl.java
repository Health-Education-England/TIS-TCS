package com.transformuk.hee.tis.tcs.service.service.impl;

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
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import static java.util.Arrays.asList;

@Service
@Transactional
public class RevalidationServiceImpl implements RevalidationService {

  private static final Logger log = LoggerFactory.getLogger(RevalidationServiceImpl.class);
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
    log.info("GMC Nos received from Revalidation application: {}", gmcIds); //Need to change this info to debug
    Map<String, RevalidationRecordDTO> revalidationRecordDTOMap = new HashMap<>();
    List<GmcDetails> gmcDetails = gmcDetailsRepository.findByGmcNumberIn(gmcIds);
    gmcDetails.forEach(gmcDetail -> {
      RevalidationRecordDTO revalidationRecordDTO = new RevalidationRecordDTO();
      final long personId = gmcDetail.getId();
      log.info("ID : {}", personId);
      revalidationRecordDTO.setGmcId(gmcDetail.getGmcNumber());

      //Programme Membership
      ProgrammeMembership programmeMembership = programmeMembershipRepository
          .findLatestProgrammeMembershipByTraineeId(personId);
      revalidationRecordDTO.setCctDate(programmeMembership.getProgrammeEndDate());
      revalidationRecordDTO.setProgrammeMembershipType(programmeMembership
          .getProgrammeMembershipType().toString());
      revalidationRecordDTO.setProgrammeName(programmeMembership.getProgramme().getProgrammeName());

      //Placement
      List<Placement> currentPlacementsForTrainee = placementRepository
          .findCurrentPlacementForTrainee(personId, LocalDate.now(), placementTypes);
      if (CollectionUtils.isNotEmpty(currentPlacementsForTrainee) && currentPlacementsForTrainee.get(0).getGradeId() != null) {
        long gradeId = currentPlacementsForTrainee.get(0).getGradeId();
        log.info("GRADE ID : {}", gradeId);
        List<GradeDTO> grades = referenceService.findGradesIdIn(Collections.singleton(gradeId));
        grades.forEach(gradeDTO -> {
          revalidationRecordDTO.setCurrentGrade(gradeDTO.getName());
        });
      }
      revalidationRecordDTOMap.put(gmcDetail.getGmcNumber(), revalidationRecordDTO);
    });
    return revalidationRecordDTOMap;
  }
}
