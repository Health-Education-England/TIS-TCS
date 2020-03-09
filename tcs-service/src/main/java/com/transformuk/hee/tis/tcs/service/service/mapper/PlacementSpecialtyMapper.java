package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class PlacementSpecialtyMapper {

  public Set<PlacementSpecialtyDTO> toDTOs(Set<PlacementSpecialty> placementSpecialties) {
    Set<PlacementSpecialtyDTO> specialties = null;
    if (CollectionUtils.isNotEmpty(placementSpecialties)) {
      specialties = Sets.newHashSet();
      for (PlacementSpecialty placementSpecialty : placementSpecialties) {
        PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
        placementSpecialtyDTO.setPlacementId(placementSpecialty.getPlacement().getId());
        placementSpecialtyDTO
            .setPlacementSpecialtyType(placementSpecialty.getPlacementSpecialtyType());
        placementSpecialtyDTO.setSpecialtyId(placementSpecialty.getSpecialty().getId());
        placementSpecialtyDTO.setSpecialtyName(placementSpecialty.getSpecialty().getName());
        specialties.add(placementSpecialtyDTO);
      }
    }
    return specialties;
  }

  public Set<PlacementSpecialty> toEntities(Set<PlacementSpecialtyDTO> placementSpecialtyDTOs) {
    Set<PlacementSpecialty> placementSpecialties = null;
    if (CollectionUtils.isNotEmpty(placementSpecialtyDTOs)) {
      placementSpecialties = Sets.newHashSet();
      for (PlacementSpecialtyDTO placementSpecialtyDTO : placementSpecialtyDTOs) {
        PlacementSpecialty placementSpecialty = new PlacementSpecialty();

        Placement placement = new Placement();
        placement.setId(placementSpecialtyDTO.getPlacementId());
        placementSpecialty.setPlacement(placement);

        Specialty specialty = new Specialty();
        specialty.setId(placementSpecialtyDTO.getSpecialtyId());
        placementSpecialty.setSpecialty(specialty);

        placementSpecialty
            .setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());

        placementSpecialties.add(placementSpecialty);
      }
    }
    return placementSpecialties;
  }
}
