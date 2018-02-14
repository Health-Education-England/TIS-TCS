package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.service.api.decorator.AsyncReferenceService;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PostView;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostGradeRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSiteRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

  private static final String SLASH = "/";
  private static final String LOCAL_OFFICE_ABBR = "NTH";
  private static final String SITE_CODE = "RTD01";
  private static final String SPECIALTY_CODE = "007";
  private static final String GRADE_ABBR = "STR";
  private static final String UNIQUE_NUMBER = "006";
  private static final String UNIQUE_NUMBER_PLUS_1 = "007";

  private static final String CURRENT_NATIONAL_POST_NUMBER = LOCAL_OFFICE_ABBR + SLASH + SITE_CODE + SLASH +
      SPECIALTY_CODE + SLASH + GRADE_ABBR + SLASH + UNIQUE_NUMBER;

  private static final Long SITE_ID = 12345L;
  private static final long POST_ID = 1L;
  private static final long GRADE_ID = 2L;
  public static final String MILITARY_SUFFIX = "M";
  @Spy
  @InjectMocks
  private PostServiceImpl testObj;

  @Mock
  private PostRepository postRepositoryMock;
  @Mock
  private PostViewRepository postViewRepositoryMock;
  @Mock
  private PostGradeRepository postGradeRepositoryMock;
  @Mock
  private PostSiteRepository postSiteRepositoryMock;
  @Mock
  private PostSpecialtyRepository postSpecialtyRepositoryMock;
  @Mock
  private ProgrammeRepository programmeRepositoryMock;
  @Mock
  private SpecialtyRepository specialtyRepositoryMock;
  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private PostMapper postMapperMock;
  @Mock
  private PostViewMapper postViewMapperMock;
  @Mock
  private PostViewDecorator postViewDecorator;

  @Mock
  private PostDTO postDTOMock1, postDTOMock2, postMappedDTOMock1, postMappedDTOMock2;

  @Mock
  private PostViewDTO postViewDTOMock1;

  @Mock
  private Post postMock1, postMock2, postSaveMock1, postSaveMock2;

  @Mock
  private PostView postViewMock1;

  @Mock
  private Pageable pageableMock;

  @Captor
  private ArgumentCaptor<Set<PostSpecialty>> postSpecialtyArgumentCaptor;
  @Mock
  private PostGradeDTO postGradeDTOMock;
  @Mock
  private PostSpecialtyDTO postSpecialtyDTOMock;
  @Mock
  private SpecialtyDTO specialtyDTOMock;
  @Mock
  private AsyncReferenceService asyncReferenceService;
  @Mock
  private PostSpecialty postSpecialtyMock;
  @Mock
  private Specialty specialtyMock;
  @Mock
  private PostGrade postGradeMock;


  @Test
  public void saveShouldSavePost() {

    when(postMapperMock.postDTOToPost(postDTOMock1)).thenReturn(postMock1);
    when(postRepositoryMock.save(postMock1)).thenReturn(postSaveMock1);
    when(postMapperMock.postToPostDTO(postSaveMock1)).thenReturn(postMappedDTOMock1);

    PostDTO result = testObj.save(postDTOMock1);

    Assert.assertEquals(postMappedDTOMock1, result);

    verify(postMapperMock).postDTOToPost(postDTOMock1);
    verify(postRepositoryMock).save(postMock1);
    verify(postMapperMock).postToPostDTO(postSaveMock1);
  }

  @Test
  public void saveShouldSaveListOfPosts() {
    List<PostDTO> postDTOsList = Lists.newArrayList(postDTOMock1, postDTOMock2);
    List<Post> postList = Lists.newArrayList(postMock1, postMock2);
    List<Post> savedPosts = Lists.newArrayList(postSaveMock1, postSaveMock2);
    List<PostDTO> savedPostDTOs = Lists.newArrayList(postMappedDTOMock1, postMappedDTOMock2);

    when(postMapperMock.postDTOsToPosts(postDTOsList)).thenReturn(postList);
    when(postRepositoryMock.save(postList)).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(savedPostDTOs);

    List<PostDTO> results = testObj.save(postDTOsList);

    Assert.assertSame(savedPostDTOs, results);

    verify(postMapperMock).postDTOsToPosts(postDTOsList);
    verify(postRepositoryMock).save(postList);
    verify(postMapperMock).postsToPostDTOs(savedPosts);
  }


  @Test
  public void updateShouldSavePostAndRefreshLinkedEntities() {
    Set<PostGrade> grades = Sets.newHashSet();
    Set<PostSite> sites = Sets.newHashSet();
    Set<PostSpecialty> specialties = Sets.newHashSet();

    when(postDTOMock1.getId()).thenReturn(1L);
    when(postMock1.getGrades()).thenReturn(grades);
    when(postMock1.getSites()).thenReturn(sites);
    when(postMock1.getSpecialties()).thenReturn(specialties);
    when(postRepositoryMock.findOne(1L)).thenReturn(postMock1);

    when(postMapperMock.postDTOToPost(postDTOMock1)).thenReturn(postMock1);
    when(postRepositoryMock.save(postMock1)).thenReturn(postSaveMock1);
    when(postMapperMock.postToPostDTO(postSaveMock1)).thenReturn(postMappedDTOMock1);

    PostDTO result = testObj.update(postDTOMock1);

    Assert.assertEquals(postMappedDTOMock1, result);

    verify(postRepositoryMock).findOne(1L);
    verify(postMapperMock).postDTOToPost(postDTOMock1);
    verify(postRepositoryMock).save(postMock1);
    verify(postMapperMock).postToPostDTO(postSaveMock1);

    verify(postGradeRepositoryMock).delete(grades);
    verify(postSiteRepositoryMock).delete(sites);
    verify(postSpecialtyRepositoryMock).delete(specialties);
  }

  @Test
  public void findAllShouldRetrieveAllInstances() {
    List<PostView> posts = Lists.newArrayList(postViewMock1);
    List<PostViewDTO> mappedPosts = Lists.newArrayList(postViewDTOMock1);
    Page<PostView> page = new PageImpl<>(posts);
    when(postViewRepositoryMock.findAll(pageableMock)).thenReturn(page);
    when(postViewMapperMock.postViewToPostViewDTO(postViewMock1)).thenReturn(postViewDTOMock1);

    Page<PostViewDTO> result = testObj.findAll(pageableMock);

    Assert.assertEquals(1, result.getTotalPages());
    Assert.assertEquals(mappedPosts, result.getContent());

    verify(postViewRepositoryMock).findAll(pageableMock);
    verify(postViewMapperMock).postViewToPostViewDTO(postViewMock1);
  }

  @Test
  public void findOneShouldRetrieveOneInstanceById() {
    when(postRepositoryMock.findOne(1L)).thenReturn(postMock1);
    when(postMapperMock.postToPostDTO(postMock1)).thenReturn(postDTOMock1);

    PostDTO result = testObj.findOne(1L);

    Assert.assertEquals(postDTOMock1, result);

    verify(postRepositoryMock).findOne(1L);
    verify(postMapperMock).postToPostDTO(postMock1);
  }

  @Test
  public void deleteShouldDeleteOneInstanceById() {
    testObj.delete(1L);

    verify(postRepositoryMock).delete(1L);
  }

  @Test
  public void patchOldNewPostsShouldApplyProvidedPosts() {
    List<Long> postIds = Lists.newArrayList(1L, 2L, 3L);
    List<String> intrepidIds = Lists.newArrayList("intrepid1", "intrepid2", "intrepid3");

    PostDTO currentPostDTO = new PostDTO();
    currentPostDTO.id(postIds.get(0)).intrepidId(intrepidIds.get(0));
    PostDTO oldPostDTO = new PostDTO();
    oldPostDTO.id(postIds.get(1)).intrepidId(intrepidIds.get(1));
    PostDTO newPostDTO = new PostDTO();
    newPostDTO.id(postIds.get(2)).intrepidId(intrepidIds.get(2));
    currentPostDTO.oldPost(oldPostDTO).newPost(newPostDTO);

    List<PostDTO> postDtos = Lists.newArrayList(currentPostDTO);

    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));

    Post oldPost = new Post();
    oldPost.setId(postIds.get(1));
    oldPost.setIntrepidId(intrepidIds.get(1));

    Post newPost = new Post();
    newPost.setId(postIds.get(2));
    newPost.setIntrepidId(intrepidIds.get(2));

    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost, oldPost, newPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);

    PostDTO expectedDTO = new PostDTO();
    expectedDTO.id(currentPostDTO.getId()).intrepidId(currentPost.getIntrepidId()).oldPost(oldPostDTO).newPost(newPostDTO);
    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);

    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds))).thenReturn(postsFromRepository);
    when(postRepositoryMock.save(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);

    List<PostDTO> result = testObj.patchOldNewPosts(postDtos);

    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postRepositoryMock).save(Lists.newArrayList(currentPost));
    verify(postMapperMock).postsToPostDTOs(savedPosts);

    Assert.assertSame(transformedPosts, result);
    Assert.assertEquals(expectedDTO, result.get(0));
    Assert.assertEquals(oldPostDTO, result.get(0).getOldPost());
    Assert.assertEquals(newPostDTO, result.get(0).getNewPost());
  }

  @Test
  public void patchPostSites() {
    List<Long> postIds = Lists.newArrayList(1L);
    List<String> intrepidIds = Lists.newArrayList("intrepid1");

    PostSiteDTO postSiteDTO = new PostSiteDTO();
    postSiteDTO.setPostSiteType(PostSiteType.PRIMARY);
    postSiteDTO.setSiteId(SITE_ID);

    PostDTO sendPostData = new PostDTO();
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0)).setSites(Sets.newHashSet(postSiteDTO));

    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));

    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);

    PostDTO expectedDTO = new PostDTO();
    HashSet<PostSiteDTO> expectedSites = Sets.newHashSet(postSiteDTO);
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId()).sites(expectedSites);

    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);

    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds))).thenReturn(postsFromRepository);
    when(postRepositoryMock.save(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);

    List<PostDTO> result = testObj.patchPostSites(Lists.newArrayList(sendPostData));

    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postSiteRepositoryMock).save(anyList());
    verify(postMapperMock).postsToPostDTOs(savedPosts);

    Assert.assertSame(transformedPosts, result);
    Assert.assertEquals(expectedDTO, result.get(0));
    Assert.assertEquals(expectedSites, result.get(0).getSites());
  }

  @Test
  public void pathPostGrades() {
    List<Long> postIds = Lists.newArrayList(1L);
    List<String> intrepidIds = Lists.newArrayList("intrepid1");

    PostGradeDTO postGradeDTO = new PostGradeDTO();
    postGradeDTO.setPostGradeType(PostGradeType.APPROVED);
    postGradeDTO.setGradeId(SITE_ID);

    PostDTO sendPostData = new PostDTO();
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0)).grades(Sets.newHashSet(postGradeDTO));

    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));

    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);

    PostDTO expectedDTO = new PostDTO();
    HashSet<PostGradeDTO> expectedGrades = Sets.newHashSet(postGradeDTO);
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId()).grades(expectedGrades);

    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);

    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds))).thenReturn(postsFromRepository);
    when(postRepositoryMock.save(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);

    List<PostDTO> result = testObj.patchPostGrades(Lists.newArrayList(sendPostData));

    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postRepositoryMock).save(Lists.newArrayList(currentPost));
    verify(postMapperMock).postsToPostDTOs(savedPosts);

    Assert.assertSame(transformedPosts, result);
    Assert.assertEquals(expectedDTO, result.get(0));
    Assert.assertEquals(expectedGrades, result.get(0).getGrades());
  }

  @Test
  public void pathPostProgrammes() {
    List<Long> postIds = Lists.newArrayList(1L);
    List<String> intrepidIds = Lists.newArrayList("intrepid1");

    ProgrammeDTO programmeDTO = new ProgrammeDTO();
    programmeDTO.setId(1L);
    programmeDTO.setIntrepidId("programme intrepid id");

    Programme programme = new Programme();
    programme.setId(1L);
    programme.setIntrepidId("programme intrepid id");

    PostDTO sendPostData = new PostDTO();
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0)).programmes(programmeDTO);

    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));

    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);

    PostDTO expectedDTO = new PostDTO();
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId()).programmes(programmeDTO);

    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    Set<Long> programmeIds = Sets.newHashSet(1L);
    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds))).thenReturn(postsFromRepository);
    when(programmeRepositoryMock.findAll(programmeIds)).thenReturn(Lists.newArrayList(programme));
    when(postRepositoryMock.save(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);

    List<PostDTO> result = testObj.patchPostProgrammes(Lists.newArrayList(sendPostData));

    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(programmeRepositoryMock).findAll(programmeIds);
    verify(postRepositoryMock).save(Lists.newArrayList(currentPost));
    verify(postMapperMock).postsToPostDTOs(savedPosts);

    Assert.assertSame(transformedPosts, result);
    Assert.assertEquals(expectedDTO, result.get(0));
    Assert.assertEquals(programmeDTO, result.get(0).getProgrammes());
  }


  @Test
  public void patchPostSpecialties() {
    List<Long> postIds = Lists.newArrayList(1L);
    List<String> intrepidIds = Lists.newArrayList("intrepid1");

    PostSpecialtyDTO newPostSpecialtyDTO = new PostSpecialtyDTO();
    newPostSpecialtyDTO.setPostSpecialtyType(PostSpecialtyType.OTHER);
    SpecialtyDTO newSpecialtyDTO = new SpecialtyDTO();
    newSpecialtyDTO.setId(2L);
    newPostSpecialtyDTO.setSpecialty(newSpecialtyDTO);

    PostDTO postDTOToSend = new PostDTO();
    postDTOToSend.id(postIds.get(0)).intrepidId(intrepidIds.get(0)).specialties(Sets.newHashSet(newPostSpecialtyDTO));

    Specialty primarySpecialty = new Specialty();
    primarySpecialty.setId(2L);
    PostSpecialty primaryPostSpecialty = new PostSpecialty();
    primaryPostSpecialty.setPostSpecialtyType(PostSpecialtyType.PRIMARY);
    primaryPostSpecialty.setSpecialty(primarySpecialty);

    Post postInRepository = new Post();
    postInRepository.setId(postIds.get(0));
    postInRepository.setIntrepidId(intrepidIds.get(0));
    postInRepository.setSpecialties(Sets.newHashSet(primaryPostSpecialty));

    Set<Post> postsFromRepository = Sets.newHashSet(postInRepository);
    List<Post> savedPosts = Lists.newArrayList(postInRepository);

    PostDTO expectedDTO = new PostDTO();
    Set<PostSpecialtyDTO> expectedSpecialties = Sets.newHashSet(newPostSpecialtyDTO);
    expectedDTO.id(postDTOToSend.getId()).intrepidId(postInRepository.getIntrepidId()).specialties(expectedSpecialties);

    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    Set<Long> specialtyIds = Sets.newHashSet(2L);


    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds))).thenReturn(postsFromRepository);
    when(specialtyRepositoryMock.findAll(specialtyIds)).thenReturn(Lists.newArrayList(primarySpecialty));
    when(postRepositoryMock.save(Lists.newArrayList(postInRepository))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);

    List<PostDTO> result = testObj.patchPostSpecialties(Lists.newArrayList(postDTOToSend));

    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postSpecialtyRepositoryMock).save(postSpecialtyArgumentCaptor.capture());
    verify(postMapperMock).postsToPostDTOs(savedPosts);

    Set<PostSpecialty> postSpecialtyValueSet = postSpecialtyArgumentCaptor.getValue();

    PostSpecialty postSpecialtyValue = null;
    for (PostSpecialty savedLink : postSpecialtyValueSet) {
      if (PostSpecialtyType.OTHER.equals(savedLink.getPostSpecialtyType())) {
        postSpecialtyValue = savedLink;
        break;
      }
    }

    Assert.assertEquals(PostSpecialtyType.OTHER, postSpecialtyValue.getPostSpecialtyType());
    Assert.assertEquals(postInRepository.getId(), postSpecialtyValue.getPost().getId());
    Assert.assertEquals(newPostSpecialtyDTO.getSpecialty().getId(), postSpecialtyValue.getSpecialty().getId());

    Assert.assertSame(transformedPosts, result);
    Assert.assertEquals(expectedDTO, result.get(0));
    Assert.assertEquals(expectedSpecialties, result.get(0).getSpecialties());
  }


  @Test
  public void patchPostPlacements() {
    List<Long> postIds = Lists.newArrayList(1L);
    List<String> intrepidIds = Lists.newArrayList("intrepid1");

    PlacementDTO placementDTO = new PlacementDTO();
    placementDTO.setId(1L);
    placementDTO.setIntrepidId("placement intrepid id");

    Placement placement = new Placement();
    placement.setId(1L);
    placement.setIntrepidId("placement intrepid id");

    PostDTO sendPostData = new PostDTO();
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0)).placementHistory(Sets.newHashSet(placementDTO));

    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));

    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);

    PostDTO expectedDTO = new PostDTO();
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId()).placementHistory(Sets.newHashSet(placementDTO));

    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    Set<String> placementIds = Sets.newHashSet("placement intrepid id");
    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds))).thenReturn(postsFromRepository);
    when(placementRepositoryMock.findByIntrepidIdIn(placementIds)).thenReturn(Sets.newHashSet(placement));
    when(postRepositoryMock.save(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);

    List<PostDTO> result = testObj.patchPostPlacements(Lists.newArrayList(sendPostData));

    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(placementRepositoryMock).findByIntrepidIdIn(placementIds);
    verify(postRepositoryMock).save(Lists.newArrayList(currentPost));
    verify(postMapperMock).postsToPostDTOs(savedPosts);

    Assert.assertSame(transformedPosts, result);
    Assert.assertEquals(expectedDTO, result.get(0));
    Assert.assertEquals(Sets.newHashSet(placementDTO), result.get(0).getPlacementHistory());
  }

  private void wireUpPostEntityAndDTO() {
    when(postGradeDTOMock.getGradeId()).thenReturn(GRADE_ID);
    when(postGradeDTOMock.getPostGradeType()).thenReturn(PostGradeType.APPROVED);
    when(postSpecialtyDTOMock.getSpecialty()).thenReturn(specialtyDTOMock);
    when(postSpecialtyDTOMock.getPostSpecialtyType()).thenReturn(PostSpecialtyType.PRIMARY);
    when(specialtyDTOMock.getSpecialtyCode()).thenReturn(SPECIALTY_CODE);

    when(postDTOMock1.getId()).thenReturn(POST_ID);
    when(postDTOMock1.getGrades()).thenReturn(Sets.newHashSet(postGradeDTOMock));
    when(postDTOMock1.getSpecialties()).thenReturn(Sets.newHashSet(postSpecialtyDTOMock));

    when(postRepositoryMock.findOne(POST_ID)).thenReturn(postMock1);
    when(postMock1.getNationalPostNumber()).thenReturn(CURRENT_NATIONAL_POST_NUMBER);
    when(postSpecialtyMock.getPostSpecialtyType()).thenReturn(PostSpecialtyType.PRIMARY);
    when(postSpecialtyMock.getSpecialty()).thenReturn(specialtyMock);
    when(specialtyMock.getSpecialtyCode()).thenReturn(SPECIALTY_CODE);
    when(postMock1.getSpecialties()).thenReturn(Sets.newHashSet(postSpecialtyMock));
    when(postGradeMock.getPostGradeType()).thenReturn(PostGradeType.APPROVED);
    when(postGradeMock.getGradeId()).thenReturn(GRADE_ID);
    when(postMock1.getGrades()).thenReturn(Sets.newHashSet(postGradeMock));
  }

  @Test
  public void requireNewNationalPostNumberShouldReturnFalseWhenNoDependantFieldChanges() {
    wireUpPostEntityAndDTO();

    doReturn(LOCAL_OFFICE_ABBR).when(testObj).getLocalOfficeAbbr();
    doReturn(SITE_CODE).when(testObj).getSiteCode(postDTOMock1);
    doReturn(GRADE_ABBR).when(testObj).getApprovedGradeOrEmpty(postDTOMock1);
    doReturn(SPECIALTY_CODE).when(testObj).getPrimarySpecialtyCodeOrEmpty(postDTOMock1);

    boolean result = testObj.requireNewNationalPostNumber(postDTOMock1);
    Assert.assertFalse(result);
  }

  @Test
  public void requireNewNationalPostNumberShouldReturnTrueWhenLocalOfficeAbbrFieldChanges() {
    wireUpPostEntityAndDTO();

    doReturn("newLocalOfficeAbbrCode").when(testObj).getLocalOfficeAbbr();
    doReturn(SITE_CODE).when(testObj).getSiteCode(postDTOMock1);
    doReturn(GRADE_ABBR).when(testObj).getApprovedGradeOrEmpty(postDTOMock1);
    doReturn(SPECIALTY_CODE).when(testObj).getPrimarySpecialtyCodeOrEmpty(postDTOMock1);

    boolean result = testObj.requireNewNationalPostNumber(postDTOMock1);

    Assert.assertTrue(result);
  }

  @Test
  public void requireNewNationalPostNumberShouldReturnTrueWhenLocationCodeChanges() {
    wireUpPostEntityAndDTO();

    doReturn(LOCAL_OFFICE_ABBR).when(testObj).getLocalOfficeAbbr();
    doReturn("newLocationCode").when(testObj).getSiteCode(postDTOMock1);
    doReturn(GRADE_ABBR).when(testObj).getApprovedGradeOrEmpty(postDTOMock1);
    doReturn(SPECIALTY_CODE).when(testObj).getPrimarySpecialtyCodeOrEmpty(postDTOMock1);

    boolean result = testObj.requireNewNationalPostNumber(postDTOMock1);

    Assert.assertTrue(result);
  }

  @Test
  public void requireNewNationalPostNumberShouldReturnTrueWhenSpecialtyCodeChanges() {
    wireUpPostEntityAndDTO();

    doReturn(LOCAL_OFFICE_ABBR).when(testObj).getLocalOfficeAbbr();
    doReturn(SITE_CODE).when(testObj).getSiteCode(postDTOMock1);
    doReturn(GRADE_ABBR).when(testObj).getApprovedGradeOrEmpty(postDTOMock1);
    doReturn("newSpecialtyCode").when(testObj).getPrimarySpecialtyCodeOrEmpty(postDTOMock1);

    boolean result = testObj.requireNewNationalPostNumber(postDTOMock1);

    Assert.assertTrue(result);
  }

  @Test
  public void requireNewNationalPostNumberShouldReturnTrueWhenSuffixChanges() {
    wireUpPostEntityAndDTO();

    doReturn(LOCAL_OFFICE_ABBR).when(testObj).getLocalOfficeAbbr();
    doReturn(SITE_CODE).when(testObj).getSiteCode(postDTOMock1);
    doReturn(GRADE_ABBR).when(testObj).getApprovedGradeOrEmpty(postDTOMock1);
    doReturn(SPECIALTY_CODE).when(testObj).getPrimarySpecialtyCodeOrEmpty(postDTOMock1);

    when(postDTOMock1.getSuffix()).thenReturn(PostSuffix.ACADEMIC);

    boolean result = testObj.requireNewNationalPostNumber(postDTOMock1);

    Assert.assertTrue(result);
  }


  @Test
  public void generateNationalPostNumberShouldReturnNewUniquePostNumberWithCounterOneHigherThanCurrentHighest() {
    when(postMock1.getNationalPostNumber()).thenReturn(LOCAL_OFFICE_ABBR + SLASH + SITE_CODE + SLASH +
        SPECIALTY_CODE + SLASH + GRADE_ABBR + SLASH + UNIQUE_NUMBER + SLASH + MILITARY_SUFFIX);
    when(postMock1.getSuffix()).thenReturn(PostSuffix.MILITARY);
    when(postRepositoryMock.findPostNumberNumberLike(LOCAL_OFFICE_ABBR + SLASH + SITE_CODE + SLASH +
        SPECIALTY_CODE + SLASH + GRADE_ABBR)).thenReturn(Sets.newHashSet(postMock1));

    String result = testObj.generateNationalPostNumber(LOCAL_OFFICE_ABBR, SITE_CODE, SPECIALTY_CODE, GRADE_ABBR, PostSuffix.MILITARY);

    String expectedNationalPostNumber = LOCAL_OFFICE_ABBR + SLASH + SITE_CODE + SLASH +
        SPECIALTY_CODE + SLASH + GRADE_ABBR + SLASH + UNIQUE_NUMBER_PLUS_1 + SLASH + MILITARY_SUFFIX;

    Assert.assertEquals(expectedNationalPostNumber, result);
  }


}