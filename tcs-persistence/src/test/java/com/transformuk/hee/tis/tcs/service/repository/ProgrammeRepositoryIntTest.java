package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;

import java.util.List;
import javax.persistence.EntityManager;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class ProgrammeRepositoryIntTest {
// TODO Rollback unneccessary parts of these changes
  @Autowired
  private ProgrammeRepository testObj;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private CurriculumMembershipRepository curriculumMembershipRepository;

  @Autowired
  private ProgrammeCurriculumRepository programmeCurriculumRepository;

  @Autowired
  private CurriculumRepository curriculumRepository;

  @Autowired
  private EntityManager entityManager;

  @Before
  public void setup() {

  }

  @Transactional
  @Test
  public void findByCurriculumMembershipPersonIdShouldReturnAUniqueCollectionOfProgrammesThePersonIsEnrolledOn() {
    Programme programme1 = new Programme(), programme2 = new Programme(), programme3 = new Programme();

    programme1.setProgrammeName("Programme 1");
    programme2.setProgrammeName("Programme 2");
    programme3.setProgrammeName("Programme 3");

    testObj.saveAll(Lists.newArrayList(programme1, programme2, programme3));
    testObj.flush();

    Person person1 = new Person(), person2 = new Person();

    personRepository.saveAll(Lists.newArrayList(person1, person2));
    personRepository.flush();

    CurriculumMembership pm1 = new CurriculumMembership(), pm2 = new CurriculumMembership(), pm3 = new CurriculumMembership(),
        pmWithSameProgramme = new CurriculumMembership();

    pm1.setProgramme(programme1);
    pm1.setPerson(person1);
    pm2.setProgramme(programme2);
    pm2.setPerson(person1);
    pmWithSameProgramme.setProgramme(programme2);
    pmWithSameProgramme.setPerson(person1);

    pm3.setProgramme(programme3);
    pm3.setPerson(person2);

    curriculumMembershipRepository.saveAll(Lists.newArrayList(pm1, pm2, pm3, pmWithSameProgramme));
    curriculumMembershipRepository.flush();

    List<Programme> result = testObj.findByCurriculumMembershipPersonId(person1.getId());

    //no duplicate programmes - this would have 3 if we didn't have the distinct
    Assert.assertEquals(2, result.size());

    Assert
        .assertEquals(1, result.stream().filter(p -> p.getId().equals(programme1.getId())).count());
    Assert
        .assertEquals(1, result.stream().filter(p -> p.getId().equals(programme2.getId())).count());
  }


  @Test
  @Transactional
  public void programmeCurriculumAssociationExistsShouldReturnTrueWhenThereIsALinkBetweenCurriculaAndProgramme() {

    Curriculum curriculum = new Curriculum();
    curriculum.setName("Curricula 1");
    curriculum = curriculumRepository.saveAndFlush(curriculum);

    ProgrammeCurriculum join = new ProgrammeCurriculum();
    join.setCurriculum(curriculum);

    Programme programme = new Programme();
    programme.setProgrammeName("Programme 1");
    programme = testObj.saveAndFlush(programme);
    join.setProgramme(programme);
    join.setCurriculum(curriculum);
    //TODO Modify tests to save/get GMC Programme Code
    join = entityManager.merge(join);

    boolean result = testObj
        .programmeCurriculumAssociationExists(programme.getId(), curriculum.getId());

    Assert.assertTrue(result);
  }

  @Test
  @Transactional
  public void programCurriculumCanBeSavedFollowingProgramme() {
    final String GMC_CODE = "GMC-XXX";
    Curriculum curriculum = new Curriculum();
    curriculum.setName("Curricula 1");
    curriculum = curriculumRepository.saveAndFlush(curriculum);

    ProgrammeCurriculum join = new ProgrammeCurriculum();
    join.setCurriculum(curriculum);

    Programme programme = new Programme();
    programme.setProgrammeName("Programme 1");
    programme = testObj.saveAndFlush(programme);
    join.setProgramme(programme);
    join.setCurriculum(curriculum);
    join.setGmcProgrammeCode(GMC_CODE);
    join = entityManager.merge(join);

    ProgrammeCurriculum byId = programmeCurriculumRepository.getOne(join.getId());
    Assert.assertEquals(GMC_CODE, byId.getGmcProgrammeCode());
  }
}
