package com.transformuk.hee.tis.tcs.service.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.PlacementEsrEventDto;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrEventDto;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PostValidator;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Test class created to do integration tests on the controller layer only while mocking everything
 * else
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostResourceTest2 {

  public static final String POST_DTO_NAME = "PostDTO";
  public static final String SPECIAL_CHARACTERS = "#%$^&**(";
  public static final String RECONCILED_FILENAME = "reconciled filename";
  public static final long RECONCILED_POST_ID = 1111L;
  public static final long RECONCILED_POSITION_ID = 2222L;
  public static final long RECONCILED_POSITION_NUMBER = 4444L;

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
  @Captor
  private ArgumentCaptor<PostEsrEventDto> postEsrReconciledDtoArgumentCaptor;
  private PostEsrEventDto postEsrReconciledDto;

  @Before
  public void setup() {
    PostResource postResource = new PostResource(postService, postValidator,
        placementViewRepository, placementViewDecorator,
        placementViewMapper, placementService, placementSummaryDecorator);
    this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");

    postDTO = new PostDTO();
    postDTO.setStatus(Status.CURRENT);
    postDTO.setOwner("Owner");

    setupPostEsrReconciledDto();
  }

  private void setupPostEsrReconciledDto() {
    postEsrReconciledDto = new PostEsrEventDto();
    postEsrReconciledDto.setReconciledAt(new Date(111L));
    postEsrReconciledDto.setFilename(RECONCILED_FILENAME);
    postEsrReconciledDto.setPostId(RECONCILED_POST_ID);
    postEsrReconciledDto.setPositionId(RECONCILED_POSITION_ID);
    postEsrReconciledDto.setPositionNumber(RECONCILED_POSITION_NUMBER);
  }

  @Test
  public void createPostShouldReturnBadRequestWhenPostIsNotValid() throws Exception {
    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(postDTO, POST_DTO_NAME);
    doThrow(new MethodArgumentNotValidException(null, bindingResult)).when(postValidator)
        .validate(postDTO);

    // Create the Post
    restPostMockMvc.perform(post("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void createPostShouldReturnBadRequestWhenIdIsNotNull() throws Exception {
    postDTO.setId(1L);
    // Create the Post
    restPostMockMvc.perform(post("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
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
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isCreated())
        .andExpect(header().string("location", "/api/posts/1"))
        .andExpect(jsonPath("$.id").value(1));

    PostDTO capturedPost = postDTOArgumentCaptor.getValue();
    Assert.assertEquals(postDTO, capturedPost);
  }

  @Test
  public void createPostShouldReturnOkWhenPostsNationalPostNumberContainsSpecialChars()
      throws Exception {
    postDTO.setNationalPostNumber(SPECIAL_CHARACTERS);

    PostDTO savedPostDTO = new PostDTO();
    savedPostDTO.setId(1L);
    savedPostDTO.setNationalPostNumber(SPECIAL_CHARACTERS);
    when(postService.save(postDTOArgumentCaptor.capture())).thenReturn(savedPostDTO);

    // Create the Post
    restPostMockMvc.perform(post("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
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
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updatePostShouldReturnBaRequestWhenFailingValidation() throws Exception {
    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(postDTO, POST_DTO_NAME);
    doThrow(new MethodArgumentNotValidException(null, bindingResult)).when(postValidator)
        .validate(postDTO);

    restPostMockMvc.perform(put("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updatePostShouldReturnOkWhenPostIsUpdated() throws Exception {
    postDTO.setId(1L);
    PostDTO updatedPost = new PostDTO().id(1L);

    when(postService.update(postDTO)).thenReturn(updatedPost);

    restPostMockMvc.perform(put("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"));
  }

  @Test
  public void updatePostShouldReturnUnauth() throws Exception {
    long postId = 1L;
    postDTO.setId(postId);

    doNothing().when(postValidator).validate(postDTO);
    doThrow(new AccessUnauthorisedException("")).when(postService)
        .canLoggedInUserViewOrAmend(postId);

    restPostMockMvc.perform(put("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
  }

  @Test
  public void getPostShouldReturnUnauthWhenUserNotPartOfSameTrust() throws Exception {
    long postId = 1L;

    doThrow(new AccessUnauthorisedException("")).when(postService)
        .canLoggedInUserViewOrAmend(postId);

    restPostMockMvc.perform(get("/api/posts/{id}", postId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
    verify(postService, never()).findOne(any());
  }

  @Test
  public void getPostPlacementShouldReturnUnauthWhenUserNotPartOfSameTrust() throws Exception {
    long personId = 1L;

    doThrow(new AccessUnauthorisedException("")).when(postService)
        .canLoggedInUserViewOrAmend(personId);

    restPostMockMvc.perform(get("/api/posts/{id}/placements", personId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
    verify(placementViewRepository, never()).findAllByPostIdOrderByDateToDesc(any());
    verify(placementViewDecorator, never()).decorate(any());
    verify(placementViewMapper, never()).placementViewsToPlacementViewDTOs(any());
  }

  @Test
  public void getPostPlacementShouldReturnUnauthWhenUserNotPartOfSameTrustNew() throws Exception {
    long postId = 1L;

    doThrow(new AccessUnauthorisedException("")).when(postService)
        .canLoggedInUserViewOrAmend(postId);

    restPostMockMvc.perform(get("/api/posts/{postId}/placements/new", postId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
    verify(placementService, never()).getPlacementForPost(any());
    verify(placementSummaryDecorator, never()).decorate(any());
  }

  @Test
  public void deletePostShouldReturnUnauthWhenUserNotPartOfSameTrust() throws Exception {
    long personId = 1L;
    postDTO.setId(personId);

    doThrow(new AccessUnauthorisedException("")).when(postService)
        .canLoggedInUserViewOrAmend(personId);

    restPostMockMvc.perform(delete("/api/posts/{id}", personId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("error.accessDenied"));
    verify(postService, never()).delete(any());
  }

  @Test
  public void updatePostShouldFailWhenThereAreMultiplePrimarySpecialtiesAttached()
      throws Exception {

    postDTO.setId(1L);

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.add(new FieldError(POST_DTO_NAME, "specialties",
        String.format("Only one Specialty of type %s allowed", PostSpecialtyType.PRIMARY)));
    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(postDTO, POST_DTO_NAME);
    fieldErrors.forEach(bindingResult::addError);
    Method method = PostValidator.class.getMethods()[0];

    doThrow(new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult))
        .when(
            postValidator).validate(any(PostDTO.class));

    restPostMockMvc.perform(put("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(postDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[0].field").value("specialties"))
        .andExpect(jsonPath("$.fieldErrors[0].message").value(StringContains.
            containsString("Only one Specialty of type PRIMARY allowed")));
  }

  @Test
  public void getAllPostsForProgrammeShouldReturnFoundDtos() throws Exception {
    long programmeId = 1L;
    String programmeName = "PROGRAMME NAME";
    long postId = 2L;
    String postNpn = "NPN";

    ProgrammeDTO programmeDTO = new ProgrammeDTO();
    programmeDTO.setId(programmeId);
    programmeDTO.setProgrammeName(programmeName);

    PostDTO postDTO = new PostDTO();
    postDTO.setId(postId);
    postDTO.setNationalPostNumber(postNpn);
    postDTO.setProgrammes(Sets.newHashSet(programmeDTO));

    List<PostDTO> expectedList = Lists.newArrayList(postDTO);

    when(postService.findPostsForProgrammeIdAndNpn(programmeId, StringUtils.EMPTY))
        .thenReturn(expectedList);

    restPostMockMvc.perform(get("/api/programme/{id}/posts", programmeId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
        .andExpect(jsonPath("$[0].id").value(postId))
        .andExpect(jsonPath("$[0].nationalPostNumber").value(postNpn))
        .andExpect(jsonPath("$[0].programmes[0].id").value(programmeId))
        .andExpect(jsonPath("$[0].programmes[0].programmeName").value(programmeName));

    verify(postService).findPostsForProgrammeIdAndNpn(programmeId, StringUtils.EMPTY);
  }

  @Test
  public void getAllPostsForProgrammeShouldReturnFoundDtosWithNpnSearch() throws Exception {
    long programmeId = 1L;
    String programmeName = "PROGRAMME NAME";
    long postId = 2L;
    String postNpn = "NPN";

    ProgrammeDTO programmeDTO = new ProgrammeDTO();
    programmeDTO.setId(programmeId);
    programmeDTO.setProgrammeName(programmeName);

    PostDTO postDTO = new PostDTO();
    postDTO.setId(postId);
    postDTO.setNationalPostNumber(postNpn);
    postDTO.setProgrammes(Sets.newHashSet(programmeDTO));

    List<PostDTO> expectedList = Lists.newArrayList(postDTO);

    when(postService.findPostsForProgrammeIdAndNpn(programmeId, postNpn)).thenReturn(expectedList);

    restPostMockMvc.perform(get("/api/programme/{id}/posts?npn=NPN", programmeId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON.toString()))
        .andExpect(jsonPath("$[0].id").value(postId))
        .andExpect(jsonPath("$[0].nationalPostNumber").value(postNpn))
        .andExpect(jsonPath("$[0].programmes[0].id").value(programmeId))
        .andExpect(jsonPath("$[0].programmes[0].programmeName").value(programmeName));

    verify(postService).findPostsForProgrammeIdAndNpn(programmeId, postNpn);
  }

  @Test
  public void markPostAsEsrMatchedShouldCallServiceToMarkItAsMatched() throws Exception {

    PostEsrEvent newPostEvent = new PostEsrEvent();
    when(postService.markPostAsEsrMatched(Mockito.eq(RECONCILED_POST_ID), postEsrReconciledDtoArgumentCaptor
        .capture()))
        .thenReturn(Optional.of(newPostEvent));

    restPostMockMvc.perform(post("/api/posts/{postId}/esr-matched", RECONCILED_POST_ID)
            .content(new ObjectMapper().writeValueAsBytes(postEsrReconciledDto))
            .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    PostEsrEventDto capturedPayload = postEsrReconciledDtoArgumentCaptor.getValue();
    Assert.assertEquals(postEsrReconciledDto.getReconciledAt(), capturedPayload.getReconciledAt());
    Assert.assertEquals(postEsrReconciledDto.getFilename(), capturedPayload.getFilename());
    Assert.assertEquals(postEsrReconciledDto.getPostId(), capturedPayload.getPostId());
    Assert.assertEquals(postEsrReconciledDto.getPositionNumber(), capturedPayload.getPositionNumber());
    Assert.assertEquals(postEsrReconciledDto.getPositionId(), capturedPayload.getPositionId());
  }
}
