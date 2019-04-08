package com.transformuk.hee.tis.tcs.service.api.validation;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PostFundingValidatorTest {

  String FUNDING_TYPE_EMPTY = "funding type is empty";
  String FUNDING_TYPE_NOT_OTHER_ERROR = "funding type specified filled although type is not Other.";
  String NOT_FOUND_ERROR = "funding type does not exist.";
  String MULTIPLE_FOUND_ERROR = "found multiple funding type.";
  Long FUNDING_TYPE_ID = 2L;
  String FUNDING_TYPE_LABEL = "1234funding";
  String FUNDING_TYPE_LABEL2 = "5678funding";
  FundingTypeDTO fundingTypeDTO;
  private List<PostFundingDTO> pfDTOs;
  private PostFundingDTO fundingTypeMissingDTO;
  private PostFundingDTO notOtherDTO;
  private PostFundingDTO validDTO;
  private PostFundingDTO fundingTypeFilledDTO;

  @Mock
  private ReferenceServiceImpl referenceService;

  @InjectMocks
  private PostFundingValidator postFundingValidator;

  private PostFundingDTO buildMockFundingTypeDTO(Long id, String fundingType, String info, String fundingBodyId) {
    PostFundingDTO result = new PostFundingDTO();
    result.setId(id);
    result.setFundingType(fundingType);
    result.setInfo(info);
    result.setFundingBodyId(fundingBodyId);
    result.setStartDate(LocalDate.now());
    result.setEndDate(LocalDate.now().plusMonths(1));
    return result;
  }

  private List<PostFundingDTO> buildCheckedMap(PostFundingDTO pfDTO) {
    pfDTOs = new ArrayList<>();
    pfDTOs.add(pfDTO);
    return pfDTOs;
  }

  @Before
  public void setup() {
    fundingTypeDTO = new FundingTypeDTO();
    fundingTypeDTO.setId(FUNDING_TYPE_ID);
    fundingTypeDTO.setLabel(FUNDING_TYPE_LABEL);

    Set<String> labels = new HashSet<>();
    labels.add(FUNDING_TYPE_LABEL);

    Set<String> labels2 = new HashSet<>();
    labels.add(FUNDING_TYPE_LABEL2);

    given(referenceService.findCurrentFundingTypesByLabelIn(labels)).willReturn(Lists.newArrayList(fundingTypeDTO));
    given(referenceService.findCurrentFundingTypesByLabelIn(labels2)).willReturn(Lists.newArrayList(fundingTypeDTO));

    validDTO = buildMockFundingTypeDTO(1L, "Other", "info", "foo");
    fundingTypeMissingDTO = buildMockFundingTypeDTO(1L, null, "info", "foo");
    notOtherDTO = buildMockFundingTypeDTO(1L, FUNDING_TYPE_LABEL, "info", "foo");
    fundingTypeFilledDTO = buildMockFundingTypeDTO(1L, FUNDING_TYPE_LABEL2, null, "foo");
  }

  @Test
  public void testValidateFailsIfIdIsEmpty() {
    pfDTOs = buildCheckedMap(fundingTypeMissingDTO);

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(result.get(0).getMessageList().contains(FUNDING_TYPE_EMPTY), is(true));
  }

  @Test
  public void testValidateFailsInfoGivenForFundingTypeNoOther() {
    pfDTOs = buildCheckedMap(notOtherDTO);
    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(result.get(0).getMessageList().contains(FUNDING_TYPE_NOT_OTHER_ERROR), is(true));

  }

  @Test
  public void testValidateFailsIfFundingTypeNotFound() {
    pfDTOs = buildCheckedMap(fundingTypeFilledDTO);
    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(result.get(0).getMessageList().contains(NOT_FOUND_ERROR), is(true));
  }

  @Test
  public void testValidateFailsIfFundingTypeIsNotUnique() {
    // Map<PostFundingDTO, List<String>> result = postFundingValidator.validateFundingType(checkedMap);
  }

  @Test
  public void testValidateSuccessfully() {
    pfDTOs = buildCheckedMap(validDTO);
    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(result.get(0).getMessageList().isEmpty(), is(true));
  }

}
