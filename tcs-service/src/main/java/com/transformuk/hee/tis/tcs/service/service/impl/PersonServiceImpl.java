package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;

import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonLiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonV2DTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PersonOwnerRule;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.BasicPage;
import com.transformuk.hee.tis.tcs.service.event.PersonCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.PersonSavedEvent;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
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
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonBasicDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonLiteMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
  private SqlQuerySupplier sqlQuerySupplier;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

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
   * Get all the people.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Cacheable(value = "personFindAll", sync = true)
  @Override
  @Transactional(readOnly = true)
  public BasicPage<PersonViewDTO> findAll(final Pageable pageable) {
    log.debug("Request to get all People");

    final int size = pageable.getPageSize() + 1;
    final long offset = pageable.getOffset();
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW);

    query = query.replaceAll("TRUST_JOIN",
        permissionService.isUserTrustAdmin() ? " left join PersonTrust pt on (pt.personId = p.id)"
            : StringUtils.EMPTY);

    String whereClause = " WHERE 1=1 ";

    if (permissionService.isUserTrustAdmin()) {
      whereClause = whereClause + "AND trustId IN (:trustList) ";
      paramSource.addValue("trustList", permissionService.getUsersTrustIds());
    }

    if (permissionService.isProgrammeObserver()) {
      whereClause = whereClause + "AND pm.programmeId IN (:programmesList) ";
      paramSource.addValue("programmesList", permissionService.getUsersProgrammeIds());
    }

    query = query.replaceAll("WHERECLAUSE", whereClause);

    //For order by clause
    final String orderByClause = createOrderByClauseWithParams(pageable);
    query = query.replaceAll("ORDERBYCLAUSE", orderByClause);

    query = query.replaceAll("LIMITCLAUSE", "limit " + size + " offset " + offset);

    log.debug("running full person query with no filters");
    final StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    List<PersonViewDTO> persons = namedParameterJdbcTemplate
        .query(query, paramSource, new PersonViewRowMapper());
    stopWatch.stop();
    log.debug("full person query finished in: [{}]s", stopWatch.getTotalTimeSeconds());

    final boolean hasNext = persons.size() > pageable.getPageSize();
    if (hasNext) {
      persons = persons.subList(0, pageable.getPageSize()); //ignore any additional
    }

    if (CollectionUtils.isEmpty(persons)) {
      return new BasicPage<>(persons, pageable);
    }
    return new BasicPage<>(persons, pageable, hasNext);
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
      specs.add(Specifications.where(containsLike("firstName", searchString)).
          or(containsLike("lastName", searchString)).
          or(containsLike("gmcDetails.gmcNumber", searchString)));
    }
    Pageable pageable = new PageRequest(0, PERSON_BASIC_DETAILS_MAX_RESULTS);

    Page<PersonBasicDetails> result;
    if (org.apache.commons.collections4.CollectionUtils.isEmpty(specs)) {
      result = personBasicDetailsRepository.findAll(pageable);
    } else {
      Specifications<PersonBasicDetails> fullSpec = Specifications.where(specs.get(0));
      result = personBasicDetailsRepository.findAll(fullSpec, pageable);
    }

    return result.map(person -> personBasicDetailsMapper.toDto(person)).getContent();
  }

  @Cacheable(value = "personAdvSearch", sync = true)
  @Override
  @Transactional(readOnly = true)
  public BasicPage<PersonViewDTO> advancedSearch(final String searchString,
      final List<ColumnFilter> columnFilters, final Pageable pageable) {
    final int size = pageable.getPageSize() + 1;
    final long offset = pageable.getOffset();
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW);
    query = query.replaceAll("TRUST_JOIN",
        permissionService.isUserTrustAdmin() ? " left join PersonTrust pt on (pt.personId = p.id)"
            : StringUtils.EMPTY);

    String whereClause = createWhereClause(searchString, columnFilters);

    if (permissionService.isUserTrustAdmin()) {
      paramSource.addValue("trustList", permissionService.getUsersTrustIds());
    }

    if (permissionService.isProgrammeObserver()) {
      whereClause = whereClause + "AND pm.programmeId in (:programmesList) ";
      paramSource.addValue("programmesList", permissionService.getUsersProgrammeIds());
    }

    query = query.replaceAll("WHERECLAUSE", whereClause);

    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      paramSource.addValue("searchString", "%" + searchString + "%");
      paramSource.addValue("searchStringArray", Arrays.asList(searchString.split(",")));
    }

    applyFilterByParams(columnFilters, paramSource);

    //For order by clause
    final String orderByClause = createOrderByClauseWithParams(pageable);
    query = query.replaceAll("ORDERBYCLAUSE", orderByClause);

    //limit is 0 based
    query = query.replaceAll("LIMITCLAUSE", "limit " + size + " offset " + offset);

    List<PersonViewDTO> persons = namedParameterJdbcTemplate
        .query(query, paramSource, new PersonViewRowMapper());

    final boolean hasNext = persons.size() > pageable.getPageSize();
    if (hasNext) {
      persons = persons.subList(0, pageable.getPageSize()); //ignore any additional
    }

    if (CollectionUtils.isEmpty(persons)) {
      return new BasicPage<>(persons, pageable);
    }

    return new BasicPage<>(persons, pageable, hasNext);
  }

  /**
   * To generate order by clause with parameterised sort
   *
   * @param pageable
   * @return
   */
  private String createOrderByClauseWithParams(Pageable pageable) {
    final StringBuilder orderByClause = new StringBuilder();
    if (pageable.getSort() != null) {
      if (pageable.getSort().iterator().hasNext()) {
        Sort.Order order = pageable.getSort().iterator().next();
        orderByClause.append(" ORDER BY ").append(order.getProperty()).append(" ")
            .append(order.getDirection()).append(" ");
      }
    }
    return orderByClause.toString();
  }

  /**
   * For parameterised query add param based on filter criteria
   *
   * @param columnFilters
   * @param paramSource
   */
  private void applyFilterByParams(List<ColumnFilter> columnFilters,
      MapSqlParameterSource paramSource) {
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> {

        switch (cf.getName()) {
          case "programmeName":
            paramSource.addValue("programmeNameList", cf.getValues());
            break;
          case "gradeId":
            paramSource.addValue("gradeIdList", cf.getValues());
            break;
          case "specialty":
            paramSource.addValue("specialtyList", cf.getValues());
            break;
          case "placementType":
            paramSource.addValue("placementTypeList", cf.getValues());
            break;
          case "siteId":
            paramSource.addValue("siteIdList", cf.getValues());
            break;
          case "role":
            paramSource.addValue("roleList", cf.getValues());
            break;
          case "currentOwner":
            paramSource.addValue("currentOwnerList", cf.getValues());
            break;
          case "status":
            paramSource.addValue("statusList",
                cf.getValues().stream().map(o -> ((Status) o).name()).collect(Collectors.toList()));
            break;
          default:
            throw new IllegalArgumentException("Not accounted for column filter [" + cf.getName() +
                "] you need to add an additional case statement or remove it from the request");
        }
      });
    }
  }


  private String createWhereClause(final String searchString,
      final List<ColumnFilter> columnFilters) {
    final StringBuilder whereClause = new StringBuilder();
    whereClause.append(" WHERE 1=1 ");

    if (permissionService.isUserTrustAdmin()) {
      whereClause.append("AND trustId in (:trustList) ");
    }

    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> {

        switch (cf.getName()) {
          case "programmeName":
            whereClause.append(" AND prg.programmeName in (:programmeNameList)");
            break;
          case "gradeId":
            whereClause.append(" AND pl.gradeId in (:gradeIdList)");
            break;
          case "specialty":
            whereClause.append(" AND s.name in (:specialtyList)");
            break;
          case "placementType":
            whereClause.append(" AND pl.placementType in (:placementTypeList)");
            break;
          case "siteId":
            whereClause.append(" AND pl.siteId in (:siteIdList)");
            break;
          case "role":
            whereClause.append(" AND p.role in (:roleList)");
            break;
          case "currentOwner":
            whereClause.append(" AND lo.owner in (:currentOwnerList)");
            break;
          case "status":
            whereClause.append(" AND p.status in (:statusList)");
            break;
          default:
            throw new IllegalArgumentException("Not accounted for column filter [" + cf.getName() +
                "] you need to add an additional case statement or remove it from the request");
        }
      });
    }

    if (StringUtils.isNotEmpty(searchString)) {
      whereClause.append(" AND (p.publicHealthNumber LIKE :searchString " +
          "OR cd.surname LIKE :searchString " +
          "OR cd.forenames LIKE :searchString " +
          "OR gmc.gmcNumber LIKE :searchString " +
          "OR gdc.gdcNumber LIKE :searchString " +
          "OR p.role LIKE :searchString ");
      if (StringUtils.isNumeric(searchString)) {
        whereClause.append(" OR p.id in (:searchStringArray)");
      }
      whereClause.append(" ) ");
    }
    return whereClause.toString();
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
      final Pageable pageable) {
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

    return personRepository.searchByRoleCategory(query, roles, pageable)
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

  private String getLoggedInUsersAssociatedTrusts() {
    String commaSepTrustIds = permissionService.getUsersTrustIds().stream()
        .map(Object::toString)
        .reduce((x, y) -> x + ", " + y).orElse(StringUtils.EMPTY);
    return commaSepTrustIds;
  }

  public class PersonViewRowMapper implements RowMapper<PersonViewDTO> {

    @Override
    public PersonViewDTO mapRow(final ResultSet rs, final int id) throws SQLException {
      final PersonViewDTO view = new PersonViewDTO();
      view.setId(rs.getLong("id"));
      view.setIntrepidId(rs.getString("intrepidId"));
      view.setSurname(rs.getString("surname"));
      view.setForenames(rs.getString("forenames"));
      view.setGmcNumber(rs.getString("gmcNumber"));
      view.setGdcNumber(rs.getString("gdcNumber"));
      view.setPublicHealthNumber(rs.getString("publicHealthNumber"));
      view.setProgrammeId(rs.getLong("programmeId"));
      view.setProgrammeName(rs.getString("programmeName"));
      view.setProgrammeNumber(rs.getString("programmeNumber"));
      view.setTrainingNumber(rs.getString("trainingNumber"));
      view.setGradeId(rs.getLong("gradeId"));
      view.setGradeAbbreviation(rs.getString("gradeAbbreviation"));

      view.setSiteCode(rs.getString("siteCode"));
      view.setSiteId(rs.getLong("siteId"));
      view.setPlacementType(rs.getString("placementType"));
      view.setSpecialty(rs.getString("specialty"));
      view.setRole(rs.getString("role"));
      final String status = rs.getString("status");
      if (StringUtils.isNotEmpty(status)) {
        view.setStatus(Status.valueOf(status));
      }
      view.setCurrentOwner(rs.getString("currentOwner"));
      final String ownerRule = rs.getString("currentOwnerRule");
      if (StringUtils.isNotEmpty(ownerRule)) {
        view.setCurrentOwnerRule(PersonOwnerRule.valueOf(ownerRule));
      }
      return view;
    }
  }

}
