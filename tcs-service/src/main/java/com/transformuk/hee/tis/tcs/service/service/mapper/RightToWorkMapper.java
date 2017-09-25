package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity RightToWork and its DTO RightToWorkDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RightToWorkMapper extends EntityMapper<RightToWorkDTO, RightToWork> {
}
