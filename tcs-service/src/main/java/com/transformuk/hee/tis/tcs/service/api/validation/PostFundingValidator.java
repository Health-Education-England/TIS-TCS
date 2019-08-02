package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFundingValidator {

  private static final String Post_Funding_DTO_NAME = "PostFundingDTO";
  @Autowired
  private ReferenceServiceImpl referenceService;

  public List<PostFundingDTO> validateFundingType(List<PostFundingDTO> checkList) {
    if (checkList.size() == 0) {
      return checkList;
    }
    String FUNDING_TYPE_EMPTY = "funding type is empty";
    String FUNDING_TYPE_NOT_OTHER_ERROR = "funding type specified filled although type is not Other.";
    String FUNDING_TYPE_NOT_FOUND_ERROR = "funding type does not exist.";
    String FUNDING_TYPE_MULTIPLE_FOUND_ERROR = "found multiple funding type.";
    Set<String> labels = new HashSet<>();
    for (PostFundingDTO pfDTO : checkList) {
      labels.add(pfDTO.getFundingType());
    }
    List<FundingTypeDTO> fundingTypeDTOs = referenceService
        .findCurrentFundingTypesByLabelIn(labels);
    // check if the funding type is unique in the fundingType table in reference
    for (PostFundingDTO pfDTO : checkList) {

      // check if funding type is empty
      if (pfDTO.getFundingType() == null || pfDTO.getFundingType().isEmpty()) {
        pfDTO.getMessageList().add(FUNDING_TYPE_EMPTY);
        break;
      }

      // check if funding type is not Other but an other value has been entered for for the row.
      if (!pfDTO.getFundingType().equals("Other") && pfDTO.getInfo() != null) {
        if (!pfDTO.getInfo().isEmpty()) {
          pfDTO.getMessageList().add(FUNDING_TYPE_NOT_OTHER_ERROR);
        }
      }

      int count = 0;
      for (FundingTypeDTO fundingTypeDTO : fundingTypeDTOs) {
        if (StringUtils.equals(fundingTypeDTO.getLabel(), pfDTO.getFundingType())) {
          count++;
        }
      }
      if (count == 0) {
        pfDTO.getMessageList().add(FUNDING_TYPE_NOT_FOUND_ERROR);
      } else if (count > 1) {
        pfDTO.getMessageList().add(FUNDING_TYPE_MULTIPLE_FOUND_ERROR);
      }
    }
    return checkList;
  }
}
