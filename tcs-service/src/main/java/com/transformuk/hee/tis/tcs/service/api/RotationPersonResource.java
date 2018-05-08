package com.transformuk.hee.tis.tcs.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.service.RotationPersonService;
import com.transformuk.hee.tis.tcs.api.dto.RotationPersonDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RotationPerson.
 */
@RestController
@RequestMapping("/api")
public class RotationPersonResource {

    private final Logger log = LoggerFactory.getLogger(RotationPersonResource.class);

    private static final String ENTITY_NAME = "rotationPerson";

    private final RotationPersonService rotationPersonService;

    public RotationPersonResource(RotationPersonService rotationPersonService) {
        this.rotationPersonService = rotationPersonService;
    }

    /**
     * POST  /rotation-people : Create a new rotationPerson.
     *
     * @param rotationPersonDTO the rotationPersonDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rotationPersonDTO, or with status 400 (Bad Request) if the rotationPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rotation-people")
    @Timed
    public ResponseEntity<RotationPersonDTO> createRotationPerson(@RequestBody @Validated(Create.class) RotationPersonDTO rotationPersonDTO) throws URISyntaxException {
        log.debug("REST request to save RotationPerson : {}", rotationPersonDTO);
        RotationPersonDTO result = rotationPersonService.save(rotationPersonDTO);
        return ResponseEntity.created(new URI("/api/rotation-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rotation-people : Updates an existing rotationPerson.
     *
     * @param rotationPersonDTO the rotationPersonDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rotationPersonDTO,
     * or with status 400 (Bad Request) if the rotationPersonDTO is not valid,
     * or with status 500 (Internal Server Error) if the rotationPersonDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rotation-people")
    @Timed
    public ResponseEntity<RotationPersonDTO> updateRotationPerson(@RequestBody @Validated(Update.class) RotationPersonDTO rotationPersonDTO) throws URISyntaxException {
        log.debug("REST request to update RotationPerson : {}", rotationPersonDTO);
        if (rotationPersonDTO.getId() == null) {
            return createRotationPerson(rotationPersonDTO);
        }
        RotationPersonDTO result = rotationPersonService.save(rotationPersonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rotationPersonDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rotation-people : get all the rotationPeople.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rotationPeople in body
     */
    @GetMapping("/rotation-people")
    @Timed
    public List<RotationPersonDTO> getAllRotationPeople() {
        log.debug("REST request to get all RotationPeople");
        return rotationPersonService.findAll();
    }

    /**
     * GET  /rotation-people/:id : get the "id" rotationPerson.
     *
     * @param id the id of the rotationPersonDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rotationPersonDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rotation-people/{id}")
    @Timed
    public ResponseEntity<RotationPersonDTO> getRotationPerson(@PathVariable Long id) {
        log.debug("REST request to get RotationPerson : {}", id);
        RotationPersonDTO rotationPersonDTO = rotationPersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rotationPersonDTO));
    }

    /**
     * GET  /rotation-people/by/person/:id : get the "id" rotationPerson.
     *
     * @param id the id of the person to retrieve rotations for
     * @return the ResponseEntity with status 200 (OK) and with body the rotationPersonDTO, or with status 404 (Not Found)
     */
    @GetMapping("/rotation-people/by/person/{id}")
    @Timed
    public ResponseEntity<List<RotationPersonDTO>> getRotationForPerson(@PathVariable Long id) {
        log.debug("REST request to get RotationPerson : {}", id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rotationPersonService.findByPersonId(id)));
    }

    /**
     * DELETE  /rotation-people/:id : delete the "id" rotationPerson.
     *
     * @param id the id of the rotationPersonDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rotation-people/{id}")
    @Timed
    public ResponseEntity<Void> deleteRotationPerson(@PathVariable Long id) {
        log.debug("REST request to delete RotationPerson : {}", id);
        rotationPersonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
