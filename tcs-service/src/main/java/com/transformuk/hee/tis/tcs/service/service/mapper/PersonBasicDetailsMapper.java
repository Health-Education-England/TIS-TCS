package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper for the entity PersonBasicDetails and its DTO PersonBasicDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface PersonBasicDetailsMapper {

  @Mappings({
      @Mapping(target = "gmcNumber", source = "gmcDetails.gmcNumber"),
      @Mapping(target = "gdcNumber", source = "gdcDetails.gdcNumber"),
      @Mapping(target = "publicHealthNumber", source = "person.publicHealthNumber")
  })
  PersonBasicDetailsDTO toDto(PersonBasicDetails entity);

}
