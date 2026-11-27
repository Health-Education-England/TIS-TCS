package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonLiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonV2DTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ApprovalStatus;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator;
import com.transformuk.hee.tis.tcs.service.event.PersonCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonSavedEvent;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonBasicDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.RightToWorkRepository;
import com.transformuk.hee.tis.tcs.service.repository.TrainerApprovalRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.GdcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PersonalDetailsService;
import com.transformuk.hee.tis.tcs.service.service.RightToWorkService;
import com.transformuk.hee.tis.tcs.service.service.TrainerApprovalService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.ContactDetailsDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.GdcDetailsDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.GmcDetailsDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonBasicDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonLiteMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonalDetailsDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.RightToWorkDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainerApprovalDtoMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

/**
 * Service Implementation for managing Person.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

  private static final int PERSON_BASIC_DETAILS_MAX_RESULTS = 100;
  private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private GmcDetailsRepository gmcDetailsRepository;
  @Autowired
  private GdcDetailsRepository gdcDetailsRepository;
  @Autowired
  private ContactDetailsRepository contactDetailsRepository;
  @Autowired
  private PersonalDetailsRepository personalDetailsRepository;
  @Autowired
  private RightToWorkRepository rightToWorkRepository;
  @Autowired
  private PersonMapper personMapper;
  @Autowired
  private PersonLiteMapper personLiteMapper;
  @Autowired
  private PersonBasicDetailsRepository personBasicDetailsRepository;
  @Autowired
  private PersonBasicDetailsMapper personBasicDetailsMapper;
  @Autowired
  private PermissionService permissionService;
  @Autowired
  private ReferenceService referenceService;
  @Autowired
  private TrainerApprovalRepository trainerApprovalRepository;

  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  private PersonValidator personValidator;

  @Autowired
  private PersonalDetailsService personalDetailsService;

  @Autowired
  private ContactDetailsService contactDetailsService;

  @Autowired
  private GmcDetailsService gmcDetailsService;

  @Autowired
  private GdcDetailsService gdcDetailsService;

  @Autowired
  private RightToWorkService rightToWorkService;

  @Autowired
  private TrainerApprovalService trainerApprovalService;

  @Autowired
  private PersonDtoMapper personDtoMapper;

  @Autowired
  private ContactDetailsDtoMapper contactDetailsDtoMapper;

  @Autowired
  private PersonalDetailsDtoMapper personalDetailsDtoMapper;

  @Autowired
  private GdcDetailsDtoMapper gdcDetailsDtoMapper;

  @Autowired
  private GmcDetailsDtoMapper gmcDetailsDtoMapper;

  @Autowired
  private RightToWorkDtoMapper rightToWorkDtoMapper;

  @Autowired
  private TrainerApprovalDtoMapper trainerApprovalDtoMapper;

  /**
   * Save a person.
   *
   * @param personDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PersonDTO save(final PersonDTO personDTO) {
    log.debug("Request to save Person : {}", personDTO);
    Person person = personMapper.toEntity(personDTO);

    final Long personDtoId = personDTO.getId();
    if (!permissionService.canEditSensitiveData() && personDtoId != null) {
      final Person originalPerson = personRepository.findById(personDtoId).orElse(null);
      if (originalPerson == null) { //this shouldn't happen
        throw new IllegalArgumentException(
            "The person record for id " + personDtoId + " could not be found");
      }

      final PersonalDetails originalPersonalDetails = originalPerson.getPersonalDetails();
      final PersonalDetailsDTO personalDetails = personDTO.getPersonalDetails();
      personalDetails.setDisability(originalPersonalDetails.getDisability());
      personalDetails.setDisabilityDetails(originalPersonalDetails.getDisabilityDetails());
      personalDetails.setReligiousBelief(originalPersonalDetails.getReligiousBelief());
      personalDetails.setSexualOrientation(originalPersonalDetails.getSexualOrientation());
    }

    person = personRepository.saveAndFlush(person);
    final PersonDTO personDTO1 = personMapper.toDto(person);
    if (!permissionService.canEditSensitiveData() && personDtoId != null) {
      clearSensitiveData(personDTO1.getPersonalDetails());
    }

    applicationEventPublisher.publishEvent(new PersonSavedEvent(personDTO));

    return personDTO1;
  }


  /**
   * Create a person.
   * <p>
   * Person is one of those entities that share the ID with the joining tables Save the person
   * object and ensure we copy the generated id to the linked entities
   *
   * @param personDTO the entity to save
   * @return the persisted entity
   */
  @Override
  @Transactional()
  public PersonDTO create(PersonDTO personDTO) {
    log.debug("Request to save Person : {}", personDTO);
    Person person = personMapper.toEntity(personDTO);
    person = personRepository.save(person);

    GdcDetails gdcDetails =
        person.getGdcDetails() != null ? person.getGdcDetails() : new GdcDetails();
    gdcDetails.setId(person.getId());
    gdcDetails = gdcDetailsRepository.save(gdcDetails);
    person.setGdcDetails(gdcDetails);

    GmcDetails gmcDetails =
        person.getGmcDetails() != null ? person.getGmcDetails() : new GmcDetails();
    gmcDetails.setId(person.getId());
    gmcDetails = gmcDetailsRepository.save(gmcDetails);
    person.setGmcDetails(gmcDetails);

    ContactDetails contactDetails =
        person.getContactDetails() != null ? person.getContactDetails() : new ContactDetails();
    contactDetails.setId(person.getId());
    contactDetails = contactDetailsRepository.save(contactDetails);
    person.setContactDetails(contactDetails);

    PersonalDetails personalDetails =
        person.getPersonalDetails() != null ? person.getPersonalDetails() : new PersonalDetails();
    personalDetails.setId(person.getId());
    if (!permissionService.canEditSensitiveData()) {
      clearSensitiveData(personalDetails);
    }
    personalDetails = personalDetailsRepository.save(personalDetails);
    person.setPersonalDetails(personalDetails);

    RightToWork rightToWork =
        person.getRightToWork() != null ? person.getRightToWork() : new RightToWork();
    rightToWork.setId(person.getId());
    rightToWork = rightToWorkRepository.save(rightToWork);
    person.setRightToWork(rightToWork);

    personDTO = personMapper.toDto(person);
    applicationEventPublisher.publishEvent(new PersonCreatedEvent(personDTO));
    return personDTO;
  }

  /**
   * Save a list of persons
   *
   * @param personDTOs the list of entities to save
   * @return a list of persisted entities
   */
  @Override
  public List<PersonDTO> save(final List<PersonDTO> personDTOs) {
    log.debug("Request to save Persons : {}", personDTOs);
    List<Person> personList = personMapper.toEntity(personDTOs);
    personList = personRepository.saveAll(personList);
    List<PersonDTO> personDTOS = personMapper.toDto(personList);

    personDTOS.stream()
        .map(PersonSavedEvent::new)
        .forEach(applicationEventPublisher::publishEvent);

    return personDTOS;
  }

  /**
   * Patch a list of persons, patching will be skipped for any entities which fail validation.
   *
   * @param personDtos the list of entities to patch.
   * @return The patched entities and skipped entities with validation errors added.
   */
  @Override
  public List<PersonDTO> patch(List<PersonDTO> personDtos) {
    List<PersonDTO> personDtoList = new ArrayList<>();
    // Perform validation.
    personValidator.validateForBulk(personDtos);

    // Patch valid persons.
    for (PersonDTO personDto : personDtos) {
      if (personDto.getMessageList().isEmpty()) {
        PersonDTO personDtoFromDb = this.findOne(personDto.getId());
        if (personDtoFromDb != null) {
          updateDtoForBulk(personDtoFromDb, personDto);
          // No cascades for DTOs inside PersonDTO, so need to save each of them
          personalDetailsService.save(personDtoFromDb.getPersonalDetails());
          contactDetailsService.save(personDtoFromDb.getContactDetails());
          gmcDetailsService.save(personDtoFromDb.getGmcDetails());
          gdcDetailsService.save(personDtoFromDb.getGdcDetails());
          rightToWorkService.save(personDtoFromDb.getRightToWork());
          PersonDTO savedPersonDto = save(personDtoFromDb);

          // Need to set the updated Person into TrainerApprovals
          TrainerApprovalDTO trainerApprovalDto = getTrainerApproveFromPersonDto(personDtoFromDb);
          if (trainerApprovalDto != null) {
            trainerApprovalDto.setPerson(savedPersonDto);
            trainerApprovalService.save(new ArrayList<>(personDtoFromDb.getTrainerApprovals()));
          }

          personDtoList.add(savedPersonDto);
        }
      } else {
        // TODO: for the PersonDTOs with errors, copy the messageList only, need to be refactored
        PersonDTO errorPersonDto = new PersonDTO();
        errorPersonDto.setId(personDto.getId());
        errorPersonDto.setMessageList(personDto.getMessageList());
        personDtoList.add(errorPersonDto);
      }
    }

    return personDtoList;
  }

  private void updateDtoForBulk(PersonDTO personDtoFromDB, PersonDTO personDto) {
    // if address1 provided, 4 existing address lines are cleared and overwritten
    if (personDto.getContactDetails() != null
        && personDto.getContactDetails().getAddress1() != null
        && personDtoFromDB.getContactDetails() != null) {
      personDtoFromDB.getContactDetails().setAddress2(null);
      personDtoFromDB.getContactDetails().setAddress3(null);
      personDtoFromDB.getContactDetails().setAddress4(null);
    }

    personDtoMapper.copyIfNotNull(personDto, personDtoFromDB);

    updateTrainerApprovalWithRole(personDto, personDtoFromDB);
  }

  private void updateTrainerApprovalWithRole(PersonDTO personDto, PersonDTO personDtoFromDB) {
    String role = personDto.getRole();
    TrainerApprovalDTO newTrainerApprovalDto = getTrainerApproveFromPersonDto(personDto);
    TrainerApprovalDTO trainerApprovalDtoFromDB = getTrainerApproveFromPersonDto(personDtoFromDB);

    if (StringUtils.isEmpty(role)) {
      trainerApprovalDtoMapper.copyIfNotNull(newTrainerApprovalDto, trainerApprovalDtoFromDB);
    } else {
      // if user input new role, delete the existing trainerApproval first
      if (trainerApprovalDtoFromDB != null && trainerApprovalDtoFromDB.getId() != null) {
        trainerApprovalService.delete(trainerApprovalDtoFromDB.getId());
      }
      personDtoFromDB.setTrainerApprovals(null);

      // if the new role contains one of the 3 catogories(ES/CS/LM), create a new trainerApproval
      List<RoleDTO> roleDtos = referenceService.findRolesIn(role);
      String trainerType = roleDtos.stream()
          .filter(roleDTO -> roleDTO.getRoleCategory().getId() != 3)
          .map(roleDTO -> roleDTO.getRoleCategory().getName()).distinct()
          .collect(Collectors.joining(","));

      if (!StringUtils.isEmpty(trainerType)) {
        TrainerApprovalDTO updatedTrainerApproval = new TrainerApprovalDTO();
        trainerApprovalDtoMapper.copyIfNotNull(newTrainerApprovalDto, updatedTrainerApproval);
        updatedTrainerApproval.setTrainerType(trainerType);
        if (updatedTrainerApproval.getApprovalStatus() == null) { // set default ApprovalStatus
          updatedTrainerApproval.setApprovalStatus(ApprovalStatus.CURRENT);
        }
        personDtoFromDB.setTrainerApprovals(Sets.newHashSet(updatedTrainerApproval));
      }
    }
  }

  private TrainerApprovalDTO getTrainerApproveFromPersonDto(PersonDTO dto) {
    Set<TrainerApprovalDTO> trainerApprovalDtoSet = dto.getTrainerApprovals();
    if (trainerApprovalDtoSet != null && !trainerApprovalDtoSet.isEmpty()) {
      return trainerApprovalDtoSet.iterator().next();
    }
    return null;
  }

  /**
   * Advanced search for person list view.
   * <p>
   * There are two queries that happen in this method, one to retrieve the data based on the search
   * and column filters and the second to get the count so that we can support pagination
   *
   * @param searchString the search string to match, can be null
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<PersonBasicDetailsDTO> basicDetailsSearch(String searchString) {
    List<Specification<PersonBasicDetails>> specs = new ArrayList<>();
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specification.where(containsLike("firstName", searchString)).
          or(containsLike("lastName", searchString)).
          or(containsLike("gmcDetails.gmcNumber", searchString)));
    }
    Pageable pageable = PageRequest.of(0, PERSON_BASIC_DETAILS_MAX_RESULTS);

    Page<PersonBasicDetails> result;
    if (org.apache.commons.collections4.CollectionUtils.isEmpty(specs)) {
      result = personBasicDetailsRepository.findAll(pageable);
    } else {
      Specification<PersonBasicDetails> fullSpec = Specification.where(specs.get(0));
      result = personBasicDetailsRepository.findAll(fullSpec, pageable);
    }

    return result.map(person -> personBasicDetailsMapper.toDto(person)).getContent();
  }

  /**
   * Get one person by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PersonDTO findOne(final Long id) {
    log.debug("Request to get Person : {}", id);
    final Person person = personRepository.findById(id).orElse(null);
    final boolean canViewSensitiveData = permissionService.canViewSensitiveData();
    if (!canViewSensitiveData) {
      final PersonalDetails personalDetails = person.getPersonalDetails();
      clearSensitiveData(personalDetails);
    }
    return personMapper.toDto(person);
  }

  /**
   * Find a person record and ensure that the
   *
   * @param id
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public PersonDTO findPersonWithProgrammeMembershipsSorted(final Long id) {
    PersonDTO personDTO = findOne(id);
    if (personDTO != null) {
      Set<ProgrammeMembershipDTO> programmeMemberships = personDTO.getProgrammeMemberships();
      if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(programmeMemberships)) {
        SortedSet<ProgrammeMembershipDTO> orderedProgrammeMemberships = new TreeSet<>(
            (o1, o2) -> ObjectUtils
                .compare(o2.getProgrammeStartDate(), o1.getProgrammeStartDate()));
        orderedProgrammeMemberships.addAll(programmeMemberships);
        personDTO.setProgrammeMemberships(orderedProgrammeMemberships);
      }
    }

    return personDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public PersonV2DTO findPersonV2WithProgrammeMembershipsSorted(final Long id) {
    PersonDTO temp = findPersonWithProgrammeMembershipsSorted(id);
    PersonV2DTO personV2DTO = new PersonV2DTO();
    copyProperties(temp, personV2DTO);
    return personV2DTO;
  }

  protected void copyProperties(PersonDTO temp, PersonV2DTO personV2DTO) {
    BeanUtils.copyProperties(temp, personV2DTO);
  }

  private void clearSensitiveData(final PersonalDetails personalDetails) {
    personalDetails.setDisability(null);
    personalDetails.setDisabilityDetails(null);
    personalDetails.setReligiousBelief(null);
    personalDetails.setSexualOrientation(null);
    personalDetails.setEthnicOrigin(null);
  }

  private void clearSensitiveData(final PersonalDetailsDTO personalDetailsDTO) {
    personalDetailsDTO.setDisability(null);
    personalDetailsDTO.setDisabilityDetails(null);
    personalDetailsDTO.setReligiousBelief(null);
    personalDetailsDTO.setSexualOrientation(null);
  }

  /**
   * Get persons by ids.
   *
   * @param ids the Ids of the entities
   * @return the entities
   */
  @Override
  @Transactional(readOnly = true)
  public List<PersonBasicDetailsDTO> findBasicDetailsByIdIn(Set<Long> ids) {
    log.debug("Request to get all person basic details {} ", ids);

    return personBasicDetailsRepository.findAllById(ids).stream()
        .map(personBasicDetailsMapper::toDto)
        .collect(Collectors.toList());
  }

  /**
   * Get persons by ids.
   *
   * @param ids the Ids of the entities
   * @return the entities
   */
  @Override
  @Transactional(readOnly = true)
  public List<PersonDTO> findByIdIn(final Set<Long> ids) {
    log.debug("Request to get all persons {} ", ids);

    return personRepository.findAllById(ids).stream()
        .map(personMapper::toDto)
        .collect(Collectors.toList());
  }

  /**
   * Get one person by GMC Id.
   *
   * @param gmcId the Gmc Id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public Long findIdByGmcId(final String gmcId) {
    log.debug("Request to get Person by GMC Id : {}", gmcId);
    return gmcDetailsRepository.findByGmcNumber(gmcId).getId();
  }

  @Override
  public List<PersonDTO> findPersonsByPublicHealthNumbersIn(List<String> publicHealthNumbers) {
    return personRepository.findByPublicHealthNumberIn(publicHealthNumbers).stream()
        .map(personMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public Page<PersonLiteDTO> searchByRoleCategory(final String query, final Long categoryId,
      final Pageable pageable, final boolean filterByTrainerApprovalStatus) {
    log.debug("Received request to search '{}' with RoleCategory ID '{}' and query '{}'",
        PersonLiteDTO.class.getSimpleName(), categoryId, query);

    log.debug("Accessing '{}' to load '{}' for Category ID '{}'",
        referenceService.getClass().getSimpleName(), RoleDTO.class.getSimpleName(), categoryId);

    final StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    final Collection<RoleDTO> rolesByCategory = referenceService.getRolesByCategory(categoryId);

    stopWatch.stop();

    log.info("'{}' returned '{}' '{}' for Category ID '{}'; took '{}'ms",
        referenceService.getClass().getSimpleName(), rolesByCategory.size(),
        RoleDTO.class.getSimpleName(), categoryId, stopWatch.getTotalTimeSeconds());

    if (rolesByCategory.isEmpty()) {
      return new PageImpl<>(java.util.Collections.emptyList(), pageable, 0);
    }

    final Set<String> roles = rolesByCategory.stream()
        .map(RoleDTO::getCode)
        .collect(Collectors.toSet());

    log.debug("Accessing '{}' to search '{}' with roles for Category ID '{}' and query '{}'",
        personRepository.getClass().getSimpleName(), PersonLiteDTO.class.getSimpleName(),
        categoryId, query);

    return personRepository
        .searchByRoleCategory(query, roles, pageable, filterByTrainerApprovalStatus)
        .map(personLiteMapper::toDto);
  }

  @Override
  public PersonBasicDetailsDTO getBasicDetails(final Long id) {
    final PersonBasicDetails details = personBasicDetailsRepository.findById(id).orElse(null);
    if (details != null) {
      return personBasicDetailsMapper.toDto(details);
    } else {
      return null;
    }
  }

  /**
   * Delete the  person by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(final Long id) {
    log.debug("Request to delete Person : {}", id);
    personRepository.deleteById(id);
    applicationEventPublisher.publishEvent(new PersonDeletedEvent(id));
  }

  /**
   * Call Stored proc to build person view
   *
   * @return
   */
  @Override
  @Transactional
  @Async
  public CompletableFuture<Void> buildPersonView() {
    log.debug("Request to build Person view");
    personRepository.buildPersonView();
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public void setRightToWorkRepository(final RightToWorkRepository rightToWorkRepository) {
    this.rightToWorkRepository = rightToWorkRepository;
  }

  /**
   * Method that will throw a not authorized exception if the current logged in user cannot view or
   * modify the person record
   *
   * @param personId the db managed id of the person record
   */
  public void canLoggedInUserViewOrAmend(Long personId) {
    if (permissionService.isUserTrustAdmin()) {
      Set<Long> userTrustIds = permissionService.getUsersTrustIds();

      Optional<Person> optionalPerson = personRepository.findPersonById(personId);
      if (optionalPerson.isPresent()) {
        Set<PersonTrust> associatedTrusts = optionalPerson.get().getAssociatedTrusts();
        if (!CollectionUtils.isEmpty(associatedTrusts)) {
          Set<Long> personTrustIds = associatedTrusts.stream().map(PersonTrust::getTrustId)
              .collect(Collectors.toSet());
          boolean noCommonElements = Collections.disjoint(personTrustIds, userTrustIds);
          if (noCommonElements) {
            throw new AccessUnauthorisedException(
                "You cannot view or modify Person with id: " + personId);
          }
        }
      }
    }
  }
}
