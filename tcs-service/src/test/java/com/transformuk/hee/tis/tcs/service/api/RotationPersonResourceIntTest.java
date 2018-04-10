package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.RotationPersonDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.RotationPerson;
import com.transformuk.hee.tis.tcs.service.repository.RotationPersonRepository;
import com.transformuk.hee.tis.tcs.service.service.RotationPersonService;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationPersonMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RotationPersonResource REST controller.
 *
 * @see RotationPersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RotationPersonResourceIntTest {

    private static final Long DEFAULT_PERSON_ID = 1L;
    private static final Long UPDATED_PERSON_ID = 2L;

    private static final Long DEFAULT_ROTATION_ID = 1L;
    private static final Long UPDATED_ROTATION_ID = 2L;

    @Autowired
    private RotationPersonRepository rotationPersonRepository;

    @Autowired
    private RotationPersonMapper rotationPersonMapper;

    @Autowired
    private RotationPersonService rotationPersonService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRotationPersonMockMvc;

    private RotationPerson rotationPerson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RotationPersonResource rotationPersonResource = new RotationPersonResource(rotationPersonService);
        this.restRotationPersonMockMvc = MockMvcBuilders.standaloneSetup(rotationPersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RotationPerson createEntity(EntityManager em) {
        RotationPerson rotationPerson = new RotationPerson()
            .personId(DEFAULT_PERSON_ID)
            .rotationId(DEFAULT_ROTATION_ID);
        return rotationPerson;
    }

    @Before
    public void initTest() {
        rotationPerson = createEntity(em);
    }

    @Test
    @Transactional
    public void createRotationPerson() throws Exception {
        int databaseSizeBeforeCreate = rotationPersonRepository.findAll().size();

        // Create the RotationPerson
        RotationPersonDTO rotationPersonDTO = rotationPersonMapper.toDto(rotationPerson);
        restRotationPersonMockMvc.perform(post("/api/rotation-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rotationPersonDTO)))
            .andExpect(status().isCreated());

        // Validate the RotationPerson in the database
        List<RotationPerson> rotationPersonList = rotationPersonRepository.findAll();
        assertThat(rotationPersonList).hasSize(databaseSizeBeforeCreate + 1);
        RotationPerson testRotationPerson = rotationPersonList.get(rotationPersonList.size() - 1);
        assertThat(testRotationPerson.getPersonId()).isEqualTo(DEFAULT_PERSON_ID);
        assertThat(testRotationPerson.getRotationId()).isEqualTo(DEFAULT_ROTATION_ID);
    }

    @Test
    @Transactional
    public void createRotationPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rotationPersonRepository.findAll().size();

        // Create the RotationPerson with an existing ID
        rotationPerson.setId(1L);
        RotationPersonDTO rotationPersonDTO = rotationPersonMapper.toDto(rotationPerson);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRotationPersonMockMvc.perform(post("/api/rotation-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rotationPersonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RotationPerson in the database
        List<RotationPerson> rotationPersonList = rotationPersonRepository.findAll();
        assertThat(rotationPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRotationPeople() throws Exception {
        // Initialize the database
        rotationPersonRepository.saveAndFlush(rotationPerson);

        // Get all the rotationPersonList
        restRotationPersonMockMvc.perform(get("/api/rotation-people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rotationPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.intValue())))
            .andExpect(jsonPath("$.[*].rotationId").value(hasItem(DEFAULT_ROTATION_ID.intValue())));
    }

    @Test
    @Transactional
    public void getRotationPerson() throws Exception {
        // Initialize the database
        rotationPersonRepository.saveAndFlush(rotationPerson);

        // Get the rotationPerson
        restRotationPersonMockMvc.perform(get("/api/rotation-people/{id}", rotationPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rotationPerson.getId().intValue()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID.intValue()))
            .andExpect(jsonPath("$.rotationId").value(DEFAULT_ROTATION_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRotationPerson() throws Exception {
        // Get the rotationPerson
        restRotationPersonMockMvc.perform(get("/api/rotation-people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRotationPerson() throws Exception {
        // Initialize the database
        rotationPersonRepository.saveAndFlush(rotationPerson);
        int databaseSizeBeforeUpdate = rotationPersonRepository.findAll().size();

        // Update the rotationPerson
        RotationPerson updatedRotationPerson = rotationPersonRepository.findOne(rotationPerson.getId());
        // Disconnect from session so that the updates on updatedRotationPerson are not directly saved in db
        em.detach(updatedRotationPerson);
        updatedRotationPerson
            .personId(UPDATED_PERSON_ID)
            .rotationId(UPDATED_ROTATION_ID);
        RotationPersonDTO rotationPersonDTO = rotationPersonMapper.toDto(updatedRotationPerson);

        restRotationPersonMockMvc.perform(put("/api/rotation-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rotationPersonDTO)))
            .andExpect(status().isOk());

        // Validate the RotationPerson in the database
        List<RotationPerson> rotationPersonList = rotationPersonRepository.findAll();
        assertThat(rotationPersonList).hasSize(databaseSizeBeforeUpdate);
        RotationPerson testRotationPerson = rotationPersonList.get(rotationPersonList.size() - 1);
        assertThat(testRotationPerson.getPersonId()).isEqualTo(UPDATED_PERSON_ID);
        assertThat(testRotationPerson.getRotationId()).isEqualTo(UPDATED_ROTATION_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingRotationPerson() throws Exception {
        int databaseSizeBeforeUpdate = rotationPersonRepository.findAll().size();

        // Create the RotationPerson
        RotationPersonDTO rotationPersonDTO = rotationPersonMapper.toDto(rotationPerson);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRotationPersonMockMvc.perform(put("/api/rotation-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rotationPersonDTO)))
            .andExpect(status().isCreated());

        // Validate the RotationPerson in the database
        List<RotationPerson> rotationPersonList = rotationPersonRepository.findAll();
        assertThat(rotationPersonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRotationPerson() throws Exception {
        // Initialize the database
        rotationPersonRepository.saveAndFlush(rotationPerson);
        int databaseSizeBeforeDelete = rotationPersonRepository.findAll().size();

        // Get the rotationPerson
        restRotationPersonMockMvc.perform(delete("/api/rotation-people/{id}", rotationPerson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RotationPerson> rotationPersonList = rotationPersonRepository.findAll();
        assertThat(rotationPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RotationPerson.class);
        RotationPerson rotationPerson1 = new RotationPerson();
        rotationPerson1.setId(1L);
        RotationPerson rotationPerson2 = new RotationPerson();
        rotationPerson2.setId(rotationPerson1.getId());
        assertThat(rotationPerson1).isEqualTo(rotationPerson2);
        rotationPerson2.setId(2L);
        assertThat(rotationPerson1).isNotEqualTo(rotationPerson2);
        rotationPerson1.setId(null);
        assertThat(rotationPerson1).isNotEqualTo(rotationPerson2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RotationPersonDTO.class);
        RotationPersonDTO rotationPersonDTO1 = new RotationPersonDTO();
        rotationPersonDTO1.setId(1L);
        RotationPersonDTO rotationPersonDTO2 = new RotationPersonDTO();
        assertThat(rotationPersonDTO1).isNotEqualTo(rotationPersonDTO2);
        rotationPersonDTO2.setId(rotationPersonDTO1.getId());
        assertThat(rotationPersonDTO1).isEqualTo(rotationPersonDTO2);
        rotationPersonDTO2.setId(2L);
        assertThat(rotationPersonDTO1).isNotEqualTo(rotationPersonDTO2);
        rotationPersonDTO1.setId(null);
        assertThat(rotationPersonDTO1).isNotEqualTo(rotationPersonDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rotationPersonMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rotationPersonMapper.fromId(null)).isNull();
    }
}
