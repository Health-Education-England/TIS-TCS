package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.*;
import com.transformuk.hee.tis.tcs.service.repository.*;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

  @InjectMocks
  private PostServiceImpl testObj;

  @Mock
  private PostRepository postRepositoryMock;
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
  private PostDTO postDTOMock1, postDTOMock2, postMappedDTOMock1, postMappedDTOMock2;
  @Mock
  private Post postMock1, postMock2, postSaveMock1, postSaveMock2;
  @Mock
  private Pageable pageableMock;

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
    List<Post> posts = Lists.newArrayList(postMock1);
    List<PostDTO> mappedPosts = Lists.newArrayList(postDTOMock1);
    Page<Post> page = new PageImpl<>(posts);
    when(postRepositoryMock.findAll(pageableMock)).thenReturn(page);
    when(postMapperMock.postToPostDTO(postMock1)).thenReturn(postDTOMock1);

    Page<PostDTO> result = testObj.findAll(pageableMock);

    Assert.assertEquals(1, result.getTotalPages());
    Assert.assertEquals(mappedPosts, result.getContent());

    verify(postRepositoryMock).findAll(pageableMock);
    verify(postMapperMock).postToPostDTO(postMock1);
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
  public void pathPostSites() {
    List<Long> postIds = Lists.newArrayList(1L);
    List<String> intrepidIds = Lists.newArrayList("intrepid1");

    PostSiteDTO postSiteDTO = new PostSiteDTO();
    postSiteDTO.setPostSiteType(PostSiteType.PRIMARY);
    postSiteDTO.setSiteId("SiteID");

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
    verify(postRepositoryMock).save(Lists.newArrayList(currentPost));
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
    postGradeDTO.setGradeId("GradeId");

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
  public void pathPostSpecialties() {
    List<Long> postIds = Lists.newArrayList(1L);
    List<String> intrepidIds = Lists.newArrayList("intrepid1");

    PostSpecialtyDTO postSpecialtyDTO = new PostSpecialtyDTO();
    postSpecialtyDTO.setPostSpecialtyType(PostSpecialtyType.PRIMARY);
    SpecialtyDTO specialtyDTO = new SpecialtyDTO();
    specialtyDTO.setId(2L);
    postSpecialtyDTO.setSpecialty(specialtyDTO);

    PostDTO sendPostData = new PostDTO();
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0)).specialties(Sets.newHashSet(postSpecialtyDTO));

    Specialty specialty = new Specialty();
    specialty.setId(2L);
    PostSpecialty postSpecialty = new PostSpecialty();
    postSpecialty.setPostSpecialtyType(PostSpecialtyType.PRIMARY);
    postSpecialty.setSpecialty(specialty);

    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));
    currentPost.setSpecialties(Sets.newHashSet(postSpecialty));

    Set<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);

    PostDTO expectedDTO = new PostDTO();
    Set<PostSpecialtyDTO> expectedSpecialties = Sets.newHashSet(postSpecialtyDTO);
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId()).specialties(expectedSpecialties);

    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    Set<Long> specialtyIds = Sets.newHashSet(2L);


    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds))).thenReturn(postsFromRepository);
    when(specialtyRepositoryMock.findAll(specialtyIds)).thenReturn(Lists.newArrayList(specialty));
    when(postRepositoryMock.save(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);

    List<PostDTO> result = testObj.patchPostSpecialties(Lists.newArrayList(sendPostData));

    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postRepositoryMock).save(Lists.newArrayList(currentPost));
    verify(postMapperMock).postsToPostDTOs(savedPosts);

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
}