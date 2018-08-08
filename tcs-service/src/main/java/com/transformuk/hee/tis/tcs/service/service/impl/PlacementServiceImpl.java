package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSupervisorDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.TCSDateColumns;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.exception.DateRangeColumnFilterException;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PlacementSupervisor;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepositoryImpl;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementSupervisorRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonLiteMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementSpecialtyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.api.util.DateUtil.getLocalDateFromString;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.isBetween;

/**
 * Service Implementation for managing Placement.
 */
@Service
@Transactional
public class PlacementServiceImpl implements PlacementService {

    public static final String PLACEMENTS_SUMMARY_MAPPER = "PlacementsSummary";
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
    private SpecialtyMapper specialtyMapper;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PlacementViewRepository placementViewRepository;
    @Autowired
    private PlacementViewMapper placementViewMapper;
    @Autowired
    private EntityManager em;
    @Autowired
    private SqlQuerySupplier sqlQuerySupplier;
    @Autowired
    private PlacementSpecialtyRepository placementSpecialtyRepository;
    @Autowired
    private PlacementSpecialtyMapper placementSpecialtyMapper;
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
        return placementMapper.placementToPlacementDTO(placement);
    }

    @Override
    public Placement findPlacementById(Long placementId) {
      return placementRepository.findOne(placementId);
    }

    @Transactional
    @Override
    public PlacementDetailsDTO createDetails(final PlacementDetailsDTO placementDetailsDTO) {
        log.debug("Request to create Placement : {}", placementDetailsDTO);
        PlacementDetails placementDetails = placementDetailsMapper.placementDetailsDTOToPlacementDetails(placementDetailsDTO);
        updateStoredCommentsWithChangesOrAdd(placementDetails);
        placementDetails = placementDetailsRepository.saveAndFlush(placementDetails);

        final Set<PlacementSpecialty> placementSpecialties = linkPlacementSpecialties(placementDetailsDTO, placementDetails);
        final PlacementDetailsDTO placementDetailsDTO1 = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placementDetails);
        placementDetailsDTO1.setSpecialties(placementSpecialtyMapper.toDTOs(placementSpecialties));
        handleEsrNewPlacementNotification(placementDetailsDTO, placementDetails);

        saveSupervisors(placementDetailsDTO.getSupervisors(), placementDetails.getId());

        return placementDetailsDTO1;
    }

  private void updateStoredCommentsWithChangesOrAdd(PlacementDetails placementDetails) {
      Set<Comment> commentsToPersist = new HashSet<>();
      for(Comment comment : placementDetails.getComments()) {
        if(comment.getId() != null) {
          Comment commentSaved = commentRepository.findOne(comment.getId());
          commentSaved.setBody(comment.getBody());
          commentSaved.setPlacement(placementDetails);
          commentSaved.setAuthor(comment.getAuthor());
          commentSaved.setSource(comment.getSource());
          commentsToPersist.add(commentSaved);
        } else {
          comment.setPlacement(placementDetails);
          commentsToPersist.add(comment);
        }
      }
      placementDetails.setComments(commentsToPersist);
  }

  @Override
  public boolean isEligibleForChangedDatesNotification(PlacementDetailsDTO updatedPlacementDetails, Placement existingPlacement) {

    if (existingPlacement != null && updatedPlacementDetails != null &&
        isEligibleForNotification(existingPlacement, updatedPlacementDetails)) {
      log.info("Change in hire or end date. Marking for notification : npn {} ",
          existingPlacement.getPost() != null ? existingPlacement.getPost().getNationalPostNumber() : null);
      return true;
    }
    return false;
  }

  @Override
  public void handleChangeOfPlacementDatesEsrNotification(PlacementDetailsDTO updatedPlacementDetails, Placement placementBeforeUpdate, boolean currentPlacementEdit) {

    if (placementBeforeUpdate != null && updatedPlacementDetails != null ) {
      // create NOT1 type record. Current and next trainee details for the post number.
      // Create NOT4 type record
      log.info("Change in hire or end date. Marking for notification : {} ", placementBeforeUpdate.getPost().getNationalPostNumber());
      try {
        esrNotificationService.loadChangeOfPlacementDatesNotification(updatedPlacementDetails, placementBeforeUpdate.getPost().getNationalPostNumber(), currentPlacementEdit);
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
        final Placement placement = placementRepository.findOne(placementDetailsDTO.getId());
        placementSpecialtyRepository.delete(placement.getSpecialties());
        placement.setSpecialties(new HashSet<>());
        placementRepository.saveAndFlush(placement);

        return createDetails(placementDetailsDTO);
    }

    @Transactional
    private Set<PlacementSpecialty> linkPlacementSpecialties(final PlacementDetailsDTO placementDetailsDTO, final PlacementDetails placementDetails) {
        final Placement placement = placementRepository.findOne(placementDetails.getId());
        final Set<PlacementSpecialty> placementSpecialties = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(placementDetailsDTO.getSpecialties())) {
            for (final PlacementSpecialtyDTO placementSpecialtyDTO : placementDetailsDTO.getSpecialties()) {
                final PlacementSpecialty placementSpecialty = new PlacementSpecialty();
                placementSpecialty.setPlacement(placement);
                placementSpecialty.setSpecialty(specialtyRepository.findOne(placementSpecialtyDTO.getSpecialtyId()));
                placementSpecialty.setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());
                placementSpecialties.add(placementSpecialty);
            }
        }

        placement.setSpecialties(Sets.newHashSet(placementSpecialties));
        placementRepository.save(placement);
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
        placements = placementRepository.save(placements);
        return placementMapper.placementsToPlacementDTOs(placements);
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
        return result.map(placementMapper::placementToPlacementDTO);
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
        final Placement placement = placementRepository.findOne(id);
        return placementMapper.placementToPlacementDTO(placement);
    }

    @Override
    @Transactional(readOnly = true)
    public PlacementDetailsDTO getDetails(final Long id) {
        final PlacementDetails pd = placementDetailsRepository.findOne(id);
        PlacementDetailsDTO placementDetailsDTO = null;

        if (pd != null) {
            placementDetailsDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(pd);

            String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PLACEMENT_DETAILS);
            MapSqlParameterSource specialtiesParamSource = new MapSqlParameterSource();
            specialtiesParamSource.addValue("id",id);
            final List<PlacementSpecialtyDTO> specialties = namedParameterJdbcTemplate.query(query,specialtiesParamSource, new PlacementDetailSpecialtyRowMapper());
            placementDetailsDTO.setSpecialties(Sets.newHashSet(specialties));

            query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PLACEMENT_SUPERVISOR);
            MapSqlParameterSource supervisorsParamSource = new MapSqlParameterSource();
            supervisorsParamSource.addValue("id",id);
            final List<PlacementSupervisorDTO> supervisors = namedParameterJdbcTemplate.query(query,supervisorsParamSource, new PlacementDetailSupervisorRowMapper(new PersonRepositoryImpl.PersonLiteRowMapper(), personLiteMapper));
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

        placementRepository.delete(id);
    }

    private void handleEsrNotificationForPlacementDelete(final Long id) {
        final List<EsrNotification> allEsrNotifications = new ArrayList<>();

        final Placement placementToDelete = placementRepository.findOne(id);
        // Only future placements can be deleted.
        if (placementToDelete != null && placementToDelete.getDateFrom() != null && placementToDelete.getDateFrom().isBefore(LocalDate.now().plusMonths(3))) {
            final List<EsrNotification> esrNotifications = esrNotificationService.loadPlacementDeleteNotification(placementToDelete, allEsrNotifications);
            log.info("Placement Delete: PERSISTING: {} EsrNotifications for post {} being deleted", esrNotifications.size(), placementToDelete.getLocalPostNumber());
            esrNotificationService.save(esrNotifications);
            log.info("Placement Delete: PERSISTED: {} EsrNotifications for post {} being deleted", esrNotifications.size(), placementToDelete.getLocalPostNumber());
        }
    }

    private Map<String, Placement> getPlacementsByIntrepidId(final List<PlacementDTO> placementDtoList) {
        final Set<String> placementIntrepidIds = placementDtoList.stream().map(PlacementDTO::getIntrepidId).collect(Collectors.toSet());
        final Set<Placement> placementsFound = placementRepository.findByIntrepidIdIn(placementIntrepidIds);
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
        final Map<String, Placement> intrepidIdToPlacement = getPlacementsByIntrepidId(placementDTOList);

        final Set<Long> specialtyIds = placementDTOList
                .stream()
                .map(PlacementDTO::getSpecialties)
                .flatMap(Collection::stream)
                .map(PlacementSpecialtyDTO::getSpecialtyId)
                .collect(Collectors.toSet());

        final Map<Long, Specialty> idToSpecialty = specialtyRepository.findAll(specialtyIds).stream().collect(Collectors.toMap(Specialty::getId, sp -> sp));
        for (final PlacementDTO dto : placementDTOList) {
            final Placement placement = intrepidIdToPlacement.get(dto.getIntrepidId());
            if (placement != null) {
                final Set<PlacementSpecialty> attachedSpecialties = placement.getSpecialties();
                final Set<Long> attachedSpecialtyIds = attachedSpecialties.stream().map(ps -> ps.getSpecialty().getId()).collect(Collectors.toSet());
                for (final PlacementSpecialtyDTO placementSpecialtyDTO : dto.getSpecialties()) {
                    final Specialty specialty = idToSpecialty.get(placementSpecialtyDTO.getSpecialtyId());
                    if (specialty != null && !attachedSpecialtyIds.contains(specialty.getId())) {
                        final PlacementSpecialty placementSpecialty = new PlacementSpecialty();
                        placementSpecialty.setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());
                        placementSpecialty.setPlacement(placement);
                        placementSpecialty.setSpecialty(specialty);
                        attachedSpecialties.add(placementSpecialty);
                    }
                }
                placement.setSpecialties(attachedSpecialties);
                placements.add(placement);
            }
        }
        final List<Placement> savedPlacements = placementRepository.save(placements);
        return placementMapper.placementsToPlacementDTOs(savedPlacements);
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
     * @throws IOException
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PlacementDetailsDTO> findFilteredPlacements(final String columnFilterJson, final Pageable pageable) throws IOException {

        log.debug("Request to get all Revalidations filtered by columns {}", columnFilterJson);
        final List<Class> filterEnumList = Collections.emptyList();
        final List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);

        final List<Specification<PlacementDetails>> specs = new ArrayList<>();

        //add the column filters criteria
        if (CollectionUtils.isNotEmpty(columnFilters)) {
            columnFilters.forEach(cf -> {
                if (TCSDateColumns.contains(cf.getName())) {
                    if (cf.getValues().isEmpty() || cf.getValues().size() != 2) {
                        throw new DateRangeColumnFilterException("Invalid values or no values supplied for date range column filter");
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
        Placement placement = placementRepository.findOne(placementId);
        if (placement != null) {
            placement.setDateTo(LocalDate.now().minusDays(1));
            placement = placementRepository.saveAndFlush(placement);
        }
        return placementMapper.placementToPlacementDTO(placement);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PlacementSummaryDTO> getPlacementForTrainee(final Long traineeId) {
        final String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.TRAINEE_PLACEMENT_SUMMARY);

        final Query traineePlacementsQuery = em.createNativeQuery(query, PLACEMENTS_SUMMARY_MAPPER)
                .setParameter("traineeId", traineeId);
        // TODO: uncomment this when changes to the FE adds a specialty on creation
//        .setParameter("specialtyType", PostSpecialtyType.PRIMARY.name());
        List<PlacementSummaryDTO> resultList = traineePlacementsQuery.getResultList();
        resultList.forEach(p -> p.setPlacementStatus(getPlacementStatus(p.getDateFrom(), p.getDateTo())));

        resultList = filterPlacements(resultList);
        return resultList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PlacementSummaryDTO> getPlacementForPost(final Long postId) {
        final String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_PLACEMENT_SUMMARY);
        final Query postPlacementsQuery = em.createNativeQuery(query, PLACEMENTS_SUMMARY_MAPPER)
                .setParameter("postId", postId);
        // TODO: uncomment this when changes to the FE adds a specialty on creation
//        .setParameter("specialtyType", PostSpecialtyType.PRIMARY.name());
        List<PlacementSummaryDTO> resultList = postPlacementsQuery.getResultList();
        resultList.forEach(p -> p.setPlacementStatus(getPlacementStatus(p.getDateFrom(), p.getDateTo())));
        resultList = filterPlacements(resultList);
        return resultList;
    }

    /**
     * this is a temporary method that filters out duplicates and preferring placements with specialties of type primary
     * this and its usage should be removed after the PUT/POST endpoints to placements is updated with specialties
     *
     * @param resultList
     * @return
     */
    private List<PlacementSummaryDTO> filterPlacements(final List<PlacementSummaryDTO> resultList) {
        final Map<BigInteger, PlacementSummaryDTO> idsToPlacementSummary = Maps.newHashMap();
        for (final PlacementSummaryDTO placementSummaryDTO : resultList) {

            final BigInteger placementId = placementSummaryDTO.getPlacementId();
            if (!idsToPlacementSummary.containsKey(placementId) ||
                    PostSpecialtyType.PRIMARY.name().equals(placementSummaryDTO.getPlacementSpecialtyType())) {
                idsToPlacementSummary.put(placementId, placementSummaryDTO);
            }
        }

        final List<PlacementSummaryDTO> placementSummaryDTOS = Lists.newArrayList(idsToPlacementSummary.values());
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

    private boolean isEligibleForNotification(final Placement currentPlacement, final PlacementDetailsDTO updatedPlacementDetails) {
        // I really do not like this null checks :-( but keeping it to work around the data from intrepid
        return
                ((currentPlacement.getDateFrom() != null && !currentPlacement.getDateFrom().equals(updatedPlacementDetails.getDateFrom())) ||
                        (currentPlacement.getDateTo() != null && !currentPlacement.getDateTo().equals(updatedPlacementDetails.getDateTo()))) &&
                        ((currentPlacement.getDateFrom() != null && currentPlacement.getDateFrom().isBefore(LocalDate.now().plusMonths(3))) ||
                                (updatedPlacementDetails.getDateFrom() != null && updatedPlacementDetails.getDateFrom().isBefore(LocalDate.now().plusMonths(3))));
    }

    private void handleEsrNewPlacementNotification(final PlacementDetailsDTO placementDetailsDTO, final PlacementDetails placementDetails) {

        log.info("Handling ESR notifications for new placement creation for deanery number {}", placementDetailsDTO.getLocalPostNumber());
        if (placementDetailsDTO.getId() == null) {
            try {
                final Placement savedPlacement = placementRepository.findOne(placementDetails.getId());
                if (savedPlacement.getDateFrom() != null && savedPlacement.getDateFrom().isBefore(LocalDate.now().plusMonths(3))) {
                    log.info("Creating ESR notification for new placement creation for deanery number {}", savedPlacement.getPost().getNationalPostNumber());
                    final List<EsrNotification> esrNotifications = esrNotificationService.handleNewPlacementEsrNotification(savedPlacement);
                    log.info("CREATED: ESR {} notifications for new placement creation for deanery number {}",
                            esrNotifications.size(), savedPlacement.getPost().getNationalPostNumber());
                }
            } catch (final Exception e) {
                // Ideally it should fail the entire update. Keeping the impact minimal for TCS and go live and revisit after go live.
                // Log and continue
                log.error("Error loading New Placement Notification : ", e);
            }
        }
    }

    private void saveSupervisors(final Set<PlacementSupervisorDTO> supervisorDTOs, final Long placementId) {
        placementSupervisorRepository.deleteAllByIdPlacementId(placementId);

        final Set<PlacementSupervisor> supervisors = new HashSet<>();

        supervisorDTOs.forEach(s -> supervisors.add(new PlacementSupervisor(placementId, s.getPerson().getId(), s.getType())));

        placementSupervisorRepository.save(supervisors);
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
}
