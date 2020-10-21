package com.transformuk.hee.tis.tcs.service.api;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.validation.SpecialtyValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import com.transformuk.hee.tis.tcs.service.repository.PostSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyGroupRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyService;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import java.util.List;
import java.util.Optional;
import org.apache.commons.codec.net.URLCodec;
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

/**
 * Test class for the SpecialtyResource REST controller.
 *
 * @see SpecialtyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SpecialtyResourceIntTest {

  private static final String VERY_LONG_STRING = "qwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnmqwertyuioplkjhgfdsazxcvbnm";
  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;
  private static final String DEFAULT_COLLEGE = "AAAAAAAAAA";
  private static final String UPDATED_COLLEGE = "BBBBBBBBBB";
  private static final String DEFAULT_NHS_SPECIALTY_CODE = "AAAAAAAAAA";
  private static final String UPDATED_NHS_SPECIALTY_CODE = "BBBBBBBBBB";
  private static final String DEFAULT_SPECIALTYGROUP_NAME = "DEFAULT GROUP";
  private static final String DEFAULT_INTREPID_ID = "123456";
  private static final String DEFAULT_NAME = "SPECIALTY_NAME";
  private static final SpecialtyType DEFAULT_SPECIALTY_TYPE = SpecialtyType.SUB_SPECIALTY;
  private static final SpecialtyType UPDATED_SPECIALTY_TYPE = SpecialtyType.POST;
  private static final String UPDATED_NAME = "UPDATED NAME";

  @Autowired
  private SpecialtyRepository specialtyRepository;

  @Autowired
  private SpecialtyGroupRepository specialtyGroupRepository;

  @Autowired
  private PostSpecialtyRepository postSpecialtyRepository;

  @Autowired
  private SpecialtyMapper specialtyMapper;

  @Autowired
  private SpecialtyService specialtyService;

  @Autowired
  private SpecialtyValidator specialtyValidator;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc restSpecialtyMockMvc;

  private Specialty specialty;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Specialty createEntity() {
    return new Specialty()
        .status(DEFAULT_STATUS)
        .college(DEFAULT_COLLEGE)
        .specialtyCode(DEFAULT_NHS_SPECIALTY_CODE)
        .specialtyTypes(newHashSet(DEFAULT_SPECIALTY_TYPE))
        .intrepidId(DEFAULT_INTREPID_ID)
        .name(DEFAULT_NAME);
  }

  public static SpecialtyGroup createSpecialtyGroupEntity() {
    return new SpecialtyGroup()
        .intrepidId("123333");
  }

  public static PostSpecialty createPostSpecialtyEntity(Specialty specialty) {
    PostSpecialty postSpecialty = new PostSpecialty();
    postSpecialty.setPostSpecialtyType(PostSpecialtyType.PRIMARY);
    postSpecialty.setSpecialty(specialty);
    return postSpecialty;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SpecialtyResource specialtyResource = new SpecialtyResource(specialtyService,
        specialtyValidator);
    this.restSpecialtyMockMvc = MockMvcBuilders.standaloneSetup(specialtyResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    specialty = createEntity();
  }

  @Test
  @Transactional
  public void createSpecialty() throws Exception {
    int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    specialtyGroupEntity = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
    // Create the Specialty
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        specialtyGroupEntity.getId());

    restSpecialtyMockMvc.perform(post("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(status().isCreated());

    // Validate the Specialty in the database
    List<Specialty> specialtyList = specialtyRepository.findAll();
    assertThat(specialtyList).hasSize(databaseSizeBeforeCreate + 1);
    Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
    assertThat(testSpecialty.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testSpecialty.getCollege()).isEqualTo(DEFAULT_COLLEGE);
    assertThat(testSpecialty.getSpecialtyCode()).isEqualTo(DEFAULT_NHS_SPECIALTY_CODE);
    assertThat(testSpecialty.getSpecialtyTypes()).isEqualTo(newHashSet(DEFAULT_SPECIALTY_TYPE));
  }

  @Test
  @Transactional
  public void createSpecialtyWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

    // Create the Specialty with an existing ID
    specialty.setId(1L);
    SpecialtyDTO specialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(specialty);

    // An entity with an existing ID cannot be created, so this API call must fail
    restSpecialtyMockMvc.perform(post("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Specialty> specialtyList = specialtyRepository.findAll();
    assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void createSpecialtyShouldFailWithNoSpecialtyCode() throws Exception {
    int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    specialtyGroupEntity = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        specialtyGroupEntity.getId());
    specialtyDTO.setSpecialtyCode(null);

    // An entity with no nhs specialty code cannot be created
    restSpecialtyMockMvc.perform(post("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("specialtyCode"))
        .andExpect(status().isBadRequest());

    // Validate that there are no new entities created
    List<Specialty> specialtyList = specialtyRepository.findAll();
    assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);


  }

  @Test
  @Transactional
  public void createSpecialtyShouldFailWithBlankSpecialtyCode() throws Exception {
    int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    specialtyGroupEntity = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        specialtyGroupEntity.getId());
    specialtyDTO.setSpecialtyCode("             ");

    // An entity with no nhs specialty code cannot be created
    restSpecialtyMockMvc.perform(post("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("specialtyCode"))
        .andExpect(status().isBadRequest());

    // Validate that there are no new entities created
    List<Specialty> specialtyList = specialtyRepository.findAll();
    assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void createSpecialtyShouldFailWithNoName() throws Exception {
    int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    specialtyGroupEntity = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        specialtyGroupEntity.getId());
    specialtyDTO.setName(null);

    // An entity with no name cannot be created
    restSpecialtyMockMvc.perform(post("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("name"))
        .andExpect(status().isBadRequest());

    // Validate that there are no new entities created
    List<Specialty> specialtyList = specialtyRepository.findAll();
    assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);


  }

  @Test
  @Transactional
  public void createSpecialtyShouldFailWithBlankName() throws Exception {
    int databaseSizeBeforeCreate = specialtyRepository.findAll().size();
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    specialtyGroupEntity = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        specialtyGroupEntity.getId());
    specialtyDTO.setName("         ");

    // An entity cannot be created with name as spaces
    restSpecialtyMockMvc.perform(post("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("name"))
        .andExpect(status().isBadRequest());

    // Validate that there are no new entities created
    List<Specialty> specialtyList = specialtyRepository.findAll();
    assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllSpecialties() throws Exception {
    // Initialize the database
    specialtyRepository.saveAndFlush(specialty);

    // Get all the specialtyList
    restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].college").value(hasItem(DEFAULT_COLLEGE)))
        .andExpect(jsonPath("$.[*].specialtyCode").value(hasItem(DEFAULT_NHS_SPECIALTY_CODE)))
        .andExpect(jsonPath("$.[*].specialtyTypes[0]").value(DEFAULT_SPECIALTY_TYPE.toString()));
  }

  @Test
  @Transactional
  public void shouldFindIn() throws Exception {
    // Initialize the database
    specialtyRepository.saveAndFlush(specialty);

    // Get all the specialtyList
    restSpecialtyMockMvc.perform(get("/api/specialties/in/" + specialty.getId() + "," + 123))
        .andExpect(status().isFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString().toUpperCase())))
        .andExpect(jsonPath("$.[*].college").value(hasItem(DEFAULT_COLLEGE)));
  }

  @Test
  @Transactional
  public void shouldIgnoreInvalidIdsWhenFindIn() throws Exception {
    // Initialize the database
    specialtyRepository.saveAndFlush(specialty);

    // Get all the specialtyList
    restSpecialtyMockMvc.perform(get("/api/specialties/in/invalid"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
  }


  @Test
  @Transactional
  public void getSpecialty() throws Exception {
    // Given
    // Initialize the database
    // Add a specialty group to the specialty
    SpecialtyGroup specialtyGroup = createSpecialtyGroupEntity();
    specialtyGroupRepository.saveAndFlush(specialtyGroup);
    specialty.setSpecialtyGroup(specialtyGroup);
    specialtyRepository.saveAndFlush(specialty);

    // When and then
    // Get the specialty
    restSpecialtyMockMvc.perform(get("/api/specialties/{id}", specialty.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
        .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString().toUpperCase()))
        .andExpect(jsonPath("$.college").value(DEFAULT_COLLEGE))
        .andExpect(jsonPath("$.specialtyCode").value(DEFAULT_NHS_SPECIALTY_CODE))
        .andExpect(jsonPath("$.specialtyTypes[0]").value(DEFAULT_SPECIALTY_TYPE.toString()))
        .andExpect(jsonPath("$.specialtyGroup.id").value(specialtyGroup.getId().intValue()));
  }

  @Test
  @Transactional
  public void shouldTextSearch() throws Exception {
    //given
    // Initialize the database
    specialtyRepository.saveAndFlush(specialty);
    Specialty otherNameSpecialty = createEntity();
    otherNameSpecialty.name("other college");
    specialtyRepository.saveAndFlush(otherNameSpecialty);

    //when & then
    // Get all the specialtyList
    restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&searchQuery=other"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].name").value("other college"));
  }

  @Test
  @Transactional
  public void shouldFilterColumns() throws Exception {
    //given
    // Initialize the database
    specialtyRepository.saveAndFlush(specialty);
    Specialty otherCollegeSpecialty = createEntity();
    otherCollegeSpecialty.setCollege("TestCollege");
    specialtyRepository.saveAndFlush(otherCollegeSpecialty);

    //when & then
    String colFilters = new URLCodec().encode("{\"college\":[\"TestCollege\"]}");
    // Get all the specialtyList
    restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].college").value("TestCollege"));
  }

  @Test
  @Transactional
  public void shouldFilterSpecialtyGroupColumns() throws Exception {
    //given
    // Initialize the database
    SpecialtyGroup specialtyGroup = new SpecialtyGroup();
    specialtyGroup.setName(DEFAULT_SPECIALTYGROUP_NAME);
    specialtyGroupRepository.saveAndFlush(specialtyGroup);

    specialtyRepository.saveAndFlush(specialty);
    Specialty otherSpecialtyTypeSpecialty = createEntity();

    otherSpecialtyTypeSpecialty.setSpecialtyGroup(specialtyGroup);
    otherSpecialtyTypeSpecialty.setSpecialtyTypes(newHashSet(SpecialtyType.CURRICULUM));
    specialtyRepository.saveAndFlush(otherSpecialtyTypeSpecialty);

    //when & then
    String colFilters = new URLCodec()
        .encode("{\"specialtyGroup.name\":[\"" + DEFAULT_SPECIALTYGROUP_NAME + "\"]}");
    // Get all the specialtyList
    restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&columnFilters=" +
        colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].specialtyGroup.name").value(DEFAULT_SPECIALTYGROUP_NAME));
  }

  @Test
  @Transactional
  public void shouldTextSearchAndFilterColumns() throws Exception {
    //given
    // Initialize the database
    specialtyRepository.saveAndFlush(specialty);
    Specialty otherSpecialtyCode = createEntity();
    otherSpecialtyCode.setSpecialtyCode("TestSpecialtyCode");
    specialtyRepository.saveAndFlush(otherSpecialtyCode);
    Specialty otherCollegeSpecialty = createEntity();
    otherCollegeSpecialty.setCollege("other college");
    otherCollegeSpecialty.setSpecialtyCode("TestSpecialtyCode");
    specialtyRepository.saveAndFlush(otherCollegeSpecialty);

    //when & then
    String colFilters = new URLCodec().encode("{\"specialtyCode\":[\"TestSpecialtyCode\"]}");
    // Get all the specialtyList
    restSpecialtyMockMvc
        .perform(get("/api/specialties?sort=id,desc&searchQuery=other&columnFilters=" +
            colFilters))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].specialtyCode").value("TestSpecialtyCode"));
  }

  @Test
  public void shouldComplainIfBadRequest() throws Exception {
    //given
    URLCodec codec = new URLCodec();
    String colFilters = codec.encode("{\"status\":[\"malformed json\"");
    //when & then
    // Get all the programmeList
    restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&columnFilters=" + colFilters))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Bad request"));
  }

  @Test
  @Transactional
  public void getNonExistingSpecialty() throws Exception {
    // Get the specialty
    restSpecialtyMockMvc.perform(get("/api/specialties/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSpecialty() throws Exception {
    // Initialize the database
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

    Specialty specialtySaved = specialtyRepository.saveAndFlush(specialty);

    PostSpecialty postSpecialty = createPostSpecialtyEntity(specialtySaved);
    PostSpecialty postSpecialtySaved = postSpecialtyRepository.saveAndFlush(postSpecialty);

    specialtySaved.getPosts().add(postSpecialty);
    specialtyRepository.saveAndFlush(specialtySaved);

    int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

    // Update the specialty
    Specialty updatedSpecialty = new Specialty();
    updatedSpecialty.setId(specialtySaved.getId());
    updatedSpecialty
        .status(UPDATED_STATUS)
        .college(UPDATED_COLLEGE)
        .specialtyCode(UPDATED_NHS_SPECIALTY_CODE)
        .specialtyTypes(newHashSet(UPDATED_SPECIALTY_TYPE))
        .name(UPDATED_NAME);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(updatedSpecialty,
        specialtyGroupEntity.getId());

    restSpecialtyMockMvc.perform(put("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(status().isOk());

    // Validate the Specialty in the database
    List<Specialty> specialtyList = specialtyRepository.findAll();
    assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
    assertThat(testSpecialty.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testSpecialty.getCollege()).isEqualTo(UPDATED_COLLEGE);
    assertThat(testSpecialty.getSpecialtyCode()).isEqualTo(UPDATED_NHS_SPECIALTY_CODE);
    assertThat(testSpecialty.getSpecialtyTypes()).isEqualTo(newHashSet(UPDATED_SPECIALTY_TYPE));

    // validate if the PostSpecialty still exists and if it's updated
    Optional<PostSpecialty> optionalPostSpecialty =
        postSpecialtyRepository.findById(postSpecialtySaved.getId());
    assertThat(optionalPostSpecialty.isPresent()).isTrue();
    PostSpecialty postSpecialtyValidate = optionalPostSpecialty.get();
    assertThat(postSpecialtyValidate.getSpecialty().getId()).isEqualTo(specialty.getId());
    assertThat(postSpecialtyValidate.getSpecialty().getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  @Transactional
  public void updateNonExistingSpecialtyShouldFail() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
    // Create the Specialty
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroup.getId());

    restSpecialtyMockMvc.perform(put("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void updateSpecialtyShouldFailWhenIdIsNegative() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
    // When SpecialtyId is negative
    specialty.setId(-11111L);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty, specialtyGroup.getId());

    restSpecialtyMockMvc.perform(put("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("id"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void updateSpecialtyShouldFailWhenStatusIsNull() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

    specialty = specialtyRepository.saveAndFlush(specialty);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty,
        specialtyGroup.getId());

    // When status is null
    specialtyDTO.setStatus(null);
    restSpecialtyMockMvc.perform(put("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("status"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void updateSpecialtyShouldFailWhenSpecialtyCodeIsNull() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

    specialty = specialtyRepository.saveAndFlush(specialty);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty,
        specialtyGroup.getId());

    // When status is null
    specialtyDTO.setSpecialtyCode(null);
    restSpecialtyMockMvc.perform(put("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("specialtyCode"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void updateSpecialtyShouldFailWhenSpecialtyTypeIsNull() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

    specialty = specialtyRepository.saveAndFlush(specialty);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty,
        specialtyGroup.getId());

    // When type is null
    specialtyDTO.setSpecialtyTypes(null);
    restSpecialtyMockMvc.perform(put("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("specialtyTypes"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void updateSpecialtyShouldFailWhenNameIsNull() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

    specialty = specialtyRepository.saveAndFlush(specialty);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty,
        specialtyGroup.getId());

    // When status is null
    specialtyDTO.setName(null);
    restSpecialtyMockMvc.perform(put("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("name"))
        .andExpect(status().isBadRequest());
  }


  @Test
  @Transactional
  public void updateSpecialtyShouldFailWhenNameIsTooLong() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    SpecialtyGroup specialtyGroup = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);

    specialty = specialtyRepository.saveAndFlush(specialty);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(this.specialty,
        specialtyGroup.getId());

    // When status is null
    specialtyDTO.setName(VERY_LONG_STRING);
    restSpecialtyMockMvc.perform(put("/api/specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
        .andExpect(jsonPath("$.fieldErrors[:1].field").value("name"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void deleteSpecialty() throws Exception {
    // Initialize the database
    specialtyRepository.saveAndFlush(specialty);
    int databaseSizeBeforeDelete = specialtyRepository.findAll().size();

    // Get the specialty
    restSpecialtyMockMvc.perform(delete("/api/specialties/{id}", specialty.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Specialty> specialtyList = specialtyRepository.findAll();
    assertThat(specialtyList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void bulkCreateShouldSucceedWhenDataIsValid() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    Specialty anotherSpecialty = new Specialty()
        .name("Name2")
        .intrepidId("IntrepidId2")
        .specialtyCode("specialtyCode")
        .name("name")
        .specialtyTypes(newHashSet(SpecialtyType.SUB_SPECIALTY))
        .college("a college");

    int databaseSizeBeforeBulkCreate = specialtyGroupRepository.findAll().size();
    int expectedDatabaseSizeAfterBulkCreate = databaseSizeBeforeBulkCreate + 2;

    SpecialtyGroup savedSpecialtyGroup = specialtyGroupRepository
        .saveAndFlush(specialtyGroupEntity);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        savedSpecialtyGroup.getId());
    SpecialtyDTO specialtyDTO1 = linkSpecialtyToSpecialtyGroup(anotherSpecialty,
        savedSpecialtyGroup.getId());

    List<SpecialtyDTO> payload = Lists.newArrayList(specialtyDTO, specialtyDTO1);
    restSpecialtyMockMvc.perform(post("/api/bulk-specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk());

    // Validate that both Specialties are in the database
    List<Specialty> specialties = specialtyRepository.findAll();
    assertThat(specialties).hasSize(expectedDatabaseSizeAfterBulkCreate);
  }

  @Test
  @Transactional
  public void bulkCreateShouldFailWhenDataHasAtLeastOneInvalidDto() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    Specialty anotherSpecialty = new Specialty()
        .name("Name2")
        .intrepidId("IntrepidId2")
        .specialtyCode("specialtyCode")
        .name("name")
        .specialtyTypes(newHashSet(SpecialtyType.SUB_SPECIALTY))
        .college("a college");

    //set id to make this specialty invalid for creation
    anotherSpecialty.setId(123456L);

    int expectedDatabaseSizeAfterBulkCreate = specialtyRepository.findAll().size();

    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        specialtyGroupEntity.getId());
    SpecialtyDTO specialtyDTO1 = linkSpecialtyToSpecialtyGroup(anotherSpecialty,
        specialtyGroupEntity.getId());

    List<SpecialtyDTO> payload = Lists.newArrayList(specialtyDTO, specialtyDTO1);
    restSpecialtyMockMvc.perform(post("/api/bulk-specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both specialties are in the database
    List<Specialty> specialties = specialtyRepository.findAll();
    assertThat(specialties).hasSize(expectedDatabaseSizeAfterBulkCreate);
  }

  @Test
  @Transactional
  public void bulkUpdateShouldSucceedWhenDataIsValid() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    Specialty anotherSpecialty = new Specialty()
        .name("Name2")
        .intrepidId("IntrepidId2")
        .specialtyCode("specialtyCode")
        .name("name")
        .specialtyTypes(newHashSet(SpecialtyType.SUB_SPECIALTY))
        .college("a college");

    specialtyGroupEntity = specialtyGroupRepository.saveAndFlush(specialtyGroupEntity);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        specialtyGroupEntity.getId());
    SpecialtyDTO specialtyDTO1 = linkSpecialtyToSpecialtyGroup(anotherSpecialty,
        specialtyGroupEntity.getId());

    //ensure specialty is in the database before an update
    Specialty savedSpecialty = specialtyRepository.saveAndFlush(specialty);
    Specialty anotherSavedSpecialty = specialtyRepository.saveAndFlush(anotherSpecialty);

    //set the ids for the Dtos
    specialtyDTO.setId(savedSpecialty.getId());
    specialtyDTO1.setId(anotherSavedSpecialty.getId());

    int expectedDatabaseSizeAfterBulkCreate = specialtyRepository.findAll().size();

    List<SpecialtyDTO> payload = Lists.newArrayList(specialtyDTO, specialtyDTO1);
    restSpecialtyMockMvc.perform(put("/api/bulk-specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isOk());

    // Validate that both Specialties are still in the database
    List<Specialty> specialties = specialtyRepository.findAll();
    assertThat(specialties).hasSize(expectedDatabaseSizeAfterBulkCreate);
  }

  @Test
  @Transactional
  public void bulkUpdateShouldFailWhenDataHasAtLeastOneInvalidDto() throws Exception {
    SpecialtyGroup specialtyGroupEntity = createSpecialtyGroupEntity();
    Specialty anotherSpecialty = new Specialty()
        .name("Name2")
        .intrepidId("IntrepidId2")
        .specialtyCode("specialtyCode")
        .name("name")
        .specialtyTypes(newHashSet(SpecialtyType.SUB_SPECIALTY))
        .college("a college");

    specialtyGroupEntity = specialtyGroupRepository.save(specialtyGroupEntity);
    SpecialtyDTO specialtyDTO = linkSpecialtyToSpecialtyGroup(specialty,
        specialtyGroupEntity.getId());
    SpecialtyDTO specialtyDTO1 = linkSpecialtyToSpecialtyGroup(anotherSpecialty,
        specialtyGroupEntity.getId());

    //ensure curricula is in the database before an update
    Specialty savedSpecialty = specialtyRepository.saveAndFlush(specialty);
    specialtyRepository.saveAndFlush(anotherSpecialty);

    //set the ids for the Dtos
    specialtyDTO.setId(savedSpecialty.getId());
    //make this specialty invalid
    specialtyDTO1.setId(null);

    int expectedDatabaseSizeAfterBulkCreate = specialtyRepository.findAll().size();

    List<SpecialtyDTO> payload = Lists.newArrayList(specialtyDTO, specialtyDTO1);
    restSpecialtyMockMvc.perform(put("/api/bulk-specialties")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(payload)))
        .andExpect(status().isBadRequest());

    // Validate that both Specialty are still in the database
    List<Specialty> specialties = specialtyRepository.findAll();
    assertThat(specialties).hasSize(expectedDatabaseSizeAfterBulkCreate);
  }

  private SpecialtyDTO linkSpecialtyToSpecialtyGroup(Specialty specialty, Long specialtyGroupId) {
    // Create the specialty
    SpecialtyDTO createdSpecialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(specialty);
    // link the specialty to an existing specialty group
    SpecialtyGroupDTO specialtyGroupDTO = new SpecialtyGroupDTO();
    specialtyGroupDTO.setId(specialtyGroupId);
    createdSpecialtyDTO.setSpecialtyGroup(specialtyGroupDTO);
    return createdSpecialtyDTO;
  }
}
