package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

  @Override
  public Map<String, ConnectionRecordDto> findAllConnectionsByGmcIds(List<String> gmcIds) {
    LOG.debug("GMCNo received from Connection service: {}", gmcIds);
    final List<GmcDetails> gmcDetails = gmcDetailsRepository.findByGmcNumberIn(gmcIds);
    final Map<String, ConnectionRecordDto> connectionRecordDtoMap = new HashMap<>();
    gmcDetails.forEach(gmcDetail -> {

      LOG.info("Searching Programme membership for person: {}", gmcDetail.getId());

      final ProgrammeMembership programmeMembership = programmeMembershipRepository
          .findLatestProgrammeMembershipByTraineeId(gmcDetail.getId());

      final ConnectionRecordDto connectionRecordDto = getConnectionStatus(programmeMembership);
      connectionRecordDtoMap.put(gmcDetail.getGmcNumber(), connectionRecordDto);
    });
    return connectionRecordDtoMap;
  }

  private ConnectionRecordDto getConnectionStatus(final ProgrammeMembership programmeMembership) {
    final ConnectionRecordDto connectionRecordDto = new ConnectionRecordDto();
    connectionRecordDto.setConnectionStatus("No");
    final LocalDate currentDate = now();

    if (Objects.nonNull(programmeMembership)) {

      LOG.info("Programme membership found membership: {}", programmeMembership.getId());

      if (!isDisconnected(currentDate, programmeMembership)) {
        connectionRecordDto.setConnectionStatus("Yes");
      }
      final String programmeMemberShipType =
          Objects.nonNull(programmeMembership.getProgrammeMembershipType()) ? programmeMembership
              .getProgrammeMembershipType().toString() : null;
      connectionRecordDto.setProgrammeMembershipType(programmeMemberShipType);
      connectionRecordDto
          .setProgrammeMembershipStartDate(programmeMembership.getProgrammeStartDate());
      connectionRecordDto.setProgrammeMembershipEndDate(programmeMembership.getProgrammeEndDate());
      if (Objects.nonNull(programmeMembership.getProgramme())) {
        connectionRecordDto.setProgrammeOwner(programmeMembership.getProgramme().getOwner());
        connectionRecordDto.setProgrammeName(programmeMembership.getProgramme().getProgrammeName());
      }
    }

    return connectionRecordDto;
  }

  private boolean isDisconnected(LocalDate currentDate, ProgrammeMembership programmeMembership) {
    return Objects.isNull(programmeMembership) ||
        Objects.isNull(programmeMembership.getProgrammeStartDate()) ||
        Objects.isNull(programmeMembership.getProgrammeEndDate()) ||
        programmeMembership.getProgrammeStartDate().isAfter(currentDate) ||
        programmeMembership.getProgrammeEndDate().isBefore(currentDate);
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
    if (Objects.nonNull(programmeMembership)) {
      LOG.debug("Programme Membership End Date : {}", programmeMembership.getProgrammeEndDate());
      revalidationRecordDto.setCctDate(programmeMembership.getProgrammeEndDate());
      revalidationRecordDto.setProgrammeMembershipType(programmeMembership
          .getProgrammeMembershipType().toString());
      revalidationRecordDto.setProgrammeName(programmeMembership.getProgramme().getProgrammeName());
    }
    //Placement
    List<Placement> currentPlacementsForTrainee = placementRepository
        .findCurrentPlacementForTrainee(personId, now(), placementTypes);
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
