package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostFunding;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity Post and its DTO PostDTO.
 * <p>
 * This mapper was created as mapstruct was having difficulty with some of the relationships within posts
 * It was having issues with the parent/child relationship between old/new post records causing stack overflows
 * and causing NPE's when trying to traverse through joins outside the JPA session.
 * <p>
 * This mapper gives more control over what details are converted
 */
@Component
public class PostMapper {

  @Autowired
  private PlacementMapper placementMapper;

  public PostDTO postToPostDTO(Post post) {
    PostDTO result = null;
    if (post != null) {
      result = postToPostDTO(post, true, true);
    }
    return result;
  }

  public PostDTO postToPostDTO(Post post, Boolean includePlacements) {
    PostDTO result = null;
    if (post != null) {
      result = postToPostDTO(post, true, includePlacements);
    }
    return result;
  }

  public List<Post> postDTOsToPosts(List<PostDTO> postDTOs) {
    List<Post> result = Lists.newArrayList();

    for (PostDTO postDTO : postDTOs) {
      result.add(postDTOToPost(postDTO));
    }

    return result;
  }

  public List<PostDTO> postsToPostDTOs(List<Post> posts) {
    List<PostDTO> result = Lists.newArrayList();

    for (Post post : posts) {
      result.add(postToPostDTO(post));
    }

    return result;
  }

  private PostDTO postToPostDTO(Post post, boolean traverseRelatedPosts, boolean includePlacements) {
    PostDTO result = new PostDTO();

    result.setId(post.getId());
    result.setIntrepidId(post.getIntrepidId());
    result.setNationalPostNumber(post.getNationalPostNumber());
    result.setStatus(post.getStatus());
    result.setSuffix(post.getSuffix());
    result.setManagingLocalOffice(post.getOwner());
    result.setPostFamily(post.getPostFamily());
    result.setEmployingBodyId(post.getEmployingBodyId());
    result.setTrainingBodyId(post.getTrainingBodyId());
    result.setTrainingDescription(post.getTrainingDescription());
    result.setLocalPostNumber(post.getLocalPostNumber());

    if (traverseRelatedPosts) {
      if (post.getOldPost() != null) {
        result.setOldPost(postToPostDTO(post.getOldPost(), false, includePlacements));
      }
      if (post.getNewPost() != null) {
        result.setNewPost(postToPostDTO(post.getNewPost(), false, includePlacements));
      }
    }

    if (CollectionUtils.isNotEmpty(post.getSites())) {
      Set<PostSiteDTO> sites = Sets.newHashSet();
      for (PostSite postSite : post.getSites()) {
        PostSiteDTO siteDTO = new PostSiteDTO();
        siteDTO.setId(postSite.getId());
        siteDTO.setPostId(postSite.getPost().getId());
        siteDTO.setSiteId(postSite.getSiteId());
        siteDTO.setPostSiteType(postSite.getPostSiteType());
        sites.add(siteDTO);
      }
      result.setSites(sites);
    }

    if (CollectionUtils.isNotEmpty(post.getGrades())) {
      Set<PostGradeDTO> grades = Sets.newHashSet();
      for (PostGrade postGrade : post.getGrades()) {
        PostGradeDTO postGradeDTO = new PostGradeDTO();
        postGradeDTO.setId(postGrade.getId());
        postGradeDTO.setPostId(postGrade.getPost().getId());
        postGradeDTO.setGradeId(postGrade.getGradeId());
        postGradeDTO.setPostGradeType(postGrade.getPostGradeType());
        grades.add(postGradeDTO);
      }
      result.setGrades(grades);
    }

    if (CollectionUtils.isNotEmpty(post.getSpecialties())) {
      Set<PostSpecialtyDTO> specialties = Sets.newHashSet();
      for (PostSpecialty postSpecialty : post.getSpecialties()) {
        PostSpecialtyDTO postSpecialtyDTO = new PostSpecialtyDTO();
        postSpecialtyDTO.setId(postSpecialty.getId());
        postSpecialtyDTO.setPostId(postSpecialty.getPost().getId());
        postSpecialtyDTO.setSpecialty(specialtyToSpecialtyDTO(postSpecialty.getSpecialty()));
        postSpecialtyDTO.setPostSpecialtyType(postSpecialty.getPostSpecialtyType());
        specialties.add(postSpecialtyDTO);
      }
      result.setSpecialties(specialties);
    }

    if (includePlacements) {
      if (CollectionUtils.isNotEmpty(post.getPlacementHistory())) {
        Set<PlacementDTO> placements = Sets.newHashSet();
        for (Placement placement : post.getPlacementHistory()) {
          placements.add(placementMapper.placementToPlacementDTO(placement));
        }
        result.setPlacementHistory(placements);
      }
    }

    if (CollectionUtils.isNotEmpty(post.getFundings())) {
      result.setFundings(post.getFundings().stream().map(this::fundingToFundingDTO).collect(Collectors.toSet()));
    }

    result.setProgrammes(programmeToProgrammeDTO(post.getProgrammes()));

    return result;
  }

  private PostFundingDTO fundingToFundingDTO(PostFunding postFunding) {
    PostFundingDTO result = null;
    if (postFunding != null) {
      result = new PostFundingDTO();
      result.setId(postFunding.getId());
      result.setFundingType(postFunding.getFundingType());
      result.setInfo(postFunding.getInfo());
      result.setStartDate(postFunding.getStartDate());
      result.setEndDate(postFunding.getEndDate());
      result.setIntrepidId(postFunding.getIntrepidId());
      result.setFundingBodyId(postFunding.getFundingBodyId());
    }
    return result;
  }

  private SpecialtyDTO specialtyToSpecialtyDTO(Specialty specialty) {
    SpecialtyDTO result = null;
    if (specialty != null) {
      result = new SpecialtyDTO();
      result.setId(specialty.getId());
      result.setName(specialty.getName());
      result.setSpecialtyTypes(specialty.getSpecialtyTypes());
      result.setSpecialtyCode(specialty.getSpecialtyCode());
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
      result.setManagingDeanery(programme.getOwner());
      result.setStatus(programme.getStatus());
    }
    return result;
  }

  public Post postDTOToPost(PostDTO postDTO) {
    Post result = postDTOToPost(postDTO, true);
    return result;
  }

  private Post postDTOToPost(PostDTO postDTO, boolean traverseRelatedPosts) {
    Post result = new Post();
    result.setId(postDTO.getId());
    result.setIntrepidId(postDTO.getIntrepidId());
    result.setNationalPostNumber(postDTO.getNationalPostNumber());
    result.setStatus(postDTO.getStatus());
    result.setSuffix(postDTO.getSuffix());
    result.setOwner(postDTO.getManagingLocalOffice());
    result.setPostFamily(postDTO.getPostFamily());
    result.setEmployingBodyId(postDTO.getEmployingBodyId());
    result.setTrainingBodyId(postDTO.getTrainingBodyId());
    result.setTrainingDescription(postDTO.getTrainingDescription());
    result.setLocalPostNumber(postDTO.getLocalPostNumber());

    if (traverseRelatedPosts) {
      if (postDTO.getOldPost() != null) {
        result.setOldPost(postDTOToPost(postDTO.getOldPost(), false));
      }
      if (postDTO.getNewPost() != null) {
        result.setNewPost(postDTOToPost(postDTO.getNewPost(), false));
      }
    }

    if (CollectionUtils.isNotEmpty(postDTO.getSites())) {
      Set<PostSite> sites = Sets.newHashSet();
      for (PostSiteDTO postSiteDTO : postDTO.getSites()) {
        PostSite site = new PostSite();
        site.setPost(result);
        site.setSiteId(postSiteDTO.getSiteId());
        site.setPostSiteType(postSiteDTO.getPostSiteType());
        sites.add(site);
      }
      result.setSites(sites);
    }

    if (CollectionUtils.isNotEmpty(postDTO.getGrades())) {
      Set<PostGrade> grades = Sets.newHashSet();
      for (PostGradeDTO postGradeDTO : postDTO.getGrades()) {
        PostGrade postGrade = new PostGrade();
        postGrade.setPost(result);
        postGrade.setGradeId(postGradeDTO.getGradeId());
        postGrade.setPostGradeType(postGradeDTO.getPostGradeType());
        grades.add(postGrade);
      }
      result.setGrades(grades);
    }

    if (CollectionUtils.isNotEmpty(postDTO.getSpecialties())) {
      Set<PostSpecialty> specialties = Sets.newHashSet();
      for (PostSpecialtyDTO postSpecialtyDTO : postDTO.getSpecialties()) {
        PostSpecialty postSpecialty = new PostSpecialty();
        postSpecialty.setPost(result);
        postSpecialty.setSpecialty(specialtyDTOToSpecialty(postSpecialtyDTO.getSpecialty()));
        postSpecialty.setPostSpecialtyType(postSpecialtyDTO.getPostSpecialtyType());
        specialties.add(postSpecialty);
      }
      result.setSpecialties(specialties);
    }

    if (CollectionUtils.isNotEmpty(postDTO.getFundings())) {
      result.setFundings(postDTO.getFundings().stream().map(f -> {
        PostFunding postFunding = fundingDTOToFunding(f);
        postFunding.setPost(result);
        return postFunding;
      }).collect(Collectors.toSet()));
    }

    result.setProgrammes(programmeDTOToProgramme(postDTO.getProgrammes()));

    return result;
  }

  private Specialty specialtyDTOToSpecialty(SpecialtyDTO specialtyDTO) {
    Specialty result = null;
    if (specialtyDTO != null) {
      result = new Specialty();
      result.setId(specialtyDTO.getId());
      result.setName(specialtyDTO.getName());
      result.setSpecialtyTypes(specialtyDTO.getSpecialtyTypes());
      result.setSpecialtyCode(specialtyDTO.getSpecialtyCode());
      result.setStatus(specialtyDTO.getStatus());
    }
    return result;
  }

  private Programme programmeDTOToProgramme(ProgrammeDTO programmeDTO) {
    Programme result = null;
    if (programmeDTO != null) {
      result = new Programme();

      result.setId(programmeDTO.getId());
      result.setIntrepidId(programmeDTO.getIntrepidId());
      result.setProgrammeNumber(programmeDTO.getProgrammeNumber());
      result.setProgrammeName(programmeDTO.getProgrammeName());
      result.setOwner(programmeDTO.getManagingDeanery());
      result.setStatus(programmeDTO.getStatus());
    }
    return result;
  }

  private PostFunding fundingDTOToFunding(PostFundingDTO postFundingDTO) {
    PostFunding result = null;
    if (postFundingDTO != null) {
      result = new PostFunding();
      result.setId(postFundingDTO.getId());
      result.setFundingType(postFundingDTO.getFundingType());
      result.setInfo(postFundingDTO.getInfo());
      result.setStartDate(postFundingDTO.getStartDate());
      result.setEndDate(postFundingDTO.getEndDate());
      result.setIntrepidId(postFundingDTO.getIntrepidId());
      result.setFundingBodyId(postFundingDTO.getFundingBodyId());
    }
    return result;
  }
}
