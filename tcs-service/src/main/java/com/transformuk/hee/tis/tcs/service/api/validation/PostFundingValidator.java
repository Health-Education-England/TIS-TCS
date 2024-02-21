package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PostFundingValidator {

  protected static final String FUNDING_TYPE_EMPTY = "Funding type is empty";
  protected static final String FUNDING_DETAILS_NOT_ALLOWED =
      "Funding details is not allowed for the funding type specified.";
  protected static final String FUNDING_TYPE_NOT_FOUND_ERROR = "Funding type does not exist.";
  protected static final String FUNDING_TYPE_MULTIPLE_FOUND_ERROR = "Found multiple funding types.";
  protected static final String FUNDING_SUB_TYPE_NOT_FOUND =
      "Funding sub type not found for this funding type";

  private ReferenceServiceImpl referenceService;

  public PostFundingValidator(ReferenceServiceImpl referenceService) {
    this.referenceService = referenceService;
  }

  public List<PostFundingDTO> validateFundingType(List<PostFundingDTO> checkList) {
    if (checkList.isEmpty()) {
      return checkList;
    }

    Set<String> labels = checkList.stream().map(PostFundingDTO::getFundingType)
        .collect(Collectors.toSet());
    List<FundingTypeDTO> fundingTypeDtos = referenceService
        .findCurrentFundingTypesByLabelIn(labels);

    // check if the funding type is unique in the fundingType table in reference
    for (PostFundingDTO pfDto : checkList) {

      // check if funding type is empty
      if (StringUtils.isEmpty(pfDto.getFundingType())) {
        pfDto.getMessageList().add(FUNDING_TYPE_EMPTY);
        break;
      }

      FundingTypeDTO matchedFundingTypeDto = checkFundingTypeExists(pfDto, fundingTypeDtos);
      if (matchedFundingTypeDto != null) {
        checkInfoForFundingType(pfDto, matchedFundingTypeDto);
        checkFundingSubType(pfDto, matchedFundingTypeDto.getId());
      }
    }
    return checkList;
  }

  private FundingTypeDTO checkFundingTypeExists(PostFundingDTO pfDto,
      List<FundingTypeDTO> fundingTypeDtos) {
    List<FundingTypeDTO> filteredFundingTypeDtos = fundingTypeDtos.stream().filter(
        fundingTypeDto -> StringUtils.equalsIgnoreCase(fundingTypeDto.getLabel(),
            pfDto.getFundingType())).collect(Collectors.toList());
    if (filteredFundingTypeDtos.size() == 1) {
      FundingTypeDTO matchedFundingTypeDto = filteredFundingTypeDtos.get(0);
      // When fundingType is verified, use the label from reference object
      pfDto.setFundingType(matchedFundingTypeDto.getLabel());
      return matchedFundingTypeDto;
    } else if (filteredFundingTypeDtos.isEmpty()) {
      pfDto.getMessageList().add(FUNDING_TYPE_NOT_FOUND_ERROR);
    } else {
      pfDto.getMessageList().add(FUNDING_TYPE_MULTIPLE_FOUND_ERROR);
    }
    return null;
  }

  private void checkInfoForFundingType(PostFundingDTO pfDto, FundingTypeDTO matchedFundingTypeDto) {
    // Should not have fundingDetails populated when allowDetails in FundingType reference is false
    if (!matchedFundingTypeDto.isAllowDetails() && pfDto.getInfo() != null) {
      pfDto.getMessageList().add(FUNDING_DETAILS_NOT_ALLOWED);
    }
  }

  private void checkFundingSubType(PostFundingDTO pfDto, Long fundingTypeId) {
    UUID fundingSubTypeId = pfDto.getFundingSubTypeId();
    if (fundingSubTypeId != null) {
      List<FundingSubTypeDto> allCurrentFundingSubTypes =
          referenceService.findCurrentFundingSubTypesForFundingTypeId(fundingTypeId);
      boolean notFound = allCurrentFundingSubTypes.stream()
          .noneMatch(dto -> dto.getId().equals(fundingSubTypeId));
      if (notFound) {
        pfDto.getMessageList().add(FUNDING_SUB_TYPE_NOT_FOUND);
      }
    }
  }
}
