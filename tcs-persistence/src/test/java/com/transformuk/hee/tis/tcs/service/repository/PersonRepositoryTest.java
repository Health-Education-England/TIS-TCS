package com.transformuk.hee.tis.tcs.service.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionDto;
import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class PersonRepositoryTest {

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private EntityManager entityManager;

  private Person personWithTrusts, personWithNoTrusts;
  private PersonTrust associatedTrust1, associatedTrust2;

  @Before
  @Transactional
  public void setup() {
    associatedTrust1 = new PersonTrust();
    associatedTrust1.setTrustId(1111L);
    associatedTrust2 = new PersonTrust();
    associatedTrust2.setTrustId(2222L);

    personWithTrusts = new Person();
    personWithTrusts.setAssociatedTrusts(Sets.newHashSet(associatedTrust1, associatedTrust2));
    entityManager.persist(personWithTrusts);

    personWithNoTrusts = new Person();
    entityManager.persist(personWithNoTrusts);
  }

  @After
  @Transactional
  public void tearDown() {
    entityManager.remove(personWithTrusts);
    entityManager.remove(associatedTrust1);
    entityManager.remove(associatedTrust2);
  }

  @Test
  @Transactional
  public void findPersonByIdShouldAlsoRetrieveAssociatedTrusts() {
    Optional<Person> result = personRepository.findPersonById(personWithTrusts.getId());

    assertTrue(result.isPresent());
    assertEquals(personWithTrusts, result.get());
    Person person = result.get();
    assertTrue(person.getAssociatedTrusts().contains(associatedTrust1));
    assertTrue(person.getAssociatedTrusts().contains(associatedTrust2));
  }

  @Test
  @Transactional
  @Sql(scripts = "/scripts/person.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePerson.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void shouldGetHiddenTraineeRecordsWhenSearchIsFalse() {
    List<String> gmcIds = Lists.newArrayList("11111111", "22222222", "33333333");
    Page<ConnectionDto> connectionDtoPage =
        personRepository.getHiddenTraineeRecords(PageRequest.of(0, 20), gmcIds, false, "11111111");

    List<ConnectionDto> connectionDtos = connectionDtoPage.getContent();
    assertEquals(1, connectionDtos.size());
    ConnectionDto connectionDto = connectionDtos.get(0);
    assertEquals(1, connectionDto.getPersonId());
    assertEquals("General Practice", connectionDto.getProgrammeName());
  }

  @Test
  @Transactional
  @Sql(scripts = "/scripts/person.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePerson.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void shouldGetHiddenTraineeRecordsWhenSearchIsTrue() {
    List<String> gmcIds = Lists.newArrayList("11111111", "22222222", "33333333");
    Page<ConnectionDto> connectionDtoPage =
        personRepository.getHiddenTraineeRecords(PageRequest.of(0, 20), gmcIds, true, "null");

    List<ConnectionDto> connectionDtos = connectionDtoPage.getContent();
    assertEquals(3, connectionDtos.size());
    List<String> returnedGmcList = connectionDtos.stream().map(cd -> cd.getGmcNumber())
        .collect(Collectors.toList());
    assertTrue(returnedGmcList.containsAll(gmcIds));
  }

  @Test
  @Transactional
  @Sql(scripts = "/scripts/person.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/scripts/deletePerson.sql",
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void shouldGetExceptionTraineeRecords() {
    List<String> gmcIds = Lists.newArrayList("11111111", "22222222", "33333333");
    Set<String> owners = Sets.newHashSet("Yorkshire and the Humber", "East of England",
        "North East", "West Midlands");
    Page<Map<String, Object>> returnPage =
        personRepository.getExceptionTraineeRecords(PageRequest.of(0, 20), gmcIds, true, "null",
            owners);

    List<Map<String, Object>> result = returnPage.getContent();
    assertEquals(4, result.size());
    List<String> resultGmcList = result.stream().map(r -> (String) (r.get("GMCNUMBER")))
        .collect(Collectors.toList());
    assertTrue(resultGmcList.containsAll(gmcIds));
    long gmc1Count = resultGmcList.stream().filter(gmc -> StringUtils.equals("11111111", gmc))
        .count();
    assertEquals(2l, gmc1Count);
  }
}
