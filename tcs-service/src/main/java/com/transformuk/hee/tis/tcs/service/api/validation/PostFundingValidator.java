package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFundingValidator {

  protected static final String FUNDING_TYPE_EMPTY = "funding type is empty";
  protected static final String FUNDING_TYPE_NOT_OTHER_OR_NOT_ACADEMIC_ERROR =
      "funding type specified filled although type is not Other or not an academic type.";
  protected static final String FUNDING_TYPE_NOT_FOUND_ERROR = "funding type does not exist.";
  protected static final String FUNDING_TYPE_MULTIPLE_FOUND_ERROR = "found multiple funding type.";

  @Autowired
  private ReferenceServiceImpl referenceService;

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
      }
    }
    return checkList;
  }

  private FundingTypeDTO checkFundingTypeExists(PostFundingDTO pfDto,
      List<FundingTypeDTO> fundingTypeDtos) {
    List<FundingTypeDTO> filteredFundingTypeDtos = fundingTypeDtos.stream().filter(
        fundingTypeDto -> StringUtils.equals(fundingTypeDto.getLabel(), pfDto.getFundingType()))
        .collect(Collectors.toList());
    if (filteredFundingTypeDtos.size() == 1) {
      return filteredFundingTypeDtos.get(0);
    } else if (filteredFundingTypeDtos.isEmpty()) {
      pfDto.getMessageList().add(FUNDING_TYPE_NOT_FOUND_ERROR);
    } else {
      pfDto.getMessageList().add(FUNDING_TYPE_MULTIPLE_FOUND_ERROR);
    }
    return null;
  }

  private void checkInfoForFundingType(PostFundingDTO pfDto, FundingTypeDTO matchedFundingTypeDto) {
    // Only when fundingType is Other or an academic type, the info(fundingDetails) is enabled.
    if (!(pfDto.getFundingType().equals("Other") || matchedFundingTypeDto.isAcademic())
        && pfDto.getInfo() != null) {
      pfDto.getMessageList().add(FUNDING_TYPE_NOT_OTHER_OR_NOT_ACADEMIC_ERROR);
    }
  }
}
