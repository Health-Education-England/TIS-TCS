package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PlacementFunderDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.PlacementFunder;
import com.transformuk.hee.tis.tcs.service.repository.PlacementFunderRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementFunderService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementFunderMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the PlacementFunderResource REST controller.
 *
 * @see PlacementFunderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PlacementFunderResourceIntTest {

  private static final String DEFAULT_LOCAL_OFFICE = "AAAAAAAAAA";
  private static final String UPDATED_LOCAL_OFFICE = "BBBBBBBBBB";

  private static final String DEFAULT_TRUST = "AAAAAAAAAA";
  private static final String UPDATED_TRUST = "BBBBBBBBBB";

  @Autowired
  private PlacementFunderRepository placementFunderRepository;

  @Autowired
  private PlacementFunderMapper placementFunderMapper;

  @Autowired
  private PlacementFunderService placementFunderService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restPlacementFunderMockMvc;

  private PlacementFunder placementFunder;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PlacementFunder createEntity(EntityManager em) {
    PlacementFunder placementFunder = new PlacementFunder()
        .localOffice(DEFAULT_LOCAL_OFFICE)
        .trust(DEFAULT_TRUST);
    return placementFunder;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PlacementFunderResource placementFunderResource = new PlacementFunderResource(placementFunderService);
    this.restPlacementFunderMockMvc = MockMvcBuilders.standaloneSetup(placementFunderResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    placementFunder = createEntity(em);
  }

  @Test
  @Transactional
  public void createPlacementFunder() throws Exception {
    int databaseSizeBeforeCreate = placementFunderRepository.findAll().size();

    // Create the PlacementFunder
    PlacementFunderDTO placementFunderDTO = placementFunderMapper.placementFunderToPlacementFunderDTO(placementFunder);
    restPlacementFunderMockMvc.perform(post("/api/placement-funders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementFunderDTO)))
        .andExpect(status().isCreated());

    // Validate the PlacementFunder in the database
    List<PlacementFunder> placementFunderList = placementFunderRepository.findAll();
    assertThat(placementFunderList).hasSize(databaseSizeBeforeCreate + 1);
    PlacementFunder testPlacementFunder = placementFunderList.get(placementFunderList.size() - 1);
    assertThat(testPlacementFunder.getLocalOffice()).isEqualTo(DEFAULT_LOCAL_OFFICE);
    assertThat(testPlacementFunder.getTrust()).isEqualTo(DEFAULT_TRUST);
  }

  @Test
  @Transactional
  public void createPlacementFunderWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = placementFunderRepository.findAll().size();

    // Create the PlacementFunder with an existing ID
    placementFunder.setId(1L);
    PlacementFunderDTO placementFunderDTO = placementFunderMapper.placementFunderToPlacementFunderDTO(placementFunder);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPlacementFunderMockMvc.perform(post("/api/placement-funders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementFunderDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<PlacementFunder> placementFunderList = placementFunderRepository.findAll();
    assertThat(placementFunderList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllPlacementFunders() throws Exception {
    // Initialize the database
    placementFunderRepository.saveAndFlush(placementFunder);

    // Get all the placementFunderList
    restPlacementFunderMockMvc.perform(get("/api/placement-funders?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(placementFunder.getId().intValue())))
        .andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE.toString())))
        .andExpect(jsonPath("$.[*].trust").value(hasItem(DEFAULT_TRUST.toString())));
  }

  @Test
  @Transactional
  public void getPlacementFunder() throws Exception {
    // Initialize the database
    placementFunderRepository.saveAndFlush(placementFunder);

    // Get the placementFunder
    restPlacementFunderMockMvc.perform(get("/api/placement-funders/{id}", placementFunder.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(placementFunder.getId().intValue()))
        .andExpect(jsonPath("$.localOffice").value(DEFAULT_LOCAL_OFFICE.toString()))
        .andExpect(jsonPath("$.trust").value(DEFAULT_TRUST.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingPlacementFunder() throws Exception {
    // Get the placementFunder
    restPlacementFunderMockMvc.perform(get("/api/placement-funders/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePlacementFunder() throws Exception {
    // Initialize the database
    placementFunderRepository.saveAndFlush(placementFunder);
    int databaseSizeBeforeUpdate = placementFunderRepository.findAll().size();

    // Update the placementFunder
    PlacementFunder updatedPlacementFunder = placementFunderRepository.findOne(placementFunder.getId());
    updatedPlacementFunder
        .localOffice(UPDATED_LOCAL_OFFICE)
        .trust(UPDATED_TRUST);
    PlacementFunderDTO placementFunderDTO = placementFunderMapper.placementFunderToPlacementFunderDTO(updatedPlacementFunder);

    restPlacementFunderMockMvc.perform(put("/api/placement-funders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementFunderDTO)))
        .andExpect(status().isOk());

    // Validate the PlacementFunder in the database
    List<PlacementFunder> placementFunderList = placementFunderRepository.findAll();
    assertThat(placementFunderList).hasSize(databaseSizeBeforeUpdate);
    PlacementFunder testPlacementFunder = placementFunderList.get(placementFunderList.size() - 1);
    assertThat(testPlacementFunder.getLocalOffice()).isEqualTo(UPDATED_LOCAL_OFFICE);
    assertThat(testPlacementFunder.getTrust()).isEqualTo(UPDATED_TRUST);
  }

  @Test
  @Transactional
  public void updateNonExistingPlacementFunder() throws Exception {
    int databaseSizeBeforeUpdate = placementFunderRepository.findAll().size();

    // Create the PlacementFunder
    PlacementFunderDTO placementFunderDTO = placementFunderMapper.placementFunderToPlacementFunderDTO(placementFunder);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPlacementFunderMockMvc.perform(put("/api/placement-funders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementFunderDTO)))
        .andExpect(status().isCreated());

    // Validate the PlacementFunder in the database
    List<PlacementFunder> placementFunderList = placementFunderRepository.findAll();
    assertThat(placementFunderList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deletePlacementFunder() throws Exception {
    // Initialize the database
    placementFunderRepository.saveAndFlush(placementFunder);
    int databaseSizeBeforeDelete = placementFunderRepository.findAll().size();

    // Get the placementFunder
    restPlacementFunderMockMvc.perform(delete("/api/placement-funders/{id}", placementFunder.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<PlacementFunder> placementFunderList = placementFunderRepository.findAll();
    assertThat(placementFunderList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PlacementFunder.class);
  }
}
