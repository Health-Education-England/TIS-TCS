package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;

/**
 * REST controller for managing Programme.
 */
@RestController
@RequestMapping("/api")
public class ProgrammeResource {

	private static final String ENTITY_NAME = "programme";
	private final Logger log = LoggerFactory.getLogger(ProgrammeResource.class);
	private final ProgrammeService programmeService;

	private ObjectMapper mapper = new ObjectMapper();

	public ProgrammeResource(ProgrammeService programmeService) {
		this.programmeService = programmeService;
	}

	/**
	 * POST  /programmes : Create a new programme.
	 *
	 * @param programmeDTO the programmeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new programmeDTO, or with status 400 (Bad Request) if the programme has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/programmes")
	@Timed
	@PreAuthorize("hasAuthority('programme:add:modify')")
	public ResponseEntity<ProgrammeDTO> createProgramme(@RequestBody ProgrammeDTO programmeDTO) throws URISyntaxException {
		log.debug("REST request to save Programme : {}", programmeDTO);
		if (programmeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programme cannot already have an ID")).body(null);
		}
		ProgrammeDTO result = programmeService.save(programmeDTO);
		return ResponseEntity.created(new URI("/api/programmes/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /programmes : Updates an existing programme.
	 *
	 * @param programmeDTO the programmeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated programmeDTO,
	 * or with status 400 (Bad Request) if the programmeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the programmeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/programmes")
	@Timed
	@PreAuthorize("hasAuthority('programme:add:modify')")
	public ResponseEntity<ProgrammeDTO> updateProgramme(@RequestBody ProgrammeDTO programmeDTO) throws URISyntaxException {
		log.debug("REST request to update Programme : {}", programmeDTO);
		if (programmeDTO.getId() == null) {
			return createProgramme(programmeDTO);
		}
		ProgrammeDTO result = programmeService.save(programmeDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programmeDTO.getId().toString()))
				.body(result);
	}


	@ApiOperation(value = "Lists Programmes data",
			notes = "Returns a list of Programmes with support for pagination, sorting, smart search and column filters \r\n" +
					"page amd size should be greater than 0 \r\n" +
					"order should con valid column followed by either 'asc' or 'desc'. \r\n" +
					"searchQuery any wildcard string to be searched. \r\n" +
					"columnFilters json object by column name and value. \r\n" +
					"(Eg: columnFilters={ \"managingDeanery\": [\"dean1\", \"dean2\"], \"dbc\": " +
					"[\"dbc1\"] }",
			response = ResponseEntity.class, responseContainer = "Programmes list")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Programmes list", response = ResponseEntity.class)})
	/**
	 * GET  /programmes : get all the programmes.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of programmes in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/programmes")
	@Timed
	@PreAuthorize("hasAuthority('programme:view')")
	public ResponseEntity<List<ProgrammeDTO>> getAllProgrammes(
			@ApiParam Pageable pageable,
			@RequestParam(value = "searchQuery", required = false) String searchQuery,
			@RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
		log.debug("REST request to get a page of Programmes");

		UserProfile userProfile = getProfileFromContext();
		List<ColumnFilter> columnFilters = getColumnFilters(columnFilterJson);
		Page<ProgrammeDTO> page;
		if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
			page = programmeService.findAll(userProfile.getDesignatedBodyCodes(), pageable);
		} else {
			page = programmeService.advancedSearch(
					userProfile.getDesignatedBodyCodes(), searchQuery, columnFilters, pageable);
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/programmes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /programmes/:id : get the "id" programme.
	 *
	 * @param id the id of the programmeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the programmeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/programmes/{id}")
	@Timed
	@PreAuthorize("hasAuthority('programme:view')")
	public ResponseEntity<ProgrammeDTO> getProgramme(@PathVariable Long id) {
		log.debug("REST request to get Programme : {}", id);
		ProgrammeDTO programmeDTO = programmeService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programmeDTO));
	}

	/**
	 * DELETE  /programmes/:id : delete the "id" programme.
	 *
	 * @param id the id of the programmeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/programmes/{id}")
	@Timed
	@PreAuthorize("hasAuthority('tcs:delete:entities')")
	public ResponseEntity<Void> deleteProgramme(@PathVariable Long id) {
		log.debug("REST request to delete Programme : {}", id);
		programmeService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	private List<ColumnFilter> getColumnFilters(String columnFilterJson) throws IOException {
		if (columnFilterJson != null) {
			if (!columnFilterJson.startsWith("{")) {
				//attempt to decode
				try {
					columnFilterJson = new URLCodec().decode(columnFilterJson);
				} catch (DecoderException e) {
					log.error(e.getMessage(), e);
					throw new IllegalArgumentException("Cannot interpret column filters: " + columnFilterJson);
				}
			}
			TypeReference<HashMap<String, List<String>>> typeRef = new TypeReference<HashMap<String, List<String>>>() {
			};
			Map<String, List<String>> columns = mapper.readValue(columnFilterJson, typeRef);
			try {
				return columns.entrySet().stream()
						.map(e -> new ColumnFilter(e.getKey(),
								e.getKey().equals("status") ?
										e.getValue().stream().map(v -> Status.valueOf(v)).collect(toList()) :
										e.getValue().stream().collect(toList())))
						.collect(toList());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new IllegalArgumentException("Cannot interpret column filters: " + columnFilterJson);
			}
		}
		return EMPTY_LIST;
	}

}
