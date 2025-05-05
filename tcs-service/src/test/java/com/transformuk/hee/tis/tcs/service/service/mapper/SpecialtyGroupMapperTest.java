package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyTypeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpecialtyGroupMapperTest {

  private SpecialtyGroupMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new SpecialtyGroupMapper();
  }

  @Test
  void testMappingFromSpecialtyGroupsToSpecialtyGroupDtosWithValidSpecialty() {
    Specialty specialty = buildSpecialty("Cardiology", "999", "123", "Medicine College",
        Set.of(SpecialtyType.CURRICULUM));
    SpecialtyGroup group = buildSpecialtyGroup("Pharmacy", "345", Set.of(specialty));

    List<SpecialtyGroupDTO> result = mapper.specialtyGroupsToSpecialtyGroupDTOs(List.of(group));

    assertNotNull(result);
    assertEquals(1, result.size());

    SpecialtyGroupDTO dto = result.get(0);
    assertSpecialtyGroupFields(group, dto);

    SpecialtyDTO specialtyDTO = dto.getSpecialties().iterator().next();
    assertSpecialtyFields(specialty, specialtyDTO);
    assertEquals("CURRICULUM", specialtyDTO.getSpecialtyTypes().iterator().next().getName());
  }

  @Test
  void testMappingSpecialtyGroupsToSpecialtyGroupDtosWithEmptyList() {
    List<SpecialtyGroupDTO> result = mapper.specialtyGroupsToSpecialtyGroupDTOs(
        Collections.emptyList());
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testMappingSpecialtyGroupsToSpecialtyGroupDtosWithNullSpecialties() {
    SpecialtyGroup group = new SpecialtyGroup();
    group.setId(1L);
    group.setName("NullSpecialtyGroup");

    List<SpecialtyGroupDTO> result = mapper.specialtyGroupsToSpecialtyGroupDTOs(List.of(group));

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("NullSpecialtyGroup", result.get(0).getName());
    assertNull(result.get(0).getSpecialties());
  }

  @Test
  void testMappingSpecialtyGroupToSpecialtyGroupDtoWithSpecialties() {
    Specialty specialty = buildSpecialty("Radiology", "101", "888", "Medical College",
        Set.of(SpecialtyType.CURRICULUM, SpecialtyType.POST));
    SpecialtyGroup group = buildSpecialtyGroup("Surgical Specialties", "123", Set.of(specialty));

    SpecialtyGroupDTO dto = mapper.specialtyGroupToSpecialtyGroupDTO(group);

    assertSpecialtyGroupFields(group, dto);

    SpecialtyDTO mappedSpecialty = dto.getSpecialties().iterator().next();
    assertSpecialtyFields(specialty, mappedSpecialty);
    assertEquals(2, mappedSpecialty.getSpecialtyTypes().size());
    assertTrue(mappedSpecialty.getSpecialtyTypes().stream()
        .anyMatch(t -> t.getName().equals("CURRICULUM")));
    assertTrue(
        mappedSpecialty.getSpecialtyTypes().stream().anyMatch(t -> t.getName().equals("POST")));
  }

  @Test
  void testMappingSpecialtyGroupDtoToSpecialtyGroupWithSpecialties() {
    SpecialtyDTO specialtyDTO = buildSpecialtyDTO("Anaesthetics", "123", "111",
        "Anaesthesia College",
        Set.of(new SpecialtyTypeDTO("CURRICULUM"), new SpecialtyTypeDTO("POST")));
    SpecialtyGroupDTO groupDTO = buildSpecialtyGroupDTO("Acute Specialties", "456",
        Set.of(specialtyDTO));

    SpecialtyGroup result = mapper.specialtyGroupDTOToSpecialtyGroup(groupDTO);

    assertSpecialtyGroupFields(groupDTO, result);

    Specialty specialty = result.getSpecialties().iterator().next();
    assertSpecialtyFields(specialtyDTO, specialty);
    assertEquals(2, specialty.getSpecialtyTypes().size());
    assertTrue(specialty.getSpecialtyTypes().contains(SpecialtyType.CURRICULUM));
    assertTrue(specialty.getSpecialtyTypes().contains(SpecialtyType.POST));
  }

  private Specialty buildSpecialty(String name, String code, String intrepidId, String college,
      Set<SpecialtyType> types) {
    Specialty s = new Specialty();
    s.setId(new Random().nextLong());
    s.setUuid(UUID.randomUUID());
    s.setName(name);
    s.setSpecialtyCode(code);
    s.setIntrepidId(intrepidId);
    s.setCollege(college);
    s.setSpecialtyTypes(types);
    return s;
  }

  private SpecialtyDTO buildSpecialtyDTO(String name, String code, String intrepidId,
      String college, Set<SpecialtyTypeDTO> typeDtos) {
    SpecialtyDTO dto = new SpecialtyDTO();
    dto.setId(new Random().nextLong());
    dto.setUuid(UUID.randomUUID());
    dto.setName(name);
    dto.setSpecialtyCode(code);
    dto.setIntrepidId(intrepidId);
    dto.setCollege(college);
    dto.setSpecialtyTypes(typeDtos);
    return dto;
  }

  private SpecialtyGroup buildSpecialtyGroup(String name, String intrepidId,
      Set<Specialty> specialties) {
    SpecialtyGroup g = new SpecialtyGroup();
    g.setId(new Random().nextLong());
    g.setUuid(UUID.randomUUID());
    g.setName(name);
    g.setIntrepidId(intrepidId);
    g.setSpecialties(specialties);
    return g;
  }

  private SpecialtyGroupDTO buildSpecialtyGroupDTO(String name, String intrepidId,
      Set<SpecialtyDTO> specialties) {
    SpecialtyGroupDTO dto = new SpecialtyGroupDTO();
    dto.setId(new Random().nextLong());
    dto.setUuid(UUID.randomUUID());
    dto.setName(name);
    dto.setIntrepidId(intrepidId);
    dto.setSpecialties(specialties);
    return dto;
  }

  private void assertSpecialtyGroupFields(SpecialtyGroup expected, SpecialtyGroupDTO actual) {
    assertNotNull(actual);
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getIntrepidId(), actual.getIntrepidId());
  }

  private void assertSpecialtyGroupFields(SpecialtyGroupDTO expected, SpecialtyGroup actual) {
    assertNotNull(actual);
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getIntrepidId(), actual.getIntrepidId());
  }

  private void assertSpecialtyFields(Specialty expected, SpecialtyDTO actual) {
    assertNotNull(actual);
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getSpecialtyCode(), actual.getSpecialtyCode());
    assertEquals(expected.getIntrepidId(), actual.getIntrepidId());
    assertEquals(expected.getCollege(), actual.getCollege());
    assertEquals(expected.getUuid(), actual.getUuid());
  }

  private void assertSpecialtyFields(SpecialtyDTO expected, Specialty actual) {
    assertNotNull(actual);
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getSpecialtyCode(), actual.getSpecialtyCode());
    assertEquals(expected.getIntrepidId(), actual.getIntrepidId());
    assertEquals(expected.getCollege(), actual.getCollege());
    assertEquals(expected.getUuid(), actual.getUuid());
  }
}
