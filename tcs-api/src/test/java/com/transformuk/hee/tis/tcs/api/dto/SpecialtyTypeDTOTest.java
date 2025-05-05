package com.transformuk.hee.tis.tcs.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SpecialtyTypeDTOTest {

  @Test
  void testDefaultConstructor() {
    SpecialtyTypeDTO dto = new SpecialtyTypeDTO();
    assertNull(dto.getName());
  }

  @Test
  void testConstructorAndGetters() {
    SpecialtyTypeDTO dto = new SpecialtyTypeDTO("POST");
    assertEquals("POST", dto.getName());
  }

  @Test
  void testFromString() {
    SpecialtyTypeDTO dto = SpecialtyTypeDTO.fromString("CURRICULUM");
    assertNotNull(dto);
    assertEquals("CURRICULUM", dto.getName());
  }

  @Test
  void testToValue() {
    SpecialtyTypeDTO dto = new SpecialtyTypeDTO("PLACEMENT");
    assertEquals("PLACEMENT", dto.toValue());
  }

  @Test
  void testEqualsAndHashCode() {
    SpecialtyTypeDTO dto1 = new SpecialtyTypeDTO("SUB_SPECIALTY");
    SpecialtyTypeDTO dto2 = new SpecialtyTypeDTO("SUB_SPECIALTY");
    SpecialtyTypeDTO dto3 = new SpecialtyTypeDTO("POST");

    assertEquals(dto1, dto2);
    assertEquals(dto1.hashCode(), dto2.hashCode());

    assertNotEquals(dto1, dto3);
    assertNotEquals(dto1.hashCode(), dto3.hashCode());
  }

  @Test
  void testEqualsWithNullAndDifferentClass() {
    SpecialtyTypeDTO dto = new SpecialtyTypeDTO("CURRICULUM");

    assertNotEquals(null, dto);
    assertNotEquals("some string", dto);
  }
}
