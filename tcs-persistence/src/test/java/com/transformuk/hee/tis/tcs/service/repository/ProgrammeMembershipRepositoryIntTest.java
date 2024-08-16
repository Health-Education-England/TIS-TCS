package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import java.util.List;
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
public class ProgrammeMembershipRepositoryIntTest {

  @Autowired
  ProgrammeMembershipRepository programmeMembershipRepository;

  @Autowired
  ProgrammeRepository programmeRepository;

  @Autowired
  PersonRepository personRepository;

  @Autowired
  TrainingNumberRepository trainingNumberRepository;

  Programme programme1;
  Person person1;
  Person person2;
  TrainingNumber trainingNumber1;
  ProgrammeMembership pm1;
  ProgrammeMembership pm2;

  @Before
  public void setUp() {
    programme1 = new Programme();
    programme1 = programmeRepository.saveAndFlush(programme1);

    person1 = new Person();
    person2 = new Person();
    person1 = personRepository.saveAndFlush(person1);
    person2 = personRepository.saveAndFlush(person2);

    trainingNumber1 = new TrainingNumber();
    trainingNumber1 = trainingNumberRepository.saveAndFlush(trainingNumber1);

    pm1 = new ProgrammeMembership();
    pm1.setProgramme(programme1);
    pm1.setPerson(person1);
    pm1.setTrainingNumber(trainingNumber1);

    pm2 = new ProgrammeMembership();
    pm2.setProgramme(programme1);
    pm2.setPerson(person2);
    pm2.setTrainingNumber(trainingNumber1);

    programmeMembershipRepository.saveAll(Lists.newArrayList(pm1, pm2));
    programmeMembershipRepository.flush();
  }
}
