package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Person.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

  private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

  private final PersonRepository personRepository;

  private final PersonMapper personMapper;

  public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
    this.personRepository = personRepository;
    this.personMapper = personMapper;
  }

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
    person = personRepository.save(person);
    return personMapper.toDto(person);
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
