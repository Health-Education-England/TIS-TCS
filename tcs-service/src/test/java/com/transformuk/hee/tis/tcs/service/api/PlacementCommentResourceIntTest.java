package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.CommentSource;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.CommentService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementCommentMapper;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the PlacementCommentResource REST controller.
 *
 * @see PlacementCommentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PlacementCommentResourceIntTest {


  @Autowired
  private PlacementDetailsRepository placementDetailsRepository;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  @Autowired
  private CommentService commentService;
  @Autowired
  private PlacementCommentMapper placementCommentMapper;
  @Autowired
  private EntityManager entityManager;

  private MockMvc restPlacementCommentMock;
  private PlacementCommentDTO placementCommentDTO;

  private static final String AUTHOR = "TEST_AUTHOR";
  private static final String BODY = "THIS_IS_A_TEST_BODY";

  private static final String UPDATED_AUTHOR = "UPDATED_AUTHOR";
  private static final String UPDATED_BODY = "UPDATED_BODY";


  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PlacementCommentDTO createEntity(EntityManager em) {
    PlacementCommentDTO placementCommentDTO = new PlacementCommentDTO();
    placementCommentDTO.setAuthor(AUTHOR);
    placementCommentDTO.setBody(BODY);
    placementCommentDTO.setSource(CommentSource.INTREPID);
    return placementCommentDTO;
  }

  public static Placement createPlacement(EntityManager em) {
    Placement placement = new Placement();
    return placement;
  }


  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PlacementCommentResource placementCommentResource = new PlacementCommentResource(commentService);
    this.restPlacementCommentMock = MockMvcBuilders.standaloneSetup(placementCommentResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    placementCommentDTO = createEntity(entityManager);
  }

  @Test
  @Transactional
  public void shouldCreatePlacementComment() throws Exception {
    int databaseSizeBeforeCreate = commentRepository.findAll().size();
    restPlacementCommentMock.perform(post("/api/placementComment")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementCommentDTO)))
        .andExpect(status().isCreated());
    List<Comment> placementComments = commentRepository.findAll();
    assertThat(placementComments).hasSize(databaseSizeBeforeCreate + 1);
    Comment comment = placementComments.get(placementComments.size() - 1);
    assertThat(comment.getAuthor()).isEqualTo(AUTHOR);
    assertThat(comment.getBody()).isEqualTo(BODY);
    assertThat(comment.getId()).isNotZero();
    assertThat(comment.getId()).isNotNull();
  }

  @Test
  @Transactional
  public void shouldNotAllowCreatePlacementIfIdisNotNull() throws Exception {
    int databaseSizeBeforeCreate = commentRepository.findAll().size();
    placementCommentDTO.setId(1L);
    restPlacementCommentMock.perform(post("/api/placementComment")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementCommentDTO)))
        .andExpect(status().isBadRequest());
    List<Comment> placementComments = commentRepository.findAll();
    assertThat(placementComments).hasSize(databaseSizeBeforeCreate);
  }


  @Test
  @Transactional
  public void shouldGetPlacementCommentById() throws Exception {
    commentRepository.saveAndFlush(placementCommentMapper.toEntity(placementCommentDTO));
    List<Comment> placementComments = commentRepository.findAll();
    Comment comment = placementComments.get(placementComments.size() - 1);
    restPlacementCommentMock.perform(get("/api/placementComment/{id}", comment.getId())
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
        .andExpect(jsonPath("$.author").value(AUTHOR))
        .andExpect(jsonPath("$.body").value(BODY));
  }

  @Test
  @Transactional
  public void shouldGetACommentForAPlacementId() throws Exception {
    // Set up placement comments in the repository
    commentRepository.saveAndFlush(placementCommentMapper.toEntity(placementCommentDTO));
    List<Comment> placementComments = commentRepository.findAll();
    Comment comment = placementComments.get(placementComments.size() - 1);
    Set<Comment> comments = Sets.newHashSet();
    comments.add(comment);

    // Set up a placement and attach the comments
    PlacementDetails placementDetails = new PlacementDetails();
    placementDetails.setComments(comments);
    placementDetailsRepository.saveAndFlush(placementDetails);

    // Add the placement to the comment
    comment.setPlacement(placementDetails);
    commentRepository.saveAndFlush(comment);

    restPlacementCommentMock.perform(get("/api/placement/{placementId}/placementComment", comment.getPlacement().getId())
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
        .andExpect(jsonPath("$.author").value(AUTHOR))
        .andExpect(jsonPath("$.body").value(BODY));
  }

  @Test
  @Transactional
  public void shouldUpdatePlacementComment() throws Exception {
    // Set up placement comment in repository
    commentRepository.saveAndFlush(placementCommentMapper.toEntity(placementCommentDTO));
    List<Comment> comments = commentRepository.findAll();
    Comment comment = comments.get(comments.size() - 1);
    comment.setBody(UPDATED_BODY);
    comment.setAuthor(UPDATED_AUTHOR);
    Long commentId = comment.getId();
    PlacementCommentDTO placementCommentDTO = placementCommentMapper.toDto(comment);
    restPlacementCommentMock.perform(put("/api/placementComment")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(placementCommentDTO)))
        .andExpect(status().isOk());
    Comment result = commentRepository.findOne(commentId);
    assertThat(result.getBody().equals(UPDATED_BODY));
    assertThat(result.getAuthor().equals(UPDATED_AUTHOR));
  }
}