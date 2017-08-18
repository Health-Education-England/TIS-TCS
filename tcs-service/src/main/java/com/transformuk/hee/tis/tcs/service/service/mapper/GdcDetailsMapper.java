package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity GdcDetails and its DTO GdcDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface GdcDetailsMapper extends EntityMapper<GdcDetailsDTO, GdcDetails> {


  default GdcDetails fromId(Long id) {
    if (id == null) {
      return null;
    }
    GdcDetails gdcDetails = new GdcDetails();
    gdcDetails.setId(id);
    return gdcDetails;
  }
}
