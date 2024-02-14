package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ConditionsOfJoiningServiceImplTest {

  private static final Long CURRICULUM_MEMBERSHIP_ID = 40L;
  private static final UUID PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();

  private ConditionsOfJoiningService conditionsOfJoiningService;
  private ConditionsOfJoiningRepository repository;
  private ProgrammeMembershipRepository programmeMembershipRepository;
  private CurriculumMembershipRepository curriculumMembershipRepository;
  private ConditionsOfJoiningDto coj;

  @BeforeEach
  void setUp() {
    repository = mock(ConditionsOfJoiningRepository.class);
    ConditionsOfJoiningMapper mapper = Mappers.getMapper(ConditionsOfJoiningMapper.class);
    programmeMembershipRepository = mock(ProgrammeMembershipRepository.class);
    curriculumMembershipRepository = mock(CurriculumMembershipRepository.class);

    conditionsOfJoiningService = new ConditionsOfJoiningServiceImpl(repository, mapper,
        programmeMembershipRepository, curriculumMembershipRepository);
    coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);
  }

  @Test
  void saveShouldThrowExceptionWhenCurriculumMembershipIdNotFound() {
    when(curriculumMembershipRepository.getOne(CURRICULUM_MEMBERSHIP_ID)).thenThrow(
        new EntityNotFoundException("Expected"));

    assertThrows(IllegalArgumentException.class,
        () -> conditionsOfJoiningService.save(CURRICULUM_MEMBERSHIP_ID, coj));
    verify(repository, never()).save(any());
  }

  @Test
  void saveShouldThrowExceptionWhenProgrammeMembershipIdNotFound() {
    when(programmeMembershipRepository.getOne(PROGRAMME_MEMBERSHIP_UUID)).thenThrow(
        new EntityNotFoundException("Expected"));

    assertThrows(IllegalArgumentException.class,
        () -> conditionsOfJoiningService.save(PROGRAMME_MEMBERSHIP_UUID, coj));
    verify(repository, never()).save(any());
  }

  @Test
  void saveShouldThrowExceptionWhenInvalidIdProvided() {
    assertThrows(IllegalArgumentException.class,
        () -> conditionsOfJoiningService.save("not a long or a uuid", coj));
    verify(repository, never()).save(any());
  }

  @Test
  void saveShouldSaveTheConditionsOfJoiningAgainstTheProgrammeMembershipFromCmId() {
    ProgrammeMembership programmeMembership = new ProgrammeMembership();
    programmeMembership.setUuid(PROGRAMME_MEMBERSHIP_UUID);
    CurriculumMembership curriculumMembership = new CurriculumMembership();
    curriculumMembership.setProgrammeMembership(programmeMembership);

    when(curriculumMembershipRepository.getOne(CURRICULUM_MEMBERSHIP_ID)).thenReturn(
        curriculumMembership);
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

    ConditionsOfJoiningDto savedCoj = conditionsOfJoiningService.save(CURRICULUM_MEMBERSHIP_ID,
        coj);

    assertThat("Unexpected programme membership uuid.", savedCoj.getProgrammeMembershipUuid(),
        is(PROGRAMME_MEMBERSHIP_UUID));
    assertThat("Unexpected programme membership signed at.", savedCoj.getSignedAt(),
        is(SIGNED_AT));
    assertThat("Unexpected programme membership version.", savedCoj.getVersion(),
        is(GoldGuideVersion.GG9));
  }

  @Test
  void saveShouldSaveTheConditionsOfJoiningAgainstTheProgrammeMembershipFromPmId() {
    ProgrammeMembership programmeMembership = new ProgrammeMembership();
    programmeMembership.setUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(programmeMembershipRepository.getOne(PROGRAMME_MEMBERSHIP_UUID)).thenReturn(
        programmeMembership);
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

    ConditionsOfJoiningDto savedCoj = conditionsOfJoiningService.save(PROGRAMME_MEMBERSHIP_UUID,
        coj);

    assertThat("Unexpected programme membership uuid.", savedCoj.getProgrammeMembershipUuid(),
        is(PROGRAMME_MEMBERSHIP_UUID));
    assertThat("Unexpected programme membership signed at.", savedCoj.getSignedAt(),
        is(SIGNED_AT));
    assertThat("Unexpected programme membership version.", savedCoj.getVersion(),
        is(GoldGuideVersion.GG9));
  }

  @Test
  void saveShouldReplaceAnyProvidedProgrammeMembershipUuid() {
    coj.setProgrammeMembershipUuid(UUID.randomUUID());

    ProgrammeMembership programmeMembership = new ProgrammeMembership();
    programmeMembership.setUuid(PROGRAMME_MEMBERSHIP_UUID);
    CurriculumMembership curriculumMembership = new CurriculumMembership();
    curriculumMembership.setProgrammeMembership(programmeMembership);

    when(curriculumMembershipRepository.getOne(CURRICULUM_MEMBERSHIP_ID)).thenReturn(
        curriculumMembership);
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

    ConditionsOfJoiningDto savedCoj = conditionsOfJoiningService.save(CURRICULUM_MEMBERSHIP_ID,
        coj);

    assertThat("Unexpected programme membership uuid.", savedCoj.getProgrammeMembershipUuid(),
        is(PROGRAMME_MEMBERSHIP_UUID));
  }
}
