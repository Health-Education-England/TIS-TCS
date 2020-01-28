package com.transformuk.hee.tis.tcs.service.repository;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
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

    Assert.assertTrue(result.isPresent());
    Assert.assertEquals(personWithTrusts, result.get());
    Person person = result.get();
    Assert.assertTrue(person.getAssociatedTrusts().contains(associatedTrust1));
    Assert.assertTrue(person.getAssociatedTrusts().contains(associatedTrust2));
  }
}
