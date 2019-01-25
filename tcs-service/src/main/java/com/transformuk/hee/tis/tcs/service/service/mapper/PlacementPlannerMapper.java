package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PersonDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.PostDTO;
import com.transformuk.hee.tis.tcs.service.dto.placementmanager.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PlacementPlannerMapper {

  public SpecialtyDTO convertSpecialty(Specialty specialty, List<SiteDTO> sites,
                                        Map<SiteDTO, Set<Post>> siteToPosts,
                                        Map<Post, Set<Placement>> postsToPlacements) {
    SpecialtyDTO result = new SpecialtyDTO();

    result.setId(specialty.getId());
    result.setName(specialty.getName());
    result.setCollege(specialty.getCollege());
    result.setSites(convertSites(sites, siteToPosts, postsToPlacements));
    return result;
  }

  private List<com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO> convertSites(List<com.transformuk.hee.tis.reference.api.dto.SiteDTO> sites,
                                                                                              Map<com.transformuk.hee.tis.reference.api.dto.SiteDTO, Set<Post>> siteToPosts,
                                                                                              Map<Post, Set<Placement>> postsToPlacements) {
    List<com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO> results = Lists.newArrayList();

    if (CollectionUtils.isNotEmpty(sites)) {
      for (com.transformuk.hee.tis.reference.api.dto.SiteDTO site : sites) {
        com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO result = new com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO();
        result.setId(site.getId());
        result.setName(site.getSiteName());
        result.setSiteKnownAs(site.getSiteKnownAs());
        result.setSiteNumber(site.getSiteNumber());
        result.setPosts(convertPosts(siteToPosts.get(site), postsToPlacements));

        results.add(result);
      }
    }

    return results;
  }

  private List<PostDTO> convertPosts(Set<Post> posts, Map<Post, Set<Placement>> postsToPlacements) {
    List<PostDTO> results = Lists.newArrayList();

    if (CollectionUtils.isNotEmpty(posts)) {
      for (Post post : posts) {
        PostDTO result = new PostDTO();
        result.setId(post.getId());
        result.setNationalPostNumber(post.getNationalPostNumber());
        result.setPlacements(convertPlacements(postsToPlacements.get(post)));

        results.add(result);
      }
    }

    return results;
  }

  private List<PlacementDTO> convertPlacements(Set<Placement> placements) {
    List<PlacementDTO> results = Lists.newArrayList();

    if (CollectionUtils.isNotEmpty(placements)) {
      for (Placement placement : placements) {
        PlacementDTO result = new PlacementDTO();
        result.setId(placement.getId());
        result.setDateFrom(placement.getDateFrom());
        result.setDateTo(placement.getDateTo());
        result.setWte(placement.getPlacementWholeTimeEquivalent());
        result.setType(placement.getPlacementType());
        result.setTrainee(convertPerson(placement.getTrainee()));

        results.add(result);
      }
    }

    return results;
  }

  private PersonDTO convertPerson(Person person) {
    PersonDTO result = new PersonDTO();
    if (person != null) {

      //todo make this better, this might make multiple db calls
      ContactDetails contactDetails = person.getContactDetails();
      GmcDetails gmcDetails = person.getGmcDetails();
      GdcDetails gdcDetails = person.getGdcDetails();
      result.setId(person.getId());
      result.setSurname(contactDetails.getSurname());
      result.setForename(contactDetails.getForenames());
      result.setGdc(gdcDetails.getGdcNumber());
      result.setGmc(gmcDetails.getGmcNumber());
    }

    return result;
  }
}
