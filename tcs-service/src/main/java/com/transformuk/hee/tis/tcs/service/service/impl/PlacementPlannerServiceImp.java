package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
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
import org.springframework.util.StopWatch;

import java.time.LocalDate;
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

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Set<Post> foundPosts = postRepository.findPostsAndPlacementsByProgrammeIdAndSpecialtyId(programmeId, specialtyId);
    stopWatch.stop();
    LOG.info("Time it took to run query: {}", stopWatch.toString());

    stopWatch = new StopWatch();
    stopWatch.start();
    LOG.info("We have {} posts", foundPosts.size());
    Set<Long> siteIds = getSiteIdsForPosts(foundPosts);
    LOG.info("We have {} sites", siteIds.size());

    List<com.transformuk.hee.tis.reference.api.dto.SiteDTO> foundSites = referenceService.findSitesIdIn(siteIds);

    Map<Long, com.transformuk.hee.tis.reference.api.dto.SiteDTO> siteIdToSite2 = getSiteIdsToSites(foundSites);

    Map<SiteDTO, Map<Post, List<Placement>>> formattedData2 = orderPostsIntoFormat(foundPosts, siteIdToSite2, fromDate, toDate);

    SpecialtyDTO specialtyDTO = placementPlannerMapper.convertSpecialty(specialty, formattedData2);
    Long count = specialtyDTO.getSites().stream().flatMap(s -> s.getPosts().stream()).flatMap(p -> p.getPlacements().stream()).count();

    result.setSpecialties(Lists.newArrayList(specialtyDTO));
    result.setTotalSpecialties(1);
    result.setTotalSites(siteIds.size());
    result.setTotalPlacements(count.intValue());

    stopWatch.stop();
    LOG.info("Time it took to process data: {}", stopWatch.toString());
    return result;
  }

  private Map<SiteDTO, Map<Post, List<Placement>>> orderPostsIntoFormat(Set<Post> foundPosts ,Map<Long, SiteDTO> siteIdToSiteDTO, LocalDate fromDate, LocalDate toDate) {
    Map<SiteDTO, Map<Post, List<Placement>>> sitesToPosts = Maps.newHashMap();

    LOG.info("We have {} posts that do have placements", foundPosts.size());

    Set<Long> postIds = foundPosts.stream().map(Post::getId).collect(Collectors.toSet());

    Set<Placement> allPlacements = placementRepository.findPlacementsByPostIds(postIds);

    for (Placement placement : allPlacements) {
      //get the site
      SiteDTO siteDTO = siteIdToSiteDTO.get(placement.getSiteId());

      //get the list of posts to site
      Map<Post, List<Placement>> postsToPlacement = Maps.newHashMap();
      if(sitesToPosts.containsKey(siteDTO)){
        postsToPlacement = sitesToPosts.get(siteDTO);
      } else {
        sitesToPosts.put(siteDTO, postsToPlacement);
      }

      Post post = placement.getPost();
      List<Placement> placements = Lists.newArrayList();
      if(postsToPlacement.containsKey(post)) {
        placements = postsToPlacement.get(post);
      } else {
        postsToPlacement.put(post, placements);
      }

      if ((placement.getDateFrom().isBefore(toDate) || placement.getDateFrom().isEqual(toDate)) &&
        (placement.getDateTo().isAfter(fromDate)) || placement.getDateTo().isEqual(fromDate)) {
        placements.add(placement);
      }

    }

    LOG.info("We have {} posts that do not have placements", foundPosts.size());
    //add posts that have missing
    for (Post foundPost : foundPosts) {
      LOG.info("post with id: {}", foundPost.getId());
      Optional<SiteDTO> optionalPrimarySite = getPrimarySite(foundPost, siteIdToSiteDTO);
      if(optionalPrimarySite.isPresent()) {
        SiteDTO siteDTO = optionalPrimarySite.get();

        if(sitesToPosts.containsKey(siteDTO)) {
          Map<Post, List<Placement>> postListMap = sitesToPosts.get(siteDTO);
          if(!postListMap.containsKey(foundPost)) {
            Map<Post, List<Placement>> emptyPlacementPosts = Maps.newHashMap();
            emptyPlacementPosts.put(foundPost, Lists.newArrayList());
            sitesToPosts.put(siteDTO, emptyPlacementPosts);
          }
        }
      }
    }

    return sitesToPosts;
  }

  private Optional<SiteDTO> getPrimarySite(Post post, Map<Long, SiteDTO> siteIdToSiteDTO){
    Optional<PostSite> optionalFirstSite = post.getSites()
      .stream()
      .filter(ps -> PostSiteType.PRIMARY.equals(ps.getPostSiteType()))
      .findFirst();

    if(optionalFirstSite.isPresent()){
      PostSite postSite = optionalFirstSite.get();
      return Optional.ofNullable(siteIdToSiteDTO.get(postSite.getSiteId()));
    } else {
      return Optional.empty();
    }
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
          if (site.getPostSiteType().equals(PostSiteType.PRIMARY)) {
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

}
