package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.TrainerApprovalValidator;
import com.transformuk.hee.tis.tcs.service.service.TrainerApprovalService;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TrainerApprovalResource {

  private static final String ENTITY_NAME = "trainerapproval";
  private final Logger log = LoggerFactory.getLogger(TrainerApprovalResource.class);

  private final TrainerApprovalService trainerApprovalService;
  private final TrainerApprovalValidator trainerApprovalValidator;

  public TrainerApprovalResource(TrainerApprovalService trainerApprovalService,
    TrainerApprovalValidator trainerApprovalValidator) {
    this.trainerApprovalService = trainerApprovalService;
    this.trainerApprovalValidator = trainerApprovalValidator;
  }

  @PostMapping("/trainer-approvals")
  @PreAuthorize("hasPermission('tis:tcs::trainerapproval:', 'Create')")
  public ResponseEntity<TrainerApprovalDTO> createTrainerApproval(
    @RequestBody @Validated(Create.class) TrainerApprovalDTO trainerApprovalDTO)
    throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save TrainerApproval : {}", trainerApprovalDTO);
    trainerApprovalValidator.validate(trainerApprovalDTO);
    TrainerApprovalDTO result = trainerApprovalService.save(trainerApprovalDTO);
    return ResponseEntity.created(new URI("/api/trainer-approvals/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  @PutMapping("/trainer-approvals")
  @PreAuthorize("hasPermission('tis:tcs::trainerapproval:', 'Update')")
  public ResponseEntity<TrainerApprovalDTO> updateTrainerApproval(
    @RequestBody @Validated(Update.class) TrainerApprovalDTO trainerApprovalDTO)
    throws MethodArgumentNotValidException {
    log.debug("REST request to update TrainerApproval : {}", trainerApprovalDTO);
    if (trainerApprovalDTO.getId() == null) {
      return ResponseEntity.badRequest()
        .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
          "You must provide an ID when updating a trainer approval")).body(null);
    }
    TrainerApprovalDTO result = trainerApprovalService.save(trainerApprovalDTO);
    return ResponseEntity.ok()
      .headers(
        HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trainerApprovalDTO.getId().toString()))
      .body(result);
  }

  @GetMapping("/people/{personId}/trainer-approvals")
  @PreAuthorize("hasPermission('tis:tcs::trainerapproval:', 'View')")
  public ResponseEntity<List<TrainerApprovalDTO>> getTrainerApproval(
    @PathVariable Long personId) {
    log.debug("REST request to get trainer approval for a particular person : {}", personId);
    List<TrainerApprovalDTO> trainerApprovals = trainerApprovalService
      .findTrainerApprovalsByPersonId(personId);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trainerApprovals));
  }

  @DeleteMapping("/trainer-approvals/{id}")
  @PreAuthorize("hasAuthority('tcs:delete:entities')")
  public ResponseEntity<Void> deleteTrainerApproval(@PathVariable Long id) {
    log.debug("REST request to delete Trainer Approval : {}", id);
    trainerApprovalService.delete(id);
    return ResponseEntity.ok()
      .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
