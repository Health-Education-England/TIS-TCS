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
import com.transformuk.hee.tis.tcs.api.enumeration.FundingType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.util.BasicPage;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.model.*;
import com.transformuk.hee.tis.tcs.service.repository.*;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewMapper;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {
  String query = "select distinct id,\n" +
      "approvedGradeId,\n" +
      "primarySpecialtyId,\n" +
      "primarySpecialtyCode,\n" +
      "primarySpecialtyName,\n" +
      "primarySiteId,\n" +
      "GROUP_CONCAT(distinct programmeName SEPARATOR ', ') programmes,\n" +
      "GROUP_CONCAT(distinct fundingType SEPARATOR ', ') fundingType,\n" +
      "nationalPostNumber,\n" +
      "status,\n" +
      "owner,\n" +
      "intrepidId,\n" +
      "GROUP_CONCAT(surnames SEPARATOR ', ') surnames, GROUP_CONCAT(forenames SEPARATOR ', ') forenames\n" +
      " from (SELECT p.`id`,\n" +
      "    pg.`gradeId` as `approvedGradeId`,\n" +
      "    ps.`specialtyId` as `primarySpecialtyId`,\n" +
      "    sp.`specialtyCode` as `primarySpecialtyCode`,\n" +
      "    sp.`name` as `primarySpecialtyName`,\n" +
      "    pst.`siteId` as `primarySiteId`,\n" +
      "    prg.`programmeName`,\n" +
      "    pf.`fundingType`,\n" +
      "    p.`nationalPostNumber`,\n" +
      "    p.`status`,\n" +
      "    p.`owner`,\n" +
      "    p.`intrepidId`,\n" +
      "    c.surname surnames, c.forenames forenames\n" +
      "    FROM `Post` p\n" +
      "    LEFT JOIN `PostGrade` pg on p.`id` = pg.`postId` AND pg.`postGradeType` = 'APPROVED'\n" +
      "    LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'\n" +
      "    LEFT JOIN `Specialty` sp on sp.`id` = ps.`specialtyId`\n" +
      "    LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'\n" +
      "    LEFT JOIN `PostFunding` pf on p.`id` = pf.`postId` and (curdate() BETWEEN pf.startDate AND pf.endDate or pf.endDate is NULL)\n" +
      "    LEFT JOIN `Placement` pl on pl.postId = p.id and curdate() BETWEEN pl.dateFrom AND pl.dateTo\n" +
      "    LEFT JOIN `ContactDetails` c on pl.traineeId = c.id\n" +
      "    LEFT JOIN `ProgrammePost` pp on pp.postId = p.id\n" +
      "    LEFT JOIN `Programme` prg on prg.`id` = pp.`programmeId`\n" +
      " TRUST_JOIN\n" +
      " WHERECLAUSE\n" +
      ") as ot\n" +
      "group by id,approvedGradeId,primarySpecialtyId,primarySpecialtyCode,primarySpecialtyName,primarySiteId,nationalPostNumber,status,owner,intrepidId\n" +
      " ORDERBYCLAUSE\n" +
      " LIMITCLAUSE\n" +
      ";";
  private static final Long SITE_ID = 12345L;
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
  private SqlQuerySupplier sqlQuerySupplierMock;
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
  private PostViewDecorator postViewDecorator;
  @Mock
  private NationalPostNumberServiceImpl nationalPostNumberServiceMock;
  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;
  @Mock
  private PermissionService permissionServiceMock;
  @Captor
  private ArgumentCaptor<String> queryCaptor;
  @Mock
  private PostFundingRepository postFundingRepositoryMock;
  @Captor
  private ArgumentCaptor<Set<PostFunding>> postFundingCaptor;

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
    Post postInDBMock = mock(Post.class);
    Set<PostGrade> grades = Sets.newHashSet();
    Set<PostSite> sites = Sets.newHashSet();
    Set<PostSpecialty> specialties = Sets.newHashSet();
    PostFunding postFunding1 = new PostFunding();
    postFunding1.setId(1L);
    PostFunding postFunding2 = new PostFunding();
    postFunding2.setId(2L);
    PostFunding postFunding3 = new PostFunding();
    postFunding3.setId(3L);
    Set<PostFunding> fundingsOnPayload = Sets.newHashSet(postFunding1, postFunding2);
    Set<PostFunding> fundingsInDatabase = Sets.newHashSet(postFunding1, postFunding2, postFunding3);

    when(postDTOMock1.getId()).thenReturn(1L);
    when(postMock1.getGrades()).thenReturn(grades);
    when(postMock1.getSites()).thenReturn(sites);
    when(postMock1.getSpecialties()).thenReturn(specialties);
    when(postMock1.getFundings()).thenReturn(fundingsOnPayload);
    when(postRepositoryMock.findOne(1L)).thenReturn(postInDBMock);
    when(postInDBMock.getFundings()).thenReturn(fundingsInDatabase);
    when(postMapperMock.postDTOToPost(postDTOMock1)).thenReturn(postMock1);
    when(postRepositoryMock.save(postMock1)).thenReturn(postSaveMock1);
    when(postMapperMock.postToPostDTO(postSaveMock1)).thenReturn(postMappedDTOMock1);
    doNothing().when(postFundingRepositoryMock).delete(postFundingCaptor.capture());

    PostDTO result = testObj.update(postDTOMock1);

    Assert.assertEquals(postMappedDTOMock1, result);
    verify(postRepositoryMock).findOne(1L);
    verify(postMapperMock).postDTOToPost(postDTOMock1);
    verify(postRepositoryMock).save(postMock1);
    verify(postMapperMock).postToPostDTO(postSaveMock1);
    verify(postGradeRepositoryMock).delete(grades);
    verify(postSiteRepositoryMock).delete(sites);
    verify(postSpecialtyRepositoryMock).delete(specialties);

    Set<PostFunding> capturedPostFundings = postFundingCaptor.getValue();
    Assert.assertTrue(capturedPostFundings.size() < fundingsInDatabase.size());

  }

  private PostFunding createPostFunding(Long id) {
    PostFunding postFunding = new PostFunding();

    postFunding.setId(id);

    return postFunding;
  }

  @Test
  public void findAllShouldRetrieveAllInstances() {
    String query = "PostQuery";
    List<PostView> posts = Lists.newArrayList(postViewMock1);
    List<PostViewDTO> mappedPosts = Lists.newArrayList(postViewDTOMock1);
    Page<PostView> page = new PageImpl<>(posts);
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(false);
    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(query);
    when(namedParameterJdbcTemplateMock.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class))).thenReturn(mappedPosts);
    when(pageableMock.getPageSize()).thenReturn(20);

    BasicPage<PostViewDTO> result = testObj.findAll(pageableMock);

    Assert.assertEquals(1, result.getContent().size());
    verify(sqlQuerySupplierMock).getQuery(SqlQuerySupplier.POST_VIEW);
    verify(namedParameterJdbcTemplateMock).query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class));
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
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0)).programmes(Collections.singleton(programmeDTO));
    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));
    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);
    PostDTO expectedDTO = new PostDTO();
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId()).programmes(Collections.singleton(programmeDTO));
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
    Assert.assertEquals(programmeDTO, result.get(0).getProgrammes().iterator().next());
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
    newPostSpecialtyDTO.setPostId(1L);
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
    PostSpecialtyDTO postSpecialtyValue = null;
    for (PostSpecialtyDTO savedLink : result.get(0).getSpecialties()) {
      if (PostSpecialtyType.OTHER.equals(savedLink.getPostSpecialtyType())) {
        postSpecialtyValue = savedLink;
        break;
      }
    }
    Assert.assertEquals(PostSpecialtyType.OTHER, postSpecialtyValue.getPostSpecialtyType());
    Assert.assertEquals(postInRepository.getId(), postSpecialtyValue.getPostId());
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

  @Test
  public void canLoggedInUserViewOrAmendShouldDoNothingWhenUserIsNotTrustAdmin() {
    Long postId = 1L;
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(false);
    testObj.canLoggedInUserViewOrAmend(postId);
    verify(permissionServiceMock, never()).getUsersTrustIds();
    verify(postRepositoryMock, never()).findPostWithTrustsById(any());
  }

  @Test
  public void canLoggedInUserViewOrAmendShouldDoNothingWhenPostCannotBeFound() {
    Long postId = 1L;
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(true);
    when(postRepositoryMock.findPostWithTrustsById(postId)).thenReturn(Optional.empty());
    testObj.canLoggedInUserViewOrAmend(postId);
    verify(permissionServiceMock).getUsersTrustIds();
    verify(postRepositoryMock).findPostWithTrustsById(postId);
  }

  @Test
  public void canLoggedInUserViewOrAmendShouldDoNothingWhenPostIsPartOfUsersTrusts() {
    PostTrust associatedTrust1 = new PostTrust();
    associatedTrust1.setTrustId(1L);
    PostTrust associatedTrust2 = new PostTrust();
    associatedTrust2.setTrustId(2L);
    Long postId = 1L;
    Post foundPost = new Post();
    foundPost.setId(postId);
    foundPost.setAssociatedTrusts(Sets.newHashSet(associatedTrust1, associatedTrust2));
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(true);
    when(permissionServiceMock.getUsersTrustIds()).thenReturn(Sets.newHashSet(1L));
    when(postRepositoryMock.findPostWithTrustsById(postId)).thenReturn(Optional.of(foundPost));
    testObj.canLoggedInUserViewOrAmend(postId);
    verify(permissionServiceMock).getUsersTrustIds();
    verify(postRepositoryMock).findPostWithTrustsById(postId);
  }

  @Test(expected = AccessUnauthorisedException.class)
  public void canLoggedInUserViewOrAmendShouldThrowExceptionWhenPostIsNotPartOfUsersTrusts() {
    PostTrust associatedTrust1 = new PostTrust();
    associatedTrust1.setTrustId(1L);
    PostTrust associatedTrust2 = new PostTrust();
    associatedTrust2.setTrustId(2L);
    Long postId = 1L;
    Post foundPost = new Post();
    foundPost.setId(postId);
    foundPost.setAssociatedTrusts(Sets.newHashSet(associatedTrust1, associatedTrust2));
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(true);
    when(permissionServiceMock.getUsersTrustIds()).thenReturn(Sets.newHashSet(99999L));
    when(postRepositoryMock.findPostWithTrustsById(postId)).thenReturn(Optional.of(foundPost));
    try {
      testObj.canLoggedInUserViewOrAmend(postId);
    } catch (Exception e) {
      verify(permissionServiceMock).getUsersTrustIds();
      verify(postRepositoryMock).findPostWithTrustsById(postId);
      throw e;
    }
  }

  @Test
  public void fundingTypeShouldExistInWhereClauseWhenFilteredByIt(){
    final int PAGE = 1;
    final int SIZE = 100;
    final Sort nationalPostNumberSortOrder = new Sort(Sort.Direction.DESC, "nationalPostNumber");
    final String SEARCH_STRING = StringUtils.EMPTY;
    final List<ColumnFilter> COLUMN_FILTERS = new ArrayList<>();
    List<Object> columnFilterValues = new ArrayList<>();
    columnFilterValues.add(FundingType.TARIFF);
    columnFilterValues.add(FundingType.MADEL);
    columnFilterValues.add(FundingType.TRUST);
    columnFilterValues.add(FundingType.OTHER);
    COLUMN_FILTERS.add(new ColumnFilter("fundingType", columnFilterValues));
    final String WHERE_CLAUSE = new String("TARIFF, MADEL, TRUST, OTHER");
    List<PostViewDTO> resultsFromQuery = new ArrayList<>();
    resultsFromQuery.add(new PostViewDTO());
    PageRequest pageable = new PageRequest(PAGE, SIZE, nationalPostNumberSortOrder);
    String whereClause = testObj.createWhereClause(SEARCH_STRING, COLUMN_FILTERS);
    Assert.assertTrue(whereClause.contains("fundingTypeList"));
  }

  @Test
  public void advancedSearchShouldSearchWithDescOrderByCurrentTraineeSurname() {
    final int PAGE = 1;
    final int SIZE = 100;
    final Sort surnameSortOrder = new Sort(Sort.Direction.DESC, "currentTraineeSurname");
    final String SEARCH_STRING = StringUtils.EMPTY;
    final List<ColumnFilter> COLUMN_FILTERS = new ArrayList<>();
    final String WHERE_CLAUSE = StringUtils.EMPTY;
    List<PostViewDTO> resultsFromQuery = new ArrayList<>();
    resultsFromQuery.add(new PostViewDTO());
    PageRequest pageable = new PageRequest(PAGE, SIZE, surnameSortOrder);
    doReturn(WHERE_CLAUSE).when(testObj).createWhereClause(SEARCH_STRING, COLUMN_FILTERS);
    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(query);
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(false);
    when(namedParameterJdbcTemplateMock.query(queryCaptor.capture(), any(MapSqlParameterSource.class), any(PostServiceImpl.PostViewRowMapper.class)))
        .thenReturn(resultsFromQuery);
    BasicPage<PostViewDTO> result = testObj.advancedSearch(SEARCH_STRING, COLUMN_FILTERS, pageable);
    String capturedQueryString = queryCaptor.getValue();
    int indexOfGroupBy = capturedQueryString.indexOf("group by");
    Assert.assertTrue(indexOfGroupBy >= 0);
    int indexOfOrderBy = capturedQueryString.indexOf("ORDER BY surnames DESC");
    Assert.assertTrue(indexOfOrderBy >= 0);
    //ordering
    Assert.assertTrue(indexOfGroupBy < indexOfOrderBy);
    //multiples
    Assert.assertFalse(capturedQueryString.substring(indexOfGroupBy + "group by".length()).contains("group by"));
  }

  @Test
  public void advancedSearchShouldSearchWithAscendingOrderByCurrentTraineeSurname() {
    final int PAGE = 1;
    final int SIZE = 100;
    final Sort surnameSortOrder = new Sort(Sort.Direction.ASC, "currentTraineeSurname");
    final String SEARCH_STRING = StringUtils.EMPTY;
    final List<ColumnFilter> COLUMN_FILTERS = new ArrayList<>();
    final String WHERE_CLAUSE = StringUtils.EMPTY;
    List<PostViewDTO> resultsFromQuery = new ArrayList<>();
    resultsFromQuery.add(new PostViewDTO());
    PageRequest pageable = new PageRequest(PAGE, SIZE, surnameSortOrder);
    doReturn(WHERE_CLAUSE).when(testObj).createWhereClause(SEARCH_STRING, COLUMN_FILTERS);
    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(query);
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(false);
    when(namedParameterJdbcTemplateMock.query(queryCaptor.capture(), any(MapSqlParameterSource.class), any(PostServiceImpl.PostViewRowMapper.class)))
        .thenReturn(resultsFromQuery);
    BasicPage<PostViewDTO> result = testObj.advancedSearch(SEARCH_STRING, COLUMN_FILTERS, pageable);
    String capturedQueryString = queryCaptor.getValue();
    int indexOfGroupBy = capturedQueryString.indexOf("group by");
    Assert.assertTrue(indexOfGroupBy >= 0);
    int indexOfOrderBy = capturedQueryString.indexOf("ORDER BY surnames ASC");
    Assert.assertTrue(indexOfOrderBy >= 0);
    //ordering
    Assert.assertTrue(indexOfGroupBy < indexOfOrderBy);
    //multiples
    Assert.assertFalse(capturedQueryString.substring(indexOfGroupBy + "group by".length()).contains("group by"));
  }
}
