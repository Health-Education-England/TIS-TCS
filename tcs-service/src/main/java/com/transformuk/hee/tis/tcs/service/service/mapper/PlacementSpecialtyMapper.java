package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PlacementSpecialtyMapper {

  public Set<PlacementSpecialtyDTO> toDTOs(Set<PlacementSpecialty> placementSpecialties) {
    Set<PlacementSpecialtyDTO> specialties = null;
    if (CollectionUtils.isNotEmpty(placementSpecialties)) {
      specialties = Sets.newHashSet();
      for (PlacementSpecialty placementSpecialty : placementSpecialties) {
        PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
        placementSpecialtyDTO.setPlacementId(placementSpecialty.getPlacement().getId());
        placementSpecialtyDTO.setPlacementSpecialtyType(placementSpecialty.getPlacementSpecialtyType());
        placementSpecialtyDTO.setSpecialtyId(placementSpecialty.getSpecialty().getId());
        specialties.add(placementSpecialtyDTO);
      }
    }
    return specialties;
  }
}
