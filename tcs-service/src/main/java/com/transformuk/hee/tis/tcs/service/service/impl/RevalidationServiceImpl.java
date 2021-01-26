package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDetailDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryRecordDto.ConnectionSummaryRecordDtoBuilder;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class RevalidationServiceImpl implements RevalidationService {

  public static final int SIZE = 20;
  private static final Logger LOG = LoggerFactory.getLogger(RevalidationServiceImpl.class);
  private static final List<String> placementTypes = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");
  private final ContactDetailsService contactDetailsService;
  private final GmcDetailsRepository gmcDetailsRepository;
  private final ProgrammeMembershipRepository programmeMembershipRepository;
  private final PlacementRepository placementRepository;
  private final ReferenceService referenceService;
  private final PersonRepository personRepository;
  private final ProgrammeMembershipMapper programmeMembershipMapper;

  public RevalidationServiceImpl(ContactDetailsService contactDetailsService,
      GmcDetailsRepository gmcDetailsRepository,
      ProgrammeMembershipRepository programmeMembershipRepository,
      PlacementRepository placementRepository,
      ReferenceService referenceService,
      PersonRepository personRepository,
      ProgrammeMembershipMapper programmeMembershipMapper) {
    this.contactDetailsService = contactDetailsService;
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.placementRepository = placementRepository;
    this.referenceService = referenceService;
    this.personRepository = personRepository;
    this.programmeMembershipMapper = programmeMembershipMapper;
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

  @Override
  public ConnectionDetailDto findAllConnectionsHistoryByGmcId(String gmcId) {
    ConnectionDetailDto connectionDetailDto = new ConnectionDetailDto();

    LOG.debug("GMCNo received from Connection History service: {}", gmcId);
    final GmcDetails gmcDetail = gmcDetailsRepository.findGmcDetailsByGmcNumber(gmcId);
    final RevalidationRecordDto revalidationRecordDto = buildRevalidationRecord(gmcDetail);

    connectionDetailDto.setGmcNumber(revalidationRecordDto.getGmcNumber());
    connectionDetailDto.setForenames(revalidationRecordDto.getForenames());
    connectionDetailDto.setSurname(revalidationRecordDto.getSurname());
    connectionDetailDto.setCctDate(revalidationRecordDto.getCctDate());
    connectionDetailDto
        .setProgrammeMembershipType(revalidationRecordDto.getProgrammeMembershipType());
    connectionDetailDto.setProgrammeName(revalidationRecordDto.getProgrammeName());
    connectionDetailDto.setCurrentGrade(revalidationRecordDto.getCurrentGrade());

    final List<ProgrammeMembership> programmeMemberships = programmeMembershipRepository
        .findByTraineeId(gmcDetail.getId());
    LOG.info("Programme memberships found for person: {}, membership: {}", gmcDetail.getId(),
        programmeMemberships);

    List<ConnectionRecordDto> programmeHistory = programmeMemberships.stream()
        .map(pm -> getConnectionStatus(pm)).collect(toList());
    connectionDetailDto.setProgrammeHistory(programmeHistory);

    return connectionDetailDto;
  }

  @Override
  public ConnectionSummaryDto getHiddenTrainees(final List<String> gmcIds, final int pageNumber,
      final String searchGmcNumber) {
    final boolean searchAble = StringUtils.isEmpty(searchGmcNumber);
    final PageRequest pageRequest = PageRequest.of(pageNumber, SIZE);
    final Page<ConnectionDto> hiddenRecords = personRepository
        .getHiddenTraineeRecords(pageRequest, gmcIds, searchAble, searchGmcNumber);
    final List<ConnectionSummaryRecordDto> connectionHiddenRecords = hiddenRecords.get()
        .map(conn -> {
          return buildHiddenConnectionList(conn);
        }).collect(toList());

    return ConnectionSummaryDto.builder()
        .connections(connectionHiddenRecords)
        .totalResults(hiddenRecords.getTotalElements())
        .totalPages(hiddenRecords.getTotalPages())
        .build();
  }

  private ConnectionSummaryRecordDto buildHiddenConnectionList(ConnectionDto conn) {
    final ProgrammeMembership latestProgrammeMembership = programmeMembershipRepository
        .findLatestProgrammeMembershipByTraineeId(conn.getPersonId());
    final ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper
        .toDto(latestProgrammeMembership);

    final ConnectionSummaryRecordDtoBuilder connectionHiddenRecordDtoBuilder =
        ConnectionSummaryRecordDto
        .builder()
        .gmcReferenceNumber(conn.getGmcNumber())
        .doctorFirstName(conn.forenames)
        .doctorLastName(conn.surname)
        .connectionStatus(getHiddenConnectionStatus(conn));

    if (programmeMembershipDTO != null) {
      final String owner = programmeMembershipDTO.getProgrammeOwner();
      final ProgrammeMembershipType membershipType = programmeMembershipDTO
          .getProgrammeMembershipType();
      connectionHiddenRecordDtoBuilder
          .designatedBody(owner != null ? DesignatedBodyMapper.getDbcByOwner(owner) : null)
          .programmeOwner(owner)
          .programmeName(programmeMembershipDTO.getProgrammeName())
          .programmeMembershipStartDate(programmeMembershipDTO.getProgrammeStartDate())
          .programmeMembershipEndDate(programmeMembershipDTO.getProgrammeEndDate())
          .programmeMembershipType(membershipType != null ? membershipType.toString() : null);
    }

    return connectionHiddenRecordDtoBuilder.build();
  }

  @Override
  public ConnectionSummaryDto getExceptionTrainees(final List<String> gmcIds, final int pageNumber,
      final String searchGmcNumber, final List<String> dbcs) {
    final boolean searchable = StringUtils.isEmpty(searchGmcNumber);
    final PageRequest pageRequest = PageRequest.of(pageNumber, SIZE);
    final Set<String> owner =
        (dbcs == null || dbcs.isEmpty()) ? null : DesignatedBodyMapper.map(Sets.newHashSet(dbcs));

    final Page<Map<String, Object>> exceptionRecordsPage = personRepository
        .getExceptionTraineeRecords(pageRequest, gmcIds, searchable, searchGmcNumber, owner);

    final List<ConnectionSummaryRecordDto> connectionExceptionRecords = exceptionRecordsPage
        .getContent()
        .stream()
        .map(this::buildConnectionList)
        .collect(toList());

    return ConnectionSummaryDto.builder()
        .connections(connectionExceptionRecords)
        .totalResults(exceptionRecordsPage.getTotalElements())
        .totalPages(exceptionRecordsPage.getTotalPages())
        .build();
  }

  private ConnectionSummaryRecordDto buildConnectionList(Map<String, Object> conn) {
    final String owner = conn.get("owner").toString();

    return ConnectionSummaryRecordDto
        .builder()
        .gmcReferenceNumber(conn.get("gmcNumber").toString())
        .doctorFirstName(conn.get("forenames").toString())
        .doctorLastName(conn.get("surname").toString())
        .designatedBody(owner != null ? DesignatedBodyMapper.getDbcByOwner(owner) : null)
        .programmeMembershipEndDate(LocalDate.parse(conn.get("programmeEndDate").toString()))
        .programmeMembershipStartDate(LocalDate.parse(conn.get("programmeStartDate").toString()))
        .programmeMembershipType(conn.get("programmeMembershipType").toString())
        .programmeName(conn.get("programmeName").toString())
        .programmeOwner(conn.get("owner").toString())
        .build();
  }

  private String getHiddenConnectionStatus(final ConnectionDto conn) {
    final LocalDate currentDate = now();
    final boolean isConnected = Objects.isNull(conn.getProgrammeStartDate()) ||
        Objects.isNull(conn.getProgrammeEndDate()) ||
        conn.getProgrammeStartDate().isAfter(currentDate) ||
        conn.getProgrammeEndDate().isBefore(currentDate);
    return isConnected ? "Yes" : "No";
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
        String programmeOwner = programmeMembership.getProgramme().getOwner();
        connectionRecordDto.setProgrammeOwner(programmeOwner);
        connectionRecordDto
            .setDesignatedBodyCode(DesignatedBodyMapper.getDbcByOwner(programmeOwner));
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

    //tisPersonId - it is needed in the Reval FE to call different TCS api
    revalidationRecordDto.setTisPersonId(personId);

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
