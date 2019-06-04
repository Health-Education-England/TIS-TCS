package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.TestConfig;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecialtyRepositoryTest {

  private static final Long PROGRAMME_ID = 1L;

  @Autowired
  private SpecialtyRepository specialtyRepository;

  @Test
  @Sql("/scripts/programmeCurriculaSpecialties.sql")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/scripts/deleteProgrammeCurriculaSpecialties.sql")
  public void findSpecialtyDistinctByCurriculaProgrammesIdShouldGetSpecialtyForProgrammeId() {
    Pageable page = PageRequest.of(0, 100);
    Page<Specialty> result = specialtyRepository.findSpecialtyDistinctByCurriculaProgrammesIdAndStatusIs(PROGRAMME_ID, Status.CURRENT, page);

    Assert.assertEquals(2, result.getNumberOfElements());
    Assert.assertFalse(result.hasNext());
    for (Specialty specialty : result.getContent()) {
      Assert.assertTrue("SPE-1".equals(specialty.getSpecialtyCode()) || "SPE-2".equals(specialty.getSpecialtyCode()));
    }
  }

  @Test
  @Sql("/scripts/programmeCurriculaSpecialties.sql")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/scripts/deleteProgrammeCurriculaSpecialties.sql")
  public void findSpecialtyByCurriculaProgrammesIdAndNameIsLikeIgnoreCaseShouldReturnSpecialtiesWithNameLikeAndLinkedToProgrammeId() {
    Pageable page = PageRequest.of(0, 100);
    long programmeId = 3;
    Page<Specialty> result = specialtyRepository.findSpecialtyDistinctByCurriculaProgrammesIdAndNameContainingIgnoreCaseAndStatusIs(programmeId, "Medicine", Status.CURRENT, page);

    Assert.assertEquals(2, result.getNumberOfElements());
    Assert.assertFalse(result.hasNext());
    for (Specialty specialty : result.getContent()) {
      Assert.assertTrue(("Metabolic Medicine".equals(specialty.getName()) && "SPE-5".equals(specialty.getSpecialtyCode()) ||
          ("Oral Medicine".equals(specialty.getName()) && "SPE-6".equals(specialty.getSpecialtyCode()))));
    }
  }

  @Test
  @Sql("/scripts/programmeCurriculaSpecialties.sql")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/scripts/deleteProgrammeCurriculaSpecialties.sql")
  public void findSpecialtyDistinctByCurriculaProgrammesIdShouldGetDistinctSpecialties() {
    Pageable page = PageRequest.of(0, 100);
    long programmeWithMultipleCurriculaPointingToSameSpecialty = 4;
    Page<Specialty> result = specialtyRepository.findSpecialtyDistinctByCurriculaProgrammesIdAndNameContainingIgnoreCaseAndStatusIs(
        programmeWithMultipleCurriculaPointingToSameSpecialty, "Medicine", Status.CURRENT, page);

    Assert.assertEquals(1, result.getNumberOfElements());
    Assert.assertFalse(result.hasNext());
    for (Specialty specialty : result.getContent()) {
      Assert.assertEquals("Paediatric inherited Metabolic Medicine", specialty.getName());
      Assert.assertEquals("SPE-7", specialty.getSpecialtyCode());
    }
  }

  @Test
  @Sql("/scripts/programmeCurriculaSpecialties.sql")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/scripts/deleteProgrammeCurriculaSpecialties.sql")
  public void findSpecialtyDistinctByCurriculaProgrammesIdShouldGetSpecialtiesThatHasInactiveStatus() {
    Pageable page = PageRequest.of(0, 100);
    long programmeWithSpecialtiesThatAreBothActiveAndInactive = 6;
    Page<Specialty> result = specialtyRepository.findSpecialtyDistinctByCurriculaProgrammesIdAndStatusIs(
        programmeWithSpecialtiesThatAreBothActiveAndInactive, Status.INACTIVE, page);

    Assert.assertEquals(1, result.getNumberOfElements());
    Assert.assertFalse(result.hasNext());
    for (Specialty specialty : result.getContent()) {
      Assert.assertEquals(Status.INACTIVE, specialty.getStatus());
    }
  }


  @Test
  @Sql("/scripts/programmeCurriculaSpecialties.sql")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/scripts/deleteProgrammeCurriculaSpecialties.sql")
  public void findDistinctByProgrammeIdAndPersonId() {
    Long programmeId = 1L;
    Long personId = 1L;
    Long expectedSpecialtyId1 = 4L, expectedSpecialtyId2 = 3L;
    String specialtyName1 = "Urogynaecology", specialtyName2 = "Oral and maxillofacial pathology";
    Set<Specialty> results = specialtyRepository.findDistinctByProgrammeIdAndPersonIdAndStatus(programmeId, personId, Status.CURRENT);

    Assert.assertEquals(2, results.size());
    for (Specialty result : results) {
      Assert.assertTrue(expectedSpecialtyId1.equals(result.getId()) || expectedSpecialtyId2.equals(result.getId()));
      Assert.assertTrue(specialtyName1.equals(result.getName()) || specialtyName2.equals(result.getName()));
    }
  }
}