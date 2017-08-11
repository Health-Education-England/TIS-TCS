package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.repository.PostGradeRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSiteRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
}