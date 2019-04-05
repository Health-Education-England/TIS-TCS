package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class PostFundingValidatorTest {

  String FUNDING_TYPE_EMPTY = "funding type is empty";
  String FUNDING_TYPE_NOT_OTHER_ERROR = "found multiple funding type.";
  String NOT_FOUND_ERROR = "funding type does not exist.";
  String MULTIPLE_FOUND_ERROR = "found multiple funding type.";
  private Set<PostFundingDTO> pfDTOs;

  @Mock
  private ReferenceServiceImpl referenceService;

  @InjectMocks
  private PostFundingValidator postFundingValidator;

  @Before
  public void setup() {
    pfDTOs = new HashSet<>();
    PostFundingDTO validDTO = new PostFundingDTO();
    pfDTOs.add(validDTO);

    // prepare a map
    Iterator iter = pfDTOs.iterator();
    Map<PostFundingDTO, List<String>> checkedMap = new HashMap<>();
    while (iter.hasNext()) {
      checkedMap.put((PostFundingDTO)iter.next(), new ArrayList<>());
    }
  }

  @Test
  public void testValidatesCorrectData() {
    postFundingValidator.validateFundingType(checkedMap);
  }

  @Test
  public void testValidateFailsIfIdIsEmpty() {

  }

  @Test
  public void testValidateFailsIfNoInfoGivenForOtherFundingType() {

  }

  @Test
  public void testValidateFailsIfFundingTypeIsNotUnique() {

  }





}
