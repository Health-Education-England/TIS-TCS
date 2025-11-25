package com.transformuk.hee.tis.tcs.service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PersonalDetailsTest {

  @ParameterizedTest
  @ValueSource(strings = {"YES", "Yes", "YEs", "YeS", "yES", "yes", "yEs", "yeS",
      "NO", "No", "no", "nO"})
  void shouldSetNormalisedDisabilityWhenEnumValues(String disability) {
    PersonalDetails personalDetails1 = new PersonalDetails();
    personalDetails1.setDisability(disability);

    PersonalDetails personalDetails2 = new PersonalDetails();
    personalDetails2.disability(disability);

    assertEquals(disability.toUpperCase(), personalDetails1.getDisability());
    assertEquals(disability.toUpperCase(), personalDetails2.getDisability());
  }

  @Test
  void shouldReturnOriginalWhenLegacyValues() {
    String legacyDisability = "I prefer not to say";

    PersonalDetails personalDetails1 = new PersonalDetails();
    personalDetails1.setDisability(legacyDisability);

    PersonalDetails personalDetails2 = new PersonalDetails();
    personalDetails2.disability(legacyDisability);

    assertEquals(legacyDisability, personalDetails1.getDisability());
    assertEquals(legacyDisability, personalDetails2.getDisability());
  }
}
