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

  private static final UUID PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();

  private ConditionsOfJoiningService conditionsOfJoiningService;
  private ConditionsOfJoiningRepository repository;
  private ProgrammeMembershipService programmeMembershipService;
  private ConditionsOfJoiningDto coj;

  @BeforeEach
  void setUp() {
    repository = mock(ConditionsOfJoiningRepository.class);
    ConditionsOfJoiningMapper mapper = Mappers.getMapper(ConditionsOfJoiningMapper.class);
    programmeMembershipService = mock(ProgrammeMembershipService.class);

    conditionsOfJoiningService = new ConditionsOfJoiningServiceImpl(repository, mapper,
        programmeMembershipService);
    coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);
  }

  @Test
  void saveShouldThrowExceptionWhenProgrammeMembershipIdNotFound() {
    when(programmeMembershipService.findOne(PROGRAMME_MEMBERSHIP_UUID)).thenReturn(null);

    assertThrows(IllegalArgumentException.class,
        () -> conditionsOfJoiningService.save(PROGRAMME_MEMBERSHIP_UUID.toString(), coj));
    verify(repository, never()).save(any());
  }

  @Test
  void saveShouldSaveTheConditionsOfJoiningAgainstTheProgrammeMembershipFromPmId() {
    ProgrammeMembershipDTO programmeMembershipDto = new ProgrammeMembershipDTO();
    programmeMembershipDto.setUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(programmeMembershipService.findOne(PROGRAMME_MEMBERSHIP_UUID)).thenReturn(
        programmeMembershipDto);
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

    ConditionsOfJoiningDto savedCoj
        = conditionsOfJoiningService.save(PROGRAMME_MEMBERSHIP_UUID.toString(), coj);

    assertThat("Unexpected programme membership uuid.", savedCoj.getProgrammeMembershipUuid(),
        is(PROGRAMME_MEMBERSHIP_UUID));
    assertThat("Unexpected programme membership signed at.", savedCoj.getSignedAt(),
        is(SIGNED_AT));
    assertThat("Unexpected programme membership version.", savedCoj.getVersion(),
        is(GoldGuideVersion.GG9));
  }
}
