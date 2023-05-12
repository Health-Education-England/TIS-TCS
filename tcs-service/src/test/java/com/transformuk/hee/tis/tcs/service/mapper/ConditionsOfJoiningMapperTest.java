package com.transformuk.hee.tis.tcs.service.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import com.transformuk.hee.tis.tcs.service.service.mapper.ConditionsOfJoiningMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;

public class ConditionsOfJoiningMapperTest {

  private static final UUID PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final UUID PROGRAMME_MEMBERSHIP_UUID_2 = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();
  private static final Instant SIGNED_AT_2 = Instant.MAX;

  private ConditionsOfJoining conditionsOfJoining1, conditionsOfJoining2;

  ConditionsOfJoiningMapper mapper = Mappers.getMapper(ConditionsOfJoiningMapper.class);

  @BeforeEach
  void setUp() {
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
  public void shouldMapAllConditionsOfJoiningToListOfDtos() {
    //Given
    List<ConditionsOfJoining> conditionsOfJoinings = Lists
        .newArrayList(conditionsOfJoining1, conditionsOfJoining2);

    //When
    List<ConditionsOfJoiningDto> result = mapper.allEntityToDto(conditionsOfJoinings);

    //Then
    assertThat("Unexpected result size.", result.size(), is(2));
  }
}
