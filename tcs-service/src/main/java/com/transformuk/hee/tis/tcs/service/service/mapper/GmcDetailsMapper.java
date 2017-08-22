package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity GmcDetails and its DTO GmcDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface GmcDetailsMapper extends EntityMapper<GmcDetailsDTO, GmcDetails> {


  default GmcDetails fromId(Long id) {
    if (id == null) {
      return null;
    }
    GmcDetails gmcDetails = new GmcDetails();
    gmcDetails.setId(id);
    return gmcDetails;
  }
}
