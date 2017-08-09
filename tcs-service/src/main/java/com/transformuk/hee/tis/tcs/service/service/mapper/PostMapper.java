package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.service.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity Post and its DTO PostDTO.
 */
@Component
public class PostMapper {

  public PostDTO postToPostDTO(Post post) {
    PostDTO result = postToPostDTO(post, true);
    return result;
  }

  public List<Post> postDTOsToPosts(List<PostDTO> postDTOs) {
    return Lists.newArrayList();
  }

  public List<PostDTO> postsToPostDTOs(List<Post> posts) {
    return Lists.newArrayList();
  }

  private PostDTO postToPostDTO(Post post, boolean followPostLinks) {
    PostDTO result = new PostDTO();

    result.setId(post.getId());
    result.setNationalPostNumber(post.getNationalPostNumber());
    result.setStatus(post.getStatus());
    result.setSuffix(post.getSuffix());
    result.setManagingLocalOffice(post.getManagingLocalOffice());
    result.setPostFamily(post.getPostFamily());
    if (followPostLinks) {
      if (post.getOldPost() != null) {
        result.setOldPost(postToPostDTO(post.getOldPost(), false));
      }
      if (post.getNewPost() != null) {
        result.setNewPost(postToPostDTO(post.getNewPost(), false));
      }
    }

    result.setEmployingBodyId(post.getEmployingBodyId());
    result.setTrainingBodyId(post.getTrainingBodyId());
    result.setTrainingDescription(post.getTrainingDescription());
    result.setLocalPostNumber(post.getLocalPostNumber());


//    if (CollectionUtils.isNotEmpty(post.getSites())) {
//      Set<PostSiteDTO> sites = Sets.newHashSet();
//      for (PostSite postSite : post.getSites()) {
//        PostSiteDTO siteDTO = new PostSiteDTO();
//        siteDTO.setPostId(postSite.getPostId().getId());
//        siteDTO.setSiteId(postSite.getSiteId());
//        siteDTO.setPostSiteType(postSite.getPostSiteType());
//        sites.add(siteDTO);
//      }
//      result.setSites(sites);
//    }


//    if (CollectionUtils.isNotEmpty(post.getGrades())) {
//      Set<PostGradeDTO> grades = Sets.newHashSet();
//      for (PostGrade postGrade : post.getGrades()) {
//        PostGradeDTO postGradeDTO = new PostGradeDTO();
//        postGradeDTO.setPostId(postGrade.getPostId().getId());
//        postGradeDTO.setGradeId(postGrade.getGradeId());
//        postGradeDTO.setPostGradeType(postGrade.getPostGradeType());
//        grades.add(postGradeDTO);
//      }
//      result.setGrades(grades);
//    }

    if (CollectionUtils.isNotEmpty(post.getSpecialties())) {
      Set<PostSpecialtyDTO> specialties = Sets.newHashSet();
      for (PostSpecialty postSpecialty : post.getSpecialties()) {
        PostSpecialtyDTO postSpecialtyDTO = new PostSpecialtyDTO();
        postSpecialtyDTO.setPostId(postSpecialty.getPost().getId());

        postSpecialtyDTO.setSpecialty(specialtyToSpecialtyDTO(postSpecialty.getSpecialty()));
        postSpecialtyDTO.setPostSpecialtyType(postSpecialty.getPostSpecialtyType());
        specialties.add(postSpecialtyDTO);
      }
      result.setSpecialties(specialties);
    }


    if (CollectionUtils.isNotEmpty(post.getPlacementHistory())) {
      Set<PlacementDTO> placements = Sets.newHashSet();
      for (Placement placement : post.getPlacementHistory()) {
        placements.add(placementToPlacementDTO(placement));
      }
      result.setPlacementHistory(placements);
    }

    result.setProgrammes(programmeToProgrammeDTO(post.getProgrammes()));

    return result;
  }

  private SpecialtyDTO specialtyToSpecialtyDTO(Specialty specialty) {
    SpecialtyDTO result = null;
    if (specialty != null) {
      result = new SpecialtyDTO();
      result.setId(specialty.getId());
      result.setName(specialty.getName());
      result.setSpecialtyType(specialty.getSpecialtyType());
      result.setNhsSpecialtyCode(specialty.getNhsSpecialtyCode());
      result.setStatus(specialty.getStatus());
    }
    return result;
  }

  private ProgrammeDTO programmeToProgrammeDTO(Programme programme) {
    ProgrammeDTO result = null;
    if (programme != null) {
      result = new ProgrammeDTO();

      result.setId(programme.getId());
      result.setIntrepidId(programme.getIntrepidId());
      result.setProgrammeNumber(programme.getProgrammeNumber());
      result.setProgrammeName(programme.getProgrammeName());
      result.setManagingDeanery(programme.getManagingDeanery());
      result.setStatus(programme.getStatus());
    }
    return result;
  }

  private PlacementDTO placementToPlacementDTO(Placement placement) {
    PlacementDTO result = null;
    if (placement != null) {
      result = new PlacementDTO();

      result.setId(placement.getId());
      result.setPlacementType(placement.getPlacementType());
      result.setStatus(placement.getStatus());
      result.setSpecialty(placement.getSpecialty());
      result.setNationalPostNumber(placement.getNationalPostNumber());
      result.setGrade(placement.getGrade());
    }
    return result;
  }

  public Post postDTOToPost(PostDTO postDTO) {
    return null;
  }

}
