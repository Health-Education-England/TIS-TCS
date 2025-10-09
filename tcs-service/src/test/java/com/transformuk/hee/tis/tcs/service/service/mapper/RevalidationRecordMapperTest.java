package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class RevalidationRecordMapperTest {

  private static final Long PERSON_ID = 1111L;
  private static final String GMC_NUMBER = "11111111";
  private static final String FIRST_NAME = "Aaa";
  private static final String LAST_NAME = "Bbb";
  private static final String PROGRAMME_NAME = "Programme name 1";
  private static final String PROGRAMME_MEMBERSHIP_TYPE = "Programme type 1";
  private static final LocalDate PROGRAMME_END_DATE = LocalDate.of(2025, 10, 10);
  private static final LocalDate CURRICULUM_END_DATE = LocalDate.of(2025, 9, 9);

  private RevalidationRecordMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(RevalidationRecordMapper.class);
  }

  @Test
  void shouldMapConnectionInfoDtoToRevalidationRecordDto() {
    // given
    ConnectionInfoDto source = ConnectionInfoDto.builder().tcsPersonId(PERSON_ID)
        .gmcReferenceNumber(GMC_NUMBER).doctorFirstName(FIRST_NAME).doctorLastName(LAST_NAME)
        .programmeName(PROGRAMME_NAME).programmeMembershipType(PROGRAMME_MEMBERSHIP_TYPE)
        .programmeMembershipEndDate(PROGRAMME_END_DATE).curriculumEndDate(CURRICULUM_END_DATE)
        .build();

    RevalidationRecordDto target = new RevalidationRecordDto();

    // when
    mapper.toRevalidationRecord(source, target);

    // then
    assertThat(target.getTisPersonId()).isEqualTo(PERSON_ID);
    assertThat(target.getForenames()).isEqualTo(FIRST_NAME);
    assertThat(target.getSurname()).isEqualTo(LAST_NAME);
    assertThat(target.getProgrammeMembershipType()).isEqualTo(PROGRAMME_MEMBERSHIP_TYPE);
    assertThat(target.getProgrammeName()).isEqualTo(PROGRAMME_NAME);
    assertThat(target.getProgrammeEndDate()).isEqualTo(PROGRAMME_END_DATE);
    assertThat(target.getCurriculumEndDate()).isEqualTo(CURRICULUM_END_DATE);
    assertThat(target.getGmcNumber()).isNull();
  }
}
