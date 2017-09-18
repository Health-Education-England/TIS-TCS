package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

  public PersonResource(PersonService personService) {
    this.personService = personService;
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
  @PreAuthorize("hasAuthority('person:add:modify')")
  public ResponseEntity<PersonDTO> createPerson(@RequestBody @Validated(Create.class) PersonDTO personDTO) throws URISyntaxException {
    log.debug("REST request to save Person : {}", personDTO);
    if (personDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new person cannot already have an ID")).body(null);
    }
    PersonDTO result = personService.save(personDTO);
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
  @PreAuthorize("hasAuthority('person:add:modify')")
  public ResponseEntity<PersonDTO> updatePerson(@RequestBody @Validated(Update.class) PersonDTO personDTO) throws URISyntaxException {
    log.debug("REST request to update Person : {}", personDTO);
    if (personDTO.getId() == null) {
      return createPerson(personDTO);
    }
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
      notes = "Returns a list of people with support for pagination, sorting, smart search and column filters \n",
      response = ResponseEntity.class, responseContainer = "Person list")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Person list", response = ResponseEntity.class)})
  @GetMapping("/people")
  @Timed
  @PreAuthorize("hasAuthority('person:view')")
  public ResponseEntity<List<PersonDTO>> getAllPeople(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.debug("REST request to get a page of People");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<PersonDTO> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = personService.findAll(pageable);
    } else {
      page = personService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/people");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /people/:id : get the "id" person.
   *
   * @param id the id of the personDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the personDTO, or with status 404 (Not Found)
   */
  @GetMapping("/people/{id}")
  @Timed
  @PreAuthorize("hasAuthority('person:view')")
  public ResponseEntity<PersonDTO> getPerson(@PathVariable Long id) {
    log.debug("REST request to get Person : {}", id);
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
    personService.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
