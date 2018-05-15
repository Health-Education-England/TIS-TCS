package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.util.BasicPage;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.PlacementView;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.impl.PersonTrustService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.api.util.StringUtil.sanitize;

/**
 * REST controller for managing Person.
 */
@RestController
@RequestMapping("/api")
public class PersonResource {

  private final Logger log = LoggerFactory.getLogger(PersonResource.class);

  private static final String ENTITY_NAME = "person";

  private final PersonService personService;
  private final PlacementViewRepository placementViewRepository;
  private final PlacementViewMapper placementViewMapper;
  private final PlacementViewDecorator placementViewDecorator;
  private final PersonViewDecorator personViewDecorator;
  private final PlacementService placementService;
  private final PlacementSummaryDecorator placementSummaryDecorator;
  private final PersonValidator personValidator;
  private final PersonTrustService  personTrustService;

  public PersonResource(PersonService personService, PlacementViewRepository placementViewRepository,
                        PlacementViewMapper placementViewMapper, PlacementViewDecorator placementViewDecorator,
                        PersonViewDecorator personViewDecorator, PlacementService placementService,
                        PlacementSummaryDecorator placementSummaryDecorator, PersonValidator personValidator,
                        PersonTrustService personTrustService) {
    this.personService = personService;
    this.placementViewRepository = placementViewRepository;
    this.placementViewMapper = placementViewMapper;
    this.placementViewDecorator = placementViewDecorator;
    this.personViewDecorator = personViewDecorator;
    this.placementService = placementService;
    this.placementSummaryDecorator = placementSummaryDecorator;
    this.personValidator = personValidator;
    this.personTrustService = personTrustService;
  }

  /**
   * POST  /people : Create a new person.
   *
   * @param personDTO the personDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new personDTO, or with status 400 (Bad Request) if the person has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/people")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<PersonDTO> createPerson(@RequestBody @Validated(Create.class) PersonDTO personDTO)
          throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save Person : {}", personDTO);
    if (personDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new person cannot already have an ID")).body(null);
    }
    personValidator.validate(personDTO);
    PersonDTO result = personService.create(personDTO);
    return ResponseEntity.created(new URI("/api/people/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /people : Updates an existing person.
   *
   * @param personDTO the personDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated personDTO,
   * or with status 400 (Bad Request) if the personDTO is not valid,
   * or with status 500 (Internal Server Error) if the personDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/people")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<PersonDTO> updatePerson(@RequestBody @Validated(Update.class) PersonDTO personDTO)
          throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update Person : {}", personDTO);
    if (personDTO.getId() == null) {
      return createPerson(personDTO);
    }
    personService.canLoggedInUserViewOrAmend(personDTO.getId());

    personValidator.validate(personDTO);
    PersonDTO result = personService.save(personDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, personDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /people : get all the people.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of people in body
   */
  @ApiOperation(value = "Lists People data",
      notes = "Returns a list of people with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Person list")})
  @GetMapping("/people")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonViewDTO>> getAllPeople(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of People begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    BasicPage<PersonViewDTO> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = personService.findAll(pageable);
    } else {
      page = personService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil.generateBasicPaginationHttpHeaders(page, "/api/people");
    log.info("REST request to get a page of People completed successfully");
    return new ResponseEntity<>(personViewDecorator.decorate(page.getContent()), headers, HttpStatus.OK);
  }


  /**
   * GET  /people : get all the people.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of people in body
   */
  @ApiOperation(value = "Lists People data",
      notes = "Returns a list of people with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Person list")})
  @GetMapping("/people/count")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<Integer> getAllPeopleCount(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of People begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Integer count = 0;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      count = personService.findAllCountQuery();
    } else {
      count = personService.advancedSearchCountQuery(searchQuery, columnFilters, pageable);
    }
    log.info("REST request to get a page of People completed successfully");
    return new ResponseEntity<>(count, HttpStatus.OK);
  }

  /**
   * GET  /people/phn/in/{publicHealthNumbers} : get people given their ID's.
   * Ignores malformed or not found people
   *
   * @param publicHealthNumbers the ids to search by
   * @return the ResponseEntity with status 200 (OK)  and the list of people in body, or empty list
   */
  @GetMapping("/people/phn/in/{publicHealthNumbers}")
  @ApiOperation(value = "Get people by public Health Numbers", notes = "Returns a list of people", responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Expects a list of ids as query parameters"),
      @ApiResponse(code = 200, message = "Person list")})
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonDTO>> getPersonsWithPublicHealthNumbersIn(@ApiParam(name = "publicHealthNumbers", allowMultiple = true) @PathVariable("publicHealthNumbers") List<String> publicHealthNumbers) {
    log.debug("REST request to find several Person: {}", publicHealthNumbers);
    if (!publicHealthNumbers.isEmpty()) {
      UrlDecoderUtil.decode(publicHealthNumbers);
      return new ResponseEntity<>(personService.findPersonsByPublicHealthNumbersIn(publicHealthNumbers), HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * GET  /people/in/{ids} : get people given their ID's.
   * Ignores malformed or not found people
   *
   * @param ids the ids to search by
   * @return the ResponseEntity with status 200 (OK)  and the list of people in body, or empty list
   */
  @GetMapping("/people/in/{ids}")
  @ApiOperation(value = "Get people by ids", notes = "Returns a list of people", responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Expects a list of ids as query parameters"),
      @ApiResponse(code = 200, message = "Person list")})
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonDTO>> getPersonsIn(@ApiParam(name = "ids", allowMultiple = true) @PathVariable("ids") Set<Long> ids) {
    log.debug("REST request to find several Person: {}", ids);
    if (!ids.isEmpty()) {
      return new ResponseEntity<>(personService.findByIdIn(ids), HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * GET  /people/in/{ids}/basic : get people given their ID's.
   * Ignores malformed or not found people
   *
   * @param ids the ids to search by
   * @return the ResponseEntity with status 200 (OK)  and the list of personBasicDetails in body, or empty list
   */
  @GetMapping("/people/in/{ids}/basic")
  @ApiOperation(value = "Get person basic details by ids", notes = "Returns a list of person basic details", responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Expects a list of ids as query parameters"),
      @ApiResponse(code = 200, message = "Person basic details list")})
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonBasicDetailsDTO>> getPersonBasicDetailsIn(@ApiParam(name = "ids", allowMultiple = true) @PathVariable("ids") Set<Long> ids) {
    log.debug("REST request to find several Person: {}", ids);
    if (!ids.isEmpty()) {
      return new ResponseEntity<>(personService.findBasicDetailsByIdIn(ids), HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * GET  /people/basic : search for people basic details. Automatically limited to 100 results.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of people basic details in body
   */
  @ApiOperation(value = "Lists People basic details data",
      notes = "Returns a list of people basic details with support for smart search \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Person basic list")})
  @GetMapping("/people/basic")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonBasicDetailsDTO>> searchBasicDetails(
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery) {
    log.debug("REST request to get a basic details page of People");
    searchQuery = sanitize(searchQuery);

    List<PersonBasicDetailsDTO> result = personService.basicDetailsSearch(searchQuery);
    return new ResponseEntity<>(result, null, HttpStatus.OK);
  }

  /**
   * GET  /people/:id : get the "id" person.
   *
   * @param id the id of the personDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the personDTO, or with status 404 (Not Found)
   */
  @GetMapping("/people/{id}")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<PersonDTO> getPerson(@PathVariable Long id) {
    log.debug("REST request to get Person : {}", id);
    personService.canLoggedInUserViewOrAmend(id);

    PersonDTO personDTO = personService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(personDTO));
  }

  /**
   * DELETE  /people/:id : delete the "id" person.
   *
   * @param id the id of the personDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/people/{id}")
  @Timed
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
    log.debug("REST request to delete Person : {}", id);
    personService.canLoggedInUserViewOrAmend(id);

    personService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * GET  /people/{id}/basic : get a person's basic details
   *
   * @param id the trainee Id
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/people/{id}/basic")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<PersonBasicDetailsDTO> getBasicDetails(@PathVariable Long id) {
    log.debug("REST request to get basic details");
    personService.canLoggedInUserViewOrAmend(id);

    PersonBasicDetailsDTO basicDetails = personService.getBasicDetails(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(basicDetails));
  }

  /**
   * GET  /people/{id}/placements : get all the placements for a trainee.
   *
   * @param id the trainee Id
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/people/{id}/placements")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PlacementViewDTO>> getPlacementsForTrainee(@PathVariable Long id) {
    log.debug("REST request to get a page of Placements");
    personService.canLoggedInUserViewOrAmend(id);

    List<PlacementView> placementViews = placementViewRepository.findAllByTraineeIdOrderByDateToDesc(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementViews != null ?
        placementViewDecorator.decorate(placementViewMapper.placementViewsToPlacementViewDTOs(placementViews)) :
        null));
  }

  /**
   * GET  /people/{id}/placements : get all the placements for a trainee.
   *
   * @param gmcId the trainee GMC Id
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/people/gmc/{gmcId}/placements")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PlacementViewDTO>> getPlacementsForTraineeByGmcId(@PathVariable String gmcId) {
    log.debug("REST request to get a page of Placements");

    Long personId = personService.findIdByGmcId(gmcId);
    personService.canLoggedInUserViewOrAmend(personId);

    return getPlacementsForTrainee(personId);
  }



  /**
   * GET  /people/{id}/placements : get all the placements for a trainee.
   *
   * @param id the trainee Id
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/people/{id}/placements/new")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PlacementSummaryDTO>> getPersonPlacements(@PathVariable Long id) {
    log.debug("REST request to get a page of Placements");
    personService.canLoggedInUserViewOrAmend(id);

    List<PlacementSummaryDTO> placementForTrainee = placementService.getPlacementForTrainee(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementForTrainee != null ? placementSummaryDecorator.decorate(placementForTrainee) : null));
  }


  /**
   * GET  /people/{id}/placements : get all the placements for a trainee.
   *
   * @param gmcId the trainee GMC Id
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/people/gmc/{gmcId}/placements/new")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PlacementSummaryDTO>> getPersonPlacementsByGmcId(@PathVariable String gmcId) {
    log.debug("REST request to get a page of Placements");
    Long personId = personService.findIdByGmcId(gmcId);
    personService.canLoggedInUserViewOrAmend(personId);

    return getPersonPlacements(personId);
  }


  /**
   * POST  /people : Bulk patch people.
   *
   * @param personDTOs the personDTOs to create/update
   * @return the ResponseEntity with status 200 and with body the new personDTOs
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PatchMapping("/people")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:consolidated_etl', 'Update')")
  public ResponseEntity<List<PersonDTO>> patchPersons(@Valid @RequestBody List<PersonDTO> personDTOs) {
    log.debug("REST request to patch Persons: {}", personDTOs);
    List<PersonDTO> result = personService.save(personDTOs);
    List<Long> ids = result.stream().map(PersonDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * POST  /people/ownership : Build the person view table
   *
   * @return the ResponseEntity with status 200 (OK)
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @ApiOperation(value = "Run the stored procedure to build the person view",
          response = ResponseEntity.class, responseContainer = "void")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Person list", response = ResponseEntity.class)})
  @PostMapping("/people/ownership")
  @Timed
  @PreAuthorize("hasPermission('tis:people::person:consolidated_etl', 'Update')")
  public ResponseEntity<Void> buildPersonsOwnership() {
    personService.buildPersonView();
    return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "procedure is underway")).build();
  }

}
