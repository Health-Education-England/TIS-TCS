package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.repository.PostFundingRepository;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostFundingServiceImplTest {

  @Mock
  private PostFundingRepository postFundingRepository;
  @InjectMocks
  private PostFundingServiceImpl postFundingService;

  private PostFundingDTO postFundingDTO1, postFundingDTO2, postFundingDTO3;
  private PostFunding postFunding1, postFunding2, postFunding3;

  @Before
  public void setup() {
    postFunding1 = new PostFunding();
    postFunding1.setId(1L);
    postFunding1.setPost(new Post());
    postFunding1.setEndDate(LocalDate.now().plusYears(1));

    postFundingDTO1 = new PostFundingDTO();
    postFundingDTO1.setId(1L);
    postFundingDTO1.setPostId(1L);
    postFundingDTO1.setEndDate(LocalDate.now().plusYears(1));

    postFunding2 = new PostFunding();
    postFunding2.setId(2L);
    postFunding2.setPost(new Post());
    postFunding2.setEndDate(LocalDate.now().plusYears(2));

    postFundingDTO2 = new PostFundingDTO();
    postFundingDTO2.setId(2L);
    postFundingDTO2.setPostId(2L);
    postFundingDTO1.setEndDate(LocalDate.now().plusYears(2));

    postFunding3 = new PostFunding();
    postFunding3.setId(3L);
    postFunding3.setPost(new Post());

    postFundingDTO3 = new PostFundingDTO();
    postFundingDTO3.setPostId(3L);
  }
  @Test
  public void getPostFundingStatusForPostShouldReturnCurrentIfCurrentPostFundingExists() {
    when(postFundingRepository.countCurrentFundings(1L)).thenReturn(1L);
    Status result = postFundingService.getPostFundingStatusForPost(1L);
    assertEquals(Status.CURRENT, result);
  }

  @Test
  public void getPostFundingStatusForPostShouldReturnInactiveIfNoCurrentPostFundingExists() {
    when(postFundingRepository.countCurrentFundings(2L)).thenReturn(0L);
    Status result = postFundingService.getPostFundingStatusForPost(2L);
    assertEquals(Status.INACTIVE, result);
  }
}
