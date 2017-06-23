package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeService;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Programme.
 */
@RestController
@RequestMapping("/api")
public class ProgrammeResource {

	private static final String ENTITY_NAME = "programme";
	private final Logger log = LoggerFactory.getLogger(ProgrammeResource.class);
	private final ProgrammeService programmeService;

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
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('tcs:add:modify:entities')")
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

	/**
	 * GET  /programmes : get all the programmes.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of programmes in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/programmes")
	@Timed
	@PreAuthorize("hasAuthority('tcs:view:entities')")
	public ResponseEntity<List<ProgrammeDTO>> getAllProgrammes(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Programmes");
		Page<ProgrammeDTO> page = programmeService.findAll(pageable);
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
	@PreAuthorize("hasAuthority('tcs:view:entities')")
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

    /**
     * POST  /bulk-programmes : Bulk create a Programme.
     *
     * @param programmeDTOS List of the programmeDTOS to create
     * @return the ResponseEntity with status 200 (Created) and with body the new programmeDTOS, or with status 400 (Bad Request) if the Programme has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bulk-programmes")
    @Timed
    @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
    public ResponseEntity<List<ProgrammeDTO>> bulkCreateProgrammes(@Valid @RequestBody List<ProgrammeDTO> programmeDTOS) throws URISyntaxException {
        log.debug("REST request to bulk save Programmes : {}", programmeDTOS);
        if (!Collections.isEmpty(programmeDTOS)) {
            List<Long> entityIds = programmeDTOS.stream()
                .filter(p -> p.getId() != null)
                .map(p -> p.getId())
                .collect(Collectors.toList());
            if (!Collections.isEmpty(entityIds)) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new Programme cannot already have an ID")).body(null);
            }
        }
        List<ProgrammeDTO> result = programmeService.save(programmeDTOS);
        List<Long> ids = result.stream().map(r -> r.getId()).collect(Collectors.toList());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
            .body(result);
    }

    /**
     * PUT  /bulk-programmes : Updates an existing Programme.
     *
     * @param programmeDTOS List of the programmeDTOS to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated programmeDTOS,
     * or with status 400 (Bad Request) if the programmeDTOS is not valid,
     * or with status 500 (Internal Server Error) if the programmeDTOS couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bulk-programmes")
    @Timed
    @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
    public ResponseEntity<List<ProgrammeDTO>> bulkUpdateProgrammes(@Valid @RequestBody List<ProgrammeDTO> programmeDTOS) throws URISyntaxException {
        log.debug("REST request to bulk update Programme : {}", programmeDTOS);
        if (Collections.isEmpty(programmeDTOS)) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
                "The request body for this end point cannot be empty")).body(null);
        } else if (!Collections.isEmpty(programmeDTOS)) {
            List<ProgrammeDTO> entitiesWithNoId = programmeDTOS.stream().filter(p -> p.getId() == null).collect(Collectors.toList());
            if (!Collections.isEmpty(entitiesWithNoId)) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                    "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
            }
        }

        List<ProgrammeDTO> results = programmeService.save(programmeDTOS);
        List<Long> ids = results.stream().map(r -> r.getId()).collect(Collectors.toList());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
            .body(results);
    }
}
