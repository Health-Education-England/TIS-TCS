package com.transformuk.hee.tis.tcs.service.api.util;

import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FieldDiffUtilTest {

  @Test
  void testDiff_sameObjects_shouldReturnEmptyMap() {
    PersonalDetailsDTO p1 = new PersonalDetailsDTO();
    p1.setGender("Female");
    p1.setDisability("NO");
    p1.setNationality("British");
    PersonalDetailsDTO p2 = new PersonalDetailsDTO();
    p2.setGender("Female");
    p2.setDisability("NO");
    p2.setNationality("British");

    Map<String, Object[]> result = FieldDiffUtil.diff(p1, p2);
    assertTrue(result.isEmpty());
  }

  @Test
  void testDiff_differentFields_shouldReturnChangedFields() {
    PersonalDetailsDTO newDto = new PersonalDetailsDTO();
    newDto.setGender("Female");
    newDto.setDisability("YES");
    newDto.setNationality("British");

    PersonalDetailsDTO oldDto = new PersonalDetailsDTO();
    oldDto.setGender("Female");
    oldDto.setDisability("NO");
    oldDto.setNationality("Japanese");

    Map<String, Object[]> result = FieldDiffUtil.diff(newDto, oldDto);
    assertEquals(2, result.size());

    assertArrayEquals(new Object[]{"NO", "YES"}, result.get("disability"));
    assertArrayEquals(new Object[]{"Japanese", "British"}, result.get("nationality"));
  }

  @Test
  void testDiff_nullObjects_shouldThrowException() {
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setGender("Female");
    dto.setDisability("YES");
    dto.setNationality("British");

    assertThrows(IllegalArgumentException.class, () -> FieldDiffUtil.diff(null, dto));
    assertThrows(IllegalArgumentException.class, () -> FieldDiffUtil.diff(dto, null));
  }

  @Test
  void testDiff_fieldOnlyInOneObject_shouldIgnore() {
    // Anonymous class with extra field
    Object newObj = new Object() {
      public String gender = "Female";
      public String extraField = "Extra";
    };

    PersonalDetailsDTO oldObj = new PersonalDetailsDTO();
    oldObj.setGender("Female");
    oldObj.setDisability("YES");
    oldObj.setNationality("British");

    Map<String, Object[]> result = FieldDiffUtil.diff(newObj, oldObj);
    assertEquals(2, result.size());

    assertArrayEquals(new Object[]{"YES", null}, result.get("disability"));
    assertArrayEquals(new Object[]{"British", null}, result.get("nationality"));
  }
}
