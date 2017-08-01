package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.FundingComponentsDTO;
import com.transformuk.hee.tis.tcs.service.model.FundingComponents;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity FundingComponents and its DTO FundingComponentsDTO.
 */
@Mapper(componentModel = "spring", uses = {PlacementFunderMapper.class,})
public interface FundingComponentsMapper {

  FundingComponentsDTO fundingComponentsToFundingComponentsDTO(FundingComponents fundingComponents);

  List<FundingComponentsDTO> fundingComponentsToFundingComponentsDTOs(List<FundingComponents> fundingComponents);

  FundingComponents fundingComponentsDTOToFundingComponents(FundingComponentsDTO fundingComponentsDTO);

  List<FundingComponents> fundingComponentsDTOsToFundingComponents(List<FundingComponentsDTO> fundingComponentsDTOs);
}
