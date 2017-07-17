package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.enumeration.AssessmentType;
import com.transformuk.hee.tis.tcs.api.enumeration.CurriculumSubType;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

	@ApiOperation(value = "Lists all Assessment types that can be associated with Curricula",
			notes = "Used by clients to retrieve all Assessment types that are currently available by this service. \n" +
					"This allows clients to dynamically list out all options for particular fields so that we do not \n" +
					"need to maintain a list on both the backend and client",
			response = ResponseEntity.class, responseContainer = "List",
			httpMethod = "GET", produces = "application/json", protocols = "http, https")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Assessment Types", response = ResponseEntity.class)})
	@RequestMapping(value = "/assessment-types", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('curriculum:view')")
	public ResponseEntity<AssessmentType[]> getAllAssessmentTypes() {
		return new ResponseEntity<>(AssessmentType.values(), HttpStatus.OK);
	}

	@ApiOperation(value = "Lists all Curriculum Sub Types that can be associated with Curricula",
			notes = "Used by clients to retrieve all Curriculum Sub Types that are currently available by this service. \n" +
					"This allows clients to dynamically list out all options for particular fields so that we do not \n" +
					"need to maintain a list on both the backend and client",
			response = ResponseEntity.class, responseContainer = "List",
			httpMethod = "GET", produces = "application/json", protocols = "http, https")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Curriculum Sub Types", response = ResponseEntity.class)})
	@RequestMapping(value = "/curriculum-sub-types", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('curriculum:view')")
	public ResponseEntity<CurriculumSubType[]> getAllCurriculumSubTypes() {
		return new ResponseEntity<>(CurriculumSubType.values(), HttpStatus.OK);
	}

	@ApiOperation(value = "Lists all Specialty Types that can be associated with Specialty",
			notes = "Used by clients to retrieve all Specialty Types that are currently available by this service. \n" +
					"This allows clients to dynamically list out all options for particular fields so that we do not \n" +
					"need to maintain a list on both the backend and client",
			response = ResponseEntity.class, responseContainer = "List",
			httpMethod = "GET", produces = "application/json", protocols = "http, https")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Specialty Types", response = ResponseEntity.class)})
	@RequestMapping(value = "/specialty-types", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('specialty:view')")
	public ResponseEntity<SpecialtyType[]> getAllSpecialtyTypes() {
		return new ResponseEntity<>(SpecialtyType.values(), HttpStatus.OK);
	}

}
