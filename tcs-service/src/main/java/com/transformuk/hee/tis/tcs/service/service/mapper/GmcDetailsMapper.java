package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtySimpleDTO;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.SpecialtySimple;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity GmcDetails and its DTO GmcDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface GmcDetailsMapper extends EntityMapper<GmcDetailsDTO, GmcDetails> {
    List<GmcDetailsDTO> gmcDetailsToGmcDetailsDTO(List<GmcDetails> gmcDetailsList);

}
