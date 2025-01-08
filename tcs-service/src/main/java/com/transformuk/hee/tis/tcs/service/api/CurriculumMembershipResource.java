package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.tcs.service.api.validation.CurriculumMembershipValidator;
import com.transformuk.hee.tis.tcs.service.service.CurriculumMembershipService;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing CurriculumMembership.
 */
@RestController
@RequestMapping("/api")
public class CurriculumMembershipResource {

  private static final String ENTITY_NAME = "curriculumMembership";
  private final Logger log = LoggerFactory.getLogger(CurriculumMembershipResource.class);
  private final CurriculumMembershipService cmService;
  private final CurriculumMembershipValidator cmValidator;

  public CurriculumMembershipResource(CurriculumMembershipService cmService,
      CurriculumMembershipValidator cmValidator) {
    this.cmService = cmService;
    this.cmValidator = cmValidator;
  }

  /**
   * POST /curriculum-membership : Add a new curriculumMembership.
   *
   * @param cmDto the curriculumMembershipDto to add
   * @return the ResponseEntity with status 201 (Created) and with body the new
   * CurriculumMembershipDTO, or with status 400 (Bad Request) if the curriculumMembershipDto has
   * already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/curriculum-membership")
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<CurriculumMembershipDTO> createCurriculumMembership(
      @RequestBody @Validated(Create.class) CurriculumMembershipDTO cmDto)
      throws URISyntaxException, MethodArgumentNotValidException, NoSuchMethodException {
    log.debug("REST request to create CurriculumMembership : {}", cmDto);
    if (cmDto.getId() != null) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "must_provide_id",
              "A new curriculumMembership cannot already have an ID")).body(null);
    }
    cmValidator.validate(cmDto);
    CurriculumMembershipDTO result = cmService.save(cmDto);
    return ResponseEntity.created(
            new URI("/api/curriculum-membership/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }
}
