package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.ColumnFilterDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.*;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.RotationService;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import io.github.jhipster.web.util.ResponseUtil;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Rotation.
 */
@RestController
@RequestMapping("/api")
public class RotationResource {

    private final Logger log = LoggerFactory.getLogger(RotationResource.class);

    private static final String ENTITY_NAME = "rotation";

    private final RotationService rotationService;

    public RotationResource(RotationService rotationService) {
        this.rotationService = rotationService;
    }

    /**
     * POST  /rotations : Create a new rotation.
     *
     * @param rotationDTO the rotationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rotationDTO, or with status 400 (Bad Request) if the rotation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rotations")
    @Timed
    public ResponseEntity<RotationDTO> createRotation(@RequestBody @Validated(Create.class) RotationDTO rotationDTO) throws URISyntaxException {
        log.debug("REST request to save Rotation : {}", rotationDTO);
        RotationDTO result = rotationService.save(rotationDTO);
        return ResponseEntity.created(new URI("/api/rotations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rotations : Updates an existing rotation.
     *
     * @param rotationDTO the rotationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rotationDTO,
     * or with status 400 (Bad Request) if the rotationDTO is not valid,
     * or with status 500 (Internal Server Error) if the rotationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rotations")
    @Timed
    public ResponseEntity<RotationDTO> updateRotation(@RequestBody @Validated(Update.class) RotationDTO rotationDTO) throws URISyntaxException {
        log.debug("REST request to update Rotation : {}", rotationDTO);
        if (rotationDTO.getId() == null) {
            return createRotation(rotationDTO);
        }
        RotationDTO result = rotationService.save(rotationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rotationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rotations : get all the rotations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rotations in body
     */
    @GetMapping("/rotations")
    @Timed
    public ResponseEntity<List<RotationDTO>> getRotations(
            @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"owner\": [\"dean1\", \"dean2\"]," +
            " \"sites.siteId\":[\"123\"],\"trainingBodyId\":[\"11\"],\"grades.gradeId\":[\"11\"],\"specialties.specialty.name\":[\"Test Specialty\"]}\"")
            @RequestParam(value = "columnFilters", required = false) String columnFilterJson,
            Pageable pageable) throws IOException {
        log.debug("REST request to get a page of Rotations");
        List<Class> filterEnumList = Collections.singletonList(Status.class);
        List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
        Page<RotationDTO> page;
        if (StringUtils.isEmpty(columnFilterJson)) {
            page = rotationService.findAll(pageable);
        } else {
            page = rotationService.advancedSearchBySpecification(columnFilters, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rotations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /rotations/:id : get the "id" rotation.
     *
     * @param id the id of the rotationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rotationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rotations/{id}")
    @Timed
    public ResponseEntity<RotationDTO> getRotation(@PathVariable Long id) {
        log.debug("REST request to get Rotation : {}", id);
        RotationDTO rotationDTO = rotationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rotationDTO));
    }

    /**
     * DELETE  /rotations/:id : delete the "id" rotation.
     *
     * @param id the id of the rotationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rotations/{id}")
    @Timed
    public ResponseEntity<Void> deleteRotation(@PathVariable Long id) {
        log.debug("REST request to delete Rotation : {}", id);
        rotationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
