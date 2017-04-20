package com.transformuk.hee.tis.web.rest;

import com.transformuk.hee.tis.TcsApp;
import com.transformuk.hee.tis.domain.Post;
import com.transformuk.hee.tis.repository.PostRepository;
import com.transformuk.hee.tis.service.PostService;
import com.transformuk.hee.tis.service.dto.PostDTO;
import com.transformuk.hee.tis.service.mapper.PostMapper;
import com.transformuk.hee.tis.web.rest.errors.ExceptionTranslator;
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
 * Test class for the PostResource REST controller.
 *
 * @see PostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcsApp.class)
public class PostResourceIntTest {

	private static final String DEFAULT_NATIONAL_POST_NUMBER = "AAAAAAAAAA";
	private static final String UPDATED_NATIONAL_POST_NUMBER = "BBBBBBBBBB";

	private static final String DEFAULT_STATUS = "AAAAAAAAAA";
	private static final String UPDATED_STATUS = "BBBBBBBBBB";

	private static final String DEFAULT_POST_OWNER = "AAAAAAAAAA";
	private static final String UPDATED_POST_OWNER = "BBBBBBBBBB";

	private static final String DEFAULT_MAIN_SITE_LOCATED = "AAAAAAAAAA";
	private static final String UPDATED_MAIN_SITE_LOCATED = "BBBBBBBBBB";

	private static final String DEFAULT_LEAD_SITE = "AAAAAAAAAA";
	private static final String UPDATED_LEAD_SITE = "BBBBBBBBBB";

	private static final String DEFAULT_EMPLOYING_BODY = "AAAAAAAAAA";
	private static final String UPDATED_EMPLOYING_BODY = "BBBBBBBBBB";

	private static final String DEFAULT_TRAINING_BODY = "AAAAAAAAAA";
	private static final String UPDATED_TRAINING_BODY = "BBBBBBBBBB";

	private static final String DEFAULT_APPROVED_GRADE = "AAAAAAAAAA";
	private static final String UPDATED_APPROVED_GRADE = "BBBBBBBBBB";

	private static final String DEFAULT_POST_SPECIALTY = "AAAAAAAAAA";
	private static final String UPDATED_POST_SPECIALTY = "BBBBBBBBBB";

	private static final Float DEFAULT_FULL_TIME_EQUIVELENT = 1F;
	private static final Float UPDATED_FULL_TIME_EQUIVELENT = 2F;

	private static final String DEFAULT_LEAD_PROVIDER = "AAAAAAAAAA";
	private static final String UPDATED_LEAD_PROVIDER = "BBBBBBBBBB";

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private PostService postService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restPostMockMvc;

	private Post post;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		PostResource postResource = new PostResource(postService);
		this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Post createEntity(EntityManager em) {
		Post post = new Post()
				.nationalPostNumber(DEFAULT_NATIONAL_POST_NUMBER)
				.status(DEFAULT_STATUS)
				.postOwner(DEFAULT_POST_OWNER)
				.mainSiteLocated(DEFAULT_MAIN_SITE_LOCATED)
				.leadSite(DEFAULT_LEAD_SITE)
				.employingBody(DEFAULT_EMPLOYING_BODY)
				.trainingBody(DEFAULT_TRAINING_BODY)
				.approvedGrade(DEFAULT_APPROVED_GRADE)
				.postSpecialty(DEFAULT_POST_SPECIALTY)
				.fullTimeEquivelent(DEFAULT_FULL_TIME_EQUIVELENT)
				.leadProvider(DEFAULT_LEAD_PROVIDER);
		return post;
	}

	@Before
	public void initTest() {
		post = createEntity(em);
	}

	@Test
	@Transactional
	public void createPost() throws Exception {
		int databaseSizeBeforeCreate = postRepository.findAll().size();

		// Create the Post
		PostDTO postDTO = postMapper.postToPostDTO(post);
		restPostMockMvc.perform(post("/api/posts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(postDTO)))
				.andExpect(status().isCreated());

		// Validate the Post in the database
		List<Post> postList = postRepository.findAll();
		assertThat(postList).hasSize(databaseSizeBeforeCreate + 1);
		Post testPost = postList.get(postList.size() - 1);
		assertThat(testPost.getNationalPostNumber()).isEqualTo(DEFAULT_NATIONAL_POST_NUMBER);
		assertThat(testPost.getStatus()).isEqualTo(DEFAULT_STATUS);
		assertThat(testPost.getPostOwner()).isEqualTo(DEFAULT_POST_OWNER);
		assertThat(testPost.getMainSiteLocated()).isEqualTo(DEFAULT_MAIN_SITE_LOCATED);
		assertThat(testPost.getLeadSite()).isEqualTo(DEFAULT_LEAD_SITE);
		assertThat(testPost.getEmployingBody()).isEqualTo(DEFAULT_EMPLOYING_BODY);
		assertThat(testPost.getTrainingBody()).isEqualTo(DEFAULT_TRAINING_BODY);
		assertThat(testPost.getApprovedGrade()).isEqualTo(DEFAULT_APPROVED_GRADE);
		assertThat(testPost.getPostSpecialty()).isEqualTo(DEFAULT_POST_SPECIALTY);
		assertThat(testPost.getFullTimeEquivelent()).isEqualTo(DEFAULT_FULL_TIME_EQUIVELENT);
		assertThat(testPost.getLeadProvider()).isEqualTo(DEFAULT_LEAD_PROVIDER);
	}

	@Test
	@Transactional
	public void createPostWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = postRepository.findAll().size();

		// Create the Post with an existing ID
		post.setId(1L);
		PostDTO postDTO = postMapper.postToPostDTO(post);

		// An entity with an existing ID cannot be created, so this API call must fail
		restPostMockMvc.perform(post("/api/posts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(postDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Post> postList = postRepository.findAll();
		assertThat(postList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllPosts() throws Exception {
		// Initialize the database
		postRepository.saveAndFlush(post);

		// Get all the postList
		restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
				.andExpect(jsonPath("$.[*].nationalPostNumber").value(hasItem(DEFAULT_NATIONAL_POST_NUMBER.toString())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
				.andExpect(jsonPath("$.[*].postOwner").value(hasItem(DEFAULT_POST_OWNER.toString())))
				.andExpect(jsonPath("$.[*].mainSiteLocated").value(hasItem(DEFAULT_MAIN_SITE_LOCATED.toString())))
				.andExpect(jsonPath("$.[*].leadSite").value(hasItem(DEFAULT_LEAD_SITE.toString())))
				.andExpect(jsonPath("$.[*].employingBody").value(hasItem(DEFAULT_EMPLOYING_BODY.toString())))
				.andExpect(jsonPath("$.[*].trainingBody").value(hasItem(DEFAULT_TRAINING_BODY.toString())))
				.andExpect(jsonPath("$.[*].approvedGrade").value(hasItem(DEFAULT_APPROVED_GRADE.toString())))
				.andExpect(jsonPath("$.[*].postSpecialty").value(hasItem(DEFAULT_POST_SPECIALTY.toString())))
				.andExpect(jsonPath("$.[*].fullTimeEquivelent").value(hasItem(DEFAULT_FULL_TIME_EQUIVELENT.doubleValue())))
				.andExpect(jsonPath("$.[*].leadProvider").value(hasItem(DEFAULT_LEAD_PROVIDER.toString())));
	}

	@Test
	@Transactional
	public void getPost() throws Exception {
		// Initialize the database
		postRepository.saveAndFlush(post);

		// Get the post
		restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(post.getId().intValue()))
				.andExpect(jsonPath("$.nationalPostNumber").value(DEFAULT_NATIONAL_POST_NUMBER.toString()))
				.andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
				.andExpect(jsonPath("$.postOwner").value(DEFAULT_POST_OWNER.toString()))
				.andExpect(jsonPath("$.mainSiteLocated").value(DEFAULT_MAIN_SITE_LOCATED.toString()))
				.andExpect(jsonPath("$.leadSite").value(DEFAULT_LEAD_SITE.toString()))
				.andExpect(jsonPath("$.employingBody").value(DEFAULT_EMPLOYING_BODY.toString()))
				.andExpect(jsonPath("$.trainingBody").value(DEFAULT_TRAINING_BODY.toString()))
				.andExpect(jsonPath("$.approvedGrade").value(DEFAULT_APPROVED_GRADE.toString()))
				.andExpect(jsonPath("$.postSpecialty").value(DEFAULT_POST_SPECIALTY.toString()))
				.andExpect(jsonPath("$.fullTimeEquivelent").value(DEFAULT_FULL_TIME_EQUIVELENT.doubleValue()))
				.andExpect(jsonPath("$.leadProvider").value(DEFAULT_LEAD_PROVIDER.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingPost() throws Exception {
		// Get the post
		restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updatePost() throws Exception {
		// Initialize the database
		postRepository.saveAndFlush(post);
		int databaseSizeBeforeUpdate = postRepository.findAll().size();

		// Update the post
		Post updatedPost = postRepository.findOne(post.getId());
		updatedPost
				.nationalPostNumber(UPDATED_NATIONAL_POST_NUMBER)
				.status(UPDATED_STATUS)
				.postOwner(UPDATED_POST_OWNER)
				.mainSiteLocated(UPDATED_MAIN_SITE_LOCATED)
				.leadSite(UPDATED_LEAD_SITE)
				.employingBody(UPDATED_EMPLOYING_BODY)
				.trainingBody(UPDATED_TRAINING_BODY)
				.approvedGrade(UPDATED_APPROVED_GRADE)
				.postSpecialty(UPDATED_POST_SPECIALTY)
				.fullTimeEquivelent(UPDATED_FULL_TIME_EQUIVELENT)
				.leadProvider(UPDATED_LEAD_PROVIDER);
		PostDTO postDTO = postMapper.postToPostDTO(updatedPost);

		restPostMockMvc.perform(put("/api/posts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(postDTO)))
				.andExpect(status().isOk());

		// Validate the Post in the database
		List<Post> postList = postRepository.findAll();
		assertThat(postList).hasSize(databaseSizeBeforeUpdate);
		Post testPost = postList.get(postList.size() - 1);
		assertThat(testPost.getNationalPostNumber()).isEqualTo(UPDATED_NATIONAL_POST_NUMBER);
		assertThat(testPost.getStatus()).isEqualTo(UPDATED_STATUS);
		assertThat(testPost.getPostOwner()).isEqualTo(UPDATED_POST_OWNER);
		assertThat(testPost.getMainSiteLocated()).isEqualTo(UPDATED_MAIN_SITE_LOCATED);
		assertThat(testPost.getLeadSite()).isEqualTo(UPDATED_LEAD_SITE);
		assertThat(testPost.getEmployingBody()).isEqualTo(UPDATED_EMPLOYING_BODY);
		assertThat(testPost.getTrainingBody()).isEqualTo(UPDATED_TRAINING_BODY);
		assertThat(testPost.getApprovedGrade()).isEqualTo(UPDATED_APPROVED_GRADE);
		assertThat(testPost.getPostSpecialty()).isEqualTo(UPDATED_POST_SPECIALTY);
		assertThat(testPost.getFullTimeEquivelent()).isEqualTo(UPDATED_FULL_TIME_EQUIVELENT);
		assertThat(testPost.getLeadProvider()).isEqualTo(UPDATED_LEAD_PROVIDER);
	}

	@Test
	@Transactional
	public void updateNonExistingPost() throws Exception {
		int databaseSizeBeforeUpdate = postRepository.findAll().size();

		// Create the Post
		PostDTO postDTO = postMapper.postToPostDTO(post);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restPostMockMvc.perform(put("/api/posts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(postDTO)))
				.andExpect(status().isCreated());

		// Validate the Post in the database
		List<Post> postList = postRepository.findAll();
		assertThat(postList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deletePost() throws Exception {
		// Initialize the database
		postRepository.saveAndFlush(post);
		int databaseSizeBeforeDelete = postRepository.findAll().size();

		// Get the post
		restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Post> postList = postRepository.findAll();
		assertThat(postList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Post.class);
	}
}
