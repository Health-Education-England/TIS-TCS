package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.service.model.Funding;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.*;

@Component
public class PostFundingValidator {

  private static final String Post_Funding_DTO_NAME = "PostFundingDTO";
  @Autowired
  private ReferenceServiceImpl referenceService;

  public Map<PostFundingDTO, List<String>> validateFundingType(Set<PostFundingDTO> pfDTOs) {
    if (pfDTOs.isEmpty()) {
      return null;
    }
    Map<PostFundingDTO, List<String>> checkFailedMap = new HashMap<>();
    String NOT_FOUND_ERROR = "funding type does not exist.";
    String MULTIPLE_FOUND_ERROR = "found multiple funding type.";
    Set<String> labels= new HashSet<>();
    for (PostFundingDTO pfDTO: pfDTOs) {
      labels.add(pfDTO.getFundingType());
    }
    List<FundingTypeDTO> fundingTypeDTOs = referenceService.findCurrentFundingTypesByLabelIn(labels);
    // check if the funding type is unique in the fundingType table in reference
    for (PostFundingDTO pfDTO: pfDTOs) {
      List<String> errorList = new ArrayList<>();
      int count = 0;
      for (FundingTypeDTO fundingTypeDTO: fundingTypeDTOs) {
        if (StringUtils.equals(fundingTypeDTO.getLabel(), pfDTO.getFundingType())) {
          count++;
        }
      }
      if (count == 0) {
        errorList.add(NOT_FOUND_ERROR);
      } else if (count > 1) {
        errorList.add(MULTIPLE_FOUND_ERROR);
      }
      if (errorList.size() != 0) {
        checkFailedMap.put(pfDTO, errorList);
      }
    }
    return checkFailedMap;
  }
}
