package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity GmcDetails and its DTO GmcDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface GmcDetailsMapper extends EntityMapper<GmcDetailsDTO, GmcDetails> {

  List<GmcDetailsDTO> gmcDetailsToGmcDetailsDTO(List<GmcDetails> gmcDetailsList);

}
