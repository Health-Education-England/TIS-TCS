package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningStatusDto;
import com.transformuk.hee.tis.tcs.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import io.github.jhipster.web.util.ResponseUtil;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing ConditionsOfJoining.
 */
@RestController
@RequestMapping("/api")
public class ConditionsOfJoiningResource {

  private final Logger log = LoggerFactory.getLogger(ConditionsOfJoiningResource.class);
  private final ConditionsOfJoiningService conditionsOfJoiningService;

  public ConditionsOfJoiningResource(ConditionsOfJoiningService conditionsOfJoiningService) {
    this.conditionsOfJoiningService = conditionsOfJoiningService;
  }

  /**
   * GET /conditions-of-joinings : get all the ConditionsOfJoinings.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of conditionsOfJoinings in body
   */
  @GetMapping("/conditions-of-joinings")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ConditionsOfJoiningDto>> getAllConditionsOfJoinings(
      Pageable pageable) {
    log.debug("REST request to get a page of ConditionsOfJoinings");
    Page<ConditionsOfJoiningDto> page = conditionsOfJoiningService.findAll(pageable);
    HttpHeaders headers =
        PaginationUtil.generatePaginationHttpHeaders(page, "/api/conditions-of-joinings");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /conditions-of-joining/:uuid : get the "uuid" ConditionsOfJoining.
   *
   * @param uuid the id of the conditionsOfJoiningDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the conditionsOfJoiningDTO, or
   *         with status 404 (Not Found). An invalid uuid will trigger a 400 (Bad Request) response.
   */
  @GetMapping("/conditions-of-joining/{uuid}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ConditionsOfJoiningDto> getConditionsOfJoining(@PathVariable String uuid) {
    log.debug("REST request to get Conditions of Joining : {}", uuid);
    ConditionsOfJoiningDto conditionsOfJoiningDto = null;
    try {
      UUID realUuid = UUID.fromString(uuid);
      conditionsOfJoiningDto = conditionsOfJoiningService.findOne(realUuid);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(conditionsOfJoiningDto));
  }

  /**
   * GET /conditions-of-joining/:uuid/text : get the "uuid" ConditionsOfJoining text.
   *
   * @param uuid the id of the conditionsOfJoiningDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the conditionsOfJoiningStatus.
   *         An invalid uuid will trigger a 400 (Bad Request) response.
   */
  @GetMapping("/conditions-of-joining/{uuid}/text")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<ConditionsOfJoiningStatusDto> getConditionsOfJoiningText(
      @PathVariable String uuid) {
    log.debug("REST request to get Conditions of Joining text: {}", uuid);
    ConditionsOfJoiningDto conditionsOfJoiningDto;
    try {
      UUID realUuid = UUID.fromString(uuid);
      conditionsOfJoiningDto = conditionsOfJoiningService.findOne(realUuid);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    String cojText = conditionsOfJoiningDto != null
        ? conditionsOfJoiningDto.toString()
        : new ConditionsOfJoiningDto().toString();

    ConditionsOfJoiningStatusDto dto = new ConditionsOfJoiningStatusDto();
    dto.setConditionsOfJoiningStatus(cojText);
    return ResponseUtil.wrapOrNotFound(Optional.of(dto));
  }

  /**
   * GET /trainee/:traineeId/conditions-of-joining : get all the ConditionsOfJoinings relating to a
   * trainee.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of conditionsOfJoining in body
   */
  @GetMapping("/trainee/{traineeId}/conditions-of-joinings")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<List<ConditionsOfJoiningDto>> getConditionsOfJoiningForTrainee(
      @PathVariable Long traineeId) {
    log.debug("REST request to get Conditions of Joinings for trainee {}", traineeId);
    List<ConditionsOfJoiningDto> conditionsOfJoiningDtos =
        conditionsOfJoiningService.findConditionsOfJoiningsForTrainee(traineeId);

    return new ResponseEntity<>(conditionsOfJoiningDtos, HttpStatus.OK);
  }
}
