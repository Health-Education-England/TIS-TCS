package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.service.impl.AbsenceService;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
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

  @Autowired
  private AbsenceService absenceService;

  @GetMapping("/{id}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<AbsenceDTO> getById(@PathVariable Long id) {
    Optional<AbsenceDTO> optionalAbsence = absenceService.findById(id);

    if (optionalAbsence.isPresent()) {
      AbsenceDTO absenceDTO = optionalAbsence.get();
      return ResponseEntity.ok(absenceDTO);
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * Find absence using the ESR absence id
   *
   * @param absenceId
   * @return
   */
  @GetMapping("/absenceId/{absenceId}")
  @PreAuthorize("hasPermission('tis:people::person:', 'View')")
  public ResponseEntity<AbsenceDTO> getByAbsenceId(@PathVariable String absenceId) {
    Optional<AbsenceDTO> optionalAbsence = absenceService.findAbsenceById(absenceId);

    if (optionalAbsence.isPresent()) {
      AbsenceDTO absenceDTO = optionalAbsence.get();
      return ResponseEntity.ok(absenceDTO);
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
  public ResponseEntity<AbsenceDTO> createAbsence(@RequestBody AbsenceDTO absenceDTO) {
    AbsenceDTO result = absenceService.createAbsence(absenceDTO);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<AbsenceDTO> updateCreateAbsence(@PathVariable Long id,
      @RequestBody AbsenceDTO absenceDTO) {
    return ResponseEntity.ok(absenceService.updateAbsence(absenceDTO));
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
  public ResponseEntity<AbsenceDTO> patchAbsence(@PathVariable Long id,
      @RequestBody ModelMap absenceDTO)
      throws Exception {
    Optional<AbsenceDTO> optionalAbsence = absenceService.patchAbsence(absenceDTO);
    return optionalAbsence.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
