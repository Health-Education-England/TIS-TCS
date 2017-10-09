package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.FieldError;

import java.util.List;

import static com.transformuk.hee.tis.tcs.service.api.validation.PostValidator.OTHER_FUNDING_TYPE;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PostValidatorTest {

  @InjectMocks
  private PostValidator testObj;

  @Mock
  private ProgrammeRepository programmeRepositoryMock;
  @Mock
  private PostRepository postRepositoryMock;
  @Mock
  private SpecialtyRepository specialtyRepositoryMock;
  @Mock
  private PlacementRepository placementRepositoryMock;
  @Mock
  private ReferenceServiceImpl referenceServiceMock;


  @Test
  public void checkFundingShouldReturnErrorsWhenOtherSelectedAndNoInfo(){
    PostDTO postDTO = new PostDTO();
    postDTO.setFundingType(OTHER_FUNDING_TYPE);

    List<FieldError> result = testObj.checkFunding(postDTO);

    Assert.assertEquals(1, result.size());
  }

  @Test
  public void checkFundingShouldReturnNoErrorsWhenOtherSelectedAndInfoProvided(){
    PostDTO postDTO = new PostDTO();
    postDTO.setFundingType(OTHER_FUNDING_TYPE);
    postDTO.setFundingInfo("Additional Information");

    List<FieldError> result = testObj.checkFunding(postDTO);

    Assert.assertEquals(0, result.size());
  }

  @Test
  public void checkFundingShouldReturnNoErrorsWhenFundingTypeIsNotOtherAndNoInfoProvided(){
    PostDTO postDTO = new PostDTO();
    postDTO.setFundingType("Tariff");

    List<FieldError> result = testObj.checkFunding(postDTO);

    Assert.assertEquals(0, result.size());
  }


}