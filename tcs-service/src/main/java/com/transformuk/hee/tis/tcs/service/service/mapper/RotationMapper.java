package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Rotation and its DTO RotationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RotationMapper extends EntityMapper<RotationDTO, Rotation> {

  Rotation fromId(Long id);
}
