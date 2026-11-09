package com.transformuk.hee.tis.tcs.service.job.person;

import static org.junit.jupiter.api.Assertions.*;

import com.transformuk.hee.tis.tcs.api.enumeration.PersonTrustStatus;
import org.junit.jupiter.api.Test;

class PersonTrustDtoTest {

  @Test
  void shouldSetFieldsViaConstructor() {
    PersonTrustDto dto = new PersonTrustDto(1L, 2L);
    dto.setStatus(PersonTrustStatus.ACTIVE);

    assertNotNull(dto);
    assertEquals(1L, dto.getPersonId());
    assertEquals(2L, dto.getTrustId());
    assertEquals(PersonTrustStatus.ACTIVE, dto.getStatus());
  }

  @Test
  void shouldBeEqualWhenAllFieldsMatch() {
    PersonTrustDto dto1 = new PersonTrustDto(1L, 2L);
    dto1.setStatus(PersonTrustStatus.ACTIVE);

    PersonTrustDto dto2 = new PersonTrustDto(1L, 2L);
    dto2.setStatus(PersonTrustStatus.ACTIVE);

    assertEquals(dto1, dto2);
    assertEquals(dto1.hashCode(), dto2.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenStatusDiffers() {
    PersonTrustDto dto1 = new PersonTrustDto(1L, 2L);
    dto1.setStatus(PersonTrustStatus.ACTIVE);

    PersonTrustDto dto2 = new PersonTrustDto(1L, 2L);
    dto2.setStatus(PersonTrustStatus.INACTIVE);

    assertNotEquals(dto1, dto2);
  }

  @Test
  void shouldNotBeEqualWhenPersonIdDiffers() {
    PersonTrustDto dto1 = new PersonTrustDto(1L, 2L);
    dto1.setStatus(PersonTrustStatus.ACTIVE);

    PersonTrustDto dto2 = new PersonTrustDto(3L, 2L);
    dto2.setStatus(PersonTrustStatus.ACTIVE);

    assertNotEquals(dto1, dto2);
  }

  @Test
  void shouldNotBeEqualWhenTrustIdDiffers() {
    PersonTrustDto dto1 = new PersonTrustDto(1L, 2L);
    dto1.setStatus(PersonTrustStatus.ACTIVE);

    PersonTrustDto dto2 = new PersonTrustDto(1L, 3L);
    dto2.setStatus(PersonTrustStatus.ACTIVE);

    assertNotEquals(dto1, dto2);
  }

  @Test
  void shouldNotBeEqualToNull() {
    PersonTrustDto dto = new PersonTrustDto(1L, 2L);

    assertNotEquals(null, dto);
  }
}
