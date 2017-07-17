package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for retrieving enum types from TCS.
 */
@RestController
@RequestMapping("/api")
public class EnumTypeResource {

	@ApiOperation(value = "Lists all Assessment types",
			notes = "Returns a list of all available Assessment Types",
			response = ResponseEntity.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Assessment Types", response = ResponseEntity.class)})
	@RequestMapping("/assessment-types")
	@PreAuthorize("hasAuthority('curriculum:view')")
	public ResponseEntity<AssessmentType[]> getAllAssessmentTypes() {
		return new ResponseEntity<>(AssessmentType.values(), HttpStatus.OK);
	}

	@ApiOperation(value = "Lists all Curriculum Sub Types",
			notes = "Returns a list of all available Curriculum Sub Types",
			response = ResponseEntity.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Curriculum Sub Types", response = ResponseEntity.class)})
	@RequestMapping("/curriculum-sub-types")
	@PreAuthorize("hasAuthority('curriculum:view')")
	public ResponseEntity<CurriculumSubType[]> getAllCurriculumSubTypes() {
		return new ResponseEntity<>(CurriculumSubType.values(), HttpStatus.OK);
	}


}
