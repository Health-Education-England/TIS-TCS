package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.service.api.decorator.AsyncReferenceService;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSite;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PostView;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostGradeRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSiteRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostSpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostViewRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PostViewMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;

/**
 * Service Implementation for managing Post.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

  private static final int LOCAL_OFFICE_ABBR_INDEX = 0;
  private static final int LOCATION_CODE_INDEX = 1;
  private static final int SPECIALTY_CODE_INDEX = 2;
  private static final int GRADE_ABBR_INDEX = 3;
  private static final int UNIQUE_COUNTER_INDEX = 4;
  private static final int SUFFIX_INDEX = 5;
  private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
  private static final String SLASH = "/";

  private static final Map<String, String> dbcToLocalOfficeAbbrMap = ImmutableMap.<String, String>builder()
      .put("1-AIIDR8", "LDN")
      .put("1-AIIDWA", "LDN")
      .put("1-AIIDVS", "LDN")
      .put("1-AIIDWI", "LDN")
      .put("1-AIIDSA", "EMD")
      .put("1-AIIDWT", "EOE")
      .put("1-AIIDSI", "NTH")
      .put("1-AIIDH1", "OXF")
      .put("1-AIIDQQ", "YHD")
      .put("1-AIIDMY", "WMD")
      .put("1-AIIDMQ", "SW")
      .put("1-AIIDHJ", "WES")
      .put("1-AIIDNQ", "NWN")
      .build();

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private PostViewRepository postViewRepository;
  @Autowired
  private PostGradeRepository postGradeRepository;
  @Autowired
  private PostSiteRepository postSiteRepository;
  @Autowired
  private PostSpecialtyRepository postSpecialtyRepository;
  @Autowired
  private ProgrammeRepository programmeRepository;
  @Autowired
  private SpecialtyRepository specialtyRepository;
  @Autowired
  private PlacementRepository placementRepository;
  @Autowired
  private PostMapper postMapper;
  @Autowired
  private PostViewMapper postViewMapper;
  @Autowired
  private PostViewDecorator postViewDecorator;
  @Autowired
  private AsyncReferenceService asyncReferenceService;

  /**
   * Save a post.
   * <p>
   * If the post is new, we need to generate a new NPN, if its an update, then we check to see we require a new number
   * to be generated AND that we're not bypassing the generation
   *
   * @param postDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PostDTO save(PostDTO postDTO) {
    log.debug("Request to save Post : {}", postDTO);
    if (postDTO.isBypassNPNGeneration()) {
      //if we bypass do no do any of the generation logic
    } else if (postDTO.getId() == null || requireNewNationalPostNumber(postDTO)) {
      generateAndSetNewNationalPostNumber(postDTO);
    }
    Post post = postMapper.postDTOToPost(postDTO);
    post = postRepository.save(post);
    return postMapper.postToPostDTO(post);
  }

  /**
   * Save a list of post.
   * <p>
   * Used ONLY by the ETL to bulk save a list of posts. It has to clear the posts relationships as the relationships
   * could have been changed since the last ETL run and we wont get that update so a complete clear down is required
   * <p>
   * The ETL will then PATCH the relationships in a later step
   *
   * @param postDTOs the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PostDTO> save(List<PostDTO> postDTOs) {
    log.debug("Request to save Post : {}", postDTOs);
    List<Post> posts = postMapper.postDTOsToPosts(postDTOs);

    Set<String> postIntrepidIds = posts.stream().map(Post::getIntrepidId).collect(Collectors.toSet());
    Set<Post> postByIntrepidIds = postRepository.findPostByIntrepidIdIn(postIntrepidIds);
    Set<PostGrade> allPostGrades = postByIntrepidIds.stream().flatMap(post -> post.getGrades().stream()).collect(Collectors.toSet());
    Set<PostSite> allPostSites = postByIntrepidIds.stream().flatMap(post -> post.getSites().stream()).collect(Collectors.toSet());
    Set<PostSpecialty> allPostSpecialties = postByIntrepidIds.stream().flatMap(post -> post.getSpecialties().stream()).collect(Collectors.toSet());

    postGradeRepository.delete(allPostGrades);
    postSiteRepository.delete(allPostSites);
    postSpecialtyRepository.delete(allPostSpecialties);

    posts = postRepository.save(posts);
    return postMapper.postsToPostDTOs(posts);
  }


  /**
   * Update a list of post so that the links to old/new posts are saved. its important to note that if a related post
   * cannot be found, the existing post is cleared but if related post id  is null then it isnt cleared, this is to ensure
   * that calls that dont send id's wont clear previously set links
   * <p>
   * This method does try to be performant by compiling the collection of post ids together before doing a
   * query (rather than doing singular find by id).
   *
   * @param postDTOList the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PostDTO> patchOldNewPosts(List<PostDTO> postDTOList) {
    List<Post> postsToSave = Lists.newArrayList();

    Set<String> postIntrepidIds = postDTOList.stream().map(PostDTO::getIntrepidId).collect(Collectors.toSet());
    Set<String> oldPostIntrepidIds = postDTOList.stream()
        .map(PostDTO::getOldPost)
        .filter(Objects::nonNull)
        .map(PostDTO::getIntrepidId)
        .collect(Collectors.toSet());

    Set<String> newPostIntrepidIds = postDTOList.stream()
        .map(PostDTO::getNewPost)
        .filter(Objects::nonNull)
        .map(PostDTO::getIntrepidId)
        .collect(Collectors.toSet());

    Set<String> postIntredpidIds = Sets.newHashSet(postIntrepidIds);
    postIntredpidIds.addAll(oldPostIntrepidIds);
    postIntredpidIds.addAll(newPostIntrepidIds);

    Map<String, Post> intrepidIdToPost = postRepository.findPostByIntrepidIdIn(postIntredpidIds)
        .stream().collect(Collectors.toMap(Post::getIntrepidId, p -> p));

    for (PostDTO dto : postDTOList) {

      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      if (post != null) {
        if (dto.getOldPost() != null && dto.getOldPost().getIntrepidId() != null) {
          Post oldPost = intrepidIdToPost.get(dto.getOldPost().getIntrepidId());
          post.setOldPost(oldPost);
        }

        if (dto.getNewPost() != null && dto.getNewPost().getIntrepidId() != null) {
          Post newPost = intrepidIdToPost.get(dto.getNewPost().getIntrepidId());
          post.setNewPost(newPost);
        }

        postsToSave.add(post);
      }
    }
    List<Post> savedPosts = postRepository.save(postsToSave);
    return postMapper.postsToPostDTOs(savedPosts);
  }

  @Override
  public List<PostDTO> patchPostSites(List<PostDTO> postDTOList) {
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    List<Post> postsModified = Lists.newArrayList();
    for (PostDTO dto : postDTOList) {
      Set<PostSite> sitesToSave;
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      if (post != null) {
        sitesToSave = post.getSites();
        for (PostSiteDTO siteDTO : dto.getSites()) {
          PostSite postSite = new PostSite();
          postSite.setPost(post);
          postSite.setPostSiteType(siteDTO.getPostSiteType());
          postSite.setSiteId(siteDTO.getSiteId());
          sitesToSave.add(postSite);
        }
        List<PostSite> savedPostSite = postSiteRepository.save(sitesToSave);
        post.setSites(Sets.newHashSet(savedPostSite));
        postsModified.add(post);
      }

    }

    return postMapper.postsToPostDTOs(postsModified);
  }

  @Override
  public List<PostDTO> patchPostGrades(List<PostDTO> postDTOList) {
    List<Post> postsToSave = Lists.newArrayList();
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    for (PostDTO dto : postDTOList) {
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      Set<PostGrade> postGrades = post.getGrades();
      if (post != null) {
        for (PostGradeDTO postGradeDTO : dto.getGrades()) {
          PostGrade postGrade = new PostGrade();
          postGrade.setPost(post);
          postGrade.setPostGradeType(postGradeDTO.getPostGradeType());
          postGrade.setGradeId(postGradeDTO.getGradeId());
          postGrades.add(postGrade);
        }
        List<PostGrade> savedGrades = postGradeRepository.save(postGrades);
        post.setGrades(Sets.newHashSet(savedGrades));
        postsToSave.add(post);
      }
    }
    List<Post> savedPosts = postRepository.save(postsToSave);
    return postMapper.postsToPostDTOs(savedPosts);
  }

  @Override
  public List<PostDTO> patchPostProgrammes(List<PostDTO> postDTOList) {
    List<Post> posts = Lists.newArrayList();
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    Set<Long> programmeIds = postDTOList
        .stream()
        .map(PostDTO::getProgrammes)
        .map(ProgrammeDTO::getId)
        .collect(Collectors.toSet());

    Map<Long, Programme> idToProgramme = programmeRepository.findAll(programmeIds)
        .stream().collect(Collectors.toMap(Programme::getId, p -> p));

    for (PostDTO dto : postDTOList) {
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      Programme programme = idToProgramme.get(dto.getProgrammes().getId());
      if (post != null && programme != null) {
        post.setProgrammes(programme);
        posts.add(post);
      }
    }
    List<Post> savedPosts = postRepository.save(posts);
    return postMapper.postsToPostDTOs(savedPosts);
  }

  @Override
  public List<PostDTO> patchPostSpecialties(List<PostDTO> postDTOList) {
    List<Post> posts = Lists.newArrayList();
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    for (PostDTO dto : postDTOList) {
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      if (post != null) {
        Set<PostSpecialty> attachedSpecialties = post.getSpecialties();
        for (PostSpecialtyDTO postSpecialtyDTO : dto.getSpecialties()) {

          Specialty specialty = new Specialty();
          specialty.setId(postSpecialtyDTO.getSpecialty().getId());

          PostSpecialty postSpecialty = new PostSpecialty();
          postSpecialty.setPostSpecialtyType(postSpecialtyDTO.getPostSpecialtyType());
          postSpecialty.setPost(post);
          postSpecialty.setSpecialty(specialty);
          attachedSpecialties.add(postSpecialty);
        }
        List<PostSpecialty> savedSpecialties = postSpecialtyRepository.save(attachedSpecialties);
        post.setSpecialties(Sets.newHashSet(savedSpecialties));
        posts.add(post);
      }
    }
    return postMapper.postsToPostDTOs(posts);
  }

  private Map<String, Post> getPostsByIntrepidId(List<PostDTO> postDtoList) {
    Set<String> postIntrepidIds = postDtoList.stream().map(PostDTO::getIntrepidId).collect(Collectors.toSet());
    Set<Post> postsFound = postRepository.findPostByIntrepidIdIn(postIntrepidIds);
    Map<String, Post> result = Maps.newHashMap();
    if (CollectionUtils.isNotEmpty(postsFound)) {
      result = postsFound.stream().collect(
          Collectors.toMap(Post::getIntrepidId, post -> post)
      );
    }
    return result;
  }

  @Override
  public List<PostDTO> patchPostPlacements(List<PostDTO> postDTOList) {
    List<Post> posts = Lists.newArrayList();
    Map<String, Post> intrepidIdToPost = getPostsByIntrepidId(postDTOList);

    Set<String> postPlamementIntrepidIds = postDTOList
        .stream()
        .map(PostDTO::getPlacementHistory)
        .flatMap(Collection::stream)
        .map(PlacementDTO::getIntrepidId)
        .collect(Collectors.toSet());

    Map<String, Placement> placementIntrepidIdToPlacements = placementRepository.findByIntrepidIdIn(postPlamementIntrepidIds)
        .stream().collect(Collectors.toMap(Placement::getIntrepidId, p -> p));

    for (PostDTO dto : postDTOList) {
      Post post = intrepidIdToPost.get(dto.getIntrepidId());
      if (post != null) {
        Set<Placement> placements = post.getPlacementHistory();
        for (PlacementDTO placementViewDTO : dto.getPlacementHistory()) {
          Placement placement = placementIntrepidIdToPlacements.get(placementViewDTO.getIntrepidId());
          if (placement != null) {
            placements.add(placement);
          }
        }
        post.setPlacementHistory(placements);
        posts.add(post);
      }
    }
    List<Post> savedPosts = postRepository.save(posts);
    return postMapper.postsToPostDTOs(savedPosts);
  }

  @Override
  public PostDTO update(PostDTO postDTO) {
    Post post = postRepository.findOne(postDTO.getId());

    //clear all the relations
    postGradeRepository.delete(post.getGrades());
    postSiteRepository.delete(post.getSites());
    postSpecialtyRepository.delete(post.getSpecialties());

    if (postDTO.isBypassNPNGeneration()) {
      //if we bypass do no do any of the generation logic
    } else if (requireNewNationalPostNumber(postDTO)) {
      generateAndSetNewNationalPostNumber(postDTO);
    } else if (!StringUtils.equals(post.getNationalPostNumber(), postDTO.getNationalPostNumber())) {
      //if the user tries to manually change the npn without override, set it back
      postDTO.setNationalPostNumber(post.getNationalPostNumber());
    }
    post = postMapper.postDTOToPost(postDTO);
    post = postRepository.save(post);
    return postMapper.postToPostDTO(post);
  }

  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostDTO> findAll(Set<String> dbcs, Pageable pageable) {
    log.debug("Request to get all Posts");

    Set<String> deaneries = DesignatedBodyMapper.map(dbcs);
    Page<Post> result = postRepository.findByOwnerIn(deaneries, pageable);
    return result.map(post -> postMapper.postToPostDTO(post));
  }

  /**
   * Get all the posts.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostViewDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Posts");
    Page<PostView> result = postViewRepository.findAll(pageable);
    Page<PostViewDTO> dtoPage = result.map(postView -> postViewMapper.postViewToPostViewDTO(postView));
    postViewDecorator.decorate(dtoPage.getContent());
    return dtoPage;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostViewDTO> advancedSearch(String searchString, List<ColumnFilter> columnFilters, Pageable pageable) {

    List<Specification<PostView>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("nationalPostNumber", searchString)).
          or(containsLike("programmeName", searchString)).
          or(containsLike("currentTraineeGmcNumber", searchString)).
          or(containsLike("currentTraineeSurname", searchString)).
          or(containsLike("currentTraineeForenames", searchString)));
    }
    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    Page<PostView> result;
    if (!specs.isEmpty()) {
      Specifications<PostView> fullSpec = Specifications.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = postViewRepository.findAll(fullSpec, pageable);
    } else {
      result = postViewRepository.findAll(pageable);
    }

    Page<PostViewDTO> dtoPage = result.map(postView -> postViewMapper.postViewToPostViewDTO(postView));
    postViewDecorator.decorate(dtoPage.getContent());
    return dtoPage;
  }

  /**
   * Get one post by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PostDTO findOne(Long id) {
    log.debug("Request to get Post : {}", id);
    Post post = postRepository.findOne(id);
    return postMapper.postToPostDTO(post);
  }

  /**
   * Delete the  post by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Post : {}", id);
    postRepository.delete(id);
  }

  /**
   * Call Stored proc to build the post view
   *
   * @return
   */
  @Override
  @Transactional
  @Async
  public CompletableFuture<Void> buildPostView() {
    log.debug("Request to build Post view");
    postRepository.buildPostView();
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public boolean isValidLocalOfficeAbbr(String localOfficeAbbr) {
    Collection<String> allLocalOfficeAbbr = dbcToLocalOfficeAbbrMap.values();
    return allLocalOfficeAbbr.contains(localOfficeAbbr);
  }

  /**
   * Check the new post against the current post in the system. If there are changes in some of the values,
   * a new national post number will need to be generated
   *
   * @param postDTO
   * @return
   */
  @Transactional(readOnly = true)
  boolean requireNewNationalPostNumber(PostDTO postDTO) {
    Post currentPost = postRepository.findOne(postDTO.getId());
    if (currentPost == null) {
      return true;
    } else {
      boolean generateNewNumber = false;

      String localOfficeAbbr = getLocalOfficeAbbr();
      SiteDTO siteCodeContainer = new SiteDTO();
      GradeDTO gradeAbbrContainer = new GradeDTO();
      CompletableFuture.allOf(
          getSiteCode(postDTO, siteCodeContainer),
          getApprovedGradeOrEmpty(postDTO, gradeAbbrContainer))
          .join();
      String specialtyCode = getPrimarySpecialtyCodeOrEmpty(postDTO);
      String suffixValue = postDTO.getSuffix() != null ? postDTO.getSuffix().getSuffixValue() : StringUtils.EMPTY;

      String currentNationalPostNumber = currentPost.getNationalPostNumber();
      String[] currentNationalPostNumberParts = currentNationalPostNumber.split(SLASH);
      if (currentNationalPostNumberParts.length == 5 || currentNationalPostNumberParts.length == 6) { // does the national post number have the correct number of parts?
        String currentPostLocalOfficeAbbr = currentNationalPostNumberParts[LOCAL_OFFICE_ABBR_INDEX];
        String currentPostLocationCode = currentNationalPostNumberParts[LOCATION_CODE_INDEX];
        String currentPostSpecialtyCode = currentNationalPostNumberParts[SPECIALTY_CODE_INDEX];
        String currentPostGradeAbbr = currentNationalPostNumberParts[GRADE_ABBR_INDEX];
        String currentPostSuffix = StringUtils.EMPTY;
        if (currentNationalPostNumberParts.length == 6) {
          currentPostSuffix = currentNationalPostNumberParts[SUFFIX_INDEX];
        }

        if (!localOfficeAbbr.equals(currentPostLocalOfficeAbbr) || !siteCodeContainer.getSiteCode().equals(currentPostLocationCode) ||
            !specialtyCode.equals(currentPostSpecialtyCode) || !gradeAbbrContainer.getAbbreviation().equals(currentPostGradeAbbr) ||
            !suffixValue.equals(currentPostSuffix)) {
          generateNewNumber = true;
        }
      }
      return generateNewNumber;
    }
  }


  @Override
  public void generateAndSetNewNationalPostNumber(PostDTO postDTO) {
    SiteDTO siteCodeContainer = new SiteDTO();
    GradeDTO gradeAbbrContainer = new GradeDTO();

    CompletableFuture.allOf(getApprovedGradeOrEmpty(postDTO, gradeAbbrContainer), getSiteCode(postDTO, siteCodeContainer))
        .join();

    String newSpecialtyCode = getPrimarySpecialtyCodeOrEmpty(postDTO);

    String nationalPostNumber = generateNationalPostNumber(getLocalOfficeAbbr(), siteCodeContainer.getSiteCode(),
        newSpecialtyCode, gradeAbbrContainer.getAbbreviation(), postDTO.getSuffix());

    postDTO.setNationalPostNumber(nationalPostNumber);
  }

  @Override
  public String generateNationalPostNumber(String localOfficeAbbr, String siteCode, String specialtyCode,
                                           String gradeAbbr, PostSuffix suffix) {
    Preconditions.checkNotNull(localOfficeAbbr);
    Preconditions.checkNotNull(siteCode);
    Preconditions.checkNotNull(specialtyCode);
    Preconditions.checkNotNull(gradeAbbr);

    String nationalPostNumberNoCounter = localOfficeAbbr + SLASH + siteCode + SLASH + specialtyCode + SLASH + gradeAbbr;

    Set<Post> postsWithSamePostNumber = postRepository.findByNationalPostNumberStartingWith(nationalPostNumberNoCounter);

    List<Integer> postNumberCounter = postsWithSamePostNumber.stream()
        .map(Post::getNationalPostNumber)
        .filter(StringUtils::isNotBlank)
        .filter(npn -> {
          if (suffix != null) {
            return npn.endsWith(suffix.getSuffixValue()); //filter out any npn's that dont match the suffix we're trying to generate
          } else {
            String[] npnParts = npn.split("/");
            String lastNpnPart = npnParts[npnParts.length - 1];
            return NumberUtils.isDigits(lastNpnPart); //if no suffix is supplied, filter out any that end with non digit
          }
        })
        .map(npn -> npn.split(SLASH))
        .filter(npn -> npn.length > 1)
        .map(npn -> {
          if (suffix != null) {
            return npn[npn.length - 2]; //if we have a suffix, then the number component is second to last
          } else {
            return npn[npn.length - 1];
          }
        })
        .filter(NumberUtils::isDigits)
        .map(Integer::parseInt)
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());

    String result;

    // up the counter part of the NPN
    if (CollectionUtils.isNotEmpty(postNumberCounter)) {
      Integer currentHighestCounter = postNumberCounter.iterator().next();
      String leftPadded = StringUtils.leftPad(Integer.toString(++currentHighestCounter), 3, '0');
      result = nationalPostNumberNoCounter + SLASH + leftPadded;

    } else {
      result = nationalPostNumberNoCounter + SLASH + "001";
    }

    // add suffix if provided
    if (suffix != null) {
      result += SLASH + suffix.getSuffixValue();
    }

    return result;
  }

  String getLocalOfficeAbbr() {
    UserProfile userProfile = TisSecurityHelper.getProfileFromContext();
    String dbc = userProfile.getDesignatedBodyCodes().stream().findFirst().orElse(StringUtils.EMPTY);
    String localOfficeAbbr = dbcToLocalOfficeAbbrMap.get(dbc);
    return localOfficeAbbr != null ? localOfficeAbbr : StringUtils.EMPTY;
  }


  String getPrimarySpecialtyCodeOrEmpty(PostDTO postDTO) {
    if (CollectionUtils.isNotEmpty(postDTO.getSpecialties())) {
      Long specialtyId = postDTO.getSpecialties().stream()
          .filter(sp -> PostSpecialtyType.PRIMARY.equals(sp.getPostSpecialtyType()))
          .map(ps -> ps.getSpecialty().getId())
          .findAny().orElse(null);

      if (specialtyId != null) {
        Specialty primarySpecialty = specialtyRepository.findOne(specialtyId);
        return primarySpecialty != null ? primarySpecialty.getSpecialtyCode() : StringUtils.EMPTY;
      }
    }
    return StringUtils.EMPTY;
  }

  CompletableFuture<Void> getApprovedGradeOrEmpty(PostDTO postDTO, GradeDTO gradeDTO) {
    if (CollectionUtils.isNotEmpty(postDTO.getGrades())) {
      Long gradeId = postDTO.getGrades().stream()
          .filter(g -> PostGradeType.APPROVED.equals(g.getPostGradeType()))
          .map(PostGradeDTO::getGradeId)
          .findAny().orElse(null);

      if (gradeId != null) {
        return asyncReferenceService.doWithGradesAsync(Sets.newHashSet(gradeId), gradeIdsToGrades -> {
          gradeDTO.setId(gradeId);
          gradeDTO.setAbbreviation(gradeIdsToGrades.get(gradeId).getAbbreviation());
        });
      }

    }
    return CompletableFuture.completedFuture(null);
  }

  CompletableFuture<Void> getSiteCode(PostDTO postDTO, SiteDTO siteDTO) {
    if (CollectionUtils.isNotEmpty(postDTO.getSites())) {
      Long siteId = postDTO.getSites().stream().filter(s -> PostSiteType.PRIMARY.equals(s.getPostSiteType()))
          .map(PostSiteDTO::getSiteId)
          .findAny().orElse(null);
      if (siteId != null) {
        return asyncReferenceService.doWithSitesAsync(Sets.newHashSet(siteId), siteIdsToSites -> {
          siteDTO.setId(siteId);
          siteDTO.setSiteCode(siteIdsToSites.get(siteId).getSiteCode());
        });
      }
    }
    return CompletableFuture.completedFuture(null);
  }

}
