package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.CommentSource;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.AsyncReferenceService;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonBasicDetailsRepositoryAccessor;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementDetailsDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PlacementValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.EsrNotificationRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonBasicDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementSupervisorRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.CommentService;
import com.transformuk.hee.tis.tcs.service.service.EsrNotificationService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
  private SpecialtyRepository specialtyRepository;
  @Autowired
  private PersonBasicDetailsRepository personBasicDetailsRepository;
  @Autowired
  private PersonBasicDetailsRepositoryAccessor asyncPersonBasicDetailsRepository;
  @Autowired
  private ContactDetailsRepository contactDetailsRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private PlacementRepository placementRepository;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private EsrNotificationRepository esrNotificationRepository;
  @Autowired
  private PlacementDetailsMapper placementDetailsMapper;
  @Autowired
  private PlacementService placementService;
  @Autowired
  private PlacementValidator placementValidator;
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  @Autowired
  private CommentService commentService;

  private AsyncReferenceService asyncReferenceService;

  @Mock
  private ReferenceService referenceService;

  @Autowired
  private EsrNotificationService esrNotificationService;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private PlacementSupervisorRepository placementSupervisorRepository;

  private PlacementDetailsDecorator placementDetailsDecorator;

  private MockMvc restPlacementCommentMock;

  private PlacementDetails placement;

  private PlacementCommentDTO placementCommentDTO;

  private static final String AUTHOR = "TEST_AUTHOR";
  private static final String BODY = "THIS_IS_A_TEST_BODY";


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
    assertThat(placementComments).hasSize(databaseSizeBeforeCreate+1);

        
    }
}