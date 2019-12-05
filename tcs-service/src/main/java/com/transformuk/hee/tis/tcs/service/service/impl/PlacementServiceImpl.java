package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;
import static com.transformuk.hee.tis.tcs.service.api.util.DateUtil.getLocalDateFromString;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.isBetween;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSupervisorDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.*;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.event.PlacementDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PlacementSavedEvent;
import com.transformuk.hee.tis.tcs.service.exception.DateRangeColumnFilterException;
import com.transformuk.hee.tis.tcs.service.model.*;
import com.transformuk.hee.tis.tcs.service.repository.*;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonLiteMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementSiteMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementSpecialtyMapper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.transformuk.hee.tis.tcs.service.service.PlacementLogService;

/**
 * Service Implementation for managing Placement.
 */
@Service
@Transactional
public class PlacementServiceImpl implements PlacementService {

  static final String PLACEMENTS_SUMMARY_MAPPER = "PlacementsSummary";
  private static final String PLACEHOLDER_ROLE_NAME = "Placeholder";
  private final Logger log = LoggerFactory.getLogger(PlacementServiceImpl.class);

  @Autowired
  private PlacementRepository placementRepository;
  @Autowired
  private PlacementDetailsRepository placementDetailsRepository;
  @Autowired
  private PlacementMapper placementMapper;
  @Autowired
  private PlacementDetailsMapper placementDetailsMapper;
  @Autowired
  private SpecialtyRepository specialtyRepository;
  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;
  @Autowired
  private PlacementSpecialtyRepository placementSpecialtyRepository;
  @Autowired
  private PlacementSpecialtyMapper placementSpecialtyMapper;
  @Autowired
  private PlacementSiteMapper placementSiteMapper;
  @Autowired
  private PersonLiteMapper personLiteMapper;
  @Autowired
  private EsrNotificationService esrNotificationService;
  @Autowired
  private PlacementSupervisorRepository placementSupervisorRepository;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private ProgrammeRepository programmeRepository;
  @Autowired
  private Clock clock;
  @Autowired
  private PlacementLogService placementLogService;
  @Autowired
  private PermissionService permissionService;

  /**
   * Save a placement.
   *
   * @param placementDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PlacementDTO save(final PlacementDTO placementDTO) {
    log.debug("Request to save Placement : {}", placementDTO);
    Placement placement = placementMapper.placementDTOToPlacement(placementDTO);
    placement = placementRepository.save(placement);
    return convertPlacementWithSupervisors(placement);
  }

  @Override
  public Placement findPlacementById(Long placementId) {
    return placementRepository.findPlacementById(placementId).orElse(null);
  }

  @Override
  public PlacementDetailsDTO checkApprovalPermWhenCreate(final PlacementDetailsDTO placementDetailsDTO) {
    // if the user doesn't have `placement approve` perm, set the placement state to draft.
    // bulk_upload user always needs to send approved state
    if (permissionService.isUserNameBulkUpload()) {
      placementDetailsDTO.setLifecycleState(LifecycleState.APPROVED);
    } else if (!permissionService.canApprovePlacement()) {
      placementDetailsDTO.setLifecycleState(LifecycleState.DRAFT);
    } else if (placementDetailsDTO.getLifecycleState() == null) {
      placementDetailsDTO.setLifecycleState(LifecycleState.APPROVED);
    }
    return placementDetailsDTO;
  }

  @Override
  public PlacementDetailsDTO checkApprovalPermWhenUpdate(final PlacementDetailsDTO placementDetailsDTO) {
    if (permissionService.isUserNameBulkUpload()) {
      placementDetailsDTO.setLifecycleState(LifecycleState.APPROVED);
    } else if (!permissionService.canApprovePlacement()) {
      placementDetailsDTO.setLifecycleState(null); // null state means there's no change
      // currently the appoved placement can NOT go back to draft
    } else if (placementDetailsDTO.getLifecycleState() == LifecycleState.DRAFT){
      placementDetailsDTO.setLifecycleState(null);
    }
    return placementDetailsDTO;
  }

  @Transactional
  @Override
  public PlacementDetailsDTO createDetails(final PlacementDetailsDTO placementDetailsDTO) {

    log.debug("Request to create Placement : {}", placementDetailsDTO);

    // if this is an update and state is changed from draft to approved
    Placement placement = null;
    boolean newPlacementNotification = false;
    if (placementDetailsDTO.getId() != null) {
      placement = placementRepository.findById(placementDetailsDTO.getId())
          .orElse(null);
      if (placement != null && placement.getLifecycleState() == LifecycleState.DRAFT
          && placementDetailsDTO.getLifecycleState() == LifecycleState.APPROVED) {
        newPlacementNotification = true;
      }
    }

    PlacementDetails placementDetails = placementDetailsMapper
        .placementDetailsDTOToPlacementDetails(placementDetailsDTO);
    updateStoredCommentsWithChangesOrAdd(placementDetails);
    if (placementDetails.getId() == null) {
      placementDetails.setAddedDate(LocalDateTime.now(clock));
    } else {
      placementDetails.setAmendedDate(LocalDateTime.now(clock));
    }
    Set<PlacementSiteDTO> siteDTOsInPlacementDTO = placementDetailsDTO.getSites();
    Set<PlacementSite> siteModels = new HashSet<>();
    for (PlacementSiteDTO placementSiteDTO : siteDTOsInPlacementDTO) {
      PlacementSite placementSite = placementSiteMapper.toEntity(placementSiteDTO);
      placementSite.setPlacement(placementDetails);
      siteModels.add(placementSite);
    }
    placementDetails.setSites(siteModels);
    placementDetails = placementDetailsRepository.saveAndFlush(placementDetails);

    final Set<PlacementSpecialty> placementSpecialties = linkPlacementSpecialties(
        placementDetailsDTO, placementDetails);
    final PlacementDetailsDTO placementDetailsDTO1 = placementDetailsMapper
        .placementDetailsToPlacementDetailsDTO(placementDetails);
    placementDetailsDTO1.setSpecialties(placementSpecialtyMapper.toDTOs(placementSpecialties));

    if (placementDetailsDTO.getId() == null || newPlacementNotification ) {
      handleEsrNewPlacementNotification(placementDetailsDTO, placementDetails);
    }

    saveSupervisors(placementDetailsDTO.getSupervisors(), placementDetails.getId());

    Set<PlacementSiteDTO> sitesToReturnToFE = new HashSet<>();
    for (PlacementSite placementSite : siteModels) {
      PlacementSiteDTO placementSiteDTO = placementSiteMapper.toDto(placementSite);
      placementSiteDTO.setPlacementId(placementSite.getPlacement().getId());
      sitesToReturnToFE.add(placementSiteDTO);
    }
    placementDetailsDTO1.setSites(sitesToReturnToFE);

    placementLogService.placementLog(placementDetails, PlacementLogType.CREATE);

    return placementDetailsDTO1;
  }

  private void updateStoredCommentsWithChangesOrAdd(PlacementDetails placementDetails) {
    Set<Comment> commentsToPersist = new HashSet<>();

    Optional<Comment> optionalLatestComment = commentRepository
        .findFirstByPlacementIdOrderByAmendedDateDesc(placementDetails.getId());
    Comment latestComment = optionalLatestComment.orElse(new Comment());

    if (CollectionUtils.isNotEmpty(placementDetails.getComments())) {
      Comment comment = placementDetails.getComments().iterator().next();
      latestComment.setBody(comment.getBody());
      latestComment.setPlacement(placementDetails);
      latestComment.setAuthor(getProfileFromContext().getFullName());
      latestComment.setSource(comment.getSource());
      latestComment.setAmendedDate(LocalDate.now(clock));
      commentsToPersist.add(latestComment);
    }
    placementDetails.setComments(commentsToPersist);
  }

  @Override
  public boolean isEligibleForChangedDatesNotification(PlacementDetailsDTO updatedPlacementDetails,
      Placement existingPlacement) {

    if (existingPlacement.getLifecycleState() == LifecycleState.APPROVED) {

      if (existingPlacement != null && updatedPlacementDetails != null &&
          isEligibleForNotification(existingPlacement, updatedPlacementDetails)) {
        Optional<Post> optionalExistingPlacementPost = postRepository
            .findPostByPlacementHistoryId(existingPlacement.getId());

        log.debug("Change in hire or end date. Marking for notification : npn {} ",
            optionalExistingPlacementPost.isPresent() ? optionalExistingPlacementPost.get()
                .getNationalPostNumber() : null);
        return true;
      }
    }
    return false;
  }

  @Override
  public void handleChangeOfPlacementDatesEsrNotification(
      PlacementDetailsDTO updatedPlacementDetails, Placement placementBeforeUpdate,
      boolean currentPlacementEdit) {

    if (updatedPlacementDetails.getLifecycleState() == LifecycleState.APPROVED
      && placementBeforeUpdate != null && updatedPlacementDetails != null) {
      // create NOT1 type record. Current and next trainee details for the post number.
      // Create NOT4 type record
      log.debug("Change in hire or end date. Marking for notification : {} ",
          placementBeforeUpdate.getPost().getNationalPostNumber());
      try {
        esrNotificationService.loadChangeOfPlacementDatesNotification(updatedPlacementDetails,
            placementBeforeUpdate.getPost().getNationalPostNumber(), currentPlacementEdit);
      } catch (final Exception e) {
        log.error("Error loading Change of Placement Dates Notification : ", e);
      }
    }
  }

  @Transactional
  @Override
  public PlacementDetailsDTO saveDetails(final PlacementDetailsDTO placementDetailsDTO) {
    log.debug("Request to save Placement : {}", placementDetailsDTO);

    //clear any linked specialties before trying to save the placement
    final Placement placement = placementRepository.findById(placementDetailsDTO.getId())
        .orElse(null);

    // null means lifecycleState should remain the same, so set it with previous value
    if (placementDetailsDTO.getLifecycleState() == null) {
      placementDetailsDTO.setLifecycleState(placement.getLifecycleState());
    }

    //Instead of batch delete we need to unlink specialties from placement one by one
    Set<PlacementSpecialty> specialties = placement.getSpecialties();
    for (PlacementSpecialty specialty : specialties) {
      placementSpecialtyRepository.delete(specialty);
    }
    specialties.removeAll(specialties);
    PlacementDetails placementDetails = placementDetailsMapper
        .placementDetailsDTOToPlacementDetails(placementDetailsDTO);
    updateStoredCommentsWithChangesOrAdd(placementDetails);

    placement.setSpecialties(new HashSet<>());

    Placement savedPlacement = placementRepository.saveAndFlush(placement);
    PlacementDTO placementDTO = convertPlacementWithSupervisors(savedPlacement);
    applicationEventPublisher.publishEvent(new PlacementSavedEvent(placementDTO));

    return createDetails(placementDetailsDTO);
  }

  @Transactional
  protected Set<PlacementSpecialty> linkPlacementSpecialties(
      final PlacementDetailsDTO placementDetailsDTO, final PlacementDetails placementDetails) {
    final Placement placement = placementRepository.findById(placementDetails.getId()).orElse(null);
    final Set<PlacementSpecialty> placementSpecialties = Sets.newHashSet();
    if (CollectionUtils.isNotEmpty(placementDetailsDTO.getSpecialties())) {
      for (final PlacementSpecialtyDTO placementSpecialtyDTO : placementDetailsDTO
          .getSpecialties()) {
        final PlacementSpecialty placementSpecialty = new PlacementSpecialty();
        placementSpecialty.setPlacement(placement);
        placementSpecialty.setSpecialty(
            specialtyRepository.findById(placementSpecialtyDTO.getSpecialtyId()).orElse(null));
        placementSpecialty
            .setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());
        placementSpecialties.add(placementSpecialty);
      }
    }

    if (placement != null) {
      placement.setSpecialties(Sets.newHashSet(placementSpecialties));
      Placement savedPlacement = placementRepository.save(placement);
      PlacementDTO placementDTO = convertPlacementWithSupervisors(savedPlacement);
      applicationEventPublisher.publishEvent(new PlacementSavedEvent(placementDTO));
    }
    return placementSpecialties;
  }

  /**
   * Save a list of placements.
   *
   * @param placementDTO the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PlacementDTO> save(final List<PlacementDTO> placementDTO) {
    log.debug("Request to save Placements : {}", placementDTO);
    List<Placement> placements = placementMapper.placementDTOsToPlacements(placementDTO);
    placements = placementRepository.saveAll(placements);
    List<PlacementDTO> placementDTOS = convertPlacements(placements);

    placementDTO.stream()
        .map(PlacementSavedEvent::new)
        .forEach(applicationEventPublisher::publishEvent);

    return placementDTOS;
  }

  /**
   * Get all the placements.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PlacementDTO> findAll(final Pageable pageable) {
    log.debug("Request to get all Placements");
    final Page<Placement> result = placementRepository.findAll(pageable);
    return result.map(this::convertPlacementWithSupervisors);
  }

  /**
   * Get one placement by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PlacementDTO findOne(final Long id) {
    log.debug("Request to get Placement : {}", id);
    final Placement placement = placementRepository.findById(id).orElse(null);
    return this.convertPlacementWithSupervisors(placement);
  }

  @Override
  @Transactional(readOnly = true)
  public PlacementDetailsDTO getDetails(final Long id) {
    final PlacementDetails pd = placementDetailsRepository.findById(id).orElse(null);
    PlacementDetailsDTO placementDetailsDTO = null;

    if (pd != null) {
      placementDetailsDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(pd);

      String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PLACEMENT_DETAILS);
      MapSqlParameterSource specialtiesParamSource = new MapSqlParameterSource();
      specialtiesParamSource.addValue("id", id);
      final List<PlacementSpecialtyDTO> specialties = namedParameterJdbcTemplate
          .query(query, specialtiesParamSource, new PlacementDetailSpecialtyRowMapper());
      placementDetailsDTO.setSpecialties(Sets.newHashSet(specialties));

      query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PLACEMENT_SUPERVISOR);
      MapSqlParameterSource supervisorsParamSource = new MapSqlParameterSource();
      supervisorsParamSource.addValue("ids", id);
      final List<PlacementSupervisorDTO> supervisors = namedParameterJdbcTemplate
          .query(query, supervisorsParamSource,
              new PlacementDetailSupervisorRowMapper(new PersonRepositoryImpl.PersonLiteRowMapper(),
                  personLiteMapper));
      placementDetailsDTO.setSupervisors(Sets.newHashSet(supervisors));
    }

    return placementDetailsDTO;
  }

  /**
   * Delete the  placement by id.
   *
   * @param id the id of the entity
   */
  @Override
  @Transactional
  public void delete(final Long id) {
    log.debug("Request to delete Placement : {}", id);
    // handle esr notification
    handleEsrNotificationForPlacementDelete(id);

    placementSupervisorRepository.deleteAllByIdPlacementId(id);
    Placement placement = placementRepository.getOne(id);
    PlacementDeletedEvent event = new PlacementDeletedEvent(id, placement.getTrainee().getId());

    placementRepository.delete(placement);

    applicationEventPublisher.publishEvent(event);
  }

  private void handleEsrNotificationForPlacementDelete(final Long id) {
    final List<EsrNotification> allEsrNotifications = new ArrayList<>();

    final Placement placementToDelete = placementRepository.findById(id).orElse(null);
    if (placementToDelete.getLifecycleState() != LifecycleState.APPROVED) {
      return;
    }
    // Only future placements can be deleted.
    if (placementToDelete != null && placementToDelete.getDateFrom() != null && placementToDelete
        .getDateFrom().isBefore(LocalDate.now(clock).plusWeeks(13))) {
      final List<EsrNotification> esrNotifications = esrNotificationService
          .loadPlacementDeleteNotification(placementToDelete, allEsrNotifications);
      log.debug("Placement Delete: PERSISTING: {} EsrNotifications for post {} being deleted",
          esrNotifications.size(), placementToDelete.getLocalPostNumber());
      esrNotificationService.save(esrNotifications);
      log.debug("Placement Delete: PERSISTED: {} EsrNotifications for post {} being deleted",
          esrNotifications.size(), placementToDelete.getLocalPostNumber());
    }
  }

  private Map<String, Placement> getPlacementsByIntrepidId(
      final List<PlacementDTO> placementDtoList) {
    final Set<String> placementIntrepidIds = placementDtoList.stream()
        .map(PlacementDTO::getIntrepidId).collect(Collectors.toSet());
    final Set<Placement> placementsFound = placementRepository
        .findByIntrepidIdIn(placementIntrepidIds);
    Map<String, Placement> result = Maps.newHashMap();
    if (CollectionUtils.isNotEmpty(placementsFound)) {
      result = placementsFound.stream().collect(
          Collectors.toMap(Placement::getIntrepidId, post -> post)
      );
    }
    return result;
  }

  @Override
  public List<PlacementDTO> patchPlacementSpecialties(final List<PlacementDTO> placementDTOList) {
    final List<Placement> placements = Lists.newArrayList();
    final Map<String, Placement> intrepidIdToPlacement = getPlacementsByIntrepidId(
        placementDTOList);

    final Set<Long> specialtyIds = placementDTOList
        .stream()
        .map(PlacementDTO::getSpecialties)
        .flatMap(Collection::stream)
        .map(PlacementSpecialtyDTO::getSpecialtyId)
        .collect(Collectors.toSet());

    final Map<Long, Specialty> idToSpecialty = specialtyRepository.findAllById(specialtyIds)
        .stream().collect(Collectors.toMap(Specialty::getId, sp -> sp));
    for (final PlacementDTO dto : placementDTOList) {
      final Placement placement = intrepidIdToPlacement.get(dto.getIntrepidId());
      if (placement != null) {
        final Set<PlacementSpecialty> attachedSpecialties = placement.getSpecialties();
        final Set<Long> attachedSpecialtyIds = attachedSpecialties.stream()
            .map(ps -> ps.getSpecialty().getId()).collect(Collectors.toSet());
        for (final PlacementSpecialtyDTO placementSpecialtyDTO : dto.getSpecialties()) {
          final Specialty specialty = idToSpecialty.get(placementSpecialtyDTO.getSpecialtyId());
          if (specialty != null && !attachedSpecialtyIds.contains(specialty.getId())) {
            final PlacementSpecialty placementSpecialty = new PlacementSpecialty();
            placementSpecialty
                .setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());
            placementSpecialty.setPlacement(placement);
            placementSpecialty.setSpecialty(specialty);
            attachedSpecialties.add(placementSpecialty);
          }
        }
        placement.setSpecialties(attachedSpecialties);
        placements.add(placement);
      }
    }
    final List<Placement> savedPlacements = placementRepository.saveAll(placements);
    return convertPlacements(savedPlacements);
  }

  /**
   * Get all placement details by given column filters.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PlacementDetailsDTO> findAllPlacementDetails(final Pageable pageable) {
    log.debug("Request to get all Placements details");
    final Page<PlacementDetails> result = placementDetailsRepository.findAll(pageable);
    return result.map(placementDetailsMapper::placementDetailsToPlacementDetailsDTO);
  }

  /**
   * Get all placement details by given column filters.
   *
   * @param columnFilterJson column filters represented in json object
   * @param pageable         the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PlacementDetailsDTO> findFilteredPlacements(final String columnFilterJson,
      final Pageable pageable) throws IOException {

    log.debug("Request to get all Revalidations filtered by columns {}", columnFilterJson);
    final List<Class> filterEnumList = Collections.emptyList();
    final List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);

    final List<Specification<PlacementDetails>> specs = new ArrayList<>();

    //add the column filters criteria
    if (CollectionUtils.isNotEmpty(columnFilters)) {
      columnFilters.forEach(cf -> {
        if (TCSDateColumns.contains(cf.getName())) {
          if (cf.getValues().isEmpty() || cf.getValues().size() != 2) {
            throw new DateRangeColumnFilterException(
                "Invalid values or no values supplied for date range column filter");
          }
          specs.add(isBetween(
              cf.getName(),
              getLocalDateFromString(cf.getValues().get(0).toString()),
              getLocalDateFromString(cf.getValues().get(1).toString())
              )
          );
        } else {
          specs.add(in(cf.getName(), Collections.unmodifiableCollection(cf.getValues())));
        }
      });
    }
    final Page<PlacementDetails> result;
    if (!specs.isEmpty()) {
      Specifications<PlacementDetails> fullSpec = Specifications.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = placementDetailsRepository.findAll(fullSpec, pageable);
    } else {
      result = placementDetailsRepository.findAll(pageable);
    }

    return result.map(placementDetailsMapper::placementDetailsToPlacementDetailsDTO);
  }

  @Override
  public PlacementDTO closePlacement(final Long placementId) {
    Placement placement = placementRepository.findById(placementId).orElse(null);
    if (placement != null) {
      placement.setDateTo(LocalDate.now(clock).minusDays(1));
      placement = placementRepository.saveAndFlush(placement);
    }
    return convertPlacementWithSupervisors(placement);
  }

  @Transactional(readOnly = true)
  @Override
  public List<PlacementSummaryDTO> getPlacementForTrainee(final Long traineeId,
      String traineeRole) {
    final String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.TRAINEE_PLACEMENT_SUMMARY);
    List<PlacementSummaryDTO> resultList;

    Map<String, Object> params = Maps.newHashMap();
    params.put("traineeId", traineeId);
    resultList = namedParameterJdbcTemplate.query(query, params, new PlacementRowMapper());
    resultList
        .forEach(p -> p.setPlacementStatus(getPlacementStatus(p.getDateFrom(), p.getDateTo())));
    resultList = filterPlacements(resultList);

    if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(traineeRole, PLACEHOLDER_ROLE_NAME)
        && CollectionUtils.isNotEmpty(resultList)) {
      long now = new Date().getTime();
      resultList = resultList.stream()
          .filter(Objects::nonNull)
          .filter(p -> p.getDateTo() != null)
          .filter(p -> now < p.getDateTo().getTime())
          .collect(Collectors.toList());
    }

    return resultList;
  }

  @Transactional(readOnly = true)
  @Override
  public List<PlacementSummaryDTO> getPlacementForPost(final Long postId) {
    final String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_PLACEMENT_SUMMARY);
    List<PlacementSummaryDTO> resultList;

    Map<String, Object> params = Maps.newHashMap();
    params.put("postId", postId);
    PlacementRowMapper placementRowMapper = new PlacementRowMapper();
    resultList = namedParameterJdbcTemplate.query(query, params, placementRowMapper);
    resultList
        .forEach(p -> p.setPlacementStatus(getPlacementStatus(p.getDateFrom(), p.getDateTo())));
    resultList = filterPlacements(resultList);

    if (CollectionUtils.isNotEmpty(resultList) && resultList.size() > 1000) {
      resultList = resultList.subList(0, 1000);
    }

    return resultList;
  }

  @Transactional(readOnly = true)
  @Override
  public boolean validateOverlappingPlacements(String npn, LocalDate fromDate, LocalDate toDate, Long placementId) {
    List<Post> posts = postRepository.findByNationalPostNumber(npn);
    Set<Long> postIds = posts.stream().map(p -> p.getId()).collect(Collectors.toSet());
    Set<Placement> allPlacements = placementRepository.findPlacementsByPostIds(postIds);

    boolean ifOverlapping = false;
    for (Placement placement : allPlacements) {
      if (placementId != null && placement.getId().equals(placementId)) {
        continue;
      }
      if (placement.getDateFrom() != null && placement.getDateTo() != null) {
        if ((placement.getDateFrom().isBefore(toDate) || placement.getDateFrom().isEqual(toDate)) &&
            (placement.getDateTo().isAfter(fromDate) || placement.getDateTo().isEqual(fromDate))) {
          ifOverlapping = true;
          break;
        }
      }
    }
    // if overlapping exists, return true
    return ifOverlapping;
  }

  /**
   * Convert a single Placement entity into a PlacementDTO
   *
   * @param placement The placement entity to convert
   * @return the converted PlacementDTO
   */
  private PlacementDTO convertPlacementWithSupervisors(Placement placement) {
    if (placement != null) {
      String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PLACEMENT_SUPERVISOR);
      HashMap<String, Object> params = Maps.newHashMap();
      params.put("ids", placement.getId());
      List<PlacementSupervisorDTO> supervisors = namedParameterJdbcTemplate.query(query, params,
          new PlacementDetailSupervisorRowMapper(new PersonRepositoryImpl.PersonLiteRowMapper(),
              personLiteMapper));
      HashMap<Long, List<PlacementSupervisorDTO>> placementToSupervisor = Maps.newHashMap();
      placementToSupervisor.put(placement.getId(), supervisors);
      return placementMapper.placementToPlacementDTO(placement, placementToSupervisor);
    }
    return null;
  }

  /**
   * Convert Placements to DTOs
   * <p>
   * This also grabs all the placement supervisors for each placement by doing a single query and
   * converts that data too
   * <p>
   * The previous implementation would convert the placements and then do a db call for the
   * supervisor for that placement instance this caused memory issues as it was creating many
   * temporary objects and calling the database thousands of times for bucket posts
   *
   * @param placements The placements entities we wish to convert
   * @return List of converted PlacementDTOs
   */
  private List<PlacementDTO> convertPlacements(List<Placement> placements) {
    if (CollectionUtils.isNotEmpty(placements)) {
      Set<Long> placementIds = placements.stream().map(Placement::getId)
          .collect(Collectors.toSet());
      String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PLACEMENT_SUPERVISOR);
      HashMap<String, Object> params = Maps.newHashMap();
      params.put("ids", placementIds);
      List<PlacementSupervisorDTO> supervisors = namedParameterJdbcTemplate.query(query, params,
          new PlacementDetailSupervisorRowMapper(new PersonRepositoryImpl.PersonLiteRowMapper(),
              personLiteMapper));
      Map<Long, List<PlacementSupervisorDTO>> placementToSupervisors = new HashMap<>();
      for (PlacementSupervisorDTO supervisor : supervisors) {
        if (!placementToSupervisors.containsKey(supervisor.getPlacementId())) {
          placementToSupervisors.put(supervisor.getPlacementId(), new ArrayList<>());
        }

        List<PlacementSupervisorDTO> placementSupervisorDTOS = placementToSupervisors
            .get(supervisor.getPlacementId());
        placementSupervisorDTOS.add(supervisor);
      }

      return placementMapper.placementsToPlacementDTOs(placements, placementToSupervisors);
    }
    return Collections.EMPTY_LIST;
  }

  /**
   * this is a temporary method that filters out duplicates and preferring placements with
   * specialties of type primary this and its usage should be removed after the PUT/POST endpoints
   * to placements is updated with specialties
   */
  private List<PlacementSummaryDTO> filterPlacements(final List<PlacementSummaryDTO> resultList) {
    final Map<Long, PlacementSummaryDTO> idsToPlacementSummary = Maps.newHashMap();
    for (final PlacementSummaryDTO placementSummaryDTO : resultList) {

      final Long placementId = placementSummaryDTO.getPlacementId();
      if (!idsToPlacementSummary.containsKey(placementId) ||
          PostSpecialtyType.PRIMARY.name()
              .equals(placementSummaryDTO.getPlacementSpecialtyType())) {
        idsToPlacementSummary.put(placementId, placementSummaryDTO);
      }
    }

    List<PlacementSummaryDTO> placementSummaryDTOS = Lists
        .newArrayList(idsToPlacementSummary.values());

    placementSummaryDTOS.sort(new Comparator<PlacementSummaryDTO>() {
      @Override
      public int compare(final PlacementSummaryDTO o1, final PlacementSummaryDTO o2) {
        return ObjectUtils.compare(o2.getDateTo(), o1.getDateTo());
      }
    });

    return placementSummaryDTOS;
  }

  private String getPlacementStatus(final Date dateFrom, final Date dateTo) {

    if (dateFrom == null || dateTo == null) {
      return PlacementStatus.PAST.name();
    }

    // Truncating the hours,minutes,seconds
    final long from = DateUtils.truncate(dateFrom, Calendar.DATE).getTime();
    final long to = DateUtils.truncate(dateTo, Calendar.DATE).getTime();
    final long now = DateUtils.truncate(new Date(), Calendar.DATE).getTime();

    if (now < from) {
      return PlacementStatus.FUTURE.name();
    } else if (now > to) {
      return PlacementStatus.PAST.name();
    }
    return PlacementStatus.CURRENT.name();

  }

  private boolean isEligibleForNotification(final Placement currentPlacement,
      final PlacementDetailsDTO updatedPlacementDetails) {
    // I really do not like this null checks :-( but keeping it to work around the data from intrepid
    return
        ((currentPlacement.getDateFrom() != null && !currentPlacement.getDateFrom()
            .equals(updatedPlacementDetails.getDateFrom())) ||
            (currentPlacement.getDateTo() != null && !currentPlacement.getDateTo()
                .equals(updatedPlacementDetails.getDateTo()))) &&
            ((currentPlacement.getDateFrom() != null && currentPlacement.getDateFrom()
                .isBefore(LocalDate.now(clock).plusWeeks(13))) ||
                (updatedPlacementDetails.getDateFrom() != null && updatedPlacementDetails
                    .getDateFrom()
                    .isBefore(LocalDate.now(clock).plusWeeks(13))));
  }

  private void handleEsrNewPlacementNotification(final PlacementDetailsDTO placementDetailsDTO,
      final PlacementDetails placementDetails) {

    if (placementDetailsDTO.getLifecycleState() != LifecycleState.APPROVED) {
      return;
    }

    log.debug("Handling ESR notifications for new placement creation for deanery number {}",
        placementDetailsDTO.getLocalPostNumber());
    try {
      final Placement savedPlacement = placementRepository.findById(placementDetails.getId())
          .orElse(null);
      if (savedPlacement.getDateFrom() != null && savedPlacement.getDateFrom()
          .isBefore(LocalDate.now(clock).plusWeeks(13))) {
        log.debug("Creating ESR notification for new placement creation for deanery number {}",
            savedPlacement.getPost().getNationalPostNumber());
        final List<EsrNotification> esrNotifications = esrNotificationService
            .handleNewPlacementEsrNotification(savedPlacement);
        log
            .debug(
                "CREATED: ESR {} notifications for new placement creation for deanery number {}",
                esrNotifications.size(), savedPlacement.getPost().getNationalPostNumber());
      }
    } catch (final Exception e) {
      // Ideally it should fail the entire update. Keeping the impact minimal for TCS and go live and revisit after go live.
      // Log and continue
      log.error("Error loading New Placement Notification : ", e);
    }
  }

  private void saveSupervisors(final Set<PlacementSupervisorDTO> supervisorDTOs,
      final Long placementId) {
    placementSupervisorRepository.deleteAllByIdPlacementId(placementId);

    final Set<PlacementSupervisor> supervisors = new HashSet<>();

    supervisorDTOs.forEach(s -> supervisors
        .add(new PlacementSupervisor(placementId, s.getPerson().getId(), s.getType())));

    placementSupervisorRepository.saveAll(supervisors);
  }

  class PlacementDetailSpecialtyRowMapper implements RowMapper<PlacementSpecialtyDTO> {

    @Override
    public PlacementSpecialtyDTO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
      final PlacementSpecialtyDTO result = new PlacementSpecialtyDTO();
      result.setPlacementId(rs.getLong("placementId"));
      result.setSpecialtyId(rs.getLong("specialtyId"));
      final String placementSpecialtyType = rs.getString("placementSpecialtyType");
      PostSpecialtyType postSpecialtyType = null;
      if (StringUtils.isNotBlank(placementSpecialtyType)) {
        postSpecialtyType = PostSpecialtyType.valueOf(placementSpecialtyType);
      }
      result.setPlacementSpecialtyType(postSpecialtyType);
      return result;
    }
  }

  @Override
  @Transactional
  public long approveAllPlacementsByProgrammeId(Long programmeId) {
    if (!permissionService.canApprovePlacement()) {
      return 0;
    }
    List<Placement> draftPlacements = getDraftPlacementsByProgrammeId(programmeId);

    if (draftPlacements.size() > 0) {
      List<PlacementDetails> placementDetailsList = placementsToPlacementDetails(draftPlacements);
      placementDetailsList.forEach(placementDetails -> {
        placementDetails.setLifecycleState(LifecycleState.APPROVED);
        placementDetailsRepository.saveAndFlush(placementDetails);
        PlacementDetailsDTO placementDetailsDTO =
            placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placementDetails);
        handleEsrNewPlacementNotification(placementDetailsDTO, placementDetails);
      });
    }
    return draftPlacements.size();
  }

  private List<PlacementDetails> placementsToPlacementDetails(List<Placement> placements) {
    List<PlacementDetails> placementDetailsList = new ArrayList<>();
    placements.forEach(placement -> {
      Optional<PlacementDetails> placementDetails = placementDetailsRepository.findById(placement.getId());
      if (placementDetails.isPresent()) {
        placementDetailsList.add(placementDetails.get());
      }
    });
    return placementDetailsList;
  }

  @Override
  @Transactional
  public long getCountOfDraftPlacementsByProgrammeId(Long programmeId) {
    return getDraftPlacementsByProgrammeId(programmeId).size();
  }

  private List<Placement> getDraftPlacementsByProgrammeId(Long programmeId) {
    Optional<Programme> programme = programmeRepository.findById(programmeId);
    List<Placement> draftPlacements = new ArrayList<>();
    if (programme.isPresent()) {
      programme.get().getPosts().stream()
          .forEach(post -> {
            if (post.getStatus() == Status.CURRENT) { // only deal with current Posts
              post.getPlacementHistory().forEach(placement -> {
                if (placement.getLifecycleState() == LifecycleState.DRAFT) {
                  draftPlacements.add(placement);
                }
              });
            }
          });
    }
    return draftPlacements;
  }
}
