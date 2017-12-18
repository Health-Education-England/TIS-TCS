package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import com.transformuk.hee.tis.tcs.service.model.PersonView;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonBasicDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonBasicDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonViewMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;


/**
 * Service Implementation for managing Person.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

  private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

  private static final int PERSON_BASIC_DETAILS_MAX_RESULTS = 100;

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private GmcDetailsRepository gmcDetailsRepository;
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
    return personViewRepository.findAll(pageable)
        .map(personViewMapper::personViewToPersonViewDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PersonViewDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<PersonView>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("publicHealthNumber", searchString)).
          or(containsLike("surname", searchString)).
          or(containsLike("forenames", searchString)).
          or(containsLike("gmcNumber", searchString)).
          or(containsLike("gdcNumber", searchString)).
          or(containsLike("placementType", searchString)).
          or(containsLike("role", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Specifications<PersonView> fullSpec = Specifications.where(specs.get(0));
    //add the rest of the specs that made it in
    for (int i = 1; i < specs.size(); i++) {
      fullSpec = fullSpec.and(specs.get(i));
    }
    Page<PersonView> result = personViewRepository.findAll(fullSpec, pageable);

    return result.map(personViewMapper::personViewToPersonViewDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PersonBasicDetailsDTO> basicDetailsSearch(String searchString) {
    List<Specification<PersonBasicDetails>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("firstName", searchString)).
          or(containsLike("lastName", searchString)).
          or(containsLike("gmcDetails.gmcNumber", searchString)));
    }
    Specifications<PersonBasicDetails> fullSpec = Specifications.where(specs.get(0));
    Pageable pageable = new PageRequest(0, PERSON_BASIC_DETAILS_MAX_RESULTS);

    Page<PersonBasicDetails> result = personBasicDetailsRepository.findAll(fullSpec, pageable);

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
  public PersonDTO findOne(Long id) {
    log.debug("Request to get Person : {}", id);
    Person person = personRepository.findOne(id);
    return personMapper.toDto(person);
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
}
