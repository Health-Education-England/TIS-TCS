package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.GdcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PersonalDetailsService;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.QualificationService;
import com.transformuk.hee.tis.tcs.service.service.RightToWorkService;
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
import java.util.Set;
import java.util.stream.Collectors;

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
  private ContactDetailsService contactDetailsService;

  @Autowired
  private GmcDetailsService gmcDetailsService;

  @Autowired
  private GdcDetailsService gdcDetailsService;

  @Autowired
  private QualificationService qualificationService;

  @Autowired
  private ProgrammeMembershipService programmeMembershipService;

  @Autowired
  private RightToWorkService rightToWorkService;

  @Autowired
  private PersonalDetailsService personalDetailsService;

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

    ContactDetailsDTO contactDetailsDTO = personDTO.getContactDetails();
    PersonalDetailsDTO personalDetailsDTO = personDTO.getPersonalDetails();
    GmcDetailsDTO gmcDetailsDTO = personDTO.getGmcDetails();
    GdcDetailsDTO gdcDetailsDTO = personDTO.getGdcDetails();
    RightToWorkDTO rightToWorkDTO = personDTO.getRightToWork();
    Set<QualificationDTO> qualificationDTOs = personDTO.getQualifications();
    Set<ProgrammeMembershipDTO> programmeMembershipDTOs = personDTO.getProgrammeMemberships();

    personDTO.setContactDetails(null);
    personDTO.setPersonalDetails(null);
    personDTO.setGmcDetails(null);
    personDTO.setGdcDetails(null);
    personDTO.setRightToWork(null);
    personDTO.setQualifications(null);
    personDTO.setProgrammeMemberships(null);

    Person person = personMapper.toEntity(personDTO);
    person = personRepository.saveAndFlush(person);

    final Long personId = person.getId();

    final PersonDTO updatedPersonDTO = personMapper.toDto(person);

    if (contactDetailsDTO == null && personDTO.getId() == null) {
      contactDetailsDTO = new ContactDetailsDTO();
    }
    if (contactDetailsDTO != null) {
      contactDetailsDTO.setId(personId);
      updatedPersonDTO.setContactDetails(contactDetailsService.save(contactDetailsDTO));
    }

    if (personalDetailsDTO == null && personDTO.getId() == null) {
      personalDetailsDTO = new PersonalDetailsDTO();
    }
    if (personalDetailsDTO != null) {
      personalDetailsDTO.setId(personId);
      updatedPersonDTO.setPersonalDetails(personalDetailsService.save(personalDetailsDTO));
    }

    if (gmcDetailsDTO == null && personDTO.getId() == null) {
      gmcDetailsDTO = new GmcDetailsDTO();
    }
    if (gmcDetailsDTO != null) {
      gmcDetailsDTO.setId(personId);
      updatedPersonDTO.setGmcDetails(gmcDetailsService.save(gmcDetailsDTO));
    }

    if (gdcDetailsDTO == null && personDTO.getId() == null) {
      gdcDetailsDTO = new GdcDetailsDTO();
    }
    if (gdcDetailsDTO != null) {
      gdcDetailsDTO.setId(personId);
      updatedPersonDTO.setGdcDetails(gdcDetailsService.save(gdcDetailsDTO));
    }

    if (rightToWorkDTO == null && personDTO.getId() == null) {
      rightToWorkDTO = new RightToWorkDTO();
    }
    if (rightToWorkDTO != null) {
      rightToWorkDTO.setId(personId);
      updatedPersonDTO.setRightToWork(rightToWorkService.save(rightToWorkDTO));
    }

    if (qualificationDTOs != null) {
      qualificationDTOs.forEach(q -> q.setPerson(updatedPersonDTO));
      updatedPersonDTO.setQualifications(Sets.newHashSet(qualificationService.save(Lists.newArrayList(qualificationDTOs))));
    }

    if (programmeMembershipDTOs != null) {
      programmeMembershipDTOs.forEach(p -> p.setPerson(updatedPersonDTO));
      updatedPersonDTO.setProgrammeMemberships(Sets.newHashSet(programmeMembershipService.save(Lists.newArrayList(programmeMembershipDTOs))));
    }

    return findOne(updatedPersonDTO.getId());
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
    return personDTOs.stream().map(this::save).collect(Collectors.toList());
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
