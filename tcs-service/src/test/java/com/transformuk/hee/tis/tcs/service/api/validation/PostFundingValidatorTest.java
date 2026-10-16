package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostFundingValidatorTest {

  private static final Long FUNDING_TYPE_ID_1 = 1L;
  private static final Long FUNDING_TYPE_ID_2 = 2L;
  private static final Long FUNDING_TYPE_ID_3 = 3L;
  private static final Long FUNDING_TYPE_ID_4 = 4L;
  private static final UUID FUNDING_SUBTYPE_UUID = UUID.randomUUID();
  private static final UUID FUNDING_SUBTYPE_UUID2 = UUID.randomUUID();
  FundingTypeDTO fundingTypeDto;
  FundingTypeDTO academicFundingTypeDto;
  FundingTypeDTO inactiveFundingTypeDto;
  FundingSubTypeDto fundingSubTypeDto;
  private List<PostFundingDTO> pfDtos;
  private PostFundingDTO fundingTypeMissingDto;
  private PostFundingDTO notOtherDto;
  private PostFundingDTO validDto;
  private PostFundingDTO fundingTypeFilledDto;
  private PostFundingDTO fundingInactiveDto;
  private PostFundingDTO academicTypeDto;
  private PostFundingDTO fundingSubTypeNotFoundDto;

  @Mock
  private ReferenceServiceImpl referenceService;

  @InjectMocks
  private PostFundingValidator postFundingValidator;

  private PostFundingDTO buildMockFundingTypeDTO(Long id, Long fundingTypeId, UUID fundingSubType,
      String info, String fundingBodyId) {
    PostFundingDTO result = new PostFundingDTO();
    result.setId(id);
    result.setFundingTypeId(fundingTypeId);
    result.setFundingSubTypeId(fundingSubType);
    result.setInfo(info);
    result.setFundingBodyId(fundingBodyId);
    result.setStartDate(LocalDate.now());
    result.setEndDate(LocalDate.now().plusMonths(1));
    return result;
  }

  private List<PostFundingDTO> buildCheckedList(PostFundingDTO pfDTO) {
    pfDtos = new ArrayList<>();
    pfDtos.add(pfDTO);
    return pfDtos;
  }

  @BeforeEach
  void setup() {
    fundingTypeDto = new FundingTypeDTO();
    fundingTypeDto.setId(FUNDING_TYPE_ID_1);
    fundingTypeDto.setAllowDetails(false);
    fundingTypeDto.setStatus(Status.CURRENT);

    inactiveFundingTypeDto = new FundingTypeDTO();
    inactiveFundingTypeDto.setId(FUNDING_TYPE_ID_3);
    inactiveFundingTypeDto.setStatus(Status.INACTIVE);

    academicFundingTypeDto = new FundingTypeDTO();
    academicFundingTypeDto.setId(FUNDING_TYPE_ID_4);
    academicFundingTypeDto.setAllowDetails(true);
    academicFundingTypeDto.setStatus(Status.CURRENT);

    fundingSubTypeDto = new FundingSubTypeDto();
    fundingSubTypeDto.setId(FUNDING_SUBTYPE_UUID);
    fundingSubTypeDto.setStatus(Status.CURRENT);

    validDto = buildMockFundingTypeDTO(1L, FUNDING_TYPE_ID_1, FUNDING_SUBTYPE_UUID, null, "foo");
    fundingTypeMissingDto = buildMockFundingTypeDTO(1L, null, null, "info", "foo");
    notOtherDto = buildMockFundingTypeDTO(1L, FUNDING_TYPE_ID_1, null, "info", "foo");
    academicTypeDto = buildMockFundingTypeDTO(1L, FUNDING_TYPE_ID_4, null, "info", "foo");
    fundingTypeFilledDto = buildMockFundingTypeDTO(1L, FUNDING_TYPE_ID_2, null, null, "foo");
    fundingInactiveDto = buildMockFundingTypeDTO(1L, FUNDING_TYPE_ID_3, null, "info", "foo");
    fundingSubTypeNotFoundDto = buildMockFundingTypeDTO(1L, FUNDING_TYPE_ID_1,
        FUNDING_SUBTYPE_UUID2, null, null);
  }

  @Test
  void testValidateFailsIfIdIsEmpty() {
    pfDtos = buildCheckedList(fundingTypeMissingDto);

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDtos);

    assertThat(
        result.get(0).getMessageList().contains(PostFundingValidator.FUNDING_TYPE_ID_REQUIRED),
        is(true));
  }

  @Test
  void testValidateFailsInfoGivenForFundingTypeNotOtherOrNotAcademic() {
    pfDtos = buildCheckedList(notOtherDto);
    when(referenceService.findFundingTypesIdIn(Set.of(FUNDING_TYPE_ID_1)))
        .thenReturn(Collections.singletonList(fundingTypeDto));

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDtos);
    assertThat(
        result.get(0).getMessageList()
            .contains(PostFundingValidator.FUNDING_DETAILS_NOT_ALLOWED),
        is(true));
  }

  @Test
  void testValidateFailsIfFundingTypeNotFound() {
    pfDtos = buildCheckedList(fundingTypeFilledDto);
    when(referenceService.findFundingTypesIdIn(Set.of(FUNDING_TYPE_ID_2)))
        .thenReturn(Collections.emptyList());

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDtos);
    assertThat(
        result.get(0).getMessageList().contains(PostFundingValidator.FUNDING_TYPE_NOT_FOUND_ERROR),
        is(true));
  }

  @Test
  void testValidateSuccessfully() {
    validDto.setFundingTypeId(FUNDING_TYPE_ID_1);
    pfDtos = buildCheckedList(validDto);

    when(referenceService.findFundingTypesIdIn(Set.of(FUNDING_TYPE_ID_1)))
        .thenReturn(Collections.singletonList(fundingTypeDto));
    when(referenceService.findCurrentFundingSubTypesForFundingTypeId(FUNDING_TYPE_ID_1))
        .thenReturn(Collections.singletonList(fundingSubTypeDto));

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDtos);

    assertThat(result.get(0).getMessageList().isEmpty(), is(true));
    assertThat(result.get(0).getFundingTypeId(), is(FUNDING_TYPE_ID_1));
  }

  @Test
  void testValidateOKInfoGivenForFundingTypeAcademic() {
    pfDtos = buildCheckedList(academicTypeDto);

    when(referenceService.findFundingTypesIdIn(Set.of(FUNDING_TYPE_ID_4)))
        .thenReturn(Collections.singletonList(academicFundingTypeDto));

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDtos);
    assertThat(result.get(0).getMessageList().isEmpty(), is(true));
  }

  @Test
  void testValidateFailsIfFundingSubTypeNotFound() {
    pfDtos = buildCheckedList(fundingSubTypeNotFoundDto);

    when(referenceService.findFundingTypesIdIn(Set.of(FUNDING_TYPE_ID_1)))
        .thenReturn(Collections.singletonList(fundingTypeDto));
    when(referenceService.findCurrentFundingSubTypesForFundingTypeId(FUNDING_TYPE_ID_1))
        .thenReturn(Collections.singletonList(fundingSubTypeDto));

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDtos);
    List<String> errorMsgList = result.get(0).getMessageList();
    assertThat(errorMsgList.size(), is(1));
    assertThat(errorMsgList.contains(PostFundingValidator.FUNDING_SUB_TYPE_NOT_FOUND), is(true));
  }

  @Test
  void testValidateFailsIfFundingTypeFoundButInactive() {
    pfDtos = buildCheckedList(fundingInactiveDto);

    when(referenceService.findFundingTypesIdIn(Set.of(FUNDING_TYPE_ID_3)))
        .thenReturn(Collections.singletonList(inactiveFundingTypeDto));

    List<PostFundingDTO> result = postFundingValidator.validateFundingType(pfDtos);
    List<String> errorMsgList = result.get(0).getMessageList();
    assertThat(errorMsgList.size(), is(1));
    assertThat(errorMsgList.contains(PostFundingValidator.FUNDING_TYPE_NOT_FOUND_ERROR), is(true));
  }
}
