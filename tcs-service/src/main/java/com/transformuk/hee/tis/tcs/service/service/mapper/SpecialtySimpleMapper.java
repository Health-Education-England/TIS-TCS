package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtySimpleDTO;
import com.transformuk.hee.tis.tcs.service.model.SpecialtySimple;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity SpecialtySimple and its DTO SpecialtyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpecialtySimpleMapper {

  SpecialtySimpleDTO specialtySimpleToSpecialtyDTO(SpecialtySimple specialty);

  List<SpecialtySimpleDTO> specialtiesSimpleToSpecialtyDTOs(List<SpecialtySimple> specialties);

}
