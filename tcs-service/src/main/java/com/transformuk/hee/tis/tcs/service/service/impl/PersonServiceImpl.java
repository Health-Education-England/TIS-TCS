package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.api.enumeration.PersonOwnerRule;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.BasicPage;
import com.transformuk.hee.tis.tcs.service.model.*;
import com.transformuk.hee.tis.tcs.service.repository.*;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonBasicDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonLiteMapper;
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
import java.util.Collection;
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
    private PersonLiteMapper personLiteMapper;
    @Autowired
    private PersonBasicDetailsRepository personBasicDetailsRepository;
    @Autowired
    private PersonBasicDetailsMapper personBasicDetailsMapper;
    @Autowired
    private PersonViewRepository personViewRepository;
    @Autowired
    private PersonViewMapper personViewMapper;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ReferenceService referenceService;

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
    public PersonDTO save(final PersonDTO personDTO) {
        log.debug("Request to save Person : {}", personDTO);
        Person person = personMapper.toEntity(personDTO);

        final Long personDtoId = personDTO.getId();
        if (!permissionService.canEditSensitiveData() && personDtoId != null) {
            final Person originalPerson = personRepository.findOne(personDtoId);
            if (originalPerson == null) { //this shouldn't happen
                throw new IllegalArgumentException("The person record for id " + personDtoId + " could not be found");
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
        return personDTO1;
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
    public PersonDTO create(final PersonDTO personDTO) {
        log.debug("Request to save Person : {}", personDTO);
        Person person = personMapper.toEntity(personDTO);
        person = personRepository.save(person);

        final GdcDetails gdcDetails = person.getGdcDetails() != null ? person.getGdcDetails() : new GdcDetails();
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
        if (!permissionService.canEditSensitiveData()) {
            clearSensitiveData(personalDetails);
        }
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
    public List<PersonDTO> save(final List<PersonDTO> personDTOs) {
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
    public BasicPage<PersonViewDTO> findAll(final Pageable pageable) {
        log.debug("Request to get all People");

        final int start = pageable.getOffset();
        final int end = start + pageable.getPageSize();

        String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW);
        query = query.replaceAll("WHERECLAUSE", " WHERE 1=1 ");
        if (pageable.getSort() != null) {
            if (pageable.getSort().iterator().hasNext()) {
                final String orderByFirstCriteria = pageable.getSort().iterator().next().toString();
                final String orderByClause = orderByFirstCriteria.replaceAll(":", " ");
                query = query.replaceAll("ORDERBYCLAUSE", " ORDER BY " + orderByClause);
            } else {
                query = query.replaceAll("ORDERBYCLAUSE", "");
            }
        } else {
            query = query.replaceAll("ORDERBYCLAUSE", "");
        }

        query = query.replaceAll("LIMITCLAUSE", "limit " + start + "," + end);

        log.debug("running full person query with no filters");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final List<PersonViewDTO> persons = jdbcTemplate.query(query, new PersonViewRowMapper());
        stopWatch.stop();
        log.debug("full person query finished in: [{}]s", stopWatch.getTotalTimeSeconds());

        if (CollectionUtils.isEmpty(persons)) {
            return new BasicPage<>(persons, pageable);
        }
        return new BasicPage<>(persons, pageable);
    }

    public Integer findAllCountQuery() {
        final Integer personCount;
        log.info("running count query");
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String countQuery = "SELECT COUNT(1) from Person";
        countQuery = countQuery.replaceAll("WHERECLAUSE", " WHERE 1=1 ");
        personCount = jdbcTemplate.queryForObject(countQuery, Integer.class);
        stopWatch.stop();
        log.info("finished count query [{}]s", stopWatch.getTotalTimeSeconds());
        return personCount;
    }

    /**
     * Advanced search for person list view.
     * <p>
     * There are two queries that happen in this method, one to retrieve the data based on the search and column filters
     * and the second to get the count so that we can support pagination
     *
     * @param searchString the search string to match, can be null
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<PersonBasicDetailsDTO> basicDetailsSearch(final String searchString) {
        final List<Specification<PersonBasicDetails>> specs = new ArrayList<>();
        if (StringUtils.isNotEmpty(searchString)) {
            specs.add(Specifications.where(containsLike("firstName", searchString)).
                    or(containsLike("lastName", searchString)).
                    or(containsLike("gmcDetails.gmcNumber", searchString)));
        }
        final Pageable pageable = new PageRequest(0, PERSON_BASIC_DETAILS_MAX_RESULTS);

        final Page<PersonBasicDetails> result;
        if (Collections.isEmpty(specs)) {
            result = personBasicDetailsRepository.findAll(pageable);
        } else {
            final Specifications<PersonBasicDetails> fullSpec = Specifications.where(specs.get(0));
            result = personBasicDetailsRepository.findAll(fullSpec, pageable);
        }

        return result.map(person -> personBasicDetailsMapper.toDto(person)).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public BasicPage<PersonViewDTO> advancedSearch(final String searchString, final List<ColumnFilter> columnFilters, final Pageable pageable) {
        final String whereClause = createWhereClause(searchString, columnFilters);
        final int start = pageable.getOffset();
        final int end = pageable.getPageSize() + 1;

        String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW);
        query = query.replaceAll("WHERECLAUSE", whereClause);
        if (pageable.getSort() != null) {
            if (pageable.getSort().iterator().hasNext()) {
                final String orderByFirstCriteria = pageable.getSort().iterator().next().toString();
                final String orderByClause = orderByFirstCriteria.replaceAll(":", " ");

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
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<PersonViewDTO> persons = jdbcTemplate.query(query, new PersonViewRowMapper());
        final boolean hasNext = persons.size() > pageable.getPageSize();
        if (hasNext) {
            persons = persons.subList(0, pageable.getPageSize()); //ignore any additional
        }
        stopWatch.stop();
        log.info("person query finished in: [{}]s", stopWatch.getTotalTimeSeconds());

        if (CollectionUtils.isEmpty(persons)) {
            return new BasicPage<>(persons, pageable);
        }

        return new BasicPage<>(persons, pageable, hasNext);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer advancedSearchCountQuery(final String searchString, final List<ColumnFilter> columnFilters, final Pageable pageable) {
        log.info("running count query");
        final String whereClause = createWhereClause(searchString, columnFilters);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String countQuery = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW_COUNT);
        countQuery = countQuery.replaceAll("WHERECLAUSE", whereClause);
        final Integer personCount = jdbcTemplate.queryForObject(countQuery, Integer.class);
        stopWatch.stop();
        log.info("count query finished in: [{}]s", stopWatch.getTotalTimeSeconds());
        return personCount;
    }

    private String createWhereClause(final String searchString, final List<ColumnFilter> columnFilters) {
        final StringBuilder whereClause = new StringBuilder();
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
    public PersonDTO findOne(final Long id) {
        log.debug("Request to get Person : {}", id);
        final Person person = personRepository.findOne(id);
        final boolean canViewSensitiveData = permissionService.canViewSensitiveData();
        if (!canViewSensitiveData) {
            final PersonalDetails personalDetails = person.getPersonalDetails();
            clearSensitiveData(personalDetails);
        }
        return personMapper.toDto(person);
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
    public List<PersonBasicDetailsDTO> findBasicDetailsByIdIn(final Set<Long> ids) {
        log.debug("Request to get all person basic details {} ", ids);

        return personBasicDetailsRepository.findAll(ids).stream()
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

        return personRepository.findAll(ids).stream()
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
    public List<PersonDTO> findPersonsByPublicHealthNumbersIn(final List<String> publicHealthNumbers) {
        return personRepository.findByPublicHealthNumberIn(publicHealthNumbers).stream()
                .map(personMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PersonLiteDTO> searchByRoleCategory(final String query, final Long categoryId, final Pageable pageable) {
        log.debug("Received request to search '{}' with RoleCategory ID '{}' and query '{}'",
                PersonLiteDTO.class.getSimpleName(), categoryId, query);

        log.debug("Accessing '{}' to load '{}' for Category ID '{}'",
                referenceService.getClass().getSimpleName(), RoleDTO.class.getSimpleName(), categoryId);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Collection<RoleDTO> rolesByCategory = referenceService.getRolesByCategory(categoryId);

        stopWatch.stop();

        log.info("'{}' returned '{}' '{}' for Category ID '{}'; took '{}'ms",
                referenceService.getClass().getSimpleName(), rolesByCategory.size(), RoleDTO.class.getSimpleName(), categoryId, stopWatch.getTotalTimeSeconds());

        if (rolesByCategory.isEmpty()) {
            return new PageImpl<>(java.util.Collections.emptyList(), pageable, 0);
        }

        final Set<String> roles = rolesByCategory.stream()
                .map(RoleDTO::getCode)
                .collect(Collectors.toSet());

        log.debug("Accessing '{}' to search '{}' with roles for Category ID '{}' and query '{}'",
                personRepository.getClass().getSimpleName(), PersonLiteDTO.class.getSimpleName(), categoryId, query);

        return personRepository.searchByRoleCategory(query, roles, pageable)
                .map(personLiteMapper::toDto);
    }

    @Override
    public PersonBasicDetailsDTO getBasicDetails(final Long id) {
        final PersonBasicDetails details = personBasicDetailsRepository.findOne(id);
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
    public void setRightToWorkRepository(final RightToWorkRepository rightToWorkRepository) {
        this.rightToWorkRepository = rightToWorkRepository;
    }

    private class PersonViewRowMapper implements RowMapper<PersonViewDTO> {

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
