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
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.repository.ConditionsOfJoiningRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ConditionsOfJoiningServiceImplTest {

  private static final Long CURRICULUM_MEMBERSHIP_ID = 40L;
  private static final UUID PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final UUID PROGRAMME_MEMBERSHIP_UUID_2 = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();
  private static final Instant SIGNED_AT_2 = Instant.MAX;

  private ConditionsOfJoiningServiceImpl service;
  private ConditionsOfJoiningRepository repository;
  private ProgrammeMembershipService programmeMembershipService;

  private ConditionsOfJoining conditionsOfJoining1, conditionsOfJoining2;

  @BeforeEach
  void setUp() {
    repository = mock(ConditionsOfJoiningRepository.class);
    ConditionsOfJoiningMapper mapper = Mappers.getMapper(ConditionsOfJoiningMapper.class);
    programmeMembershipService = mock(ProgrammeMembershipService.class);

    service = new ConditionsOfJoiningServiceImpl(repository, mapper,
        programmeMembershipService);
    conditionsOfJoining1 = new ConditionsOfJoining();
    conditionsOfJoining1.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);
    conditionsOfJoining1.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoining1.setSignedAt(SIGNED_AT);
    conditionsOfJoining2 = new ConditionsOfJoining();
    conditionsOfJoining2.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID_2);
    conditionsOfJoining2.setVersion(GoldGuideVersion.GG9);
    conditionsOfJoining2.setSignedAt(SIGNED_AT_2);
  }

  @Test
  void saveShouldThrowExceptionWhenProgrammeMembershipIdNotFound() {
    ConditionsOfJoiningDto coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);

    when(programmeMembershipService.findOne(CURRICULUM_MEMBERSHIP_ID)).thenReturn(null);

    assertThrows(IllegalArgumentException.class,
        () -> service.save(CURRICULUM_MEMBERSHIP_ID, coj));
    verify(repository, never()).save(any());
  }

  @Test
  void saveShouldSaveTheConditionsOfJoiningAgainstTheProgrammeMembership() {
    ConditionsOfJoiningDto coj = new ConditionsOfJoiningDto();
    coj.setSignedAt(SIGNED_AT);
    coj.setVersion(GoldGuideVersion.GG9);

    ProgrammeMembershipDTO programmeMembershipDto = new ProgrammeMembershipDTO();
    programmeMembershipDto.setUuid(PROGRAMME_MEMBERSHIP_UUID);

    when(programmeMembershipService.findOne(CURRICULUM_MEMBERSHIP_ID)).thenReturn(
        programmeMembershipDto);
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

    ConditionsOfJoiningDto savedCoj = service.save(CURRICULUM_MEMBERSHIP_ID,
        coj);

    assertThat("Unexpected programme membership uuid.", savedCoj.getProgrammeMembershipUuid(),
        is(PROGRAMME_MEMBERSHIP_UUID));
    assertThat("Unexpected programme membership uuid.", savedCoj.getSignedAt(), is(SIGNED_AT));
    assertThat("Unexpected programme membership uuid.", savedCoj.getVersion(),
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

    ConditionsOfJoiningDto savedCoj = service.save(CURRICULUM_MEMBERSHIP_ID,
        coj);

    assertThat("Unexpected programme membership uuid.", savedCoj.getProgrammeMembershipUuid(),
        is(PROGRAMME_MEMBERSHIP_UUID));
  }

  @Test
  void findConditionsOfJoiningsForTraineeShouldNotFailWhenTraineeIsNull() {
    List<ConditionsOfJoiningDto> result = service
        .findConditionsOfJoiningsForTrainee(null);

    Assertions.assertNotNull(result);
    assertThat("Unexpected result size", result.size(), is(0));
  }

  @Test()
  void findConditionsOfJoiningsForTraineeShouldReturnEmptyListWhenNoResultsFound() {
    List<ConditionsOfJoining> emptyConditionsOfJoiningList = Lists.emptyList();
    when(repository.findByTraineeId(40L))
        .thenReturn(emptyConditionsOfJoiningList);

    List<ConditionsOfJoiningDto> result = service
        .findConditionsOfJoiningsForTrainee(40L);

    Assertions.assertNotNull(result);
    assertThat("Unexpected result size", result.size(), is(0));
  }

  @Test()
  void findConditionsOfJoiningsForTraineeShouldReturnPopulatedDto() {
    List<ConditionsOfJoining> conditionsOfJoiningList = Lists
        .newArrayList(conditionsOfJoining1, conditionsOfJoining2);

    when(repository.findByTraineeId(40L)).thenReturn(conditionsOfJoiningList);

    List<ConditionsOfJoiningDto> result = service
        .findConditionsOfJoiningsForTrainee(40L);

    Assertions.assertNotNull(result);
    assertThat("Unexpected result size", result.size(), is(2));
  }

  @Test()
  void findAllShouldReturnPageOfPopulatedDtos() {
    //given
    List<ConditionsOfJoining> conditionsOfJoiningList = Lists
        .newArrayList(conditionsOfJoining1, conditionsOfJoining2);
    Page<ConditionsOfJoining> conditionsOfJoiningPage = new PageImpl<>(conditionsOfJoiningList);
    when(repository.findAll(any(Pageable.class))).thenReturn(conditionsOfJoiningPage);
    Pageable pageable = PageRequest.of(1, 10);

    //when
    Page<ConditionsOfJoiningDto> result = service.findAll(pageable);

    //then
    Assertions.assertNotNull(result);
    assertThat("Unexpected result size", result.toList().size(), is(2));
  }

  @Test()
  void findOneShouldReturnConditionsOfJoiningDto() {
    //given
    UUID theUuid = UUID.randomUUID();
    when(repository.findById(theUuid)).thenReturn(Optional.of(conditionsOfJoining1));

    //when
    ConditionsOfJoiningDto result = service.findOne(theUuid);

    //then
    Assertions.assertNotNull(result);
    assertThat("Unexpected programmeMembershipUuid",
        result.getProgrammeMembershipUuid(), is(PROGRAMME_MEMBERSHIP_UUID));
    assertThat("Unexpected version",
        result.getVersion(), is(GoldGuideVersion.GG9));
    assertThat("Unexpected signedAt",
        result.getSignedAt(), is(SIGNED_AT));
  }
}
