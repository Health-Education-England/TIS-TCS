package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.RotationPost;
import com.transformuk.hee.tis.tcs.service.repository.RotationPostRepository;
import com.transformuk.hee.tis.tcs.service.service.RotationPostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationPostMapper;

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
 * Test class for the RotationPostResource REST controller.
 *
 * @see RotationPostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RotationPostResourceIntTest {

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Long DEFAULT_ROTATION_ID = 1L;
    private static final Long UPDATED_ROTATION_ID = 2L;

    @Autowired
    private RotationPostRepository rotationPostRepository;

    @Autowired
    private RotationPostMapper rotationPostMapper;

    @Autowired
    private RotationPostService rotationPostService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRotationPostMockMvc;

    private RotationPost rotationPost;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RotationPostResource rotationPostResource = new RotationPostResource(rotationPostService);
        this.restRotationPostMockMvc = MockMvcBuilders.standaloneSetup(rotationPostResource)
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
    public static RotationPost createEntity(EntityManager em) {
        RotationPost rotationPost = new RotationPost()
            .postId(DEFAULT_POST_ID)
            .rotationId(DEFAULT_ROTATION_ID);
        return rotationPost;
    }

    @Before
    public void initTest() {
        rotationPost = createEntity(em);
    }

    @Test
    @Transactional
    public void createRotationPost() throws Exception {
        int databaseSizeBeforeCreate = rotationPostRepository.findAll().size();

        // Create the RotationPost
        RotationPostDTO rotationPostDTO = rotationPostMapper.toDto(rotationPost);
        restRotationPostMockMvc.perform(post("/api/rotation-posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rotationPostDTO)))
            .andExpect(status().isCreated());

        // Validate the RotationPost in the database
        List<RotationPost> rotationPostList = rotationPostRepository.findAll();
        assertThat(rotationPostList).hasSize(databaseSizeBeforeCreate + 1);
        RotationPost testRotationPost = rotationPostList.get(rotationPostList.size() - 1);
        assertThat(testRotationPost.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testRotationPost.getRotationId()).isEqualTo(DEFAULT_ROTATION_ID);
    }

    @Test
    @Transactional
    public void createRotationPostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rotationPostRepository.findAll().size();

        // Create the RotationPost with an existing ID
        rotationPost.setId(1L);
        RotationPostDTO rotationPostDTO = rotationPostMapper.toDto(rotationPost);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRotationPostMockMvc.perform(post("/api/rotation-posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rotationPostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RotationPost in the database
        List<RotationPost> rotationPostList = rotationPostRepository.findAll();
        assertThat(rotationPostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRotationPosts() throws Exception {
        // Initialize the database
        rotationPostRepository.saveAndFlush(rotationPost);

        // Get all the rotationPostList
        restRotationPostMockMvc.perform(get("/api/rotation-posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rotationPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].rotationId").value(hasItem(DEFAULT_ROTATION_ID.intValue())));
    }

    @Test
    @Transactional
    public void getRotationPost() throws Exception {
        // Initialize the database
        rotationPostRepository.saveAndFlush(rotationPost);

        // Get the rotationPost
        restRotationPostMockMvc.perform(get("/api/rotation-posts/{id}", rotationPost.getPostId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rotationPost.getId().intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.rotationId").value(DEFAULT_ROTATION_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRotationPost() throws Exception {
        // Get the rotationPost
        restRotationPostMockMvc.perform(get("/api/rotation-posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRotationPost() throws Exception {
        // Initialize the database
        rotationPostRepository.saveAndFlush(rotationPost);
        int databaseSizeBeforeUpdate = rotationPostRepository.findAll().size();

        // Update the rotationPost
        RotationPost updatedRotationPost = rotationPostRepository.findOne(rotationPost.getId());
        // Disconnect from session so that the updates on updatedRotationPost are not directly saved in db
        em.detach(updatedRotationPost);
        updatedRotationPost
            .postId(UPDATED_POST_ID)
            .rotationId(UPDATED_ROTATION_ID);
        RotationPostDTO rotationPostDTO = rotationPostMapper.toDto(updatedRotationPost);

        restRotationPostMockMvc.perform(put("/api/rotation-posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rotationPostDTO)))
            .andExpect(status().isOk());

        // Validate the RotationPost in the database
        List<RotationPost> rotationPostList = rotationPostRepository.findAll();
        assertThat(rotationPostList).hasSize(databaseSizeBeforeUpdate);
        RotationPost testRotationPost = rotationPostList.get(rotationPostList.size() - 1);
        assertThat(testRotationPost.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testRotationPost.getRotationId()).isEqualTo(UPDATED_ROTATION_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingRotationPost() throws Exception {
        int databaseSizeBeforeUpdate = rotationPostRepository.findAll().size();

        // Create the RotationPost
        RotationPostDTO rotationPostDTO = rotationPostMapper.toDto(rotationPost);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRotationPostMockMvc.perform(put("/api/rotation-posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rotationPostDTO)))
            .andExpect(status().isCreated());

        // Validate the RotationPost in the database
        List<RotationPost> rotationPostList = rotationPostRepository.findAll();
        assertThat(rotationPostList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRotationPost() throws Exception {
        // Initialize the database
        rotationPostRepository.saveAndFlush(rotationPost);
        int databaseSizeBeforeDelete = rotationPostRepository.findAll().size();

        // Get the rotationPost
        restRotationPostMockMvc.perform(delete("/api/rotation-posts/{id}", rotationPost.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RotationPost> rotationPostList = rotationPostRepository.findAll();
        assertThat(rotationPostList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RotationPost.class);
        RotationPost rotationPost1 = new RotationPost();
        rotationPost1.setId(1L);
        RotationPost rotationPost2 = new RotationPost();
        rotationPost2.setId(rotationPost1.getId());
        assertThat(rotationPost1).isEqualTo(rotationPost2);
        rotationPost2.setId(2L);
        assertThat(rotationPost1).isNotEqualTo(rotationPost2);
        rotationPost1.setId(null);
        assertThat(rotationPost1).isNotEqualTo(rotationPost2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RotationPostDTO.class);
        RotationPostDTO rotationPostDTO1 = new RotationPostDTO();
        rotationPostDTO1.setId(1L);
        RotationPostDTO rotationPostDTO2 = new RotationPostDTO();
        assertThat(rotationPostDTO1).isNotEqualTo(rotationPostDTO2);
        rotationPostDTO2.setId(rotationPostDTO1.getId());
        assertThat(rotationPostDTO1).isEqualTo(rotationPostDTO2);
        rotationPostDTO2.setId(2L);
        assertThat(rotationPostDTO1).isNotEqualTo(rotationPostDTO2);
        rotationPostDTO1.setId(null);
        assertThat(rotationPostDTO1).isNotEqualTo(rotationPostDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rotationPostMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rotationPostMapper.fromId(null)).isNull();
    }
}
