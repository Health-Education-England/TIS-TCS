package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSupervisorDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity Placement and its DTO PlacementDTO.
 */
@Component
public class PlacementMapper {

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private SpecialtyRepository specialtyRepository;
  @Autowired
  private PlacementSpecialtyMapper placementSpecialtyMapper;
  @Autowired
  private PersonLiteMapper personLiteMapper;

  public PlacementDTO placementToPlacementDTO(final Placement placement,
      Map<Long, List<PlacementSupervisorDTO>> placementSupervisorMap) {
    PlacementDTO placementDTO = null;
    if (placement != null) {
      placementDTO = new PlacementDTO();
      placementDTO.setId(placement.getId());
      placementDTO
          .setTraineeId(placement.getTrainee() != null ? placement.getTrainee().getId() : null);
      placementDTO.setPostId(placement.getPost() != null ? placement.getPost().getId() : null);
      placementDTO.setGradeId(placement.getGradeId());
      placementDTO.setGradeAbbreviation(placement.getGradeAbbreviation());
      placementDTO.setSiteId(placement.getSiteId());
      placementDTO.setSiteCode(placement.getSiteCode());
      placementDTO.setDateFrom(placement.getDateFrom());
      placementDTO.setDateTo(placement.getDateTo());
      placementDTO.setIntrepidId(placement.getIntrepidId());
      placementDTO.setLocalPostNumber(placement.getLocalPostNumber());
      placementDTO.setPlacementType(placement.getPlacementType());
      placementDTO.setTrainingDescription(placement.getTrainingDescription());
      placementDTO.setPlacementWholeTimeEquivalent(placement.getPlacementWholeTimeEquivalent());
      placementDTO.setStatus(getStatus(placement.getDateFrom(), placement.getDateTo()));
      placementDTO.setSpecialties(placementSpecialtyMapper.toDTOs(placement.getSpecialties()));
      placementDTO.setLifecycleState(placement.getLifecycleState());

      if (placementSupervisorMap != null && placementSupervisorMap.containsKey(placement.getId())) {
        List<PlacementSupervisorDTO> supervisors = placementSupervisorMap.get(placement.getId());
        placementDTO.setSupervisors(Sets.newHashSet(supervisors));

      }
    }
    return placementDTO;
  }

  public List<PlacementDTO> placementsToPlacementDTOs(final List<Placement> placements,
      Map<Long, List<PlacementSupervisorDTO>> placementSupervisorMap) {
    return placements.stream().map(p -> placementToPlacementDTO(p, placementSupervisorMap))
        .collect(Collectors.toList());
  }

  public Placement placementDTOToPlacement(final PlacementDTO placementDTO) {
    if (placementDTO == null) {
      return null;
    }

    final Placement placement = new Placement();
    placement.setId(placementDTO.getId());
    placement.setTrainee(personRepository.findById(placementDTO.getTraineeId()).orElse(null));
    if (placementDTO.getPostId() != null) {
      placement.setPost(postRepository.findById(placementDTO.getPostId()).orElse(null));
    }
    placement.setGradeId(placementDTO.getGradeId());
    placement.setGradeAbbreviation(placementDTO.getGradeAbbreviation());
    placement.setSiteCode(placementDTO.getSiteCode());
    placement.setSiteId(placementDTO.getSiteId());
    placement.setDateFrom(placementDTO.getDateFrom());
    placement.setDateTo(placementDTO.getDateTo());
    placement.setIntrepidId(placementDTO.getIntrepidId());
    placement.setLocalPostNumber(placementDTO.getLocalPostNumber());
    placement.setPlacementType(placementDTO.getPlacementType());
    placement.setTrainingDescription(placementDTO.getTrainingDescription());
    placement.setPlacementWholeTimeEquivalent(placementDTO.getPlacementWholeTimeEquivalent());
    placement.setLifecycleState(placementDTO.getLifecycleState());

    if (CollectionUtils.isNotEmpty(placementDTO.getSpecialties())) {
      final Set<PlacementSpecialty> specialties = Sets.newHashSet();
      for (final PlacementSpecialtyDTO placementSpecialtyDTO : placementDTO.getSpecialties()) {
        final PlacementSpecialty placementSpecialty = new PlacementSpecialty();
        placementSpecialty
            .setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());
        placementSpecialty.setPlacement(placement);
        placementSpecialty.setSpecialty(
            specialtyRepository.findById(placementSpecialtyDTO.getSpecialtyId()).orElse(null));
        specialties.add(placementSpecialty);
      }
      placement.setSpecialties(specialties);
    }
    return placement;
  }

  public List<Placement> placementDTOsToPlacements(final List<PlacementDTO> placementDTOs) {
    return placementDTOs.stream().map(this::placementDTOToPlacement).collect(Collectors.toList());
  }

  private PlacementStatus getStatus(final LocalDate dateFrom, final LocalDate dateTo) {
    if (dateFrom == null || dateTo == null) {
      return null;
    }

    final LocalDateTime today = LocalDateTime.now();
    if (today.isBefore(dateFrom.atStartOfDay())) {
      return PlacementStatus.FUTURE;
    } else if (today.isAfter(dateTo.atStartOfDay())) {
      return PlacementStatus.PAST;
    }
    return PlacementStatus.CURRENT;
  }

  public Placement fromId(Long id) {
    Placement placement = null;

    if (id != null) {
      placement = new Placement();
      placement.setId(id);
    }

    return placement;
  }
}
