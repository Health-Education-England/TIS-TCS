package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementsResultDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementPlannerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlacementPlannerServiceImp {

  private static final int PLACEMENTS_YEARS_IN_THE_PAST = 1;
  private static final int PLACEMENTS_YEARS_IN_THE_FUTURE = 1;
  private static final Logger LOG = LoggerFactory.getLogger(PlacementPlannerServiceImp.class);

  @Autowired
  private PlacementRepository placementRepository;
  @Autowired
  private PostRepository postRepository;
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

    System.out.println(foundPlacements);

    Set<Long> siteIds = getSiteIdsForPlacements(foundPlacements);
    List<com.transformuk.hee.tis.reference.api.dto.SiteDTO> foundSites = referenceService.findSitesIdIn(siteIds);
    Map<Long, com.transformuk.hee.tis.reference.api.dto.SiteDTO> siteIdToSite = getSiteIdsToSites(foundSites);
    Map<SiteDTO, Map<Post, List<Placement>>> formattedData = orderPlacementsIntoFormat(foundPlacements, siteIdToSite);
    System.out.println(formattedData);

    Set<Post> foundPosts = postRepository.findPostsAndPlacementsByProgrammeIdAndSpecialtyId(programmeId, specialtyId);
    System.out.println(foundPosts);

    Set<Long> postIds = getSiteIdsForPosts(foundPosts);
    System.out.println(postIds);

    Map<Long, com.transformuk.hee.tis.reference.api.dto.SiteDTO> siteIdToSite2 = getSiteIdsToSites(foundSites);

    Map<SiteDTO, Map<Post, List<Placement>>> formattedData2 = orderPostsIntoFormat(foundPosts, siteIdToSite2, fromDate, toDate);
    System.out.println((formattedData2));

    SpecialtyDTO specialtyDTO = placementPlannerMapper.convertSpecialty(specialty, formattedData2);
    Long count = specialtyDTO.getSites().stream().flatMap(s -> s.getPosts().stream()).flatMap(p -> p.getPlacements().stream()).count();

    result.setSpecialties(Lists.newArrayList(specialtyDTO));
    result.setTotalSpecialties(1);
    result.setTotalSites(siteIds.size());
    result.setTotalPlacements(count.intValue());

    return result;
  }

  private Map<SiteDTO, Map<Post, List<Placement>>> orderPostsIntoFormat(Set<Post> foundPosts, Map<Long, SiteDTO> siteIdToSiteDTO, LocalDate fromDate, LocalDate toDate) {
    Map<SiteDTO, Map<Post, List<Placement>>> sitesToPosts = Maps.newHashMap();

    for (Post foundPost: foundPosts) {

      Map<Post, List<Placement>> postsToPlacements = Maps.newHashMap();
      List<Placement> postPlacements = new ArrayList<>();

      // if the post does not have placements, add post with empty placements
      if (foundPost.getPlacementHistory().isEmpty()) {
        for (PostSite site: foundPost.getSites()) {
          Long siteId = site.getSiteId();
          if (siteIdToSiteDTO.containsKey(siteId)) {
              SiteDTO siteDTO = siteIdToSiteDTO.get(siteId);
              if (sitesToPosts.containsKey(siteDTO)) {
                postsToPlacements = sitesToPosts.get(siteDTO);
              }
              sitesToPosts.put(siteDTO, postsToPlacements);
              postsToPlacements.put(foundPost, postPlacements);
          }
        }
        // if the post has placements, add post with placements
      } else {
        for (Placement placement : foundPost.getPlacementHistory()) {
            Long siteId = placement.getSiteId();
            if (siteIdToSiteDTO.containsKey(siteId)) {
              SiteDTO siteDTO = siteIdToSiteDTO.get(siteId);
              if (sitesToPosts.containsKey(siteDTO)) {
                postsToPlacements = sitesToPosts.get(siteDTO);
              }
              sitesToPosts.put(siteDTO, postsToPlacements);

              if ((placement.getDateFrom().isBefore(toDate) && placement.getDateTo().isAfter(fromDate)) ||
                placement.getDateFrom().isEqual(fromDate) || placement.getDateTo().isEqual(toDate)
              ) {
                postPlacements.add(placement);
              }
              postsToPlacements.put(foundPost, postPlacements);
            }
          }
        }
    }
    return sitesToPosts;
  }

  private Map<SiteDTO, Map<Post, List<Placement>>> orderPlacementsIntoFormat(Set<Placement> foundPlacements, Map<Long, SiteDTO> siteIdToSiteDTO) {
    Map<SiteDTO, Map<Post, List<Placement>>> sitesToPosts = Maps.newHashMap();

    for (Placement foundPlacement : foundPlacements) {
      Long placementSiteId = foundPlacement.getSiteId();
      if (siteIdToSiteDTO.containsKey(placementSiteId)) {
        SiteDTO siteDTO = siteIdToSiteDTO.get(placementSiteId);

        Map<Post, List<Placement>> postsToPlacements = Maps.newHashMap();
        if (sitesToPosts.containsKey(siteDTO)) {
          postsToPlacements = sitesToPosts.get(siteDTO);
        }
        sitesToPosts.put(siteDTO, postsToPlacements);

        Post placementPost = foundPlacement.getPost();

        List<Placement> postPlacements = new ArrayList<>();
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

  private Set<Long> getSiteIdsForPosts(Set<Post> foundPosts) {
    Set<Long> foundIds = Sets.newHashSet();

    for (Post post: foundPosts) {
      // if the post does not have placements, add the primary site id of the post
      if (post.getPlacementHistory().isEmpty()) {
        for (PostSite site: post.getSites()) {
          if (site.getPostSiteType().equals("PRIMARY")) {
            foundIds.add(site.getSiteId());
          }
        }
      // if the post has placements, add the site id of the placement
      } else {
        for (Placement placement : post.getPlacementHistory()) {
          foundIds.add(placement.getSiteId());
        }
      }
    }
    return foundIds;
  }

  private Set<Long> getSiteIdsForPlacements(Set<Placement> foundPlacements) {
    return foundPlacements.stream()
        .filter(Objects::nonNull)
        .filter(p -> Objects.nonNull(p.getSiteId()))
        .map(Placement::getSiteId)
        .collect(Collectors.toSet());
  }

}
