package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity Placement and its DTO PlacementDTO.
 */
@Component
public class PlacementViewMapper {

  @Autowired
  private PersonMapper personMapper;

  @Autowired
  private PostMapper postMapper;

  @Autowired
  private PlacementSpecialtyMapper placementSpecialtyMapper;

  public PlacementViewDTO placementToPlacementViewDTO(Placement placement, Boolean includePost, Boolean includeTrainee) {
    PlacementViewDTO placementDTO = null;
    if (placement != null) {
      placementDTO = new PlacementViewDTO();
      placementDTO.setId(placement.getId());
      placementDTO.setClinicalSupervisor(personMapper.toDto(placement.getClinicalSupervisor(), false));
      if (includeTrainee) {
        placementDTO.setTrainee(personMapper.toDto(placement.getTrainee(), false));
      }
      if (includePost) {
        placementDTO.setPost(postMapper.postToPostDTO(placement.getPost(), false));
      }
      placementDTO.setGradeId(placement.getGradeId());
      placementDTO.setSiteId(placement.getSiteId());
      placementDTO.setDateFrom(placement.getDateFrom());
      placementDTO.setDateTo(placement.getDateTo());
      placementDTO.setIntrepidId(placement.getIntrepidId());
      placementDTO.setLocalPostNumber(placement.getLocalPostNumber());
      placementDTO.setPlacementType(placement.getPlacementType());
      placementDTO.setTrainingDescription(placement.getTrainingDescription());
      placementDTO.setPlacementWholeTimeEquivalent(placement.getPlacementWholeTimeEquivalent());
      placementDTO.setSpecialties(placementSpecialtyMapper.toDTOs(placement.getSpecialties()));
    }
    return placementDTO;
  }

  public List<PlacementViewDTO> placementsToPlacementViewDTOs(List<Placement> placements, Boolean includePost, Boolean includeTrainee) {
    return placements.stream().map(p -> placementToPlacementViewDTO(p, includePost, includeTrainee)).collect(Collectors.toList());
  }
}
