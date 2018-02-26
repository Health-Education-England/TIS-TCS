package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity GdcDetails and its DTO GdcDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface GdcDetailsMapper extends EntityMapper<GdcDetailsDTO, GdcDetails> {
	List<GdcDetailsDTO> gdcDetailsToGdcDetailsDTO(List<GdcDetails> gdcDetailsList);

}
