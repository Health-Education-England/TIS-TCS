package com.transformuk.hee.tis.tcs.service.api;

import static uk.nhs.tis.StringConverter.getConverter;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PersonBasicDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonLiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonV2DTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSummaryDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.decorator.PersonViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementSummaryDecorator;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.ContactDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.GdcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.PlacementView;
import com.transformuk.hee.tis.tcs.service.repository.PlacementViewRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementViewMapper;
import io.github.jhipster.web.util.ResponseUtil;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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

/**
 * REST controller for managing Person.
 */
@RestController
@RequestMapping({"/api", "/etl/api"})
public class PersonResource {

  private static final String ENTITY_NAME = "person";
  private final Logger log = LoggerFactory.getLogger(PersonResource.class);
  private final PersonService personService;
  private final PlacementViewRepository placementViewRepository;
  private final PlacementViewMapper placementViewMapper;
  private final PlacementViewDecorator placementViewDecorator;
  private final PersonViewDecorator personViewDecorator;
  private final PlacementService placementService;
  private final PlacementSummaryDecorator placementSummaryDecorator;
  private final PersonValidator personValidator;
  private final GmcDetailsValidator gmcDetailsValidator;
  private final GdcDetailsValidator gdcDetailsValidator;
  private final PersonalDetailsValidator personalDetailsValidator;
  private final ContactDetailsValidator contactDetailsValidator;
  private final PersonElasticSearchService personElasticSearchService;
  @Value("${enable.es.search}")
  private boolean enableEsSearch;

  public PersonResource(PersonService personService,
      PlacementViewRepository placementViewRepository,
      PlacementViewMapper placementViewMapper, PlacementViewDecorator placementViewDecorator,
      PersonViewDecorator personViewDecorator, PlacementService placementService,
      PlacementSummaryDecorator placementSummaryDecorator, PersonValidator personValidator,
      GmcDetailsValidator gmcDetailsValidator, GdcDetailsValidator gdcDetailsValidator,
      PersonalDetailsValidator personalDetailsValidator,
      ContactDetailsValidator contactDetailsValidator,
      PersonElasticSearchService personElasticSearchService) {
    this.personService = personService;
    this.placementViewRepository = placementViewRepository;
    this.placementViewMapper = placementViewMapper;
    this.placementViewDecorator = placementViewDecorator;
    this.personViewDecorator = personViewDecorator;
    this.placementService = placementService;
    this.placementSummaryDecorator = placementSummaryDecorator;
    this.personValidator = personValidator;
    this.gmcDetailsValidator = gmcDetailsValidator;
    this.gdcDetailsValidator = gdcDetailsValidator;
    this.personalDetailsValidator = personalDetailsValidator;
    this.contactDetailsValidator = contactDetailsValidator;
    this.personElasticSearchService = personElasticSearchService;
  }

  /**
   * POST  /people : Create a new person.
   *
   * @param personDTO the personDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new personDTO, or with
   * status 400 (Bad Request) if the person has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/people")
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<PersonDTO> createPerson(
      @RequestBody @Validated(Create.class) final PersonDTO personDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save Person : {}", personDTO);
    if (personDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists", "A new person cannot already have an ID"))
          .body(null);
    }
    personValidator.validate(personDTO);
    gmcDetailsValidator.validate(personDTO.getGmcDetails());
    gdcDetailsValidator.validate(personDTO.getGdcDetails());
    personalDetailsValidator.validate(personDTO.getPersonalDetails());
    contactDetailsValidator.validate(personDTO.getContactDetails());

    final PersonDTO result = personService.create(personDTO);
    return ResponseEntity.created(new URI("/api/people/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /people : Updates an existing person.
   *
   * @param personDTO the personDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated personDTO, or with
   * status 400 (Bad Request) if the personDTO is not valid, or with status 500 (Internal Server
   * Error) if the personDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/people")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<PersonDTO> updatePerson(
      @RequestBody @Validated(Update.class) PersonDTO personDTO)
      throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to update Person : {}", personDTO);
    if (personDTO.getId() == null) {
      return createPerson(personDTO);
    }
    personService.canLoggedInUserViewOrAmend(personDTO.getId());

    personValidator.validate(personDTO);
    gmcDetailsValidator.validate(personDTO.getGmcDetails());
    gdcDetailsValidator.validate(personDTO.getGdcDetails());
    personalDetailsValidator.validate(personDTO.getPersonalDetails());
    contactDetailsValidator.validate(personDTO.getContactDetails());

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
  @GetMapping("/people")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonViewDTO>> getAllPeople(
      final Pageable pageable,
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @RequestParam(value = "columnFilters", required = false) final String columnFilterJson,
      @RequestParam(required = false, defaultValue = "false") boolean enableES) throws IOException {

    log.debug("REST request to get a page of People begin");
    searchQuery = getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    String searchQueryES = getConverter(searchQuery).fromJson().decodeUrl().escapeForElasticSearch()
        .toString();
    final List<Class> filterEnumList = Lists.newArrayList(Status.class);
    final List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    final Page<PersonViewDTO> page;

    //feature flag to enable es, allow the enabling from the FE
    if (enableEsSearch || enableES) {
      page = personElasticSearchService.searchForPage(searchQueryES, columnFilters, pageable);
    } else {
      if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
        page = personService.findAll(pageable);
      } else {
        page = personService.advancedSearch(searchQuery, columnFilters, pageable);
      }
      log.debug("REST request to get a page of People completed successfully");
    }
    final HttpHeaders headers = PaginationUtil
        .generateBasicPaginationHttpHeaders(page, "/api/people");
    return new ResponseEntity<>(personViewDecorator.decorate(page.getContent()), headers,
        HttpStatus.OK);
  }


  /**
   * GET  /people/phn/in/{publicHealthNumbers} : get people given their ID's. Ignores malformed or
   * not found people
   *
   * @param publicHealthNumbers the ids to search by
   * @return the ResponseEntity with status 200 (OK)  and the list of people in body, or empty list
   */
  @GetMapping("/people/phn/in/{publicHealthNumbers}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonDTO>> getPersonsWithPublicHealthNumbersIn(
      @PathVariable("publicHealthNumbers") final List<String> publicHealthNumbers) {
    log.debug("REST request to find several Person: {}", publicHealthNumbers);
    if (!publicHealthNumbers.isEmpty()) {
      UrlDecoderUtil.decode(publicHealthNumbers);
      return new ResponseEntity<>(
          personService.findPersonsByPublicHealthNumbersIn(publicHealthNumbers), HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/people/roles/categories/{categoryId}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<Collection<PersonLiteDTO>> getPersonsByRoleCategory(
      final Pageable pageable,
      @PathVariable("categoryId") final Long categoryId,
      @RequestParam(value = "searchQuery", required = false) final String searchQuery) {
    log.info(
        "Received request to search '{}' with RoleCategory ID '{}', searchQuery '{}' and pageable '{}'",
        PersonLiteDTO.class.getSimpleName(), categoryId, searchQuery, pageable);

    log.debug("Accessing '{}' to search '{}' with RoleCategory ID '{}' and searchQuery '{}'",
        personService.getClass().getSimpleName(), PersonLiteDTO.class.getSimpleName(), categoryId,
        searchQuery);

    final Page<PersonLiteDTO> page = personService.searchByRoleCategory(
        Optional.ofNullable(searchQuery).orElse("").replace("\"", ""), categoryId, pageable, true);

    final HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/people/roles/categories/" + categoryId);

    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /people/in/{ids} : get people given their ID's. Ignores malformed or not found people
   *
   * @param ids the ids to search by
   * @return the ResponseEntity with status 200 (OK)  and the list of people in body, or empty list
   */
  @GetMapping("/people/in/{ids}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonDTO>> getPersonsIn(@PathVariable("ids") final Set<Long> ids) {
    log.debug("REST request to find several Person: {}", ids);
    if (!ids.isEmpty()) {
      return new ResponseEntity<>(personService.findByIdIn(ids), HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * GET  /people/in/{ids}/basic : get people given their ID's. Ignores malformed or not found
   * people
   *
   * @param ids the ids to search by
   * @return the ResponseEntity with status 200 (OK)  and the list of personBasicDetails in body, or
   * empty list
   */
  @GetMapping("/people/in/{ids}/basic")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonBasicDetailsDTO>> getPersonBasicDetailsIn(
      @PathVariable("ids") final Set<Long> ids) {
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
  @GetMapping("/people/basic")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PersonBasicDetailsDTO>> searchBasicDetails(
      @RequestParam(value = "searchQuery", required = false) String searchQuery) {
    log.debug("REST request to get a basic details page of People");
    searchQuery = getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();

    final List<PersonBasicDetailsDTO> result = personService.basicDetailsSearch(searchQuery);
    return new ResponseEntity<>(result, null, HttpStatus.OK);
  }

  /**
   * GET  /people/:id : get the "id" person.
   *
   * @param id the id of the personDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the personDTO, or with status 404
   * (Not Found)
   */
  @GetMapping("/people/{id}")
  @PreAuthorize("(hasRole('ETL') or hasPermission('tis:people::person:', 'View')) AND hasPermission(#id, 'com.transformuk.hee.tis.tcs.service.model.Person', 'read')")
  public ResponseEntity<PersonDTO> getPerson(@PathVariable Long id) {
    log.debug("REST request to get Person : {}", id);
    personService.canLoggedInUserViewOrAmend(id);

    PersonDTO personDTO = personService.findPersonWithProgrammeMembershipsSorted(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(personDTO));
  }

  /**
   * GET  /people/v2/:id : get the "id" person.
   * <p>
   * This endpoint was created because we want to send back a person obj with no qualification as
   * trust users shouldn't be able to access it.
   *
   * @param id the id of the personDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the personDTO, or with status 404
   * (Not Found)
   */
  @GetMapping("/people/v2/{id}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View') AND hasPermission(#id, 'com.transformuk.hee.tis.tcs.service.model.Person', 'read')")
  public ResponseEntity<PersonV2DTO> getPersonV2(@PathVariable Long id) {
    log.debug("REST request to get Person : {}", id);
    personService.canLoggedInUserViewOrAmend(id);

    PersonV2DTO personDTO = personService.findPersonV2WithProgrammeMembershipsSorted(id);

    // Remove the national insurance number form personal details, as it is not needed in the UI.
    PersonalDetailsDTO personalDetailsDto = personDTO.getPersonalDetails();
    if (personalDetailsDto != null) {
      personalDetailsDto.setNationalInsuranceNumber(null);
    }

    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(personDTO));
  }

  /**
   * DELETE  /people/:id : delete the "id" person.
   *
   * @param id the id of the personDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/people/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
    log.debug("REST request to delete Person : {}", id);
    personService.canLoggedInUserViewOrAmend(id);

    personService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * GET  /people/{id}/basic : get a person's basic details
   *
   * @param id the trainee Id
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/people/{id}/basic")
  @PreAuthorize("hasPermission('tis:people::person:', 'View') AND hasPermission(#id, 'com.transformuk.hee.tis.tcs.service.model.Person', 'read')")
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
  @PreAuthorize("hasPermission('tis:people::person:', 'View') AND hasPermission(#id, 'com.transformuk.hee.tis.tcs.service.model.Person', 'read')")
  public ResponseEntity<List<PlacementViewDTO>> getPlacementsForTrainee(@PathVariable Long id) {
    log.debug("REST request to get a page of Placements");
    personService.canLoggedInUserViewOrAmend(id);

    List<PlacementView> placementViews = placementViewRepository
        .findAllByTraineeIdOrderByDateToDesc(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementViews != null ?
        placementViewDecorator
            .decorate(placementViewMapper.placementViewsToPlacementViewDTOs(placementViews)) :
        null));
  }

  /**
   * GET  /people/{id}/placements : get all the placements for a trainee.
   *
   * @param gmcId the trainee GMC Id
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/people/gmc/{gmcId}/placements")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PlacementViewDTO>> getPlacementsForTraineeByGmcId(
      @PathVariable String gmcId) {
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
  @PreAuthorize("hasPermission('tis:people::person:', 'View') AND hasPermission(#id, 'com.transformuk.hee.tis.tcs.service.model.Person', 'read')")
  public ResponseEntity<List<PlacementSummaryDTO>> getPersonPlacements(@PathVariable Long id) {
    log.debug("REST request to get a page of Placements");
    personService.canLoggedInUserViewOrAmend(id);

    PersonDTO trainee = personService.findOne(id);
    if (trainee != null) {
      List<PlacementSummaryDTO> placementForTrainee = placementService
          .getPlacementForTrainee(id, trainee.getRole());
      return ResponseUtil.wrapOrNotFound(Optional.ofNullable(
          placementForTrainee != null ? placementSummaryDecorator.decorate(placementForTrainee)
              : null));
    }
    return ResponseUtil.wrapOrNotFound(Optional.empty());
  }


  /**
   * GET  /people/{id}/placements : get all the placements for a trainee.
   *
   * @param gmcId the trainee GMC Id
   * @return the ResponseEntity with status 200 (OK) and the list of placements in body
   */
  @GetMapping("/people/gmc/{gmcId}/placements/new")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<PlacementSummaryDTO>> getPersonPlacementsByGmcId(
      @PathVariable String gmcId) {
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
  @PreAuthorize("hasPermission('tis:people::person:consolidated_etl', 'Update')")
  public ResponseEntity<List<PersonDTO>> patchPersons(
      @Valid @RequestBody final List<PersonDTO> personDTOs) {
    log.debug("REST request to patch Persons: {}", personDTOs);
    final List<PersonDTO> result = personService.save(personDTOs);
    final List<Long> ids = result.stream().map(PersonDTO::getId).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PATCH /bulk-people : People patch for bulk operations.
   *
   * @param personDtos The {@link PersonDTO PersonDTOs} with data to patch.
   * @return the ResponseEntity with status 200 (OK) and the list of patched people in body.
   */
  @PatchMapping("/bulk-people")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<List<PersonDTO>> patchPeople(@RequestBody List<PersonDTO> personDtos) {
    log.debug("REST request to patch People: {}", personDtos);

    List<PersonDTO> result = personService.patch(personDtos);
    final List<String> ids = result.stream().map(dto -> dto.getId().toString())
        .collect(Collectors.toList());

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
  @PostMapping("/people/ownership")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<Void> buildPersonsOwnership() {
    personService.buildPersonView();
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, "procedure is underway")).build();
  }

  /**
   * Advance search endpoint that support fuzzy search.
   * <p>
   * Find people that are enrolled to certain programme by id
   *
   * @param id          the id of the programme
   * @param searchQuery fuzzy search param
   * @param pageable    the page in which we need
   * @return
   */
  @GetMapping("/programme/{id}/people")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<Page<PersonViewDTO>> findPeopleOnProgramme(@PathVariable Long id,
      @RequestParam(required = false) String searchQuery, Pageable pageable) {
    Page<PersonViewDTO> results = personElasticSearchService
        .findPeopleOnProgramme(id, searchQuery, pageable);
    final HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(results, "/api/people");
    return new ResponseEntity<>(results, headers, HttpStatus.OK);
  }
}
