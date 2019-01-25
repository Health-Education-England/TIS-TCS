package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementsResultDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementPlannerMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class PlacementPlannerServiceImp {

  private static final int PLACEMENTS_YEARS_IN_THE_PAST = 5;
  private static final int PLACEMENTS_YEARS_IN_THE_FUTURE = 5;

  @Autowired
  private PlacementRepository placementRepository;
  @Autowired
  private SpecialtyRepository specialtyRepository;
  @Autowired
  private ReferenceService referenceService;
  @Autowired
  private PlacementPlannerMapper placementPlannerMapper;

  @Transactional(readOnly = true)
  public PlacementsResultDTO findPlacementsForProgrammeAndSpecialty(Long programmeId, Long specialtyId,
                                                                    LocalDate fromDate, LocalDate toDate) {
    Preconditions.checkNotNull(programmeId, "Programme Id cannot be null");
    Preconditions.checkNotNull(specialtyId, "Specialty Id cannot be null");

    PlacementsResultDTO result = new PlacementsResultDTO();
    Optional<Specialty> optionalSpecialty = specialtyRepository.findById(specialtyId);

    if (!optionalSpecialty.isPresent()) {
      return result;
    }

    Specialty specialty = optionalSpecialty.get();

    if (fromDate == null) {
      fromDate = LocalDate.now().minusYears(PLACEMENTS_YEARS_IN_THE_PAST);
    }
    if (toDate == null) {
      toDate = LocalDate.now().plusYears(PLACEMENTS_YEARS_IN_THE_FUTURE);
    }

    List<Placement> foundPlacements = placementRepository.findPlacementsByProgrammeIdAndSpecialtyId(programmeId, specialtyId, fromDate, toDate);

    Set<Long> siteIds = getSiteIdsForPlacements(foundPlacements);
    List<com.transformuk.hee.tis.reference.api.dto.SiteDTO> foundSites = referenceService.findSitesIdIn(siteIds);
    Map<Long, com.transformuk.hee.tis.reference.api.dto.SiteDTO> siteIdToSite = getSiteIdsToSites(foundSites);

    Map<com.transformuk.hee.tis.reference.api.dto.SiteDTO, Set<Post>> siteToPosts = getSiteToPosts(siteIdToSite, foundPlacements);
    Map<Post, Set<Placement>> postsToPlacements = getPostToPlacements(foundPlacements);

    result.setSpecialties(Lists.newArrayList(placementPlannerMapper.convertSpecialty(specialty, foundSites, siteToPosts, postsToPlacements)));
    result.setTotalSpecialties(1);
    result.setTotalSites(siteIds.size());
    result.setTotalPlacements(foundPlacements.size());
    return result;
  }

  private Map<Long, SiteDTO> getSiteIdsToSites(List<SiteDTO> foundSites) {
    return foundSites
        .stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toMap((s -> s.getId()), (s -> s)));
  }

  private Set<Long> getSiteIdsForPlacements(List<Placement> foundPlacements) {
    return foundPlacements.stream()
        .filter(Objects::nonNull)
        .filter(p -> Objects.nonNull(p.getSiteId()))
        .map(Placement::getSiteId)
        .collect(Collectors.toSet());
  }

  private Map<com.transformuk.hee.tis.reference.api.dto.SiteDTO, Set<Post>> getSiteToPosts(Map<Long,
      com.transformuk.hee.tis.reference.api.dto.SiteDTO> siteIdToSite, List<Placement> placements) {

    Map<com.transformuk.hee.tis.reference.api.dto.SiteDTO, Set<Post>> result = Maps.newHashMap();

    for (Placement placement : placements) {
      Set<Post> posts = Sets.newHashSet();
      Post placementPost = placement.getPost();
      com.transformuk.hee.tis.reference.api.dto.SiteDTO siteDTO = siteIdToSite.get(placement.getSiteId());
      if (result.containsKey(siteDTO)) {
        posts = result.get(siteDTO);
      }
      posts.add(placementPost);
      result.put(siteDTO, posts);
    }
    return result;
  }

  private Map<Post, Set<Placement>> getPostToPlacements(List<Placement> placements) {
    Map<Post, Set<Placement>> result = Maps.newHashMap();
    for (Placement placement : placements) {
      Post post = placement.getPost();
      Set<Placement> placementsForPost = new TreeSet<>((o1, o2) -> ObjectUtils.compare(o1.getDateFrom(), o2.getDateFrom()));
      if (result.containsKey(post)) {
        placementsForPost = result.get(post);
      }
      placementsForPost.add(placement);
      result.put(post, placementsForPost);
    }
    return result;
  }

}
