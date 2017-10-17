package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private PersonMapper personMapper;

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
  public Page<PersonDTO> findAll(Pageable pageable) {
    log.debug("Request to get all People");
    return personRepository.findAll(pageable)
        .map(personMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PersonDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<Person>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("publicHealthNumber", searchString)).
          or(containsLike("contactDetails.surname", searchString)).
          or(containsLike("contactDetails.forenames", searchString)).
          or(containsLike("gmcDetails.gmcNumber", searchString)).
          or(containsLike("gdcDetails.gdcNumber", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Specifications<Person> fullSpec = Specifications.where(specs.get(0));
    //add the rest of the specs that made it in
    for (int i = 1; i < specs.size(); i++) {
      fullSpec = fullSpec.and(specs.get(i));
    }
    Page<Person> result = personRepository.findAll(fullSpec, pageable);

    return result.map(person -> personMapper.toDto(person));
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
