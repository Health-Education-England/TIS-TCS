package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementsResultDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementPlannerMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger LOG = LoggerFactory.getLogger(PlacementPlannerServiceImp.class);

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

    Set<Placement> foundPlacements = placementRepository.findPlacementsByProgrammeIdAndSpecialtyId(programmeId, specialtyId, fromDate, toDate);

    Set<Long> siteIds = getSiteIdsForPlacements(foundPlacements);
    List<com.transformuk.hee.tis.reference.api.dto.SiteDTO> foundSites = referenceService.findSitesIdIn(siteIds);
    Map<Long, com.transformuk.hee.tis.reference.api.dto.SiteDTO> siteIdToSite = getSiteIdsToSites(foundSites);
    Map<SiteDTO, Map<Post, Set<Placement>>> formattedData = orderPlacementsIntoFormat(foundPlacements, siteIdToSite);

    SpecialtyDTO specialtyDTO = placementPlannerMapper.convertSpecialty(specialty, formattedData);
    Long count = specialtyDTO.getSites().stream().flatMap(s -> s.getPosts().stream()).flatMap(p -> p.getPlacements().stream()).count();

    result.setSpecialties(Lists.newArrayList(specialtyDTO));
    result.setTotalSpecialties(1);
    result.setTotalSites(siteIds.size());
    result.setTotalPlacements(count.intValue());

    return result;
  }

  private Map<SiteDTO, Map<Post, Set<Placement>>> orderPlacementsIntoFormat(Set<Placement> foundPlacements, Map<Long, SiteDTO> siteIdToSiteDTO) {
    Map<SiteDTO, Map<Post, Set<Placement>>> sitesToPosts = Maps.newHashMap();

    for (Placement foundPlacement : foundPlacements) {
      Long placementSiteId = foundPlacement.getSiteId();
      if (siteIdToSiteDTO.containsKey(placementSiteId)) {
        SiteDTO siteDTO = siteIdToSiteDTO.get(placementSiteId);

        Map<Post, Set<Placement>> postsToPlacements = Maps.newHashMap();
        if (sitesToPosts.containsKey(siteDTO)) {
          postsToPlacements = sitesToPosts.get(siteDTO);
        }
        sitesToPosts.put(siteDTO, postsToPlacements);

        Post placementPost = foundPlacement.getPost();

        Set<Placement> postPlacements = new TreeSet<>((o1, o2) -> ObjectUtils.compare(o1.getDateFrom(), o2.getDateFrom()));
        if (postsToPlacements.containsKey(placementPost)) {
          postPlacements = postsToPlacements.get(placementPost);
        }

        postsToPlacements.put(placementPost, postPlacements);
        postPlacements.add(foundPlacement);

      } else {
        LOG.warn("Placement with id [{}] has siteId [{}] that is not in map", foundPlacement.getId(), placementSiteId);
      }
    }
    return sitesToPosts;
  }

  private Map<Long, SiteDTO> getSiteIdsToSites(List<SiteDTO> foundSites) {
    return foundSites
        .stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toMap((s -> s.getId()), (s -> s)));
  }

  private Set<Long> getSiteIdsForPlacements(Set<Placement> foundPlacements) {
    return foundPlacements.stream()
        .filter(Objects::nonNull)
        .filter(p -> Objects.nonNull(p.getSiteId()))
        .map(Placement::getSiteId)
        .collect(Collectors.toSet());
  }

}
