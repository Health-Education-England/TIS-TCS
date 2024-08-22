package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType.MEDICAL_CURRICULUM;
import static com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType.SUB_SPECIALTY;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.ProgrammeMembershipType;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.service.CurriculumService;
import com.transformuk.hee.tis.tcs.service.service.TrainingNumberService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class TrainingNumberServiceImplTest {

  private static final String CURRICULUM_NAME = "curriculumName";
  private static final String CURRICULUM_SPECIALTY_CODE = "ABC";
  private static final String GDC_NUMBER = "12345";
  private static final String GMC_NUMBER = "1234567";
  private static final String OWNER_NAME = "London LETBs";
  private static final String PROGRAMME_NAME = "Programme Name";
  private static final String PROGRAMME_NUMBER = "PROG123";
  private static final String TRAINING_PATHWAY = "N/A";

  private static final LocalDate NOW = LocalDate.now();
  private static final LocalDate PAST = NOW.minusYears(1);
  private static final LocalDate FUTURE = NOW.plusYears(1);

  private TrainingNumberService service;
  private CurriculumService curriculumService;

  @BeforeEach
  void setUp() {
    curriculumService = mock(CurriculumService.class);
    service = new TrainingNumberServiceImpl(null, null, null, curriculumService);
  }

  @Test
  void shouldNotPopulateTrainingNumberWhenNoPersonalDetails(CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    pm.setPerson(null);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());

    assertThat("Expected log not found.", output.getOut(),
        containsString("Skipping training number population as person details not available."));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "abcd", "1234"})
  void shouldNotPopulateTrainingNumberWhenNoGmcOrGdcNumber(String referenceNumber,
      CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(referenceNumber, referenceNumber);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());

    assertThat("Expected log not found.", output.getOut(),
        containsString("Skipping training number population as reference number not valid."));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = " ")
  void shouldNotPopulateTrainingNumberWhenNoProgrammeNumber(String programmeNumber,
      CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(programmeNumber);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());

    assertThat("Expected log not found.", output.getOut(),
        containsString("Skipping training number population as programme number is blank."));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = " ")
  void shouldNotPopulateTrainingNumberWhenNoProgrammeName(String programmeName,
      CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(programmeName);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());

    assertThat("Expected log not found.", output.getOut(),
        containsString("Skipping training number population as programme name is blank."));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "foundation", "FOUNDATION", "prefix foundation", "foundation suffix",
      "prefix foundation suffix"
  })
  void shouldNotPopulateTrainingNumberWhenProgrammeIsFoundation(String programmeName,
      CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(programmeName);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());

    assertThat("Expected log not found.", output.getOut(), containsString(
        "Skipping training number population as programme name '" + programmeName
            + "' is excluded."));
  }

  @Test
  void shouldNotPopulateTrainingNumberWhenNoCurricula(CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);
    pm.setCurriculumMemberships(new HashSet<>());

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());
    assertThat("Expected log not found.", output.getOut(),
        containsString("Skipping training number population as there are no valid curricula."));
  }

  @Test
  void shouldNotPopulateTrainingNumberWhenNoCurrentCurricula(CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership past = createCurriculumMembership(CURRICULUM_NAME, PAST, NOW.minusDays(1),
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    CurriculumMembership future = createCurriculumMembership(CURRICULUM_NAME, NOW.plusDays(1),
        FUTURE, MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);

    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(past, future)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());
    assertThat("Expected log not found.", output.getOut(),
        containsString("Skipping training number population as there are no valid curricula."));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = " ")
  void shouldNotPopulateTrainingNumberWhenNoCurriculaSpecialtyCode(String specialtyCode,
      CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, specialtyCode);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());
    assertThat("Expected log not found.", output.getOut(),
        containsString("Skipping training number population as there are no valid curricula."));
  }

  @Test
  void shouldNotPopulateTrainingNumberWhenTrainingPathwayNull(CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(null);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber, nullValue());
    assertThat("Expected log not found.", output.getOut(),
        containsString("Unable to generate training number as training pathway was null."));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "Unknown Organization"})
  void shouldNotThrowExceptionPopulatingTrainingNumberWhenParentOrganizationNull(String ownerName,
      CapturedOutput output) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(ownerName);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    List<ProgrammeMembership> pms = Collections.singletonList(pm);
    assertDoesNotThrow(() -> service.populateTrainingNumbers(pms));

    assertThat("Unexpected message.", output.getOut(),
        containsString("Unable to calculate the parent organization."));
  }

  @Test
  void shouldPopulateFullTrainingNumberWhenProgrammeIsCurrent() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, "ABC");
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, NOW, FUTURE,
        SUB_SPECIALTY, "123");
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, PAST, NOW, SUB_SPECIALTY,
        "XYZ");
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber.getTrainingNumber(),
        is("LDN/ABC.XYZ.123/1234567/D"));
  }

  @Test
  void shouldPopulateFullTrainingNumberWhenProgrammeIsFuture() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(FUTURE);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, NOW, FUTURE.plusDays(1),
        MEDICAL_CURRICULUM, "ABC");
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, FUTURE,
        FUTURE.plusDays(1), SUB_SPECIALTY, "123");
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, NOW, FUTURE,
        SUB_SPECIALTY, "XYZ");
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.", trainingNumber.getTrainingNumber(),
        is("LDN/ABC.XYZ.123/1234567/D"));
  }

  @Test
  void shouldUseTsdPrefixWhenMilitaryProgrammeMembershipType() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(FUTURE);
    pm.setProgrammeMembershipType(ProgrammeMembershipType.MILITARY);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, NOW, FUTURE.plusDays(1),
        MEDICAL_CURRICULUM, "ABC");
    pm.setCurriculumMemberships(new HashSet<>(Collections.singletonList(cm1)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.",
        trainingNumber.getTrainingNumber().startsWith("TSD"), is(true));
  }

  @ParameterizedTest
  @EnumSource(value = ProgrammeMembershipType.class, names = "MILITARY", mode = EXCLUDE)
  void shouldNotForceTsdPrefixWhenNotMilitaryProgrammeMembershipType(ProgrammeMembershipType type) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(FUTURE);
    pm.setProgrammeMembershipType(type);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, NOW, FUTURE.plusDays(1),
        MEDICAL_CURRICULUM, "ABC");
    pm.setCurriculumMemberships(new HashSet<>(Collections.singletonList(cm1)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    assertThat("Unexpected training number.",
        trainingNumber.getTrainingNumber().startsWith("TSD"), is(false));
  }

  @Test
  void shouldPopulateTrainingNumbersWhenTraineeProfileHasMultiplePms() {
    Person person = createPerson(GMC_NUMBER, null);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    ProgrammeMembership pm1 = new ProgrammeMembership();
    pm1.setPerson(person);
    pm1.setProgramme(programme);
    pm1.setTrainingPathway(TRAINING_PATHWAY);
    pm1.setProgrammeStartDate(PAST);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, PAST,
        MEDICAL_CURRICULUM, "ABC");
    pm1.setCurriculumMemberships(Collections.singleton(cm1));

    ProgrammeMembership pm2 = new ProgrammeMembership();
    pm2.setPerson(person);
    pm2.setProgramme(programme);
    pm2.setTrainingPathway(TRAINING_PATHWAY);
    pm2.setProgrammeStartDate(NOW);

    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, NOW, NOW,
        MEDICAL_CURRICULUM, "123");
    pm2.setCurriculumMemberships(Collections.singleton(cm2));

    ProgrammeMembership pm3 = new ProgrammeMembership();
    pm3.setPerson(person);
    pm3.setProgramme(programme);
    pm3.setTrainingPathway(TRAINING_PATHWAY);
    pm3.setProgrammeStartDate(FUTURE);

    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, "XYZ");
    pm3.setCurriculumMemberships(Collections.singleton(cm3));

    service.populateTrainingNumbers(Arrays.asList(pm1, pm2, pm3));

    assertThat("Unexpected training number.", pm1.getTrainingNumber(), nullValue());
    assertThat("Unexpected training number.", pm2.getTrainingNumber().getTrainingNumber(),
        is("LDN/123/1234567/D"));
    assertThat("Unexpected training number.", pm3.getTrainingNumber().getTrainingNumber(),
        is("LDN/XYZ/1234567/D"));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "Defence Postgraduate Medical Deanery                   | TSD",
      "Health Education England East Midlands                 | EMD",
      "Health Education England East of England               | EAN",
      "Health Education England Kent, Surrey and Sussex       | KSS",
      "Health Education England North Central and East London | LDN",
      "Health Education England North East                    | NTH",
      "Health Education England North West                    | NWE",
      "Health Education England North West London             | LDN",
      "Health Education England South London                  | LDN",
      "Health Education England South West                    | SWN",
      "Health Education England Thames Valley                 | OXF",
      "Health Education England Wessex                        | WES",
      "Health Education England West Midlands                 | WMD",
      "Health Education England Yorkshire and the Humber      | YHD",
      "London LETBs                                           | LDN"
  })
  void shouldPopulateTrainingNumberWithParentOrganizationWhenMappedByOwner(String ownerName,
      String ownerCode) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(ownerName);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected parent organization.", trainingNumberParts[0], is(ownerCode));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "AAA | ZZZ | 111 | 999 | ZZZ-AAA-999-111",
      "999 | 111 | ZZZ | AAA | ZZZ-AAA-999-111",
      "001 | 010 | 100 | 111 | 111-100-010-001"
  })
  void shouldPopulateTrainingNumberWithOrderedSpecialtyConcatWhenMultipleSpecialty(
      String specialty1,
      String specialty2, String specialty3, String specialty4, String trainingNumberPart) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, specialty1);
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, specialty2);
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, specialty3);
    CurriculumMembership cm4 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, specialty4);
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3, cm4)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected specialty concat.", trainingNumberParts[1], is(trainingNumberPart));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 10})
  void shouldPopulateTrainingNumberWithDotNotatedSpecialtyConcatWhenHasSubSpecialties(
      int additionalCurriculaCount) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    Set<CurriculumMembership> cms = new HashSet<>();
    pm.setCurriculumMemberships(cms);

    CurriculumMembership cm1 = createCurriculumMembership("A sub spec 1", PAST, FUTURE,
        SUB_SPECIALTY, "888");
    cms.add(cm1);

    CurriculumMembership cm2 = createCurriculumMembership("A sub spec 2", PAST, FUTURE,
        SUB_SPECIALTY, "999");
    cms.add(cm2);

    for (int i = 1; i <= additionalCurriculaCount; i++) {
      CurriculumMembership additionalCm = createCurriculumMembership("Not sub spec " + i, PAST,
          FUTURE, MEDICAL_CURRICULUM, String.format("%03d", i));
      cms.add(additionalCm);
    }

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected specialty concat.", trainingNumberParts[1], endsWith(".999.888"));

    long dotCount = trainingNumberParts[1].chars().filter(ch -> ch == '.').count();
    assertThat("Unexpected sub specialty count.", dotCount, is(2L));
  }

  @Test
  void shouldPopulateTrainingNumberWithFixedSpecialtyConcatWhenFirstSpecialtyIsAft() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership("AFT", PAST, FUTURE, MEDICAL_CURRICULUM,
        "ACA");
    CurriculumMembership cm2 = createCurriculumMembership("Not AFT 2", PAST, FUTURE,
        MEDICAL_CURRICULUM, "003");
    CurriculumMembership cm3 = createCurriculumMembership("Not AFT 1", PAST, FUTURE,
        MEDICAL_CURRICULUM, "777");
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected specialty concat.", trainingNumberParts[1], is("ACA-FND"));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "AAA | ZZZ | 111",
      "AAA | 111 | ZZZ",
      "ZZZ | AAA | 111",
      "ZZZ | 111 | AAA",
      "111 | AAA | ZZZ",
      "111 | ZZZ | AAA"
  })
  void shouldFilterCurriculaWhenPopulatingTrainingNumberAndCurriculaEnding(String pastSpecialty,
      String endingSpecialty, String futureSpecialty) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, PAST,
        MEDICAL_CURRICULUM, pastSpecialty);
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, futureSpecialty);
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, PAST, NOW,
        MEDICAL_CURRICULUM, endingSpecialty);
    CurriculumMembership cm4 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, null);
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3, cm4)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected specialty concat.", trainingNumberParts[1], is(endingSpecialty));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "AAA | ZZZ | 111",
      "AAA | 111 | ZZZ",
      "ZZZ | AAA | 111",
      "ZZZ | 111 | AAA",
      "111 | AAA | ZZZ",
      "111 | ZZZ | AAA"
  })
  void shouldFilterCurriculaWhenPopulatingTrainingNumberCurriculaCurrent(String pastSpecialty,
      String currentSpecialty, String futureSpecialty) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, PAST,
        MEDICAL_CURRICULUM, pastSpecialty);
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, futureSpecialty);
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, currentSpecialty);
    CurriculumMembership cm4 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, null);
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3, cm4)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected specialty concat.", trainingNumberParts[1], is(currentSpecialty));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "AAA | ZZZ | 111",
      "AAA | 111 | ZZZ",
      "ZZZ | AAA | 111",
      "ZZZ | 111 | AAA",
      "111 | AAA | ZZZ",
      "111 | ZZZ | AAA"
  })
  void shouldFilterCurriculaWhenPopulatingTrainingNumberAndCurriculaStarting(String pastSpecialty,
      String startingSpecialty, String futureSpecialty) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, PAST,
        MEDICAL_CURRICULUM, pastSpecialty);
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, futureSpecialty);
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, NOW, FUTURE,
        MEDICAL_CURRICULUM, startingSpecialty);
    CurriculumMembership cm4 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, null);
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3, cm4)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected specialty concat.", trainingNumberParts[1], is(startingSpecialty));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "AAA | ZZZ | 111",
      "AAA | 111 | ZZZ",
      "ZZZ | AAA | 111",
      "ZZZ | 111 | AAA",
      "111 | AAA | ZZZ",
      "111 | ZZZ | AAA"
  })
  void shouldFilterCurriculaWhenPopulatingTrainingNumberAndProgrammeFuture(String currentSpecialty,
      String futureSpecialty, String farFutureSpecialty) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(FUTURE);
    pm.setCurriculumMemberships(new HashSet<>());

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, NOW, NOW,
        MEDICAL_CURRICULUM, currentSpecialty);
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, futureSpecialty);
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, FUTURE.plusDays(1),
        FUTURE.plusDays(1), MEDICAL_CURRICULUM, farFutureSpecialty);
    CurriculumMembership cm4 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, null);
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3, cm4)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected specialty concat.", trainingNumberParts[1], is(futureSpecialty));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "111 | AAA | AAA",
      "AAA | 111 | AAA",
      "AAA | AAA | 111",
      "AAA | 111 | 111",
      "111 | AAA | 111",
      "111 | 111 | AAA"
  })
  void shouldFilterCurriculaWhenPopulatingTrainingNumberAndDuplicateSpecialties(String specialty1,
      String specialty2, String specialty3) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, NOW, FUTURE,
        MEDICAL_CURRICULUM, specialty1);
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, NOW, NOW,
        MEDICAL_CURRICULUM, specialty2);
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, PAST, NOW,
        MEDICAL_CURRICULUM, specialty3);
    CurriculumMembership cm4 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, null);
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3, cm4)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected specialty concat.", trainingNumberParts[1], is("AAA-111"));
  }

  @Test
  void shouldPopulateTrainingNumberWithGmcNumberWhenValid() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected reference number.", trainingNumberParts[2], is(GMC_NUMBER));
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc", "12345678"})
  void shouldPopulateTrainingNumberWithGdcNumberWhenValidAndGmcInvalid(String gmcNumber) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(gmcNumber, GDC_NUMBER);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected reference number.", trainingNumberParts[2], is(GDC_NUMBER));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "CCT  | C",
      "CESR | CP",
      "N/A  | D"
  })
  void shouldPopulateTrainingNumberWithSuffixWhenMappedByTrainingPathway(String trainingPathway,
      String suffix) {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(trainingPathway);
    pm.setProgrammeStartDate(NOW);
    pm.setCurriculumMemberships(new HashSet<>());

    CurriculumMembership cm = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, CURRICULUM_SPECIALTY_CODE);
    pm.setCurriculumMemberships(Collections.singleton(cm));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected suffix.", trainingNumberParts[3], is(suffix));
  }

  @Test
  void shouldPopulateTrainingNumberWithSuffixWhenSpecialtyIsAcademic() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, "123");
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, "ACA");
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected suffix.", trainingNumberParts[3], is("C"));
  }

  @Test
  void shouldFilterCurriculaWhenPopulatingTrainingNumberWithSuffixAndCurriculaEnding() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, PAST,
        MEDICAL_CURRICULUM, "ACA");
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, PAST, NOW,
        MEDICAL_CURRICULUM, "123");
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, "ACA");
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected suffix.", trainingNumberParts[3], is("D"));
  }

  @Test
  void shouldFilterCurriculaWhenPopulatingTrainingNumberWithSuffixAndCurriculaCurrent() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, PAST,
        MEDICAL_CURRICULUM, "ACA");
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, PAST, FUTURE,
        MEDICAL_CURRICULUM, "123");
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, "ACA");
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected suffix.", trainingNumberParts[3], is("D"));
  }

  @Test
  void shouldFilterCurriculaWhenPopulatingTrainingNumberWithSuffixAndCurriculaStarting() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(NOW);

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, PAST,
        MEDICAL_CURRICULUM, "ACA");
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, NOW, FUTURE,
        MEDICAL_CURRICULUM, "123");
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, "ACA");
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected suffix.", trainingNumberParts[3], is("D"));
  }

  @Test
  void shouldFilterCurriculaWhenPopulatingTrainingNumberWithSuffixAndProgrammeFuture() {
    ProgrammeMembership pm = new ProgrammeMembership();
    Person person = createPerson(GMC_NUMBER, null);
    pm.setPerson(person);

    Programme programme = new Programme();
    programme.setOwner(OWNER_NAME);
    programme.setProgrammeName(PROGRAMME_NAME);
    programme.setProgrammeNumber(PROGRAMME_NUMBER);

    pm.setProgramme(programme);
    pm.setTrainingPathway(TRAINING_PATHWAY);
    pm.setProgrammeStartDate(FUTURE);
    pm.setCurriculumMemberships(new HashSet<>());

    CurriculumMembership cm1 = createCurriculumMembership(CURRICULUM_NAME, PAST, PAST,
        MEDICAL_CURRICULUM, "ACA");
    CurriculumMembership cm2 = createCurriculumMembership(CURRICULUM_NAME, NOW, NOW,
        MEDICAL_CURRICULUM, "ACA");
    CurriculumMembership cm3 = createCurriculumMembership(CURRICULUM_NAME, FUTURE, FUTURE,
        MEDICAL_CURRICULUM, "123");
    CurriculumMembership cm4 = createCurriculumMembership(CURRICULUM_NAME, FUTURE.plusDays(1),
        FUTURE.plusDays(1), MEDICAL_CURRICULUM, "ACA");
    pm.setCurriculumMemberships(new HashSet<>(Arrays.asList(cm1, cm2, cm3, cm4)));

    service.populateTrainingNumbers(Collections.singletonList(pm));

    TrainingNumber trainingNumber = pm.getTrainingNumber();
    String[] trainingNumberParts = trainingNumber.getTrainingNumber().split("/");
    assertThat("Unexpected suffix.", trainingNumberParts[3], is("D"));
  }

  /**
   * Create a person with GMC and GDC details.
   *
   * @param gmcNumber The GMC number to use, may be null.
   * @param gdcNumber The GDC number to use, may be null.
   * @return The created person.
   */
  private Person createPerson(String gmcNumber, String gdcNumber) {
    Person person = new Person();

    if (gmcNumber != null) {
      GmcDetails gmcDetails = new GmcDetails();
      gmcDetails.setGmcNumber(gmcNumber);
      person.setGmcDetails(gmcDetails);
    }

    if (gdcNumber != null) {
      GdcDetails gdcDetails = new GdcDetails();
      gdcDetails.setGdcNumber(gdcNumber);
      person.setGdcDetails(gdcDetails);
    }

    return person;
  }

  /**
   * Creates a curriculum membership with mocked response for curriculum and specialty.
   *
   * @param startDate     The curriculum start date.
   * @param endDate       The curriculum end date.
   * @param subType       The curriculum sub type.
   * @param specialtyCode The curriculum's specialty code.
   * @return The created curriculum membership, curriculum lookup will be mocked.
   */
  private CurriculumMembership createCurriculumMembership(String name, LocalDate startDate,
      LocalDate endDate, CurriculumSubType subType, String specialtyCode) {
    long curriculumId = new Random().nextLong();

    CurriculumMembership cm = new CurriculumMembership();
    cm.setCurriculumStartDate(startDate);
    cm.setCurriculumEndDate(endDate);
    cm.setCurriculumId(curriculumId);

    SpecialtyDTO specialty = new SpecialtyDTO();
    specialty.setSpecialtyCode(specialtyCode);

    CurriculumDTO curriculum = new CurriculumDTO();
    curriculum.setId(curriculumId);
    curriculum.setName(name);
    curriculum.setCurriculumSubType(subType);
    curriculum.setSpecialty(specialty);

    when(curriculumService.findOne(curriculumId)).thenReturn(curriculum);
    return cm;
  }
}
