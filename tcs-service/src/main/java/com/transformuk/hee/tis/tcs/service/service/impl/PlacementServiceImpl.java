package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.TCSDateColumns;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.exception.DateRangeColumnFilterException;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PlacementSupervisor;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementSpecialtyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
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
  private EsrNotificationService esrNotificationService;

  /**
   * Save a placement.
   *
   * @param placementDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PlacementDTO save(PlacementDTO placementDTO) {
    log.debug("Request to save Placement : {}", placementDTO);
    Placement placement = placementMapper.placementDTOToPlacement(placementDTO);
    placement = placementRepository.save(placement);
    return placementMapper.placementToPlacementDTO(placement);
  }

  @Transactional
  @Override
  public PlacementDetailsDTO createDetails(PlacementDetailsDTO placementDetailsDTO) {
    log.debug("Request to create Placement : {}", placementDetailsDTO);
    PlacementDetails placementDetails = placementDetailsMapper.placementDetailsDTOToPlacementDetails(placementDetailsDTO);
    placementDetails = placementDetailsRepository.save(placementDetails);

    Set<PlacementSpecialty> placementSpecialties = linkPlacementSpecialties(placementDetailsDTO, placementDetails);
    PlacementDetailsDTO placementDetailsDTO1 = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placementDetails);
    placementDetailsDTO1.setSpecialties(placementSpecialtyMapper.toDTOs(placementSpecialties));
    handleEsrNewPlacementNotification(placementDetailsDTO, placementDetails);
    return placementDetailsDTO1;
  }

  @Transactional
  @Override
  public PlacementDetailsDTO saveDetails(PlacementDetailsDTO placementDetailsDTO) {

    log.debug("Request to save Placement : {}", placementDetailsDTO);

    //clear any linked specialties before trying to save the placement
    Placement placement = placementRepository.findOne(placementDetailsDTO.getId());
    handleChangeOfPlacementDatesEsrNotification(placement, placementDetailsDTO);
    placementSpecialtyRepository.delete(placement.getSpecialties());
    placement.setSpecialties(new HashSet<>());
    placementRepository.saveAndFlush(placement);

    return createDetails(placementDetailsDTO);
  }

  @Transactional
  private Set<PlacementSpecialty> linkPlacementSpecialties(PlacementDetailsDTO placementDetailsDTO, PlacementDetails placementDetails) {
    Placement placement = placementRepository.findOne(placementDetails.getId());
    Set<PlacementSpecialty> placementSpecialties = Sets.newHashSet();
    if(CollectionUtils.isNotEmpty(placementDetailsDTO.getSpecialties())) {
      for (PlacementSpecialtyDTO placementSpecialtyDTO : placementDetailsDTO.getSpecialties()) {
        PlacementSpecialty placementSpecialty = new PlacementSpecialty();
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
  public List<PlacementDTO> save(List<PlacementDTO> placementDTO) {
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
  public Page<PlacementDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Placements");
    Page<Placement> result = placementRepository.findAll(pageable);
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
  public PlacementDTO findOne(Long id) {
    log.debug("Request to get Placement : {}", id);
    Placement placement = placementRepository.findOne(id);
    return placementMapper.placementToPlacementDTO(placement);
  }

  @Override
  @Transactional(readOnly = true)
  public PlacementDetailsDTO getDetails(Long id) {
    PlacementDetails pd = placementDetailsRepository.findOne(id);
    PlacementDetailsDTO placementDetailsDTO = null;
    if (pd != null) {
      Set<PlacementSpecialty> placementSpecialties = placementSpecialtyRepository.findByPlacementId(id);
      Set<PlacementSpecialtyDTO> placementSpecialtyDTOS = placementSpecialtyMapper.toDTOs(placementSpecialties);
      placementDetailsDTO = placementDetailsMapper.placementDetailsToPlacementDetailsDTO(pd);
      placementDetailsDTO.setSpecialties(placementSpecialtyDTOS);
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
  public void delete(Long id) {
    log.debug("Request to delete Placement : {}", id);
    // handle esr notification
    handleEsrNotificationForPlacementDelete(id);
    placementRepository.delete(id);
  }

  private void handleEsrNotificationForPlacementDelete(Long id) {
    List<EsrNotification> allEsrNotifications = new ArrayList<>();

    Placement placementToDelete = placementRepository.findOne(id);
    if (placementToDelete.getDateFrom() != null && placementToDelete.getDateFrom().isBefore(LocalDate.now().plusMonths(3))) {
      List<EsrNotification> esrNotifications = esrNotificationService.loadPlacementDeleteNotification(placementToDelete, allEsrNotifications);
      log.info("Placement Delete: PERSISTING: {} EsrNotifications for post {} being deleted", esrNotifications.size(), placementToDelete.getLocalPostNumber());
      esrNotificationService.save(esrNotifications);
      log.info("Placement Delete: PERSISTED: {} EsrNotifications for post {} being deleted", esrNotifications.size(), placementToDelete.getLocalPostNumber());
    }
  }

  private Map<String, Placement> getPlacementsByIntrepidId(List<PlacementDTO> placementDtoList) {
    Set<String> placementIntrepidIds = placementDtoList.stream().map(PlacementDTO::getIntrepidId).collect(Collectors.toSet());
    Set<Placement> placementsFound = placementRepository.findByIntrepidIdIn(placementIntrepidIds);
    Map<String, Placement> result = Maps.newHashMap();
    if (CollectionUtils.isNotEmpty(placementsFound)) {
      result = placementsFound.stream().collect(
          Collectors.toMap(Placement::getIntrepidId, post -> post)
      );
    }
    return result;
  }

  @Override
  public List<PlacementDTO> patchPlacementSpecialties(List<PlacementDTO> placementDTOList) {
    List<Placement> placements = Lists.newArrayList();
    Map<String, Placement> intrepidIdToPlacement = getPlacementsByIntrepidId(placementDTOList);

    Set<Long> specialtyIds = placementDTOList
        .stream()
        .map(PlacementDTO::getSpecialties)
        .flatMap(Collection::stream)
        .map(PlacementSpecialtyDTO::getSpecialtyId)
        .collect(Collectors.toSet());

    Map<Long, Specialty> idToSpecialty = specialtyRepository.findAll(specialtyIds).stream().collect(Collectors.toMap(Specialty::getId, sp -> sp));
    for (PlacementDTO dto : placementDTOList) {
      Placement placement = intrepidIdToPlacement.get(dto.getIntrepidId());
      if (placement != null) {
        Set<PlacementSpecialty> attachedSpecialties = placement.getSpecialties();
        Set<Long> attachedSpecialtyIds = attachedSpecialties.stream().map(ps -> ps.getSpecialty().getId()).collect(Collectors.toSet());
        for (PlacementSpecialtyDTO placementSpecialtyDTO : dto.getSpecialties()) {
          Specialty specialty = idToSpecialty.get(placementSpecialtyDTO.getSpecialtyId());
          if (specialty != null && !attachedSpecialtyIds.contains(specialty.getId())) {
            PlacementSpecialty placementSpecialty = new PlacementSpecialty();
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
    List<Placement> savedPlacements = placementRepository.save(placements);
    return placementMapper.placementsToPlacementDTOs(savedPlacements);
  }

  @Override
  public List<PlacementDTO> patchPlacementClinicalSupervisors(List<PlacementDTO> placementDTOList) {
    List<Placement> placements = Lists.newArrayList();
    Map<String, Placement> intrepidIdToPlacement = getPlacementsByIntrepidId(placementDTOList);

    Set<Long> clinicalSupervisorIds = placementDTOList
        .stream()
        .map(PlacementDTO::getClinicalSupervisorIds)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    Map<Long, Person> idToPerson = personRepository.findAll(clinicalSupervisorIds).stream().collect(Collectors.toMap(Person::getId, p -> p));
    for (PlacementDTO dto : placementDTOList) {
      Placement placement = intrepidIdToPlacement.get(dto.getIntrepidId());
      if (placement != null) {
        Set<PlacementSupervisor> attachedClinicalSupervisors = placement.getClinicalSupervisors();
        Set<Long> attachedClinicalSupervisorIds = attachedClinicalSupervisors.stream().map(ps -> ps.getClinicalSupervisor().getId()).collect(Collectors.toSet());
        for (Long clinicalSupervisorId : dto.getClinicalSupervisorIds()) {
          Person person = idToPerson.get(clinicalSupervisorId);
          if (person != null && !attachedClinicalSupervisorIds.contains(clinicalSupervisorId)) {
            PlacementSupervisor placementSupervisor = new PlacementSupervisor();
            placementSupervisor.setPlacement(placement);
            placementSupervisor.setClinicalSupervisor(person);
            attachedClinicalSupervisors.add(placementSupervisor);
          }
        }
        placement.setClinicalSupervisors(attachedClinicalSupervisors);
        placements.add(placement);
      }
    }
    List<Placement> savedPlacements = placementRepository.save(placements);
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
  public Page<PlacementDetailsDTO> findAllPlacementDetails(Pageable pageable) {
    log.debug("Request to get all Placements details");
    Page<PlacementDetails> result = placementDetailsRepository.findAll(pageable);
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
  public Page<PlacementDetailsDTO> findFilteredPlacements(String columnFilterJson, Pageable pageable) throws IOException {

    log.debug("Request to get all Revalidations filtered by columns {}", columnFilterJson);
    List<Class> filterEnumList = Collections.emptyList();
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);

    List<Specification<PlacementDetails>> specs = new ArrayList<>();

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
    Page<PlacementDetails> result;
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
  public PlacementDTO closePlacement(Long placementId) {
    Placement placement = placementRepository.findOne(placementId);
    if(placement != null) {
      placement.setDateTo(LocalDate.now().minusDays(1));
      placement = placementRepository.saveAndFlush(placement);
    }
    return placementMapper.placementToPlacementDTO(placement);
  }

  @Transactional(readOnly = true)
  @Override
  public List<PlacementSummaryDTO> getPlacementForTrainee(Long traineeId) {
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.TRAINEE_PLACEMENT_SUMMARY);

    Query traineePlacementsQuery = em.createNativeQuery(query, PLACEMENTS_SUMMARY_MAPPER)
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
  public List<PlacementSummaryDTO> getPlacementForPost(Long postId) {
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_PLACEMENT_SUMMARY);
    Query postPlacementsQuery = em.createNativeQuery(query, PLACEMENTS_SUMMARY_MAPPER)
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
  private List<PlacementSummaryDTO> filterPlacements(List<PlacementSummaryDTO> resultList) {
    Map<BigInteger, PlacementSummaryDTO> idsToPlacementSummary = Maps.newHashMap();
    for (PlacementSummaryDTO placementSummaryDTO : resultList) {

      BigInteger placementId = placementSummaryDTO.getPlacementId();
      if (!idsToPlacementSummary.containsKey(placementId) ||
          PostSpecialtyType.PRIMARY.name().equals(placementSummaryDTO.getPlacementSpecialtyType())) {
        idsToPlacementSummary.put(placementId, placementSummaryDTO);
      }
    }

    List<PlacementSummaryDTO> placementSummaryDTOS = Lists.newArrayList(idsToPlacementSummary.values());
    placementSummaryDTOS.sort(new Comparator<PlacementSummaryDTO>() {
      @Override
      public int compare(PlacementSummaryDTO o1, PlacementSummaryDTO o2) {
        if(o2.getDateTo() != null && o1.getDateTo() != null) {
          return o2.getDateTo().compareTo(o1.getDateTo());
        }
        return 0;
      }
    });
    return placementSummaryDTOS;
  }

  private String getPlacementStatus(Date dateFrom, Date dateTo) {

    if (dateFrom == null || dateTo == null) {
      return PlacementStatus.PAST.name();
    }

    // Truncating the hours,minutes,seconds
    long from = DateUtils.truncate(dateFrom,Calendar.DATE).getTime();
    long to = DateUtils.truncate(dateTo,Calendar.DATE).getTime();
    long now = DateUtils.truncate(new Date(),Calendar.DATE).getTime();

    if (now < from) {
      return PlacementStatus.FUTURE.name();
    } else if (now > to) {
      return PlacementStatus.PAST.name();
    }
    return PlacementStatus.CURRENT.name();

  }

  private void handleChangeOfPlacementDatesEsrNotification(Placement currentPlacement, PlacementDetailsDTO updatedPlacementDetails) {

    if (currentPlacement != null && updatedPlacementDetails != null &&
        isEligibleForNotification(currentPlacement, updatedPlacementDetails)) {
      // create NOT1 type record. Current and next trainee details for the post number.
      // Create NOT4 type record
      log.info("Change in hire or end date. Marking for notification : {} ", currentPlacement.getPost().getNationalPostNumber());
      try {
        esrNotificationService.loadChangeOfPlacementDatesNotification(updatedPlacementDetails, currentPlacement.getPost().getNationalPostNumber());
      } catch (Exception e) {
        // Ideally it should fail the entire update. Keeping the impact minimal for go live and revisit after go live.
        // Log and continue
        log.error("Error loading Change of Placement Dates Notification : ", e);
      }
    }
  }

  private boolean isEligibleForNotification(Placement currentPlacement, PlacementDetailsDTO updatedPlacementDetails) {
    // I really do not like this null checks :-( but keeping it to work around the data from intrepid
    return
        ((currentPlacement.getDateFrom() != null && !currentPlacement.getDateFrom().equals(updatedPlacementDetails.getDateFrom())) ||
            (currentPlacement.getDateTo() != null && !currentPlacement.getDateTo().equals(updatedPlacementDetails.getDateTo()))) &&
        ((currentPlacement.getDateFrom() != null && currentPlacement.getDateFrom().isBefore(LocalDate.now().plusMonths(3))) ||
            (updatedPlacementDetails.getDateFrom() != null && updatedPlacementDetails.getDateFrom().isBefore(LocalDate.now().plusMonths(3))));
  }

  private void handleEsrNewPlacementNotification(final PlacementDetailsDTO placementDetailsDTO, PlacementDetails placementDetails) {

    log.info("Handling ESR notifications for new placement creation for deanery number {}", placementDetailsDTO.getLocalPostNumber());
    if (placementDetailsDTO.getId() == null) {
      try {
        Placement savedPlacement = placementRepository.findOne(placementDetails.getId());
        if (savedPlacement.getDateFrom() != null && savedPlacement.getDateFrom().isBefore(LocalDate.now().plusMonths(3))) {
          log.info("Creating ESR notification for new placement creation for deanery number {}", savedPlacement.getPost().getNationalPostNumber());
          List<EsrNotification> esrNotifications = esrNotificationService.handleNewPlacementEsrNotification(savedPlacement);
          log.info("CREATED: ESR {} notifications for new placement creation for deanery number {}",
              esrNotifications.size(), savedPlacement.getPost().getNationalPostNumber());
        }
      } catch (Exception e) {
        // Ideally it should fail the entire update. Keeping the impact minimal for TCS and go live and revisit after go live.
        // Log and continue
        log.error("Error loading New Placement Notification : ", e);
      }
    }
  }
}
