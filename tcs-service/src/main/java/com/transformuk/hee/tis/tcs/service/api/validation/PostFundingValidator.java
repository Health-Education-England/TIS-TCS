package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PostFundingValidator {

  protected static final String FUNDING_TYPE_ID_REQUIRED = "Funding type id is required.";
  protected static final String FUNDING_DETAILS_NOT_ALLOWED =
      "Funding details is not allowed for the funding type specified.";
  protected static final String FUNDING_TYPE_NOT_FOUND_ERROR = "Funding type does not exist.";
  protected static final String FUNDING_SUB_TYPE_NOT_FOUND =
      "Funding sub type not found for this funding type";

  private final ReferenceServiceImpl referenceService;

  public PostFundingValidator(ReferenceServiceImpl referenceService) {
    this.referenceService = referenceService;
  }

  public List<PostFundingDTO> validateFundingType(List<PostFundingDTO> checkList) {
    if (checkList.isEmpty()) {
      return checkList;
    }

    Set<Long> fundingTypeIds = checkList.stream().map(PostFundingDTO::getFundingTypeId)
        .collect(Collectors.toSet());
    List<FundingTypeDTO> fundingTypeDtos = referenceService.findFundingTypesIdIn(fundingTypeIds);

    checkList.forEach(pfDto -> {
      if (pfDto.getFundingTypeId() != null) {
        FundingTypeDTO matchedFundingTypeDto = checkFundingTypeExists(pfDto, fundingTypeDtos);
        if (matchedFundingTypeDto != null) {
          checkInfoForFundingType(pfDto, matchedFundingTypeDto);
          checkFundingSubType(pfDto, matchedFundingTypeDto.getId());
        }
      } else {
        pfDto.getMessageList().add(FUNDING_TYPE_ID_REQUIRED);
      }
    });
    return checkList;
  }

  private FundingTypeDTO checkFundingTypeExists(PostFundingDTO pfDto,
      List<FundingTypeDTO> fundingTypeDtos) {
    return fundingTypeDtos.stream()
        .filter(fundingTypeDto -> fundingTypeDto.getId().equals(pfDto.getFundingTypeId())
            && fundingTypeDto.getStatus().equals(Status.CURRENT))
        .findFirst()
        .orElseGet(() -> {
          pfDto.getMessageList().add(FUNDING_TYPE_NOT_FOUND_ERROR);
          return null;
        });
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
