package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.decorator.PlacementDetailsDecorator;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.PlacementValidator;
import com.transformuk.hee.tis.tcs.service.api.validation.ValidationException;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Placement.
 */
@RestController
@RequestMapping("/api")
public class PlacementResource {

    private static final String ENTITY_NAME = "placement";
    private static final String REQUEST_BODY_EMPTY = "request.body.empty";
    private static final String REQUEST_BODY_CANNOT_BE_EMPTY = "The request body for this end point cannot be empty";
    private static final String BULK_UPDATE_FAILED_NOID = "bulk.update.failed.noId";
    private static final String NOID_ERR_MSG = "Some DTOs you've provided have no Id, cannot update entities that don't exist";
    private final Logger log = LoggerFactory.getLogger(PlacementResource.class);
    private final PlacementService placementService;
    private final PlacementValidator placementValidator;
    private final PlacementDetailsDecorator placementDetailsDecorator;

    public PlacementResource(final PlacementService placementService, final PlacementValidator placementValidator,
                             final PlacementDetailsDecorator placementDetailsDecorator) {
        this.placementService = placementService;
        this.placementValidator = placementValidator;
        this.placementDetailsDecorator = placementDetailsDecorator;
    }

    /**
     * POST  /placements : Create a new placement.
     *
     * @param placementDetailsDTO the placementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new placementDTO, or with status 400 (Bad Request) if the placement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/placements")
    @Timed
    @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
    public ResponseEntity<PlacementDetailsDTO> createPlacement(@RequestBody @Validated(Create.class) final PlacementDetailsDTO placementDetailsDTO) throws URISyntaxException, ValidationException {
        log.debug("REST request to save Placement : {}", placementDetailsDTO);
        placementValidator.validate(placementDetailsDTO);
        if (placementDetailsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new placement cannot already have an ID")).body(null);
        }

        final PlacementDetailsDTO result = placementService.createDetails(placementDetailsDTO);
        return ResponseEntity.created(new URI("/api/placements/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /placements : Updates an existing placement.
     *
     * @param placementDetailsDTO the placementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated placementDTO,
     * or with status 400 (Bad Request) if the placementDTO is not valid,
     * or with status 500 (Internal Server Error) if the placementDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/placements")
    @Timed
    @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
    public ResponseEntity<PlacementDetailsDTO> updatePlacement(@RequestBody @Validated(Update.class) final PlacementDetailsDTO placementDetailsDTO) throws ValidationException, URISyntaxException {
        log.debug("REST request to update Placement : {}", placementDetailsDTO);
        placementValidator.validate(placementDetailsDTO);
        if (placementDetailsDTO.getId() == null) {
            return createPlacement(placementDetailsDTO);
        }
        final PlacementDetailsDTO result = placementService.saveDetails(placementDetailsDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * GET  /placements : get all the placements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of placements in body
     */
    @GetMapping("/placements")
    @Timed
    @PreAuthorize("hasAuthority('tcs:view:entities')")
    public ResponseEntity<List<PlacementDTO>> getAllPlacements(@ApiParam final Pageable pageable) {
        log.debug("REST request to get a page of Placements");
        final Page<PlacementDTO> page = placementService.findAll(pageable);
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/placements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /placements/:id : get the "id" placement.
     *
     * @param id the id of the placementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the placementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/placements/{id}")
    @Timed
    @PreAuthorize("hasAuthority('tcs:view:entities')")
    public ResponseEntity<PlacementDetailsDTO> getPlacement(@PathVariable final Long id) {
        log.debug("REST request to get Placement : {}", id);
        final PlacementDetailsDTO placementDetailsDTO = placementService.getDetails(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementDetailsDecorator.decorate(placementDetailsDTO)));
    }

    /**
     * DELETE  /placements/:id : delete the "id" placement.
     *
     * @param id the id of the placementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/placements/{id}")
    @Timed
    @PreAuthorize("hasAuthority('tcs:delete:entities')")
    public ResponseEntity<Void> deletePlacement(@PathVariable final Long id) {
        log.debug("REST request to delete Placement : {}", id);
        placementValidator.validatePlacementForDelete(id);
        placementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    /**
     * PATCH  /placements : Bulk patch Placements.
     *
     * @param placementDTOS List of the placementDTOS to create
     * @return the ResponseEntity with status 200 (OK) and with body the new placementDTOS
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping("/placements")
    @Timed
    @PreAuthorize("hasAuthority('tcs:add:modify:entities')")
    public ResponseEntity<List<PlacementDTO>> patchPlacements(@RequestBody final List<PlacementDTO> placementDTOS) {
        log.debug("REST request to bulk save Placement : {}", placementDTOS);
        final List<PlacementDTO> result = placementService.save(placementDTOS);
        final List<Long> ids = result.stream().map(PlacementDTO::getId).collect(Collectors.toList());
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
                .body(result);
    }


    /**
     * GET  /placements/filter : get filtered placements.
     *
     * @param pageable
     * @param columnFilterJson
     * @return the ResponseEntity with status 200 (OK) and with body the placementDTO, or with status 404 (Not Found)
     * @throws IOException
     */
    @ApiOperation(value = "Gets filtered placement details based on supplied column filters",
            notes = "Retrieves paginated placement details with support for pagination, sorting and column filters \n",
            response = ResponseEntity.class,
            responseContainer = "Placement details list")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Placement details list",
                    response = ResponseEntity.class)
    })
    @GetMapping("/placements/filter")
    @Timed
    @PreAuthorize("hasAuthority('tcs:view:entities')")
    public ResponseEntity<List<PlacementDetailsDTO>> getFilteredPlacementDetails(
            @ApiParam(value = "request parameters reflecting pagination filter. (Eg: page=2&size=25)") final
            Pageable pageable,
            @ApiParam(value = "json object by column name and value. (Eg: columnFilters=" +
                    "{ \"proposedOutcomeCode\": [\"revalidate\", \"defer\"], \"dateFrom\":[\"10-10-2017\", \"20-10-2017\"] }\"")
            @RequestParam(value = "columnFilters", required = false) final String columnFilterJson) throws IOException {
        log.debug("REST request to get Placements by filter : {}", columnFilterJson);
        final Page<PlacementDetailsDTO> page;
        if (org.apache.commons.lang.StringUtils.isEmpty(columnFilterJson)) {
            page = placementService.findAllPlacementDetails(pageable);
        } else {
            page = placementService.findFilteredPlacements(columnFilterJson, pageable);
        }
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/placements/filter");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/placements/{id}/close")
    @Timed
    @PreAuthorize("hasAuthority('tcs:view:entities')")
    public ResponseEntity<?> closePlacement(@PathVariable final Long id) throws ValidationException {
        placementValidator.validatePlacementForClose(id);
        final PlacementDTO refreshedPlacement = placementService.closePlacement(id);
        return ResponseEntity.ok(refreshedPlacement);
    }

    //Required for the auditing aspect
    public ResponseEntity<PlacementDetailsDTO> getPlacementDetails(@PathVariable final Long id) {
        return getPlacement(id);
    }
}
