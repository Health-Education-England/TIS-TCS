package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostFundingValidatorTest {

  private final static Long FUNDING_TYPE_ID = 2L;
  private final static Long FUNDING_TYPE_ID2 = 3L;
  private final static String FUNDING_TYPE_LABEL = "1234funding";
  private final static String FUNDING_TYPE_LABEL2 = "5678funding";
  private final static String FUNDING_TYPE_LABEL3 = "8910funding";
  private final static String FUNDING_TYPE_LABEL4 = "academicFunding";
  private final static UUID FUNDING_SUBTYPE_UUID = UUID.randomUUID();
  private final static UUID FUNDING_SUBTYPE_UUID2 = UUID.randomUUID();
  FundingTypeDTO fundingTypeDTO;
  FundingTypeDTO academicFundingTypeDto;
  FundingTypeDTO multipleFundingTypeDTO;
  FundingSubTypeDto fundingSubTypeDto;
  private List<PostFundingDTO> pfDTOs;
  private PostFundingDTO fundingTypeMissingDTO;
  private PostFundingDTO notOtherDTO;
  private PostFundingDTO validDTO;
  private PostFundingDTO fundingTypeFilledDTO;
  private PostFundingDTO multipleFundingTypesDTO;
  private PostFundingDTO academicTypeDTO;
  private PostFundingDTO fundingSubTypeNotFoundDto;

  @Mock
  private ReferenceServiceImpl referenceService;

  @InjectMocks
  private PostFundingValidator postFundingValidator;

  private PostFundingDTO buildMockFundingTypeDTO(Long id, String fundingType, UUID fundingSubType,
      String info, String fundingBodyId) {
    PostFundingDTO result = new PostFundingDTO();
    result.setId(id);
    result.setFundingType(fundingType);
    result.setFundingSubTypeId(fundingSubType);
    result.setInfo(info);
    result.setFundingBodyId(fundingBodyId);
    result.setStartDate(LocalDate.now());
    result.setEndDate(LocalDate.now().plusMonths(1));
    return result;
  }

  private List<PostFundingDTO> buildCheckedList(PostFundingDTO pfDTO) {
    pfDTOs = new ArrayList<>();
    pfDTOs.add(pfDTO);
    return pfDTOs;
  }

  @Before
  public void setup() {
    fundingTypeDTO = new FundingTypeDTO();
    fundingTypeDTO.setId(FUNDING_TYPE_ID);
    fundingTypeDTO.setAllowDetails(false);
    fundingTypeDTO.setLabel(FUNDING_TYPE_LABEL);

    multipleFundingTypeDTO = new FundingTypeDTO();
    multipleFundingTypeDTO.setId(FUNDING_TYPE_ID);
    multipleFundingTypeDTO.setLabel(FUNDING_TYPE_LABEL3);

    academicFundingTypeDto = new FundingTypeDTO();
    academicFundingTypeDto.setId(FUNDING_TYPE_ID);
    academicFundingTypeDto.setLabel(FUNDING_TYPE_LABEL4);
    academicFundingTypeDto.setAllowDetails(true);

    fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setId(FUNDING_SUBTYPE_UUID);
    fundingSubTypeDto.setStatus(Status.CURRENT);

    when(referenceService.findCurrentFundingTypesByLabelIn(
        Collections.singleton(FUNDING_TYPE_LABEL)))
        .thenReturn(Collections.singletonList(fundingTypeDTO));
    when(referenceService.findCurrentFundingTypesByLabelIn(
        Collections.singleton(FUNDING_TYPE_LABEL2)))
        .thenReturn(Collections.singletonList(fundingTypeDTO));
    when(referenceService.findCurrentFundingTypesByLabelIn(
        Collections.singleton(FUNDING_TYPE_LABEL3)))
        .thenReturn(Lists.newArrayList(multipleFundingTypeDTO, multipleFundingTypeDTO));
    when(referenceService.findCurrentFundingTypesByLabelIn(
        Collections.singleton(FUNDING_TYPE_LABEL4)))
        .thenReturn(Collections.singletonList(academicFundingTypeDto));
    when(referenceService.findCurrentFundingSubTypesForFundingTypeId(FUNDING_TYPE_ID))
        .thenReturn(Collections.singletonList(fundingSubTypeDto));

    validDTO = buildMockFundingTypeDTO(1L, FUNDING_TYPE_LABEL, FUNDING_SUBTYPE_UUID, null, "foo");
    fundingTypeMissingDTO = buildMockFundingTypeDTO(1L, null, null, "info", "foo");
    notOtherDTO = buildMockFundingTypeDTO(1L, FUNDING_TYPE_LABEL, null, "info", "foo");
    academicTypeDTO = buildMockFundingTypeDTO(1L, FUNDING_TYPE_LABEL4, null, "info", "foo");
    fundingTypeFilledDTO = buildMockFundingTypeDTO(1L, FUNDING_TYPE_LABEL2, null, null, "foo");
    multipleFundingTypesDTO = buildMockFundingTypeDTO(1L, FUNDING_TYPE_LABEL3, null, "info", "foo");
    fundingSubTypeNotFoundDto = buildMockFundingTypeDTO(1L, FUNDING_TYPE_LABEL,
        FUNDING_SUBTYPE_UUID2, null, null);
  }

  @Test
  public void testValidateFailsIfIdIsEmpty() {
    pfDTOs = buildCheckedList(fundingTypeMissingDTO);

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);

    assertThat(result.get(0).getMessageList().contains(PostFundingValidator.FUNDING_TYPE_EMPTY),
        is(true));
  }

  @Test
  public void testValidateFailsInfoGivenForFundingTypeNotOtherOrNotAcademic() {
    pfDTOs = buildCheckedList(notOtherDTO);
    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(
        result.get(0).getMessageList()
            .contains(PostFundingValidator.FUNDING_DETAILS_NOT_ALLOWED),
        is(true));
  }

  @Test
  public void testValidateFailsIfFundingTypeNotFound() {
    pfDTOs = buildCheckedList(fundingTypeFilledDTO);
    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(
        result.get(0).getMessageList().contains(PostFundingValidator.FUNDING_TYPE_NOT_FOUND_ERROR),
        is(true));
  }

  @Test
  public void testValidateFailsIfFundingTypeIsNotUnique() {
    pfDTOs = buildCheckedList(multipleFundingTypesDTO);
    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(result.get(0).getMessageList()
        .contains(PostFundingValidator.FUNDING_TYPE_MULTIPLE_FOUND_ERROR), is(true));
  }

  @Test
  public void testValidateSuccessfully() {
    pfDTOs = buildCheckedList(validDTO);
    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(result.get(0).getMessageList().isEmpty(), is(true));
  }

  @Test
  public void testValidateOKInfoGivenForFundingTypeAcademic() {
    pfDTOs = buildCheckedList(academicTypeDTO);
    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    assertThat(result.get(0).getMessageList().isEmpty(), is(true));
  }

  @Test
  public void testValidateFailsIfFundingSubTypeNotFound() {
    pfDTOs = buildCheckedList(fundingSubTypeNotFoundDto);

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDTOs);
    List<String> errorMsgList = result.get(0).getMessageList();
    assertThat(errorMsgList.size(), is(1));
    assertThat(errorMsgList.contains(PostFundingValidator.FUNDING_SUB_TYPE_NOT_FOUND), is(true));
  }
}
