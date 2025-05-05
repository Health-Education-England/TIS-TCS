package com.transformuk.hee.tis.tcs.api.enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SpecialtyTypeTest {

  @Test
  void testShouldPassWhenGetNameReturnsValue() {
    assertEquals("CURRICULUM", SpecialtyType.CURRICULUM.getName());
    assertEquals("POST", SpecialtyType.POST.getName());
    assertEquals("PLACEMENT", SpecialtyType.PLACEMENT.getName());
    assertEquals("SUB_SPECIALTY", SpecialtyType.SUB_SPECIALTY.getName());
  }

  @Test
  void testShouldPassWhenEnumValueOfIsCalledFromNameAndEnumConstantNameIsReturned() {
    assertEquals(SpecialtyType.CURRICULUM, SpecialtyType.valueOf("CURRICULUM"));
    assertEquals(SpecialtyType.POST, SpecialtyType.valueOf("POST"));
    assertEquals(SpecialtyType.PLACEMENT, SpecialtyType.valueOf("PLACEMENT"));
    assertEquals(SpecialtyType.SUB_SPECIALTY, SpecialtyType.valueOf("SUB_SPECIALTY"));
  }
}
