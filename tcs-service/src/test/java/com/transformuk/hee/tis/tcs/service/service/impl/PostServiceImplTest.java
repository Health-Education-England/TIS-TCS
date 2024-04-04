package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostEsrEventDto;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
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
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.validation.PostFundingValidator;
import com.transformuk.hee.tis.tcs.service.event.PostSavedEvent;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostEsrEvent;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PostEsrEventRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostFundingRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostGradeRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSiteRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostEsrEventDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

  private static final Long SITE_ID = 12345L;
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
      "GROUP_CONCAT(surnames SEPARATOR ', ') surnames, GROUP_CONCAT(forenames SEPARATOR ', ') forenames\n"
      +
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
      "    LEFT JOIN `PostSpecialty` ps on p.`id` = ps.`postId` AND ps.`postSpecialtyType` = 'PRIMARY'\n"
      +
      "    LEFT JOIN `Specialty` sp on sp.`id` = ps.`specialtyId`\n" +
      "    LEFT JOIN `PostSite` pst on p.`id` = pst.`postId` AND pst.`postSiteType` = 'PRIMARY'\n" +
      "    LEFT JOIN `PostFunding` pf on p.`id` = pf.`postId` and (curdate() BETWEEN pf.startDate AND pf.endDate or pf.endDate is NULL)\n"
      +
      "    LEFT JOIN `Placement` pl on pl.postId = p.id and curdate() BETWEEN pl.dateFrom AND pl.dateTo\n"
      +
      "    LEFT JOIN `ContactDetails` c on pl.traineeId = c.id\n" +
      "    LEFT JOIN `ProgrammePost` pp on pp.postId = p.id\n" +
      "    LEFT JOIN `Programme` prg on prg.`id` = pp.`programmeId`\n" +
      " TRUST_JOIN\n" +
      " WHERECLAUSE\n" +
      ") as ot\n" +
      "group by id,approvedGradeId,primarySpecialtyId,primarySpecialtyCode,primarySpecialtyName,primarySiteId,nationalPostNumber,status,owner,intrepidId\n"
      +
      " ORDERBYCLAUSE\n" +
      " LIMITCLAUSE\n" +
      ";";
  @Spy
  @InjectMocks
  private PostServiceImpl testObj;
  @Mock
  private PostRepository postRepositoryMock;
  @Mock
  private PostEsrEventRepository postEsrEventRepositoryMock;
  @Mock
  private PostGradeRepository postGradeRepositoryMock;
  @Mock
  private PostSiteRepository postSiteRepositoryMock;
  @Mock
  private PostSpecialtyRepository postSpecialtyRepositoryMock;
  @Mock
  private ProgrammeRepository programmeRepositoryMock;
  @Mock
  private PostMapper postMapperMock;
  @Mock
  private PostEsrEventDtoMapper postEsrEventDtoMapperMock;
  @Mock
  private SqlQuerySupplier sqlQuerySupplierMock;
  @Mock
  private PostDTO postDTOMock1, postDTOMock2, postMappedDTOMock1, postMappedDTOMock2;
  @Mock
  private PostViewDTO postViewDTOMock1;
  @Mock
  private Post postMock1, postMock2, postSaveMock1, postSaveMock2;
  @Mock
  private Pageable pageableMock;
  @Captor
  private ArgumentCaptor<Set<PostSpecialty>> postSpecialtyArgumentCaptor;
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
  @Mock
  private NationalPostNumberServiceImpl nationalPostNumberServiceMock;
  @Mock
  private PostViewDecorator postViewDecoratorMock;
  @Mock
  private PostFundingValidator postFundingValidatorMock;
  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @Captor
  ArgumentCaptor<PostSavedEvent> postSavedEventArgumentCaptor;

  @Captor
  ArgumentCaptor<Post> postArgumentCaptor;

  @Test
  public void saveShouldSavePost() {
    when(postMapperMock.postDTOToPost(postDTOMock1)).thenReturn(postMock1);
    when(postRepositoryMock.save(postMock1)).thenReturn(postSaveMock1);
    when(postDTOMock1.getId()).thenReturn(1L);
    when(postMapperMock.postToPostDTO(postSaveMock1)).thenReturn(postMappedDTOMock1);
    PostDTO result = testObj.save(postDTOMock1);
    Assert.assertEquals(postMappedDTOMock1, result);
    verify(postMapperMock).postDTOToPost(postDTOMock1);
    verify(postRepositoryMock).save(postMock1);
    verify(postMapperMock).postToPostDTO(postSaveMock1);
  }

  @Test
  public void saveShouldPublishPostSavedEventForExistingPost() {
    when(postMapperMock.postDTOToPost(postDTOMock1)).thenReturn(postMock1);
    when(postRepositoryMock.save(postMock1)).thenReturn(postSaveMock1);
    when(postDTOMock1.getId()).thenReturn(1L);
    when(postMapperMock.postToPostDTO(postSaveMock1)).thenReturn(postMappedDTOMock1);
    PostDTO result = testObj.save(postDTOMock1);
    Assert.assertEquals(postMappedDTOMock1, result);
    verify(applicationEventPublisher).publishEvent(postSavedEventArgumentCaptor.capture());
    Assert.assertEquals(postSavedEventArgumentCaptor.getValue().getPostDTO(), postDTOMock1);
  }

  @Test
  public void saveShouldSaveListOfPosts() {
    List<PostDTO> postDTOsList = Lists.newArrayList(postDTOMock1, postDTOMock2);
    List<Post> postList = Lists.newArrayList(postMock1, postMock2);
    List<Post> savedPosts = Lists.newArrayList(postSaveMock1, postSaveMock2);
    List<PostDTO> savedPostDTOs = Lists.newArrayList(postMappedDTOMock1, postMappedDTOMock2);
    when(postMapperMock.postDTOsToPosts(postDTOsList)).thenReturn(postList);
    when(postRepositoryMock.saveAll(postList)).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(savedPostDTOs);
    List<PostDTO> results = testObj.save(postDTOsList);
    Assert.assertSame(savedPostDTOs, results);
    verify(postMapperMock).postDTOsToPosts(postDTOsList);
    verify(postRepositoryMock).saveAll(postList);
    verify(postMapperMock).postsToPostDTOs(savedPosts);
  }

  @Test
  public void saveShouldPublishPostSavedEvents() {
    List<PostDTO> postDTOsList = Lists.newArrayList(postDTOMock1, postDTOMock2);
    List<Post> postList = Lists.newArrayList(postMock1, postMock2);
    List<Post> savedPosts = Lists.newArrayList(postSaveMock1, postSaveMock2);
    List<PostDTO> savedPostDTOs = Lists.newArrayList(postMappedDTOMock1, postMappedDTOMock2);
    when(postMapperMock.postDTOsToPosts(postDTOsList)).thenReturn(postList);
    when(postRepositoryMock.saveAll(postList)).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(savedPostDTOs);
    List<PostDTO> results = testObj.save(postDTOsList);

    verify(applicationEventPublisher, times(2)).publishEvent(
        postSavedEventArgumentCaptor.capture());
    List<PostSavedEvent> events = postSavedEventArgumentCaptor.getAllValues();
    List<PostDTO> eventDtos = events.stream().map(PostSavedEvent::getPostDTO)
        .collect(Collectors.toList());
    Assert.assertEquals(eventDtos, postDTOsList);
  }

  @Test
  public void updateShouldSavePostAndRefreshLinkedEntities() {
    Post postInDBMock = mock(Post.class);
    Post payloadPostMock = mock(Post.class);
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

    when(postRepositoryMock.findById(1L)).thenReturn(Optional.of(postInDBMock));

    when(postDTOMock1.getId()).thenReturn(1L);
    when(postInDBMock.getGrades()).thenReturn(grades);
    when(postInDBMock.getSites()).thenReturn(sites);
    when(postInDBMock.getSpecialties()).thenReturn(specialties);

    when(postMapperMock.postDTOToPost(postDTOMock1)).thenReturn(payloadPostMock);

    when(payloadPostMock.getFundings()).thenReturn(fundingsOnPayload);

    when(postInDBMock.getFundings()).thenReturn(fundingsInDatabase);
    doNothing().when(postFundingRepositoryMock).deleteAll(postFundingCaptor.capture());

    when(postRepositoryMock.save(payloadPostMock)).thenReturn(postSaveMock1);
    when(postMapperMock.postToPostDTO(postSaveMock1)).thenReturn(postMappedDTOMock1);

    PostDTO result = testObj.update(postDTOMock1);

    Assert.assertEquals(postMappedDTOMock1, result);
    verify(postRepositoryMock).findById(1L);
    verify(postMapperMock).postDTOToPost(postDTOMock1);
    verify(postRepositoryMock).save(payloadPostMock);
    verify(postMapperMock).postToPostDTO(postSaveMock1);
    verify(postGradeRepositoryMock).deleteAll(grades);
    verify(postSiteRepositoryMock).deleteAll(sites);
    verify(postSpecialtyRepositoryMock).deleteAll(specialties);

    Set<PostFunding> capturedPostFundings = postFundingCaptor.getValue();
    Assert.assertTrue(capturedPostFundings.size() < fundingsInDatabase.size());

  }

  @Test
  public void updateFundingStatusShouldSetFundingStatus() {
    Post testPost = new Post();
    testPost.setId(1L);
    when(postRepositoryMock.findById(1L)).thenReturn(Optional.of(testPost));
    testObj.updateFundingStatus(1L, Status.CURRENT);
    verify(postRepositoryMock).save(postArgumentCaptor.capture());
    Post result = postArgumentCaptor.getValue();
    Assert.assertEquals(result.getFundingStatus(), Status.CURRENT);
  }

  @Test
  public void updateFundingStatusShouldNotSetFundingStatusIfNullPost() {
    Post testPost = new Post();
    testPost.setId(1L);
    when(postRepositoryMock.findById(1L)).thenReturn(Optional.empty());
    testObj.updateFundingStatus(1L, Status.CURRENT);
    verify(postRepositoryMock, never()).save(testPost);
  }

  @Test
  public void updateFundingStatusShouldNotPublishSavedEvent() {
    Post testPost = new Post();
    testPost.setId(1L);
    when(postRepositoryMock.findById(1L)).thenReturn(Optional.of(testPost));
    testObj.updateFundingStatus(1L, Status.CURRENT);
    verify(applicationEventPublisher, never()).publishEvent(any());
  }

  @Test
  public void findAllShouldRetrieveAllInstances() {
    String query = "PostQuery";
    List<PostViewDTO> mappedPosts = Lists.newArrayList(postViewDTOMock1);
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(false);
    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(query);
    when(namedParameterJdbcTemplateMock
        .query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
        .thenReturn(mappedPosts);
    when(pageableMock.getPageSize()).thenReturn(20);

    Page<PostViewDTO> result = testObj.findAll(pageableMock);

    Assert.assertEquals(1, result.getContent().size());
    verify(sqlQuerySupplierMock).getQuery(SqlQuerySupplier.POST_VIEW);
    verify(namedParameterJdbcTemplateMock)
        .query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class));
  }

  @Test
  public void findOneShouldRetrieveOneInstanceById() {
    when(postRepositoryMock.findPostByIdWithJoinFetch(1L)).thenReturn(Optional.of(postMock1));
    when(postMapperMock.postToPostDTO(postMock1)).thenReturn(postDTOMock1);

    PostDTO result = testObj.findOne(1L);
    Assert.assertEquals(postDTOMock1, result);

    verify(postRepositoryMock).findPostByIdWithJoinFetch(1L);
    verify(postMapperMock).postToPostDTO(postMock1);
  }

  @Test
  public void deleteShouldDeleteOneInstanceById() {
    testObj.delete(1L);
    verify(postRepositoryMock).deleteById(1L);
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
    expectedDTO.id(currentPostDTO.getId()).intrepidId(currentPost.getIntrepidId())
        .oldPost(oldPostDTO).newPost(newPostDTO);
    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds)))
        .thenReturn(postsFromRepository);
    when(postRepositoryMock.saveAll(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);
    List<PostDTO> result = testObj.patchOldNewPosts(postDtos);
    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postRepositoryMock).saveAll(Lists.newArrayList(currentPost));
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
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0))
        .setSites(Sets.newHashSet(postSiteDTO));
    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));
    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);
    PostDTO expectedDTO = new PostDTO();
    HashSet<PostSiteDTO> expectedSites = Sets.newHashSet(postSiteDTO);
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId())
        .sites(expectedSites);
    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds)))
        .thenReturn(postsFromRepository);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);
    List<PostDTO> result = testObj.patchPostSites(Lists.newArrayList(sendPostData));
    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postSiteRepositoryMock).saveAll(any());
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
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0))
        .grades(Sets.newHashSet(postGradeDTO));
    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));
    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);
    PostDTO expectedDTO = new PostDTO();
    HashSet<PostGradeDTO> expectedGrades = Sets.newHashSet(postGradeDTO);
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId())
        .grades(expectedGrades);
    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds)))
        .thenReturn(postsFromRepository);
    when(postRepositoryMock.saveAll(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);
    List<PostDTO> result = testObj.patchPostGrades(Lists.newArrayList(sendPostData));
    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postRepositoryMock).saveAll(Lists.newArrayList(currentPost));
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
    sendPostData.id(postIds.get(0)).intrepidId(intrepidIds.get(0))
        .programmes(Collections.singleton(programmeDTO));
    Post currentPost = new Post();
    currentPost.setId(postIds.get(0));
    currentPost.setIntrepidId(intrepidIds.get(0));
    HashSet<Post> postsFromRepository = Sets.newHashSet(currentPost);
    List<Post> savedPosts = Lists.newArrayList(currentPost);
    PostDTO expectedDTO = new PostDTO();
    expectedDTO.id(sendPostData.getId()).intrepidId(currentPost.getIntrepidId())
        .programmes(Collections.singleton(programmeDTO));
    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    Set<Long> programmeIds = Sets.newHashSet(1L);
    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds)))
        .thenReturn(postsFromRepository);
    when(programmeRepositoryMock.findAllById(programmeIds))
        .thenReturn(Lists.newArrayList(programme));
    when(postRepositoryMock.saveAll(Lists.newArrayList(currentPost))).thenReturn(savedPosts);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);
    List<PostDTO> result = testObj.patchPostProgrammes(Lists.newArrayList(sendPostData));
    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(programmeRepositoryMock).findAllById(programmeIds);
    verify(postRepositoryMock).saveAll(Lists.newArrayList(currentPost));
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
    postDTOToSend.id(postIds.get(0)).intrepidId(intrepidIds.get(0))
        .specialties(Sets.newHashSet(newPostSpecialtyDTO));
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
    expectedDTO.id(postDTOToSend.getId()).intrepidId(postInRepository.getIntrepidId())
        .specialties(expectedSpecialties);
    List<PostDTO> transformedPosts = Lists.newArrayList(expectedDTO);
    when(postRepositoryMock.findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds)))
        .thenReturn(postsFromRepository);
    when(postMapperMock.postsToPostDTOs(savedPosts)).thenReturn(transformedPosts);
    List<PostDTO> result = testObj.patchPostSpecialties(Lists.newArrayList(postDTOToSend));
    verify(postRepositoryMock).findPostByIntrepidIdIn(Sets.newHashSet(intrepidIds));
    verify(postSpecialtyRepositoryMock).saveAll(postSpecialtyArgumentCaptor.capture());
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
    Assert.assertEquals(newPostSpecialtyDTO.getSpecialty().getId(),
        postSpecialtyValue.getSpecialty().getId());
    Assert.assertSame(transformedPosts, result);
    Assert.assertEquals(expectedDTO, result.get(0));
    Assert.assertEquals(expectedSpecialties, result.get(0).getSpecialties());
  }

  @Test
  public void patchPostFundingsShouldReturnNullWhenPostDTOIsNull() {
    PostDTO postDTO = null;
    List<PostFundingDTO> retList = testObj.patchPostFundings(postDTO);
    Assert.assertNull(retList);
  }

  @Test
  public void patchPostFundingsShouldReturnNullWhenPostIsNotFound() {
    PostDTO postDTO = new PostDTO();
    Set<PostFundingDTO> postFundingDTOs = new HashSet<>();
    postDTO.id(1L).setFundings(postFundingDTOs);
    doReturn(null).when(testObj).findOne(any());
    List<PostFundingDTO> retList = testObj.patchPostFundings(postDTO);
    Assert.assertNull(retList);
  }

  @Test
  public void patchPostFundingShouldSucceed() {
    PostDTO postDTO = new PostDTO();
    Set<PostFundingDTO> postFundingDTOs = new HashSet<>();
    postDTO.id(1L).setFundings(postFundingDTOs);
    PostDTO postDTOInDBMock = mock(PostDTO.class);
    List<PostFundingDTO> checkList = new ArrayList<>(postFundingDTOs);
    doReturn(postDTOInDBMock).when(testObj).findOne(any());
    when(postFundingValidatorMock.validateFundingType(checkList)).thenReturn(checkList);
    doReturn(postDTOInDBMock).when(testObj).update(any());
    List<PostFundingDTO> retList = testObj.patchPostFundings(postDTO);
    Assert.assertEquals(checkList, retList);
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
  public void fundingTypeShouldExistInWhereClauseWhenFilteredByIt() {
    final String SEARCH_STRING = StringUtils.EMPTY;
    final List<ColumnFilter> COLUMN_FILTERS = new ArrayList<>();
    List<Object> columnFilterValues = new ArrayList<>();
    columnFilterValues.add(FundingType.TARIFF);
    columnFilterValues.add(FundingType.MADEL);
    columnFilterValues.add(FundingType.TRUST);
    columnFilterValues.add(FundingType.OTHER);
    COLUMN_FILTERS.add(new ColumnFilter("fundingType", columnFilterValues));
    List<PostViewDTO> resultsFromQuery = new ArrayList<>();
    resultsFromQuery.add(new PostViewDTO());
    String whereClause = testObj.createWhereClause(SEARCH_STRING, COLUMN_FILTERS);
    Assert.assertTrue(whereClause.contains("fundingTypeList"));
  }

  @Test
  public void advancedSearchShouldSearchWithDescOrderByCurrentTraineeSurname() {
    final int PAGE = 1;
    final int SIZE = 100;
    final Sort surnameSortOrder = Sort.by(Sort.Direction.DESC, "currentTraineeSurname");
    final String SEARCH_STRING = StringUtils.EMPTY;
    final List<ColumnFilter> COLUMN_FILTERS = new ArrayList<>();
    final String WHERE_CLAUSE = StringUtils.EMPTY;
    List<PostViewDTO> resultsFromQuery = new ArrayList<>();
    resultsFromQuery.add(new PostViewDTO());
    PageRequest pageable = PageRequest.of(PAGE, SIZE, surnameSortOrder);
    doReturn(WHERE_CLAUSE).when(testObj).createWhereClause(SEARCH_STRING, COLUMN_FILTERS);
    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(query);
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(false);
    when(namedParameterJdbcTemplateMock
        .query(queryCaptor.capture(), any(MapSqlParameterSource.class),
            any(PostServiceImpl.PostViewRowMapper.class)))
        .thenReturn(resultsFromQuery);
    testObj.advancedSearch(SEARCH_STRING, COLUMN_FILTERS, pageable);
    String capturedQueryString = queryCaptor.getValue();
    int indexOfGroupBy = capturedQueryString.indexOf("group by");
    Assert.assertTrue(indexOfGroupBy >= 0);
    int indexOfOrderBy = capturedQueryString.indexOf("ORDER BY surnames DESC");
    Assert.assertTrue(indexOfOrderBy >= 0);
    //ordering
    Assert.assertTrue(indexOfGroupBy < indexOfOrderBy);
    //multiples
    Assert.assertFalse(
        capturedQueryString.substring(indexOfGroupBy + "group by".length()).contains("group by"));
  }

  @Test
  public void advancedSearchShouldSearchWithAscendingOrderByCurrentTraineeSurname() {
    final int PAGE = 1;
    final int SIZE = 100;
    final Sort surnameSortOrder = Sort.by(Sort.Direction.ASC, "currentTraineeSurname");
    final String SEARCH_STRING = StringUtils.EMPTY;
    final List<ColumnFilter> COLUMN_FILTERS = new ArrayList<>();
    final String WHERE_CLAUSE = StringUtils.EMPTY;
    List<PostViewDTO> resultsFromQuery = new ArrayList<>();
    resultsFromQuery.add(new PostViewDTO());
    PageRequest pageable = PageRequest.of(PAGE, SIZE, surnameSortOrder);
    doReturn(WHERE_CLAUSE).when(testObj).createWhereClause(SEARCH_STRING, COLUMN_FILTERS);
    when(sqlQuerySupplierMock.getQuery(SqlQuerySupplier.POST_VIEW)).thenReturn(query);
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(false);
    when(namedParameterJdbcTemplateMock
        .query(queryCaptor.capture(), any(MapSqlParameterSource.class),
            any(PostServiceImpl.PostViewRowMapper.class)))
        .thenReturn(resultsFromQuery);
    testObj.advancedSearch(SEARCH_STRING, COLUMN_FILTERS, pageable);
    String capturedQueryString = queryCaptor.getValue();
    int indexOfGroupBy = capturedQueryString.indexOf("group by");
    Assert.assertTrue(indexOfGroupBy >= 0);
    int indexOfOrderBy = capturedQueryString.indexOf("ORDER BY surnames ASC");
    Assert.assertTrue(indexOfOrderBy >= 0);
    //ordering
    Assert.assertTrue(indexOfGroupBy < indexOfOrderBy);
    //multiples
    Assert.assertFalse(
        capturedQueryString.substring(indexOfGroupBy + "group by".length()).contains("group by"));
  }

  @Test(expected = NullPointerException.class)
  public void findPostsForProgrammeIdAndNpnShouldThrowExceptionWhenProgrammeIdIsNull() {
    try {
      testObj.findPostsForProgrammeIdAndNpn(null, "DUMMY TEXT");
    } catch (Exception e) {
      verifyZeroInteractions(postRepositoryMock);
      verifyZeroInteractions(postMapperMock);
      throw e;
    }
  }

  @Test
  public void findPostsForProgrammeIdAndNpnShouldDoSearchAndConvertResultToDto() {
    long programmeId = 1L;
    String npn = "DUMMY TEXT";

    List<Post> postsFromDb = Lists.newArrayList(new Post());
    List<PostDTO> convertedPosts = Lists.newArrayList(new PostDTO());

    when(postRepositoryMock.findPostsForProgrammeIdAndNpnLike(programmeId, npn, Status.CURRENT))
        .thenReturn(postsFromDb);
    when(postMapperMock.postsToPostDTOs(postsFromDb)).thenReturn(convertedPosts);

    List<PostDTO> result = testObj.findPostsForProgrammeIdAndNpn(programmeId, npn);

    verify(postRepositoryMock).findPostsForProgrammeIdAndNpnLike(programmeId, npn, Status.CURRENT);
    verify(postMapperMock).postsToPostDTOs(postsFromDb);
    Assert.assertSame(convertedPosts, result);
  }

  @Test
  public void markPostAsEsrPositionChangedShouldReturnEmptyIfPostIdNotFound() {
    //given
    Long postId = 1L;
    when(postRepositoryMock.findPostWithTrustsById(postId)).thenReturn(Optional.empty());
    PostEsrEventDto postEsrEventDto = new PostEsrEventDto();

    //when
    Optional<PostEsrEventDto> postEsrEvent =
        testObj.markPostAsEsrPositionChanged(postId, postEsrEventDto);

    //then
    boolean postEsrEventIsPresent = postEsrEvent.isPresent();
    Assert.assertFalse("Unexpected PostEsrEvent returned", postEsrEventIsPresent);
  }

  @Test
  public void markPostAsEsrPositionChangedShouldSaveUpdatedPostEsrEvent() {
    //given
    Long postId = 1L;
    Post post = new Post();
    post.setId(postId);
    PostEsrEventDto postEsrEventDto = new PostEsrEventDto();
    PostEsrEvent postEsrEvent = new PostEsrEvent();

    when(postEsrEventDtoMapperMock.postEsrEventDtoToPostEsrEvent(any())).thenReturn(postEsrEvent);
    when(postRepositoryMock.findPostWithTrustsById(postId)).thenReturn(Optional.of(post));

    //when
    testObj.markPostAsEsrPositionChanged(postId, postEsrEventDto);

    //then
    verify(postEsrEventRepositoryMock).save(postEsrEvent);
  }
}
