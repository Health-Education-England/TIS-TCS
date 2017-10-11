package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyGroupRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyGroupService;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyGroupMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the SpecialtyGroupResource REST controller.
 *
 * @see SpecialtyGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpecialtyGroupResourceIntTest {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";
  private static final String WRONG_SPECIALTY_NAME = "ZZZZZZZZZZ";

  private static final String DEFAULT_INTREPID_ID = "123456";

  @Autowired
  private SpecialtyGroupRepository specialtyGroupRepository;

  @Autowired
  private SpecialtyGroupMapper specialtyGroupMapper;

  @Autowired
  private SpecialtyGroupService specialtyGroupService;

  @Autowired
  private SpecialtyMapper specialtyMapper;

  @Autowired
  private SpecialtyRepository specialtyRepository;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restSpecialtyGroupMockMvc;

  private SpecialtyGroup specialtyGroup;

  private Specialty specialty;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static SpecialtyGroup createEntity() {
    SpecialtyGroup specialtyGroup = new SpecialtyGroup()
        .name(DEFAULT_NAME)
        .intrepidId(DEFAULT_INTREPID_ID);
    return specialtyGroup;
  }

  public Specialty createSpecialty() {
    Specialty specialty = new Specialty()
        .name(DEFAULT_NAME)
        .specialtyGroup(specialtyGroup);
    return specialty;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SpecialtyGroupResource specialtyGroupResource = new SpecialtyGroupResource(specialtyGroupService, specialtyRepository, specialtyMapper);
    this.restSpecialtyGroupMockMvc = MockMvcBuilders.standaloneSetup(specialtyGroupResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    specialtyGroup = createEntity();
    specialty = createSpecialty();
  }

  @Test
  @Transactional
  public void createSpecialtyGroup() throws Exception {
    int databaseSizeBeforeCreate = specialtyGroupRepository.findAll().size();

    // Create the SpecialtyGroup
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup);
    restSpecialtyGroupMockMvc.perform(post("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isCreated());

    // Validate the SpecialtyGroup in the database
    List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
    assertThat(specialtyGroupList).hasSize(databaseSizeBeforeCreate + 1);
    SpecialtyGroup testSpecialtyGroup = specialtyGroupList.get(specialtyGroupList.size() - 1);
    assertThat(testSpecialtyGroup.getName()).isEqualTo(DEFAULT_NAME);
  }

  @Test
  @Transactional
  public void createSpecialtyGroupWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = specialtyGroupRepository.findAll().size();

    // Create the SpecialtyGroup with an existing ID
    specialtyGroup.setId(1L);
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup);

    // An entity with an existing ID cannot be created, so this API call must fail
    restSpecialtyGroupMockMvc.perform(post("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
    assertThat(specialtyGroupList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllSpecialtyGroups() throws Exception {
    // Initialize the database
    specialtyGroupRepository.saveAndFlush(specialtyGroup);

    // Get all the specialtyGroupList
    restSpecialtyGroupMockMvc.perform(get("/api/specialty-groups?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(specialtyGroup.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
  }

  @Test
  @Transactional
  public void shouldTextSearch() throws Exception {
    //given
    // Initialize the database
    specialtyGroupRepository.saveAndFlush(specialtyGroup);
    SpecialtyGroup otherNameSpecialtyGroup = createEntity();
    otherNameSpecialtyGroup.setName("other name");
    specialtyGroupRepository.saveAndFlush(otherNameSpecialtyGroup);
    //when & then
    // Get all the specialtyGroupList
    restSpecialtyGroupMockMvc.perform(get("/api/specialty-groups?sort=id,desc&searchQuery=other"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].name").value("other name"));
  }

  @Test
  @Transactional
  public void shouldGetAllSpecialtiesAttachedToGroup() throws Exception {
    // given
    // Initialise the database and get the specialty group ID
    specialtyGroupRepository.saveAndFlush(specialtyGroup);
    specialtyRepository.saveAndFlush(specialty);
    Long groupID = specialtyGroup.getId();

    // Add another specialtyGroup (one with a specialty we don't expect to see in the test)
    SpecialtyGroup wrongSpecialtyGroup = createEntity();
    wrongSpecialtyGroup.setName("Wrong Group");
    specialtyGroupRepository.saveAndFlush(wrongSpecialtyGroup);

    // Add a second specialty (one we expect)
    Specialty otherSpecialty = createSpecialty();
    otherSpecialty.setName("Other Specialty");
    specialtyRepository.saveAndFlush(otherSpecialty);

    // Add another specialty to the "Wrong Group"
    Specialty wrongSpecialty = createSpecialty();
    wrongSpecialty.setName(WRONG_SPECIALTY_NAME);
    wrongSpecialty.setSpecialtyGroup(wrongSpecialtyGroup);
    specialtyRepository.saveAndFlush(wrongSpecialty);

    //when & then
    restSpecialtyGroupMockMvc.perform(get("/api/specialty-groups/specialties/" + groupID))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
        .andExpect(jsonPath("$.[*].id").value(hasItem(otherSpecialty.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(jsonPath("$.[*].name").value(hasItem(otherSpecialty.getName())))
        .andExpect(jsonPath("$.[*].name").value(not(contains(wrongSpecialty.getName()))));
  }

  @Test
  @Transactional
  public void getSpecialtyGroup() throws Exception {
    // Initialize the database
    specialtyGroupRepository.saveAndFlush(specialtyGroup);

    // Get the specialtyGroup
    restSpecialtyGroupMockMvc.perform(get("/api/specialty-groups/{id}", specialtyGroup.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(specialtyGroup.getId().intValue()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingSpecialtyGroup() throws Exception {
    // Get the specialtyGroup
    restSpecialtyGroupMockMvc.perform(get("/api/specialty-groups/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSpecialtyGroup() throws Exception {
    // Initialize the database
    specialtyGroupRepository.saveAndFlush(specialtyGroup);
    int databaseSizeBeforeUpdate = specialtyGroupRepository.findAll().size();

    // Update the specialtyGroup
    SpecialtyGroup updatedSpecialtyGroup = specialtyGroupRepository.findOne(specialtyGroup.getId());
    updatedSpecialtyGroup
        .name(UPDATED_NAME);
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(updatedSpecialtyGroup);

    restSpecialtyGroupMockMvc.perform(put("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isOk());

    // Validate the SpecialtyGroup in the database
    List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
    assertThat(specialtyGroupList).hasSize(databaseSizeBeforeUpdate);
    SpecialtyGroup testSpecialtyGroup = specialtyGroupList.get(specialtyGroupList.size() - 1);
    assertThat(testSpecialtyGroup.getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  @Transactional
  public void updateNonExistingSpecialtyGroup() throws Exception {
    //given
    int databaseSizeBeforeUpdate = specialtyGroupRepository.findAll().size();
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup);

    //when and then
    restSpecialtyGroupMockMvc.perform(put("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isBadRequest());

    // Validate the SpecialtyGroup in the database
    List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
    assertThat(specialtyGroupList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteSpecialtyGroup() throws Exception {
    // Initialize the database
    specialtyGroupRepository.saveAndFlush(specialtyGroup);
    int databaseSizeBeforeDelete = specialtyGroupRepository.findAll().size();

    // Get the specialtyGroup
    restSpecialtyGroupMockMvc.perform(delete("/api/specialty-groups/{id}", specialtyGroup.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<SpecialtyGroup> specialtyGroupList = specialtyGroupRepository.findAll();
    assertThat(specialtyGroupList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void shouldValidateNameExistsWhenCreating() throws Exception {
    //given
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(createEntity());
    specialtyGroupDTO.setName("");
    //when & then
    restSpecialtyGroupMockMvc.perform(post("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
  }

  @Test
  @Transactional
  public void shouldValidateNameLengthWhenCreating() throws Exception {
    //given
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(createEntity());
    specialtyGroupDTO.setName(
        "more_than_100_chars_more_than_100_chars_more_than_100_chars_more_than_100_chars_more_than_100_chars_1");
    //when & then
    restSpecialtyGroupMockMvc.perform(post("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
  }

  @Test
  @Transactional
  public void shouldValidateNameContentsWhenCreating() throws Exception {
    //given
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(createEntity());
    specialtyGroupDTO.setName("$^%&*()_");
    //when & then
    restSpecialtyGroupMockMvc.perform(post("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
  }

  @Test
  @Transactional
  public void shouldValidateNameExistsWhenUpdating() throws Exception {
    //given
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(createEntity());
    specialtyGroupDTO.setName("");
    specialtyGroupDTO.setId(1L);
    //when & then
    restSpecialtyGroupMockMvc.perform(put("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
  }

  @Test
  @Transactional
  public void shouldValidateNameLengthWhenUpdating() throws Exception {
    //given
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(createEntity());
    specialtyGroupDTO.setName(
        "more_than_100_chars_more_than_100_chars_more_than_100_chars_more_than_100_chars_more_than_100_chars_1");
    specialtyGroupDTO.setId(1L);
    //when & then
    restSpecialtyGroupMockMvc.perform(put("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
  }

  @Test
  @Transactional
  public void shouldValidateNameContentsWhenUpdating() throws Exception {
    //given
    SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(createEntity());
    specialtyGroupDTO.setName("$^%&*()_");
    specialtyGroupDTO.setId(1L);
    //when & then
    restSpecialtyGroupMockMvc.perform(put("/api/specialty-groups")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(specialtyGroupDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(SpecialtyGroup.class);
  }
}
