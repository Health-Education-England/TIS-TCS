package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for retrieving enum types from TCS.
 */
@RestController
@RequestMapping("/api")
public class EnumTypeResource {


	@RequestMapping("/assessment-types")
	public ResponseEntity<AssessmentType[]> getAllAssessmentTypes() {
		return new ResponseEntity<>(AssessmentType.values(), HttpStatus.OK);
	}

	@RequestMapping("/curriculum-sub-types")
	public ResponseEntity<CurriculumSubType[]> getAllCurriculumSubTypes() {
		return new ResponseEntity<>(CurriculumSubType.values(), HttpStatus.OK);
	}


}
