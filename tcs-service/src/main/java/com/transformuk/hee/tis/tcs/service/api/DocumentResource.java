package com.transformuk.hee.tis.tcs.service.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_API;

@RestController
@RequestMapping(PATH_API)
public class DocumentResource {
    static final String PATH_API = "/api";
    static final String PATH_DOCUMENTS = "/documents";
    static final String PATH_DOWNLOADS = "/downloads";
    static final String PATH_TAGS = "/tags";

    // TODO: check if personId is needed everywhere

    @ApiOperation(value = "Retrieves a list documents", response = String.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "Document could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @Produces(MediaType.APPLICATION_JSON)
    @GetMapping(value = PATH_DOCUMENTS)
    public ResponseEntity<Void> getAllDocumentsForPerson(
            @ApiParam(value = "The Person the document belongs to", required = true)
            @RequestParam("personId")
                    Long personId,
            @ApiParam(value = "Query to filter documents by")
            @RequestParam(value = "query", required = false)
                    String query,
            @ApiParam(value = "Status to filter documents by")
            @RequestParam(value = "status", required = false)
                    String status,
            @ApiParam(value = "Tags to filter documents by")
            @RequestParam(value = "tags", required = false)
                    List<String> tags) {
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Retrieves a specific document", response = String.class, produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "Document could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @Produces(MediaType.APPLICATION_JSON)
    @GetMapping(PATH_DOCUMENTS + "/{documentId}")
    public ResponseEntity<Void> getDocumentById(@PathVariable(value = "documentId") Long documentId, @RequestParam("personId") Long personId /* person might not be necessary nor make sense*/) {
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Downloads a specific document", response = String.class, produces = MediaType.APPLICATION_OCTET_STREAM)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "Document could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GetMapping(PATH_DOCUMENTS + PATH_DOWNLOADS + "/{documentId}")
    public ResponseEntity<Void> downloadDocumentById(@PathVariable(value = "documentId") Long documentId, @RequestParam("personId") Long personId /* person might not be necessary nor make sense*/) {
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Uploads documents", response = String.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Documents uploaded successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "Document could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PostMapping(PATH_DOCUMENTS)
    public ResponseEntity<Void> uploadDocument(@RequestParam("personId") Long personId, MultipartHttpServletRequest documents) {
        // just one file at the time and returns the documentId for the uploaded document
        // maybe MultipartHttpServletRequest to something else
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Bulk update of documents", response = String.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters or metadata", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "Document could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PutMapping(PATH_DOCUMENTS)
    public ResponseEntity<Void> updateDocuments(@RequestParam("personId") Long personId) {
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Deletes documents", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Documents deleted successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "Document could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @DeleteMapping(PATH_DOCUMENTS)
    public ResponseEntity<Void> deleteAllDocuments(@RequestParam("personId") Long personId) {
        //receive in the body the document ids to delete
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Retrieves a list tags", response = String.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "Document could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @Produces(MediaType.APPLICATION_JSON)
    @GetMapping(value = PATH_DOCUMENTS + PATH_TAGS)
    public ResponseEntity<Void> getAllTags(
            @ApiParam(value = "Query to filter tags by")
            @RequestParam("query")
                    String query) {
        return ResponseEntity.ok().build();
    }
}
