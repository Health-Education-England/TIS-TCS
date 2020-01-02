package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {PlacementMapper.class, SpecialtyMapper.class})
public interface PlacementSpecialtyMapper {

  @Mappings({
      @Mapping(target = "placementId", source = "placement.id"),
      @Mapping(target = "specialtyId", source = "specialty.id"),
      @Mapping(target = "specialtyName", source = "specialty.name")
  })
  PlacementSpecialtyDTO toDto(PlacementSpecialty placementSpecialty);

  Set<PlacementSpecialtyDTO> toDTOs(Set<PlacementSpecialty> placementSpecialties);

  @Mappings({
      @Mapping(target = "placement", expression = "java(new Placement() {{ setId(placementSpecialtyDto.getPlacementId()); }})"),
      @Mapping(target = "specialty", expression = "java(new Specialty() {{ setId(placementSpecialtyDto.getSpecialtyId()); }})")
  })
  PlacementSpecialty toEntity(PlacementSpecialtyDTO placementSpecialtyDto);

  Set<PlacementSpecialty> toEntities(Set<PlacementSpecialtyDTO> placementSpecialtyDTOs);
}
