package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostFundingValidator {

  private static final String Post_Funding_DTO_NAME = "PostFundingDTO";
  @Autowired
  private ReferenceServiceImpl referenceService;

  public List<FieldError> validateFundingType(PostFundingDTO pfDTO) {
    String label = pfDTO.getFundingType();
    List<FieldError> fieldErrors = new ArrayList<>();

    List<FundingTypeDTO> fundingTypeDTOs = referenceService.findFundingTypeByLabel(label);
    int size = fundingTypeDTOs.size();
    if (size == 0) {
      fieldErrors.add(new FieldError(Post_Funding_DTO_NAME, "fundingType",
        String.format("funding type %s does not exist.", label)));
    } else if (size > 1) {
      fieldErrors.add(new FieldError(Post_Funding_DTO_NAME, "fundingType",
        String.format("funding type %s is not unique.", label)));
    }
    return fieldErrors;
  }
}
