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

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class PlacementPlannerMapper {

  public SpecialtyDTO convertSpecialty(Specialty specialty, Map<SiteDTO, Map<Post, List<Placement>>> data) {
    SpecialtyDTO result = new SpecialtyDTO();

    result.setId(specialty.getId());
    result.setName(specialty.getName());
    result.setCollege(specialty.getCollege());
    List<com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO> sites = Lists.newArrayList();
    result.setSites(sites);

    for (Map.Entry<SiteDTO, Map<Post, List<Placement>>> siteDTOMapEntry : data.entrySet()) {
      SiteDTO siteDTO = siteDTOMapEntry.getKey();
      com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO convertedSite = convertSite(siteDTO);
      sites.add(convertedSite);

      Map<Post, List<Placement>> postToPlacements = siteDTOMapEntry.getValue();
      List<PostDTO> posts = Lists.newArrayList();
      convertedSite.setPosts(posts);
      for (Map.Entry<Post, List<Placement>> postSetEntry : postToPlacements.entrySet()) {
        Post post = postSetEntry.getKey();
        PostDTO postDTO = convertPost(post);
        posts.add(postDTO);

        List<Placement> placements = postSetEntry.getValue();
        List<PlacementDTO> placementDTOS = convertPlacements(placements);
        postDTO.setPlacements(placementDTOS);
      }

    }
    return result;
  }

  private com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO convertSite(SiteDTO site) {
    com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO result = new com.transformuk.hee.tis.tcs.service.dto.placementmanager.SiteDTO();
    result.setId(site.getId());
    result.setName(site.getSiteName());
    result.setSiteKnownAs(site.getSiteKnownAs());
    result.setSiteNumber(site.getSiteNumber());
    return result;
  }

  private PostDTO convertPost(Post post) {
    PostDTO result = new PostDTO();
    result.setId(post.getId());
    result.setNationalPostNumber(post.getNationalPostNumber());
    return result;
  }

  private List<PlacementDTO> convertPlacements(List<Placement> placements) {
    List<PlacementDTO> results = Lists.newArrayList();
    if (CollectionUtils.isNotEmpty(placements)) {
      for (Placement placement : placements) {
        PlacementDTO result = new PlacementDTO();
        result.setId(placement.getId());
        result.setDateFrom(placement.getDateFrom());
        result.setDateTo(placement.getDateTo());
        LocalDate fromDate = LocalDate.of(2017, 3, 12);
        LocalDate toDate = LocalDate.of(2021, 3, 12);

        result.setWte(placement.getPlacementWholeTimeEquivalent());
        result.setType(placement.getPlacementType());
        result.setTrainee(convertPerson(placement.getTrainee()));

        results.add(result);
      }
      Collections.sort(results, Comparator.comparing(PlacementDTO::getDateFrom));
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
