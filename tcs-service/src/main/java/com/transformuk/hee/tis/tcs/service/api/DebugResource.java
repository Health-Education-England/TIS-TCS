package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugResource {

  private final ProgrammeMembershipService service;

  private DebugResource(ProgrammeMembershipService service) {
    this.service = service;
  }

  @PostMapping("/{id}")
  public ResponseEntity<ProgrammeMembershipDTO> debug(@PathVariable Long id) {
    ProgrammeMembershipDTO programmeMembership = service.findOne(id);

    if (programmeMembership != null) {
      programmeMembership = service.save(programmeMembership);
    }

    return ResponseEntity.of(Optional.ofNullable(programmeMembership));
  }
}
