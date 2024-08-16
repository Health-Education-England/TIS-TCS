package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType.SUB_SPECIALTY;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.event.TrainingNumberSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.TrainingNumberRepository;
import com.transformuk.hee.tis.tcs.service.service.CurriculumService;
import com.transformuk.hee.tis.tcs.service.service.TrainingNumberService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainingNumberMapper;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing TrainingNumber.
 */
@Service
@Transactional
public class TrainingNumberServiceImpl implements TrainingNumberService {

  private final Logger log = LoggerFactory.getLogger(TrainingNumberServiceImpl.class);

  private final TrainingNumberRepository trainingNumberRepository;
  private final TrainingNumberMapper trainingNumberMapper;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final CurriculumService curriculumService;

  public TrainingNumberServiceImpl(TrainingNumberRepository trainingNumberRepository,
      TrainingNumberMapper trainingNumberMapper,
      ApplicationEventPublisher applicationEventPublisher, CurriculumService curriculumService) {
    this.trainingNumberRepository = trainingNumberRepository;
    this.trainingNumberMapper = trainingNumberMapper;
    this.applicationEventPublisher = applicationEventPublisher;
    this.curriculumService = curriculumService;
  }

  /**
   * Save a trainingNumber.
   *
   * @param trainingNumberDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public TrainingNumberDTO save(TrainingNumberDTO trainingNumberDTO) {
    log.debug("Request to save TrainingNumber : {}", trainingNumberDTO);
    TrainingNumber trainingNumber = trainingNumberMapper
        .trainingNumberDTOToTrainingNumber(trainingNumberDTO);
    trainingNumber = trainingNumberRepository.save(trainingNumber);
    TrainingNumberDTO trainingNumberDTO1 = trainingNumberMapper
        .trainingNumberToTrainingNumberDTO(trainingNumber);

    applicationEventPublisher.publishEvent(new TrainingNumberSavedEvent(trainingNumberDTO));

    return trainingNumberDTO1;
  }

  /**
   * Save a list of trainingNumbers.
   *
   * @param trainingNumberDTO the entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<TrainingNumberDTO> save(List<TrainingNumberDTO> trainingNumberDTO) {
    log.debug("Request to save TrainingNumber : {}", trainingNumberDTO);
    List<TrainingNumber> trainingNumbers = trainingNumberMapper
        .trainingNumberDTOsToTrainingNumbers(trainingNumberDTO);
    trainingNumbers = trainingNumberRepository.saveAll(trainingNumbers);
    List<TrainingNumberDTO> trainingNumberDTOS = trainingNumberMapper
        .trainingNumbersToTrainingNumberDTOs(trainingNumbers);

    if (CollectionUtils.isNotEmpty(trainingNumberDTOS)) {
      trainingNumberDTO.stream()
          .forEach(tn -> applicationEventPublisher.publishEvent(new TrainingNumberSavedEvent(tn)));
    }

    return trainingNumberDTOS;
  }

  /**
   * Get all the trainingNumbers.
   *
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<TrainingNumberDTO> findAll(Pageable pageable) {
    log.debug("Request to get all TrainingNumbers");
    Page<TrainingNumber> trainingNumberPage = trainingNumberRepository.findAll(pageable);
    return trainingNumberPage.map(trainingNumberMapper::trainingNumberToTrainingNumberDTO);
  }

  /**
   * Get one trainingNumber by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public TrainingNumberDTO findOne(Long id) {
    log.debug("Request to get TrainingNumber : {}", id);
    TrainingNumber trainingNumber = trainingNumberRepository.findById(id).orElse(null);
    return trainingNumberMapper.trainingNumberToTrainingNumberDTO(trainingNumber);
  }

  /**
   * Delete the  trainingNumber by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete TrainingNumber : {}", id);
    trainingNumberRepository.deleteById(id);
  }

  @Override
  public void populateTrainingNumbers(List<ProgrammeMembership> programmeMemberships) {
    programmeMemberships.forEach(this::populateTrainingNumber);
  }

  /**
   * Populate the trainingNumber for the given programme membership.
   *
   * @param programmeMembership The programme membership to generate the training number for.
   */
  private void populateTrainingNumber(ProgrammeMembership programmeMembership) {
    log.info("Populating training number for programme membership '{}'.",
        programmeMembership.getUuid());

    if (isExcluded(programmeMembership)) {
      return;
    }

    String parentOrganization = getParentOrganization(programmeMembership);
    String specialtyConcat = getSpecialtyConcat(programmeMembership);
    String referenceNumber = getReferenceNumber(programmeMembership.getPerson());
    String suffix = getSuffix(programmeMembership);

    TrainingNumber trainingNumber = new TrainingNumber();
    trainingNumber.setTrainingNumber(
        parentOrganization + "/" + specialtyConcat + "/" + referenceNumber + "/" + suffix);
    programmeMembership.setTrainingNumber(trainingNumber);
    log.info("Populated training number: {}.", trainingNumber);
  }


  /**
   * Get the parent organization for the given programme membership.
   *
   * @param programmeMembership The programme membership to calculate the parent organization for.
   * @return The calculated parent organization.
   */
  private String getParentOrganization(ProgrammeMembership programmeMembership) {
    String managingDeanery = programmeMembership.getProgramme().getOwner();
    log.info("Calculating parent organization for managing deanery '{}'.", managingDeanery);
    String parentOrganization = null;

    if (managingDeanery != null) {
      switch (managingDeanery) {
        case "Defence Postgraduate Medical Deanery":
          parentOrganization = "TSD";
          break;
        case "Health Education England East Midlands":
          parentOrganization = "EMD";
          break;
        case "Health Education England East of England":
          parentOrganization = "EAN";
          break;
        case "Health Education England Kent, Surrey and Sussex":
          parentOrganization = "KSS";
          break;
        case "Health Education England North Central and East London":
        case "Health Education England South London":
        case "Health Education England North West London":
        case "London LETBs":
          parentOrganization = "LDN";
          break;
        case "Health Education England North East":
          parentOrganization = "NTH";
          break;
        case "Health Education England North West":
          parentOrganization = "NWE";
          break;
        case "Health Education England South West":
          parentOrganization = "SWN";
          break;
        case "Health Education England Thames Valley":
          parentOrganization = "OXF";
          break;
        case "Health Education England Wessex":
          parentOrganization = "WES";
          break;
        case "Health Education England West Midlands":
          parentOrganization = "WMD";
          break;
        case "Health Education England Yorkshire and the Humber":
          parentOrganization = "YHD";
          break;
        default:
          break;
      }
    }

    if (parentOrganization == null) {
      throw new IllegalArgumentException("Unable to calculate the parent organization.");
    }

    log.info("Calculated parent organization: '{}'.", parentOrganization);
    return parentOrganization;
  }

  /**
   * Get the concatenated specialty string for the programme membership's training number.
   *
   * @param programmeMembership The programme membership to get the specialty string for.
   * @return The concatenated specialty string.
   */
  private String getSpecialtyConcat(ProgrammeMembership programmeMembership) {
    log.info("Calculating specialty concat.");
    List<CurriculumDTO> sortedCurricula = filterAndSortCurricula(programmeMembership);

    StringBuilder sb = new StringBuilder();

    for (ListIterator<CurriculumDTO> curriculaIterator = sortedCurricula.listIterator();
        curriculaIterator.hasNext(); ) {
      int index = curriculaIterator.nextIndex();
      CurriculumDTO curriculum = curriculaIterator.next();
      String specialtyCode = curriculum.getSpecialty().getSpecialtyCode();

      if (index > 0) {
        if (curriculum.getCurriculumSubType().equals(SUB_SPECIALTY)) {
          log.info("Appending sub-specialty '{}'.", specialtyCode);
          sb.append(".");
        } else {
          log.info("Appending specialty '{}'.", specialtyCode);
          sb.append("-");
        }
      } else {
        log.info("Using '{}' as first specialty.", specialtyCode);
      }

      sb.append(specialtyCode);

      if (index == 0 && Objects.equals(curriculum.getName(), "AFT")) {
        sb.append("-FND");
        break;
      }
    }

    log.info("Calculated specialty concat: '{}'.", sb);
    return sb.toString();
  }

  /**
   * Get the GMC/GDC reference number for the given person details.
   *
   * @param person The person details to get the reference number for.
   * @return The GMC/GDC number, depending on which is valid.
   */
  private String getReferenceNumber(Person person) {
    GmcDetails gmcDetails = person.getGmcDetails();
    String gmcNumber = gmcDetails == null ? "" : gmcDetails.getGmcNumber();

    GdcDetails gdcDetails = person.getGdcDetails();
    String gdcNumber = gdcDetails == null ? null : gdcDetails.getGdcNumber();

    return gmcNumber.matches("\\d{7}") ? gmcNumber : gdcNumber;
  }

  /**
   * Get the training number suffix for the given programme membership.
   *
   * @param programmeMembership The programme membership to get the suffix for.
   * @return The calculated suffix for the programme membership's training number.
   */
  private String getSuffix(ProgrammeMembership programmeMembership) {
    log.info("Calculating suffix.");
    String trainingPathway = programmeMembership.getTrainingPathway();
    log.info("Using training pathway '{}' to calculate suffix.", trainingPathway);

    String suffix;
    switch (trainingPathway) {
      case "CCT":
        suffix = "C";
        break;
      case "CESR":
        suffix = "CP";
        break;
      default:
        List<CurriculumDTO> sortedCurricula = filterAndSortCurricula(programmeMembership);
        String firstSpecialtyCode = sortedCurricula.get(0).getSpecialty().getSpecialtyCode();
        log.info("Using specialty code '{}' to calculate suffix.", trainingPathway);

        suffix = Objects.equals(firstSpecialtyCode, "ACA") ? "C" : "D";
    }

    log.info("Calculated suffix: '{}'.", suffix);
    return suffix;
  }

  /**
   * Filter a programme membership's curricula and sort them alphanumerically.
   *
   * @param programmeMembership The programme membership to filter and sort the curricula of.
   * @return The valid curricula for this PM, sorted alphanumerically by subtype and code.
   */
  private List<CurriculumDTO> filterAndSortCurricula(ProgrammeMembership programmeMembership) {
    LocalDate startDate = programmeMembership.getProgrammeStartDate();
    LocalDate now = LocalDate.now();
    LocalDate filterDate = startDate.isAfter(now) ? startDate : now;

    Set<String> uniqueSpecialtyCodes = new HashSet<>();

    return programmeMembership.getCurriculumMemberships().stream()
        .filter(cm -> !cm.getCurriculumStartDate().isAfter(filterDate))
        .filter(cm -> !cm.getCurriculumEndDate().isBefore(filterDate))
        .map(cm -> curriculumService.findOne(cm.getCurriculumId()))
        .filter(c -> c.getSpecialty().getSpecialtyCode() != null && !Strings.isBlank(
            c.getSpecialty().getSpecialtyCode()))
        .sorted(Comparator
            .comparing(CurriculumDTO::getCurriculumSubType)
            .reversed()
            .thenComparing(c -> c.getSpecialty().getSpecialtyCode())
            .reversed()
        )
        .filter(c -> uniqueSpecialtyCodes.add(c.getSpecialty().getSpecialtyCode()))
        .collect(Collectors.toList());
  }

  /**
   * Check whether the given programme membership is excluded for training number generation.
   *
   * @param programmeMembership The programme membership to check.
   * @return true if training number generated cannot continue, else false.
   */
  private boolean isExcluded(ProgrammeMembership programmeMembership) {
    if (isExcluded(programmeMembership.getPerson())) {
      return true;
    }

    Programme programme = programmeMembership.getProgramme();
    String programmeNumber = programme.getProgrammeNumber();
    if (programmeNumber == null || Strings.isBlank(programmeNumber)) {
      log.info("Skipping training number population as programme number is blank.");
      return true;
    }

    String programmeName = programme.getProgrammeName();
    if (programmeName == null || Strings.isBlank(programmeName)) {
      log.info("Skipping training number population as programme name is blank.");
      return true;
    }

    String lowerProgrammeName = programmeName.toLowerCase();
    if (lowerProgrammeName.contains("foundation")) {
      log.info("Skipping training number population as programme name '{}' is excluded.",
          programmeName);
      return true;
    }

    List<CurriculumDTO> validCurricula = filterAndSortCurricula(programmeMembership);
    if (validCurricula.isEmpty()) {
      log.info("Skipping training number population as there are no valid curricula.");
      return true;
    }

    if (programmeMembership.getTrainingPathway() == null) {
      log.error("Unable to generate training number as training pathway was null.");
      return true;
    }

    return false;
  }

  /**
   * Check whether the given person details excludes training number generation.
   *
   * @param person The person details to check.
   * @return true if training number generated cannot continue, else false.
   */
  private boolean isExcluded(Person person) {
    if (person == null) {
      log.info("Skipping training number population as person details not available.");
      return true;
    }

    GmcDetails gmcDetails = person.getGmcDetails();

    if (gmcDetails != null && gmcDetails.getGmcNumber() != null && gmcDetails.getGmcNumber()
        .matches("\\d{7}")) {
      return false;
    }

    GdcDetails gdcDetails = person.getGdcDetails();

    if (gdcDetails != null && gdcDetails.getGdcNumber() != null && gdcDetails.getGdcNumber()
        .matches("\\d{5}.*")) {
      return false;
    }

    log.info("Skipping training number population as reference number not valid.");
    return true;
  }
}
