package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDetailDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionSummaryRecordDto.ConnectionSummaryRecordDtoBuilder;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMembershipMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.RevalidationRecordMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class RevalidationServiceImpl implements RevalidationService {

  private static final String PERSON_ID_FIELD = "personId";
  private static final String CONNECTION_OWNER = "owner";
  private static final String CURRICULUM_END_DATE_FIELD = "curriculumEndDate";
  private static final String GMC_NUMBER_FIELD = "gmcNumber";
  private static final String FORENAMES_FIELD = "forenames";
  private static final String PROGRAMME_END_DATE_FIELD = "programmeEndDate";
  private static final String PROGRAMME_MEMBERSHIP_TYPE_FIELD = "programmeMembershipType";
  private static final String PROGRAMME_NAME_FIELD = "programmeName";
  private static final String PROGRAMME_START_DATE_FIELD = "programmeStartDate";
  private static final String SURNAME_FIELD = "surname";
  private static final String PLACEMENT_GRADE_FIELD = "currentGrades";
  /**
   * This RegEx matches strings in format of WHERECLAUSE(p, id).
   * Whitespaces are allowed in the brackets.
   * and it also recognises p and id as parenthesized match sub patterns.
   */
  private static final String WHERE_CLAUSE_MACRO_REGEX =
      "WHERECLAUSE\\(\\s*(\\w+?)\\s*,\\s*(\\w+?)\\s*\\)";
  public static final int SIZE = 20;
  private static final Logger LOG = LoggerFactory.getLogger(RevalidationServiceImpl.class);
  private static final List<String> placementTypes = asList("In post", "In Post - Acting Up",
      "In post - Extension", "Parental Leave", "Long-term sick", "Suspended", "Phased Return");
  private final GmcDetailsRepository gmcDetailsRepository;
  private final CurriculumMembershipRepository curriculumMembershipRepository;
  private final PlacementRepository placementRepository;
  private final ReferenceService referenceService;
  private final PersonRepository personRepository;
  private final CurriculumMembershipMapper curriculumMembershipMapper;
  private final RevalidationRecordMapper revalidationRecordMapper;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SqlQuerySupplier sqlQuerySupplier;

  public RevalidationServiceImpl(
      GmcDetailsRepository gmcDetailsRepository,
      CurriculumMembershipRepository curriculumMembershipRepository,
      PlacementRepository placementRepository,
      ReferenceService referenceService,
      PersonRepository personRepository,
      CurriculumMembershipMapper curriculumMembershipMapper,
      RevalidationRecordMapper revalidationRecordMapper,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      SqlQuerySupplier sqlQuerySupplier) {
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.curriculumMembershipRepository = curriculumMembershipRepository;
    this.placementRepository = placementRepository;
    this.referenceService = referenceService;
    this.personRepository = personRepository;
    this.curriculumMembershipMapper = curriculumMembershipMapper;
    this.revalidationRecordMapper = revalidationRecordMapper;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.sqlQuerySupplier = sqlQuerySupplier;
  }

  @Override
  public RevalidationRecordDto findRevalidationByGmcId(final String gmcId) {
    LOG.debug("GMC No received from Revalidation application: {}", gmcId);
    GmcDetails gmcDetails = gmcDetailsRepository.findGmcDetailsByGmcNumber(gmcId);
    if (gmcDetails == null) {
      return null;
    }
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

      LOG.info("Searching Curriculum membership for person: {}", gmcDetail.getId());

      final CurriculumMembership curriculumMembership = curriculumMembershipRepository
          .findLatestCurriculumMembershipByTraineeId(gmcDetail.getId());

      final ConnectionRecordDto connectionRecordDto = getConnectionStatus(curriculumMembership);
      connectionRecordDtoMap.put(gmcDetail.getGmcNumber(), connectionRecordDto);
    });
    return connectionRecordDtoMap;
  }

  @Override
  public ConnectionDetailDto findAllConnectionsHistoryByGmcId(String gmcId) {
    ConnectionDetailDto connectionDetailDto = new ConnectionDetailDto();

    LOG.debug("GMCNo received from Connection History service: {}", gmcId);
    final GmcDetails gmcDetail = gmcDetailsRepository.findGmcDetailsByGmcNumber(gmcId);
    if (gmcDetail == null) {
      return null;
    }
    final RevalidationRecordDto revalidationRecordDto = buildRevalidationRecord(gmcDetail);

    connectionDetailDto.setGmcNumber(revalidationRecordDto.getGmcNumber());
    connectionDetailDto.setForenames(revalidationRecordDto.getForenames());
    connectionDetailDto.setSurname(revalidationRecordDto.getSurname());
    connectionDetailDto.setCurriculumEndDate(revalidationRecordDto.getCurriculumEndDate());
    connectionDetailDto
        .setProgrammeMembershipType(revalidationRecordDto.getProgrammeMembershipType());
    connectionDetailDto.setProgrammeName(revalidationRecordDto.getProgrammeName());
    connectionDetailDto.setCurrentGrade(revalidationRecordDto.getCurrentGrade());

    final List<CurriculumMembership> curriculumMemberships = curriculumMembershipRepository
        .findAllCurriculumMembershipInDescOrderByTraineeId(gmcDetail.getId());
    LOG.info("Curriculum memberships found for person: {}, membership: {}", gmcDetail.getId(),
        curriculumMemberships);

    List<ConnectionRecordDto> programmeHistory = curriculumMemberships.stream()
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

  @Override
  public ConnectionInfoDto buildTcsConnectionInfo(Long personId) {

    // $1 and $2 will be injected with the parenthesized match sub patterns from the RegEx
    final String whereClause = String.format("where $1.$2 = %s", personId);

    final String query = sqlQuerySupplier
        .getQuery(SqlQuerySupplier.TRAINEE_CONNECTION_INFO)
        .replaceAll(WHERE_CLAUSE_MACRO_REGEX, whereClause)
        .replace("ORDERBYCLAUSE", "")
        .replace("LIMITCLAUSE", "");

    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    List<ConnectionInfoDto> connectionInfoDtos = namedParameterJdbcTemplate
        .query(query, paramSource, new RevalidationConnectionInfoMapper());
    if (connectionInfoDtos.size() == 1) {
      return connectionInfoDtos.get(0);
    } else {
      return null;
    }
  }

  @Override
  public List<ConnectionInfoDto> extractConnectionInfoForSync() {
    final String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.TRAINEE_CONNECTION_INFO)
        .replaceAll(WHERE_CLAUSE_MACRO_REGEX, "")
        .replace("ORDERBYCLAUSE", "")
        .replace("LIMITCLAUSE", "");
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    return namedParameterJdbcTemplate
        .query(query, paramSource, new RevalidationConnectionInfoMapper());
  }

  private ConnectionSummaryRecordDto buildHiddenConnectionList(ConnectionDto conn) {
    final CurriculumMembership latestCurriculumMembership = curriculumMembershipRepository
        .findLatestCurriculumMembershipByTraineeId(conn.getPersonId());
    final ProgrammeMembershipDTO programmeMembershipDTO = curriculumMembershipMapper
        .toDto(latestCurriculumMembership);

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

  private ConnectionSummaryRecordDto buildConnectionList(Map<String, Object> conn) {
    final String owner =
        conn.get(CONNECTION_OWNER) == null ? null : conn.get(CONNECTION_OWNER).toString();

    return ConnectionSummaryRecordDto
        .builder()
        .gmcReferenceNumber(
            conn.get(GMC_NUMBER_FIELD) == null ? null : conn.get(GMC_NUMBER_FIELD).toString()
        )
        .doctorFirstName(
            conn.get(FORENAMES_FIELD) == null ? null : conn.get(FORENAMES_FIELD).toString()
        )
        .doctorLastName(conn.get(SURNAME_FIELD) == null ? null : conn.get(SURNAME_FIELD).toString())
        .designatedBody(owner != null ? DesignatedBodyMapper.getDbcByOwner(owner) : null)
        .programmeMembershipEndDate(conn.get(PROGRAMME_END_DATE_FIELD) == null
            ? null : LocalDate.parse(conn.get(PROGRAMME_END_DATE_FIELD).toString()))
        .programmeMembershipStartDate(conn.get(PROGRAMME_START_DATE_FIELD) == null
            ? null : LocalDate.parse(conn.get(PROGRAMME_START_DATE_FIELD).toString()))
        .programmeMembershipType(conn.get(PROGRAMME_MEMBERSHIP_TYPE_FIELD) == null
            ? null : conn.get(PROGRAMME_MEMBERSHIP_TYPE_FIELD).toString())
        .programmeName(conn.get(PROGRAMME_NAME_FIELD) == null
            ? null : conn.get(PROGRAMME_NAME_FIELD).toString())
        .programmeOwner(owner)
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

  private ConnectionRecordDto getConnectionStatus(final CurriculumMembership curriculumMembership) {
    final ConnectionRecordDto connectionRecordDto = new ConnectionRecordDto();
    connectionRecordDto.setConnectionStatus("No");
    final LocalDate currentDate = now();

    if (Objects.nonNull(curriculumMembership)) {

      LOG.info("Curriculum membership found membership: {}", curriculumMembership.getId());

      if (!isDisconnected(currentDate, curriculumMembership)) {
        connectionRecordDto.setConnectionStatus("Yes");
      }
      ProgrammeMembership programmeMembership = curriculumMembership.getProgrammeMembership();
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

  private boolean isDisconnected(LocalDate currentDate, CurriculumMembership curriculumMembership) {
    if (Objects.isNull(curriculumMembership)) {
      return true;
    }
    ProgrammeMembership programmeMembership = curriculumMembership.getProgrammeMembership();
    return Objects.isNull(programmeMembership)
        || Objects.isNull(programmeMembership.getProgrammeStartDate())
        || Objects.isNull(programmeMembership.getProgrammeEndDate())
        || programmeMembership.getProgrammeStartDate().isAfter(currentDate)
        || programmeMembership.getProgrammeEndDate().isBefore(currentDate);
  }

  private RevalidationRecordDto buildRevalidationRecord(GmcDetails gmcDetails) {
    RevalidationRecordDto revalidationRecordDto = new RevalidationRecordDto();
    revalidationRecordDto.setGmcNumber(gmcDetails.getGmcNumber());

    final long personId = gmcDetails.getId();
    LOG.debug("Person ID : {}", personId);

    ConnectionInfoDto connectionInfoDto = buildTcsConnectionInfo(personId);
    revalidationRecordMapper.toRevalidationRecord(connectionInfoDto, revalidationRecordDto);

    //Placement
    List<Placement> currentPlacementsForTrainee = placementRepository
        .findCurrentPlacementForTrainee(personId, now(), placementTypes);

    if (CollectionUtils.isNotEmpty(currentPlacementsForTrainee)
        && currentPlacementsForTrainee.get(0).getGradeId() != null) {
      LOG.debug("Placement ID : {}", currentPlacementsForTrainee.get(0).getId());
      Long gradeId = currentPlacementsForTrainee.get(0).getGradeId();
      LOG.debug("GRADE ID : {}", gradeId);
      List<GradeDTO> grades = referenceService.findGradesIdIn(Collections.singleton(gradeId));
      grades.forEach(gradeDTO -> revalidationRecordDto.setCurrentGrade(gradeDTO.getName()));
    }
    return revalidationRecordDto;
  }

  private class RevalidationConnectionInfoMapper implements RowMapper<ConnectionInfoDto> {

    @Override
    public ConnectionInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
      String owner = rs.getString(CONNECTION_OWNER);
      LocalDate start;
      LocalDate end;
      LocalDate curriculumEnd;
      try {
        start = rs.getDate(PROGRAMME_START_DATE_FIELD).toLocalDate();
        end = rs.getDate(PROGRAMME_END_DATE_FIELD).toLocalDate();
        curriculumEnd = rs.getDate(CURRICULUM_END_DATE_FIELD).toLocalDate();

      } catch (Exception e) {
        start = null;
        end = null;
        curriculumEnd = null;
      }
      return ConnectionInfoDto.builder()
          .tcsPersonId(rs.getLong(PERSON_ID_FIELD))
          .gmcReferenceNumber(rs.getString(GMC_NUMBER_FIELD))
          .doctorFirstName(rs.getString(FORENAMES_FIELD))
          .doctorLastName(rs.getString(SURNAME_FIELD))
          .programmeName(rs.getString(PROGRAMME_NAME_FIELD))
          .programmeMembershipType(rs.getString(PROGRAMME_MEMBERSHIP_TYPE_FIELD))
          .tcsDesignatedBody(owner != null ? DesignatedBodyMapper.getDbcByOwner(owner) : null)
          .programmeOwner(owner)
          .programmeMembershipStartDate(start)
          .programmeMembershipEndDate(end)
          .curriculumEndDate(curriculumEnd)
          .placementGrade(PLACEMENT_GRADE_FIELD)
          .dataSource("TCS")
          .build();
    }
  }
}
