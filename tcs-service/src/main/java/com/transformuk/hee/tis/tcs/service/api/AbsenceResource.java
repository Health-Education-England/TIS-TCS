package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.service.impl.AbsenceService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/absence")
public class AbsenceResource {

  public static final String PERMISSION_PERSON_VIEW = "hasPermission('tis:people::person:', 'View')";
  public static final String PERMISSION_PERSON_CREATE = "hasPermission('tis:people::person:', 'Create')";
  public static final String PERMISSION_PERSON_UPDATE = "hasPermission('tis:people::person:', 'Update')";
  @Autowired
  private AbsenceService absenceService;

  @GetMapping("/{id}")
  @PreAuthorize(PERMISSION_PERSON_VIEW)
  public ResponseEntity<AbsenceDTO> getById(@PathVariable Long id) {
    Optional<AbsenceDTO> optionalAbsence = absenceService.findById(id);
    return optionalAbsence.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Find absence using the ESR absence id
   *
   * @param absenceId
   * @return
   */
  @GetMapping("/absenceId/{absenceId}")
  @PreAuthorize(AbsenceResource.PERMISSION_PERSON_VIEW)
  public ResponseEntity<AbsenceDTO> getByAbsenceId(@PathVariable String absenceId) {
    Optional<AbsenceDTO> optionalAbsence = absenceService
        .findAbsenceByAbsenceAttendanceId(absenceId);
    return optionalAbsence.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  @PreAuthorize(PERMISSION_PERSON_CREATE)
  public ResponseEntity<AbsenceDTO> createAbsence(@RequestBody AbsenceDTO absenceDTO) {
    AbsenceDTO result = absenceService.createAbsence(absenceDTO);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/{id}")
  @PreAuthorize(PERMISSION_PERSON_UPDATE)
  public ResponseEntity<AbsenceDTO> updateCreateAbsence(@PathVariable Long id,
      @RequestBody AbsenceDTO absenceDTO) {
    return ResponseEntity.ok(absenceService.updateAbsence(absenceDTO));
  }

  @PatchMapping("/{id}")
  @PreAuthorize(PERMISSION_PERSON_UPDATE)
  public ResponseEntity<AbsenceDTO> patchAbsence(@PathVariable Long id,
      @RequestBody ModelMap absenceDTO)
      throws Exception {
    Optional<AbsenceDTO> optionalAbsence = absenceService.patchAbsence(absenceDTO);
    return optionalAbsence.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
