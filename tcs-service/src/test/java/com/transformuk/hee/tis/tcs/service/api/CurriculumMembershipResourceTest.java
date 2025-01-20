package com.transformuk.hee.tis.tcs.service.api;

import static com.transformuk.hee.tis.tcs.service.api.CurriculumResourceIntTest.createCurriculumEntity;
import static com.transformuk.hee.tis.tcs.service.api.ProgrammeMembershipResourceIntTest.createPersonEntity;
import static com.transformuk.hee.tis.tcs.service.api.ProgrammeMembershipResourceIntTest.createProgrammeMembershipEntity;
import static com.transformuk.hee.tis.tcs.service.api.ProgrammeResourceIntTest.createEntity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.CurriculumMembershipService;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
class CurriculumMembershipResourceTest {

  private static final LocalDate START_DATE_1 = LocalDate.of(2020, 1, 1);
  private static final LocalDate END_DATE_1 = LocalDate.of(2025, 1, 1);
  private static final LocalDate START_DATE_2 = LocalDate.of(2021, 1, 1);
  private static final LocalDate END_DATE_2 = LocalDate.of(2024, 1, 1);

  private Person person;
  private Programme programme;
  private Curriculum curriculum;
  private ProgrammeCurriculum programmeCurriculum;
  private ProgrammeMembership programmeMembership;
  @Autowired
  private CurriculumMembershipService cmService;
  @Autowired
  private CurriculumMembershipValidator cmValidator;
  @Autowired
  private ProgrammeMembershipRepository pmRepository;
  @Autowired
  private ProgrammeRepository programmeRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private CurriculumRepository cmRepository;
  @Autowired
  private ProgrammeMembershipRepository programmeMembershipRepository;
  @Autowired
  private CurriculumMembershipRepository curriculumMembershipRepository;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  private MockMvc restCmMockMvc;

  @BeforeEach
  void setUp() {
    CurriculumMembershipResource cmResource = new CurriculumMembershipResource(
        cmService, cmValidator);

    this.restCmMockMvc = MockMvcBuilders
        .standaloneSetup(cmResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @BeforeEach
  void initTest() {
    person = createPersonEntity();
    programme = createEntity();
    curriculum = createCurriculumEntity();
    programmeCurriculum = new ProgrammeCurriculum(programme, curriculum, null);
    programmeMembership = createProgrammeMembershipEntity();
  }

  @Test
  @Transactional
  void shouldCreateCurriculumMembership() throws Exception {
    personRepository.saveAndFlush(person);
    curriculum.setStatus(Status.CURRENT);
    cmRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setProgramme(programme);
    programmeMembership.setProgrammeStartDate(START_DATE_1);
    programmeMembership.setProgrammeEndDate(END_DATE_1);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    Long curriculumId = programme.getCurricula().iterator().next().getCurriculum().getId();
    cmDto.setCurriculumId(curriculumId);
    cmDto.setCurriculumStartDate(START_DATE_2);
    cmDto.setCurriculumEndDate(END_DATE_2);
    cmDto.setProgrammeMembershipUuid(programmeMembership.getUuid());

    restCmMockMvc.perform(post("/api/curriculum-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmDto)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.curriculumStartDate").value(START_DATE_2.toString()))
        .andExpect(jsonPath("$.curriculumEndDate").value(END_DATE_2.toString()))
        .andExpect(
            jsonPath("$.programmeMembershipUuid").value(programmeMembership.getUuid().toString()))
        .andExpect(jsonPath("$.curriculumId").value(curriculumId));
  }

  @Test
  @Transactional
  void shouldPatchCurriculumMembership() throws Exception {
    personRepository.saveAndFlush(person);
    curriculum.setStatus(Status.CURRENT);
    cmRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setProgramme(programme);
    programmeMembership.setProgrammeStartDate(START_DATE_1);
    programmeMembership.setProgrammeEndDate(END_DATE_1);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    CurriculumMembership curriculumMembership = new CurriculumMembership();
    curriculumMembership.setCurriculumId(curriculum.getId());
    curriculumMembership.setProgrammeMembership(programmeMembership);
    curriculumMembership.setCurriculumStartDate(START_DATE_1);
    curriculumMembership.setCurriculumEndDate(END_DATE_1);
    curriculumMembershipRepository.saveAndFlush(curriculumMembership);

    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    Long curriculumId = programme.getCurricula().iterator().next().getCurriculum().getId();
    cmDto.setId(curriculumMembership.getId());
    cmDto.setCurriculumId(curriculumId);
    cmDto.setCurriculumStartDate(START_DATE_2);
    cmDto.setCurriculumEndDate(END_DATE_2);
    cmDto.setProgrammeMembershipUuid(programmeMembership.getUuid());

    restCmMockMvc.perform(patch("/api/curriculum-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmDto)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.curriculumStartDate").value(START_DATE_2.toString()))
        .andExpect(jsonPath("$.curriculumEndDate").value(END_DATE_2.toString()))
        .andExpect(
            jsonPath("$.programmeMembershipUuid").value(programmeMembership.getUuid().toString()))
        .andExpect(jsonPath("$.curriculumId").value(curriculumId));
  }

  @Test
  @Transactional
  void shouldGetBadRequestWhenIdSpecified() throws Exception {
    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    cmDto.setId(1L);
    cmDto.setCurriculumId(1L);
    cmDto.setCurriculumStartDate(START_DATE_2);
    cmDto.setCurriculumEndDate(END_DATE_2);
    cmDto.setProgrammeMembershipUuid(programmeMembership.getUuid());

    restCmMockMvc.perform(post("/api/curriculum-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmDto)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void shouldGetBadRequestWhenDataValidationFails() throws Exception {
    personRepository.saveAndFlush(person);
    curriculum.setStatus(Status.INACTIVE);
    cmRepository.saveAndFlush(curriculum);
    programme.setCurricula(Collections.singleton(programmeCurriculum));
    programmeRepository.saveAndFlush(programme);
    programmeMembership.setProgramme(programme);
    programmeMembership.setProgrammeStartDate(START_DATE_1);
    programmeMembership.setProgrammeEndDate(END_DATE_1);
    programmeMembershipRepository.saveAndFlush(programmeMembership);

    CurriculumMembershipDTO cmDto = new CurriculumMembershipDTO();
    Long curriculumId = programme.getCurricula().iterator().next().getCurriculum().getId();
    cmDto.setCurriculumId(curriculumId);
    cmDto.setCurriculumStartDate(START_DATE_2);
    cmDto.setCurriculumEndDate(END_DATE_2);
    cmDto.setProgrammeMembershipUuid(programmeMembership.getUuid());

    restCmMockMvc.perform(post("/api/curriculum-memberships")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cmDto)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fieldErrors[0].message").value(
            String.format("Could not find current curriculum for id \"%s\" under the programme.",
                curriculumId)));
  }
}
