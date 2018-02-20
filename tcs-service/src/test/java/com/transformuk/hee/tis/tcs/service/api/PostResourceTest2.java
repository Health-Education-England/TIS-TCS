package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PostValidator;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class created to do integration tests on the controller layer only
 * while mocking everything else
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostResourceTest2 {
  public static final String POST_DTO_NAME = "PostDTO";
  public static final String SPECIAL_CHARACTERS = "#%$^&**(";

  @MockBean
  private PostService postService;
  @MockBean
  private PostValidator postValidator;
  @MockBean
  private PlacementViewRepository placementViewRepository;
  @MockBean
  private PlacementViewDecorator placementViewDecorator;
  @MockBean
  private PlacementViewMapper placementViewMapper;
  @MockBean
  private PlacementService placementService;
  @MockBean
  private PlacementSummaryDecorator placementSummaryDecorator;

  private MockMvc restPostMockMvc;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;
  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private PostDTO postDTO;
  @Captor
  private ArgumentCaptor<PostDTO> postDTOArgumentCaptor;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PostResource postResource = new PostResource(postService, postValidator, placementViewRepository, placementViewDecorator,
        placementViewMapper, placementService, placementSummaryDecorator);
    this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");

    postDTO = new PostDTO();
    postDTO.setStatus(Status.CURRENT);
    postDTO.setOwner("Owner");
  }

  @Test
  public void createPostShouldReturnBadRequestWhenPostIsNotValid() throws Exception {
    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(postDTO, POST_DTO_NAME);
    doThrow(new MethodArgumentNotValidException(null, bindingResult)).when(postValidator).validate(postDTO);

    // Create the Post
    restPostMockMvc.perform(post("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void createPostShouldReturnBadRequestWhenIdIsNotNull() throws Exception {
    postDTO.setId(1L);
    // Create the Post
    restPostMockMvc.perform(post("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void createPostShouldReturnOk() throws Exception {
    PostDTO savedPostDTO = new PostDTO();
    savedPostDTO.setId(1L);
    when(postService.save(postDTOArgumentCaptor.capture())).thenReturn(savedPostDTO);

    // Create the Post
    restPostMockMvc.perform(post("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isCreated())
        .andExpect(header().string("location", "/api/posts/1"))
        .andExpect(jsonPath("$.id").value(1));

    PostDTO capturedPost = postDTOArgumentCaptor.getValue();
    Assert.assertEquals(postDTO, capturedPost);
  }

  @Test
  public void createPostShouldReturnOkWhenPostsNationalPostNumberContainsSpecialChars() throws Exception {
    postDTO.setNationalPostNumber(SPECIAL_CHARACTERS);

    PostDTO savedPostDTO = new PostDTO();
    savedPostDTO.setId(1L);
    savedPostDTO.setNationalPostNumber(SPECIAL_CHARACTERS);
    when(postService.save(postDTOArgumentCaptor.capture())).thenReturn(savedPostDTO);

    // Create the Post
    restPostMockMvc.perform(post("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isCreated())
        .andExpect(header().string("location", "/api/posts/1"))
        .andExpect(jsonPath("$.id").value(1));

    PostDTO capturedPost = postDTOArgumentCaptor.getValue();
    Assert.assertEquals(postDTO, capturedPost);
    Assert.assertEquals(SPECIAL_CHARACTERS, capturedPost.getNationalPostNumber());
  }

  @Test
  public void updatePostShouldFailValidationWhenNoIdIsProvided() throws Exception {
    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updatePostShouldReturnBaRequestWhenFailingValidation() throws Exception {
    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(postDTO, POST_DTO_NAME);
    doThrow(new MethodArgumentNotValidException(null, bindingResult)).when(postValidator).validate(postDTO);

    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updatePostShouldReturnOkWhenPostIsUpdated() throws Exception {
    postDTO.setId(1L);
    PostDTO updatedPost = new PostDTO().id(1L);

    when(postService.update(postDTO)).thenReturn(updatedPost);

    restPostMockMvc.perform(put("/api/posts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"));
  }
}