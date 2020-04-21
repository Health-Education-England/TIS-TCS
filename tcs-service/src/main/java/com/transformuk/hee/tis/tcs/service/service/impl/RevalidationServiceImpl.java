package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
@Transactional
public class RevalidationServiceImpl implements RevalidationService {

  private static final Logger log = LoggerFactory.getLogger(RevalidationServiceImpl.class);
  private final GmcDetailsRepository gmcDetailsRepository;
  private final ContactDetailsRepository contactDetailsRepository;
  private final ProgrammeMembershipRepository programmeMembershipRepository;
  private final PlacementRepository placementRepository;
  private final ProgrammeMembershipMapper programmeMembershipMapper;
  private final ReferenceService referenceService;

  public RevalidationServiceImpl(GmcDetailsRepository gmcDetailsRepository,
                                 ContactDetailsRepository contactDetailsRepository,
                                 ProgrammeMembershipRepository programmeMembershipRepository,
                                 PlacementRepository placementRepository,
                                 ProgrammeMembershipMapper programmeMembershipMapper,
                                 ReferenceService referenceService) {
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.contactDetailsRepository = contactDetailsRepository;
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.placementRepository = placementRepository;
    this.programmeMembershipMapper = programmeMembershipMapper;
    this.referenceService = referenceService;
  }

  @Override
  public List<RevalidationRecordDTO> findAllRevalidationsByGmcIds(final List<String> gmcIds) {
    log.info("INSIDE findAllRevalidationsByGmcIds: {}", gmcIds);
    List<GmcDetails> gmcDetails = gmcDetailsRepository.findByGmcNumberIn(gmcIds);

    //List<Long> tisPersonIds = gmcDetails.stream().map(GmcDetails::getId).collect(Collectors.toList());

    List<RevalidationRecordDTO> revalidationRecordDTOList = new ArrayList<>();
    //for(Long personId : tisPersonIds) {
    RevalidationRecordDTO revalidationRecordDTO = new RevalidationRecordDTO();
    gmcDetails.forEach(gmcDetails1 -> {
      final long personId = gmcDetails1.getId();
      revalidationRecordDTO.setGmcId(gmcDetails1.getGmcNumber());


//      ContactDetails contactDetails = contactDetailsRepository.findById(personId).orElse(null);
//      log.info("ID : {}", personId);
//      revalidationRecordDTO.setTraineeName(contactDetails.getForenames() + " " + contactDetails.getSurname());

      //Programme Memberships
      //get single object
      List<ProgrammeMembership> programmeMembershipList = programmeMembershipRepository.findByTraineeId(personId);
      List<ProgrammeMembershipDTO> programmeMembershipDtos = programmeMembershipMapper
          .programmeMembershipsToProgrammeMembershipDTOs(programmeMembershipList);
      //add programme name
      programmeMembershipDtos.forEach(pm -> {
        revalidationRecordDTO.setCctDate(pm.getProgrammeEndDate());
        revalidationRecordDTO.setProgrammeMembershipType(pm.getProgrammeMembershipType().toString());
      });
      //revalidationRecordDTO.setProgrammeMemberships(programmeMembershipDtos);

      //Placements
      //find latest placement (check findCurrentPlacementForTrainee)
      List<Placement> placements = placementRepository.findPlacementsByTraineeId(personId);

      placements.forEach(placement -> {
        Set<Long> gradeIds = new HashSet<>();
        if (placement.getGradeId() != null) {
          gradeIds.add(placement.getGradeId());
        }
        List<GradeDTO> grades = referenceService.findGradesIdIn(gradeIds);
        grades.forEach(gradeDTO -> {
          revalidationRecordDTO.setCurrentGrade(gradeDTO.getName());
        });

      });
      revalidationRecordDTOList.add(revalidationRecordDTO);
    });
    return revalidationRecordDTOList;
  }
}
