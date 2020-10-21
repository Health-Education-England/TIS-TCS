package com.transformuk.hee.tis.tcs.service.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.CommentSource;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.service.CommentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the PlacementCommentResource REST controller.
 *
 * @see PlacementCommentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PlacementCommentResourceIntTest {

  private static final Long NEW_COMMENT_ID = 1L;
  private static final Long ID_LONG = 1L;
  private static final Long ID_PLACEMENT = 2L;
  private static final String AUTHOR = "TEST_AUTHOR";
  private static final String BODY = "THIS_IS_A_TEST_BODY";
  private static final String SPECIAL_CHARACTERS = "#%$^&**(";
  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;
  @MockBean
  private CommentService commentService;
  @Captor
  private ArgumentCaptor<PlacementCommentDTO> placementCommentDTOArgumentCaptor;
  private MockMvc restPlacementCommentMock;
  private PlacementCommentDTO placementCommentDTO;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static PlacementCommentDTO createPlacementCommentDTO() {
    PlacementCommentDTO placementCommentDTO = new PlacementCommentDTO();
    placementCommentDTO.setAuthor(AUTHOR);
    placementCommentDTO.setBody(BODY);
    placementCommentDTO.setSource(CommentSource.INTREPID);
    return placementCommentDTO;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PlacementCommentResource placementCommentResource = new PlacementCommentResource(
        commentService);
    this.restPlacementCommentMock = MockMvcBuilders.standaloneSetup(placementCommentResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();

    placementCommentDTO = createPlacementCommentDTO();

  }

  @Test
  @Transactional
  public void shouldCreatePlacementComment() throws Exception {
    PlacementCommentDTO placementCommentDTOsaved = new PlacementCommentDTO();
    placementCommentDTOsaved.setId(NEW_COMMENT_ID);
    when(commentService.save(placementCommentDTOArgumentCaptor.capture()))
        .thenReturn(placementCommentDTOsaved);

    restPlacementCommentMock.perform(post("/api/placementComment")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(placementCommentDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(NEW_COMMENT_ID))
        .andExpect(
            header().string("location", "/api/placementComment/" + NEW_COMMENT_ID.toString()));

    PlacementCommentDTO placementCommentDTOArgumentCaptorValue = placementCommentDTOArgumentCaptor
        .getValue();
    Assert.assertEquals(placementCommentDTO, placementCommentDTOArgumentCaptorValue);
  }

  @Test
  @Transactional
  public void shouldNotAllowCreatePlacementIfIdisNotNull() throws Exception {
    placementCommentDTO.setId(ID_LONG);
    restPlacementCommentMock.perform(post("/api/placementComment")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(placementCommentDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void createPlacementShouldReturnOkWhenCommentContainsSpecialCharacters() throws Exception {
    placementCommentDTO.setBody(SPECIAL_CHARACTERS);
    PlacementCommentDTO placementCommentDTOsaved = new PlacementCommentDTO();
    placementCommentDTOsaved.setId(ID_LONG);
    placementCommentDTOsaved.setBody(SPECIAL_CHARACTERS);
    when(commentService.save(placementCommentDTOArgumentCaptor.capture()))
        .thenReturn(placementCommentDTOsaved);

    restPlacementCommentMock.perform(post("/api/placementComment")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(placementCommentDTO)))
        .andExpect(status().isCreated())
        .andExpect(
            header().string("location", "/api/placementComment/" + NEW_COMMENT_ID.toString()))
        .andExpect(jsonPath("$.id").value(NEW_COMMENT_ID));

    PlacementCommentDTO placementCommentDTOcaptured = placementCommentDTOArgumentCaptor.getValue();
    Assert.assertEquals(placementCommentDTO, placementCommentDTOcaptured);
    Assert.assertEquals(SPECIAL_CHARACTERS, placementCommentDTOcaptured.getBody());
  }

  @Test
  @Transactional
  public void shouldGetPlacementCommentById() throws Exception {
    PlacementCommentDTO placementCommentDTOreturned = new PlacementCommentDTO();
    placementCommentDTOreturned.setId(ID_LONG);
    when(commentService.findById(ID_LONG)).thenReturn(placementCommentDTOreturned);

    restPlacementCommentMock.perform(get("/api/placementComment/{id}", ID_LONG)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID_LONG));
  }

  @Test
  @Transactional
  public void shouldGetACommentForAPlacementId() throws Exception {
    PlacementCommentDTO placementCommentDTOreturned = new PlacementCommentDTO();
    placementCommentDTOreturned.setId(ID_LONG);
    when(commentService.findByPlacementId(ID_PLACEMENT)).thenReturn(placementCommentDTOreturned);

    restPlacementCommentMock
        .perform(get("/api/placement/{placementId}/placementComment", ID_PLACEMENT)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID_LONG));
  }

  @Test
  @Transactional
  public void shouldUpdatePlacementComment() throws Exception {
    placementCommentDTO.setId(ID_LONG);
    PlacementCommentDTO placementCommentDTOupdated = new PlacementCommentDTO();
    placementCommentDTOupdated.setId(ID_LONG);

    when(commentService.save(placementCommentDTO)).thenReturn(placementCommentDTOupdated);

    restPlacementCommentMock.perform(put("/api/placementComment")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(placementCommentDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID_LONG));
  }

  @Test
  public void updateShouldReturnBadRequestIfIdIsNull() throws Exception {
    restPlacementCommentMock.perform(put("/api/placementComment")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(placementCommentDTO)))
        .andExpect(status().isBadRequest());
  }
}
