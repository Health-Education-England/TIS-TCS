/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
