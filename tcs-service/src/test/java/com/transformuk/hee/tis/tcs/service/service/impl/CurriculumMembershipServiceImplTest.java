package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import java.time.LocalDate;
import java.util.UUID;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CurriculumMembershipServiceImplTest {

  private static final long TRAINEE_ID = 1L;
  private static final String TRAINEE_NUMBER = "XXX/XXX/XXX";
  private static final long CURRICULUM_1_ID = 5L;
  private static final long PROGRAMME_ID = 2L;
  private static final ProgrammeMembershipType PROGRAMME_MEMBERSHIP_TYPE = ProgrammeMembershipType.SUBSTANTIVE;
  private static final UUID PROGRAMME_MEMBERSHIP_ID_1 = UUID.randomUUID();
  private static final UUID PROGRAMME_MEMBERSHIP_ID_2 = UUID.randomUUID();
  private static final Long CURRICULUM_MEMBERSHIP_ID_1 = 7777L;
  private static final Long CURRICULUM_MEMBERSHIP_ID_2 = 8888L;
  private static final LocalDate PROGRAMME_START_DATE = LocalDate.of(2020, 1, 1);
  private static final LocalDate PROGRAMME_END_DATE = LocalDate.of(2022, 1, 1);
  private static final String PROGRAMME_NUMBER1 = "Programme number";
  private static final String PROGRAMME_NAME = "Programme Name";
  private final CurriculumMembership curriculumMembership1 = new CurriculumMembership();
  private final CurriculumMembership curriculumMembership2 = new CurriculumMembership();
  private final ProgrammeMembership programmeMembership1 = new ProgrammeMembership();
  private final Curriculum curriculum1 = new Curriculum();
  private final Curriculum curriculum2 = new Curriculum();
  private final Programme programme = new Programme();
  private final Person person = new Person();
  private final ProgrammeMembershipDTO programmeMembershipDto1 = new ProgrammeMembershipDTO();
  private CurriculumMembershipServiceImpl testObj;
  @Mock
  private ProgrammeMembershipRepository programmeMembershipRepositoryMock;
  @Mock
  private CurriculumMembershipRepository curriculumMembershipRepositoryMock;
  @Mock
  private ProgrammeMembershipServiceImpl programmeMembershipServiceImplMock;

  @Before
  public void setup() {
    testObj = new CurriculumMembershipServiceImpl(curriculumMembershipRepositoryMock,
        programmeMembershipRepositoryMock, programmeMembershipServiceImplMock);
    initialiseData();
  }

  private void initialiseData() {
    person.setId(1L);
    person.setProgrammeMemberships(Sets.newHashSet());

    programme.setId(PROGRAMME_ID);
    programme.setProgrammeNumber(PROGRAMME_NUMBER1);
    programme.setProgrammeName(PROGRAMME_NAME);

    curriculumMembership1.setId(CURRICULUM_MEMBERSHIP_ID_1);
    curriculumMembership1.setCurriculumId(5L);
    curriculumMembership1.setProgrammeMembership(programmeMembership1);

    curriculumMembership2.setId(CURRICULUM_MEMBERSHIP_ID_2);
    curriculumMembership2.setCurriculumId(6L);
    curriculumMembership2.setProgrammeMembership(programmeMembership1);

    programmeMembership1.setUuid(PROGRAMME_MEMBERSHIP_ID_1);
    programmeMembership1.setProgrammeMembershipType(PROGRAMME_MEMBERSHIP_TYPE);
    programmeMembership1.setProgramme(programme);
    programmeMembership1.setProgrammeStartDate(PROGRAMME_START_DATE);
    programmeMembership1.setProgrammeEndDate(PROGRAMME_END_DATE);
    programmeMembership1.setPerson(person);
    programmeMembership1.setCurriculumMemberships(
        Sets.newLinkedHashSet(curriculumMembership1, curriculumMembership2));
  }

  @Test
  public void shouldDeleteCurriculumMemberships() {
    //given
    when(curriculumMembershipRepositoryMock.getOne(CURRICULUM_MEMBERSHIP_ID_1))
        .thenReturn(curriculumMembership1);
    when(curriculumMembershipRepositoryMock.getOne(CURRICULUM_MEMBERSHIP_ID_2))
        .thenReturn(curriculumMembership2);

    //when
    testObj.delete(CURRICULUM_MEMBERSHIP_ID_1);

    //then
    verify(programmeMembershipRepositoryMock, times(1))
        .save(any()); //since there were 2 CMs, so the PM is updated not deleted

    testObj.delete(CURRICULUM_MEMBERSHIP_ID_2);

    verify(programmeMembershipRepositoryMock, times(1))
        .delete(any()); //since the last CM is deleted, so the PM is deleted too
  }
}
