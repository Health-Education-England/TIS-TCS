package com.transformuk.hee.tis.tcs.service.repository;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProgrammeRepositoryIntTest {

  @Autowired
  private ProgrammeRepository testObj;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private ProgrammeMembershipRepository programmeMembershipRepository;

  @Autowired
  private CurriculumRepository curriculumRepository;

  @Before
  public void setup() {

  }

  @Transactional
  @Test
  public void findByProgrammeMembershipPersonIdShouldReturnAUniqueCollectionOfProgrammesThePersonIsEnrolledOn() {
    Programme programme1 = new Programme(), programme2 = new Programme(), programme3 = new Programme();

    programme1.setProgrammeName("Programme 1");
    programme2.setProgrammeName("Programme 2");
    programme3.setProgrammeName("Programme 3");

    testObj.saveAll(Lists.newArrayList(programme1, programme2, programme3));
    testObj.flush();

    Person person1 = new Person(), person2 = new Person();

    personRepository.saveAll(Lists.newArrayList(person1, person2));
    personRepository.flush();

    ProgrammeMembership pm1 = new ProgrammeMembership(), pm2 = new ProgrammeMembership(), pm3 = new ProgrammeMembership(),
        pmWithSameProgramme = new ProgrammeMembership();

    pm1.setProgramme(programme1);
    pm1.setPerson(person1);
    pm2.setProgramme(programme2);
    pm2.setPerson(person1);
    pmWithSameProgramme.setProgramme(programme2);
    pmWithSameProgramme.setPerson(person1);

    pm3.setProgramme(programme3);
    pm3.setPerson(person2);

    programmeMembershipRepository.saveAll(Lists.newArrayList(pm1, pm2, pm3, pmWithSameProgramme));
    programmeMembershipRepository.flush();

    List<Programme> result = testObj.findByProgrammeMembershipPersonId(person1.getId());

    //no duplicate programmes - this would have 3 if we didn't have the distinct
    Assert.assertEquals(2, result.size());

    Assert.assertEquals(1, result.stream().filter(p -> p.getId().equals(programme1.getId())).count());
    Assert.assertEquals(1, result.stream().filter(p -> p.getId().equals(programme2.getId())).count());
  }


  @Test
  public void programmeCurriculumAssociationExistsShouldReturnTrueWhenThereIsALinkBetweenCurriculaAndProgramme() {

    Curriculum curriculum = new Curriculum();
    curriculum.setName("Curricula 1");
    curriculum = curriculumRepository.saveAndFlush(curriculum);

    Programme programme = new Programme();
    programme.setProgrammeName("Programme 1");
    programme.setCurricula(Sets.newHashSet(curriculum));
    programme = testObj.saveAndFlush(programme);

    boolean result = testObj.programmeCurriculumAssociationExists(programme.getId(), curriculum.getId());

    Assert.assertTrue(result);
  }
}
