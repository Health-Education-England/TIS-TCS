package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PersonOwnerRule;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonBasicDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.RightToWorkRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonBasicDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonViewMapper;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;


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
  private PersonBasicDetailsRepository personBasicDetailsRepository;
  @Autowired
  private PersonBasicDetailsMapper personBasicDetailsMapper;
  @Autowired
  private PersonViewRepository personViewRepository;
  @Autowired
  private PersonViewMapper personViewMapper;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;

  /**
   * Save a person.
   *
   * @param personDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PersonDTO save(PersonDTO personDTO) {
    log.debug("Request to save Person : {}", personDTO);
    Person person = personMapper.toEntity(personDTO);
    person = personRepository.saveAndFlush(person);
    return personMapper.toDto(person);
  }


  /**
   * Create a person.
   * <p>
   * Person is one of those entities that share the ID with the joining tables
   * Save the person object and ensure we copy the generated id to the linked entities
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

    GdcDetails gdcDetails = person.getGdcDetails() != null ? person.getGdcDetails() : new GdcDetails();
    gdcDetails.setId(person.getId());
    gdcDetailsRepository.save(gdcDetails);
    person.setGdcDetails(gdcDetails);

    GmcDetails gmcDetails = person.getGmcDetails() != null ? person.getGmcDetails() : new GmcDetails();
    gmcDetails.setId(person.getId());
    gmcDetails = gmcDetailsRepository.save(gmcDetails);
    person.setGmcDetails(gmcDetails);

    ContactDetails contactDetails = person.getContactDetails() != null ? person.getContactDetails() : new ContactDetails();
    contactDetails.setId(person.getId());
    contactDetails = contactDetailsRepository.save(contactDetails);
    person.setContactDetails(contactDetails);

    PersonalDetails personalDetails = person.getPersonalDetails() != null ? person.getPersonalDetails() : new PersonalDetails();
    personalDetails.setId(person.getId());
    personalDetails = personalDetailsRepository.save(personalDetails);
    person.setPersonalDetails(personalDetails);

    RightToWork rightToWork = person.getRightToWork() != null ? person.getRightToWork() : new RightToWork();
    rightToWork.setId(person.getId());
    rightToWork = rightToWorkRepository.save(rightToWork);
    person.setRightToWork(rightToWork);

    return personMapper.toDto(person);
  }

  /**
   * Save a list of persons
   *
   * @param personDTOs the list of entities to save
   * @return a list of persisted entities
   */
  @Override
  public List<PersonDTO> save(List<PersonDTO> personDTOs) {
    log.debug("Request to save Persons : {}", personDTOs);
    List<Person> personList = personMapper.toEntity(personDTOs);
    personList = personRepository.save(personList);
    return personMapper.toDto(personList);
  }

  /**
   * Get all the people.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PersonViewDTO> findAll(Pageable pageable) {
    log.debug("Request to get all People");

    int start = pageable.getOffset();
    int end = start + pageable.getPageSize();

    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW);
    query = query.replaceAll("WHERECLAUSE", " WHERE 1=1 ");
    if (pageable.getSort() != null) {
      if (pageable.getSort().iterator().hasNext()) {
        String orderByFirstCriteria = pageable.getSort().iterator().next().toString();
        String orderByClause = orderByFirstCriteria.replaceAll(":", " ");
        query = query.replaceAll("ORDERBYCLAUSE", " ORDER BY " + orderByClause);
      } else {
        query = query.replaceAll("ORDERBYCLAUSE", "");
      }
    } else {
      query = query.replaceAll("ORDERBYCLAUSE", "");
    }

    query = query.replaceAll("LIMITCLAUSE", "limit " + start + "," + end);

    log.debug("running full person query with no filters");
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    List<PersonViewDTO> persons = jdbcTemplate.query(query, new PersonViewRowMapper());
    stopWatch.stop();
    log.debug("full person query finished in: [{}]s", stopWatch.getTotalTimeSeconds());

    if (CollectionUtils.isEmpty(persons)) {
      return new PageImpl<>(persons);
    }

    Integer personCount;
    log.info("running count query");
    stopWatch.start();
    String countQuery = "SELECT COUNT(1) from Person";
    countQuery = countQuery.replaceAll("WHERECLAUSE", " WHERE 1=1 ");
    personCount = jdbcTemplate.queryForObject(countQuery, Integer.class);
    stopWatch.stop();
    log.info("finished count query [{}]s", stopWatch.getTotalTimeSeconds());

    return new PageImpl<>(persons, pageable, personCount);
  }

  /**
   * Advanced search for person list view.
   * <p>
   * There are two queries that happen in this method, one to retrieve the data based on the search and column filters
   * and the second to get the count so that we can support pagination
   *
   * @param searchString  the search string to match, can be null
   * @param columnFilters
   * @param pageable      the pagination information
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<PersonBasicDetailsDTO> basicDetailsSearch(String searchString) {
    List<Specification<PersonBasicDetails>> specs = new ArrayList<>();
    if (StringUtils.isNotEmpty(searchString)) {
  //add the text search criteria
      specs.add(Specifications.where(containsLike("firstName", searchString)).
          or(containsLike("lastName", searchString)).
          or(containsLike("gmcDetails.gmcNumber", searchString)));
    }
    Pageable pageable = new PageRequest(0, PERSON_BASIC_DETAILS_MAX_RESULTS);

    Page<PersonBasicDetails> result;
    if (Collections.isEmpty(specs)) {
      result = personBasicDetailsRepository.findAll(pageable);
    } else {
      Specifications<PersonBasicDetails> fullSpec = Specifications.where(specs.get(0));
      result = personBasicDetailsRepository.findAll(fullSpec, pageable);
    }

    return result.map(person -> personBasicDetailsMapper.toDto(person)).getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PersonViewDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    String whereClause = createWhereClause(searchString, columnFilters);

    log.info("running count query");
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    String countQuery = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW_COUNT);
    countQuery = countQuery.replaceAll("WHERECLAUSE", whereClause);
    Integer personCount = jdbcTemplate.queryForObject(countQuery, Integer.class);
    stopWatch.stop();
    log.info("count query finished in: [{}]s", stopWatch.getTotalTimeSeconds());

    int start = pageable.getOffset();
    int end = pageable.getPageSize();

    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW);
    query = query.replaceAll("WHERECLAUSE", whereClause);
    if (pageable.getSort() != null) {
      if (pageable.getSort().iterator().hasNext()) {
        String orderByFirstCriteria = pageable.getSort().iterator().next().toString();
        String orderByClause = orderByFirstCriteria.replaceAll(":", " ");

        query = query.replaceAll("ORDERBYCLAUSE", " ORDER BY " + orderByClause);
      } else {
        query = query.replaceAll("ORDERBYCLAUSE", "");
      }
    } else {
      query = query.replaceAll("ORDERBYCLAUSE", "");
    }

    //limit is 0 based
    query = query.replaceAll("LIMITCLAUSE", "limit " + start + "," + end);

    log.info("running person query");
    stopWatch = new StopWatch();
    stopWatch.start();
    List<PersonViewDTO> persons = jdbcTemplate.query(query, new PersonViewRowMapper());
    stopWatch.stop();
    log.info("person query finished in: [{}]s", stopWatch.getTotalTimeSeconds());

    if (CollectionUtils.isEmpty(persons)) {
      return new PageImpl<>(persons);
    }

    return new PageImpl<>(persons, pageable, personCount);
  }

  private String createWhereClause(String searchString, List<ColumnFilter> columnFilters) {
    StringBuilder whereClause = new StringBuilder();
    whereClause.append(" WHERE 1=1 ");

    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> {

        switch (cf.getName()) {
          case "programmeName":
            whereClause.append(" AND prg.programmeName in (");
            break;
          case "gradeId":
            whereClause.append(" AND pl.gradeId in (");
            break;
          case "specialty":
            whereClause.append(" AND s.name in (");
            break;
          case "placementType":
            whereClause.append(" AND pl.placementType in (");
            break;
          case "siteId":
            whereClause.append(" AND pl.siteId in (");
            break;
          case "role":
            whereClause.append(" AND p.role in (");
            break;
          case "currentOwner":
            whereClause.append(" AND lo.owner in (");
            break;
          case "status":
            whereClause.append(" AND p.status in (");
            break;
          default:
            throw new IllegalArgumentException("Not accounted for column filter [" + cf.getName() +
                "] you need to add an additional case statement or remove it from the request");
        }
        cf.getValues().stream().forEach(k -> whereClause.append("'" + k + "',"));
        whereClause.deleteCharAt(whereClause.length() - 1);
        whereClause.append(")");
      });
    }

    if (StringUtils.isNotEmpty(searchString)) {
      whereClause.append(" AND ( p.publicHealthNumber like ").append("'%" + searchString + "%'");
      whereClause.append(" OR cd.surname like ").append("'%" + searchString + "%'");
      whereClause.append(" OR cd.forenames like ").append("'%" + searchString + "%'");
      whereClause.append(" OR gmc.gmcNumber like ").append("'%" + searchString + "%'");
      whereClause.append(" OR gdc.gdcNumber like ").append("'%" + searchString + "%'");
      whereClause.append(" OR p.role like ").append("'%" + searchString + "%'");
      if (StringUtils.isNumeric(searchString)) {
        whereClause.append(" OR p.id in ").append("(" + Lists.newArrayList(Long.parseLong(searchString)).
            toString().replace("[", "").replace("]", "") + ")");
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
  public PersonDTO findOne(Long id) {
    log.debug("Request to get Person : {}", id);
    Person person = personRepository.findOne(id);
    return personMapper.toDto(person);
  }

  /**
   * Get persons by ids.
   *
   * @param ids the Ids of the entities
   * @return the entities
   */
  @Override
  @Transactional(readOnly = true)
  public List<PersonBasicDetailsDTO> findByIdIn(Set<Long> ids) {
  log.debug("Request to get all person basic details {} ", ids);

  return personBasicDetailsRepository.findByIdIn(ids).stream()
      .map(personBasicDetailsMapper::toDto)
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
  public Long findIdByGmcId(String gmcId) {
    log.debug("Request to get Person by GMC Id : {}", gmcId);
    return gmcDetailsRepository.findByGmcNumber(gmcId).getId();
  }

  @Override
  public PersonBasicDetailsDTO getBasicDetails(Long id) {
    PersonBasicDetails details = personBasicDetailsRepository.findOne(id);
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
  public void delete(Long id) {
    log.debug("Request to delete Person : {}", id);
    personRepository.delete(id);
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
  public void setRightToWorkRepository(RightToWorkRepository rightToWorkRepository) {
    this.rightToWorkRepository = rightToWorkRepository;
  }

  private class PersonViewRowMapper implements RowMapper<PersonViewDTO> {

    @Override
    public PersonViewDTO mapRow(ResultSet rs, int i) throws SQLException {
      PersonViewDTO view = new PersonViewDTO();
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
      String status = rs.getString("status");
      if (StringUtils.isNotEmpty(status)) {
        view.setStatus(Status.valueOf(status));
      }
      view.setCurrentOwner(rs.getString("currentOwner"));
      String ownerRule = rs.getString("currentOwnerRule");
      if (StringUtils.isNotEmpty(ownerRule)) {
        view.setCurrentOwnerRule(PersonOwnerRule.valueOf(ownerRule));
      }
      return view;
    }
  }
}
