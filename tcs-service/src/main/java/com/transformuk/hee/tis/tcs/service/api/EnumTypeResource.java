package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for retrieving enum types from TCS.
 */
@RestController
@RequestMapping("/api")
public class EnumTypeResource {

  @Deprecated
  @RequestMapping(value = "/assessment-types", method = RequestMethod.GET)
  @PreAuthorize("hasAuthority('curriculum:view')")
  public ResponseEntity<AssessmentType[]> getAllAssessmentTypes() {
    return new ResponseEntity<>(AssessmentType.values(), HttpStatus.OK);
  }

  @RequestMapping(value = "/curriculum-sub-types", method = RequestMethod.GET)
  @PreAuthorize("hasAuthority('curriculum:view')")
  public ResponseEntity<CurriculumSubType[]> getAllCurriculumSubTypes() {
    return new ResponseEntity<>(CurriculumSubType.values(), HttpStatus.OK);
  }

  @RequestMapping(value = "/specialty-types", method = RequestMethod.GET)
  @PreAuthorize("hasAuthority('specialty:view')")
  public ResponseEntity<SpecialtyType[]> getAllSpecialtyTypes() {
    return new ResponseEntity<>(SpecialtyType.values(), HttpStatus.OK);
  }

}
