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
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ConditionsOfJoiningServiceImplTest {

  private static final Long CURRICULUM_MEMBERSHIP_ID = 40L;
  private static final UUID PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();

  private ConditionsOfJoiningService conditionsOfJoiningService;
  private ConditionsOfJoiningRepository repository;
  private ProgrammeMembershipService programmeMembershipService;

  @BeforeEach
  void setUp() {
    repository = mock(ConditionsOfJoiningRepository.class);
    ConditionsOfJoiningMapper mapper = Mappers.getMapper(ConditionsOfJoiningMapper.class);
    programmeMembershipService = mock(ProgrammeMembershipService.class);

    conditionsOfJoiningService = new ConditionsOfJoiningServiceImpl(repository, mapper,
        programmeMembershipService);
  }

  @Test
  void saveShouldThrowExceptionWhenCurriculumMembershipIdNotFound() {
    ConditionsOfJoiningDto coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);

    when(programmeMembershipService.findOne(CURRICULUM_MEMBERSHIP_ID)).thenReturn(null);

    assertThrows(IllegalArgumentException.class,
        () -> conditionsOfJoiningService.save(CURRICULUM_MEMBERSHIP_ID, coj));
    verify(repository, never()).save(any());
  }

  @Test
  void saveShouldThrowExceptionWhenProgrammeMembershipIdNotFound() {
    ConditionsOfJoiningDto coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);

    when(programmeMembershipService.findOne(PROGRAMME_MEMBERSHIP_UUID)).thenReturn(null);

    assertThrows(IllegalArgumentException.class,
        () -> conditionsOfJoiningService.save(PROGRAMME_MEMBERSHIP_UUID, coj));
    verify(repository, never()).save(any());
  }

  @Test
  void saveShouldThrowExceptionWhenInvalidIdProvided() {
    ConditionsOfJoiningDto coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);

    assertThrows(IllegalArgumentException.class,
        () -> conditionsOfJoiningService.save("not a long or a uuid", coj));
    verify(repository, never()).save(any());
  }

  @Test
  void saveShouldSaveTheConditionsOfJoiningAgainstTheProgrammeMembershipFromCmId() {
    ConditionsOfJoiningDto coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);

    ProgrammeMembershipDTO programmeMembershipDto = new ProgrammeMembershipDTO();
    programmeMembershipDto.setUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(programmeMembershipService.findOne(CURRICULUM_MEMBERSHIP_ID)).thenReturn(
        programmeMembershipDto);
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
    ConditionsOfJoiningDto coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);

    ProgrammeMembershipDTO programmeMembershipDto = new ProgrammeMembershipDTO();
    programmeMembershipDto.setUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(programmeMembershipService.findOne(PROGRAMME_MEMBERSHIP_UUID)).thenReturn(
        programmeMembershipDto);
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
    ConditionsOfJoiningDto coj = new ConditionsOfJoiningDto();
    coj.setProgrammeMembershipUuid(UUID.randomUUID());

    ProgrammeMembershipDTO programmeMembershipDto = new ProgrammeMembershipDTO();
    programmeMembershipDto.setUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(programmeMembershipService.findOne(CURRICULUM_MEMBERSHIP_ID)).thenReturn(
        programmeMembershipDto);
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

    ConditionsOfJoiningDto savedCoj = conditionsOfJoiningService.save(CURRICULUM_MEMBERSHIP_ID,
        coj);

    assertThat("Unexpected programme membership uuid.", savedCoj.getProgrammeMembershipUuid(),
        is(PROGRAMME_MEMBERSHIP_UUID));
  }
}
