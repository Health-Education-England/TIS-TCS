package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.api.dto.TagDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_API;
import static javax.ws.rs.core.MediaType.*;

@RestController
@RequestMapping(PATH_API)
public class DocumentResource {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentResource.class);
    static final String PATH_API = "/api";
    static final String PATH_DOCUMENTS = "/documents";
    static final String PATH_DOWNLOADS = "/downloads";
    static final String PATH_TAGS = "/tags";

    private final DocumentService documentService;

    DocumentResource(final DocumentService documentService) {
        this.documentService = documentService;
    }

    @ApiOperation(value = "Retrieves a list documents", response = DocumentDTO.class, responseContainer = "List", produces = APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = DocumentDTO.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "DocumentDTO could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @GetMapping(value = PATH_DOCUMENTS, produces = APPLICATION_JSON)
    public ResponseEntity<Collection<DocumentDTO>> getAllDocumentsForPerson(
            @ApiParam(value = "The Person the document belongs to", required = true)
            @RequestParam("personId") final Long personId,
            @ApiParam(value = "Query to filter documents by")
            @RequestParam(value = "query", required = false) final String query,
            @ApiParam(value = "Status to filter documents by")
            @RequestParam(value = "status", required = false) final String status,
            @ApiParam(value = "Tags to filter documents by")
            @RequestParam(value = "tags", required = false) final List<String> tags) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieves a specific document", response = DocumentDTO.class, produces = APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = DocumentDTO.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "DocumentDTO could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @GetMapping(value = PATH_DOCUMENTS + "/{documentId}", produces = APPLICATION_JSON)
    public ResponseEntity<DocumentDTO> getDocumentById(
            @ApiParam(value = "The document id", required = true)
            @PathVariable(value = "documentId") final Long documentId) {
        return ResponseEntity.ok(documentService.findOne(documentId));
    }

    @ApiOperation(value = "Downloads a specific document", response = String.class, produces = APPLICATION_OCTET_STREAM)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "DocumentDTO could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @GetMapping(value = PATH_DOCUMENTS + PATH_DOWNLOADS + "/{documentId}", produces = APPLICATION_JSON)
    public ResponseEntity<Void> downloadDocumentById(
            @ApiParam(value = "The document id", required = true)
            @PathVariable(value = "documentId") final Long documentId) {
        // TODO: investigate the right way to download a file
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Uploads documents and returns the created document id", response = DocumentId.class, consumes = MULTIPART_FORM_DATA, produces = APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Documents uploaded successfully", response = DocumentId.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
    @PostMapping(value = PATH_DOCUMENTS, consumes = MULTIPART_FORM_DATA, produces = APPLICATION_JSON)
    public ResponseEntity<DocumentId> uploadDocument(
            @ApiParam(value = "The Person the document belongs to", required = true)
            @RequestParam("personId") final Long personId,
            @ApiParam(value = "The document to upload", required = true)
            @RequestParam("document") final MultipartFile documentParam
    ) {
        LOG.info("Received 'UploadDocument' request with person '{}' and document name '{}'",
                personId, documentParam.getOriginalFilename());

        final Optional<DocumentDTO> document = createDocument(documentParam, personId);

        if (!document.isPresent()) {
            LOG.warn("Document with person '{}' and document name '{}' failed validation; rejecting request",
                    personId, documentParam.getOriginalFilename());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final DocumentDTO documentDTO;
        try {
            LOG.debug("Accessing service to save document with person '{}' and document name '{}'",
                    personId, documentParam.getOriginalFilename());
            documentDTO = documentService.save(document.get());
        } catch (final Exception ex) {
            LOG.error("Error while accessing service to save document with person '{}' and document name '{}'",
                    personId, documentParam.getOriginalFilename());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        LOG.debug("Document with person '{}' and document name '{}' saved successfully",
                personId, documentParam.getOriginalFilename());

        return ResponseEntity.status(HttpStatus.CREATED).body(new DocumentId(documentDTO.getId()));
    }


    @ApiOperation(value = "Bulk update of documents", response = String.class, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters or metadata", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "Document could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
    @PatchMapping(value = PATH_DOCUMENTS, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<Void> bulkUpdateDocuments(
            @ApiParam(value = "The list of documents to update", required = true)
            @RequestBody @Validated final Collection<DocumentDTO> documents) {

        for (final DocumentDTO documentParam : documents) {
            final DocumentDTO documentRepository = documentService.findOne(documentParam.getId());

            if (documentRepository == null) {
                return ResponseEntity.notFound().build();
            }

            final Set<TagDTO> combinedTags = documentRepository.getTags().stream()
                    .filter(tag -> Optional.ofNullable(documentParam.getTags()).orElse(Collections.emptySet()).contains(new TagDTO(tag.getName())))
                    .collect(Collectors.toSet());


            final Stream<TagDTO> combinedTags2 = Stream.concat(
                    Optional.ofNullable(combinedTags).orElse(Collections.emptySet()).stream(),
                    Optional.ofNullable(documentParam.getTags()).orElse(Collections.emptySet()).stream()
            );

            documentRepository.setName(documentParam.getName());
            documentRepository.setStatus(documentParam.getStatus());
            documentRepository.setVersion(documentParam.getVersion());
            documentRepository.setTags(combinedTags2.collect(Collectors.toSet()));

            documentService.save(documentRepository);
        }

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Deletes documents", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Documents deleted successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "DocumentDTO could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @DeleteMapping(PATH_DOCUMENTS)
    public ResponseEntity<Void> deleteAllDocuments(
            @ApiParam(value = "The list of documents to delete", required = true)
            @RequestBody final Collection<DocumentId> documents) {
        //receive in the body the document ids to delete
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Retrieves a list tags", response = TagDTO.class, responseContainer = "List", produces = APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = TagDTO.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "DocumentDTO could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @GetMapping(value = PATH_DOCUMENTS + PATH_TAGS, produces = APPLICATION_JSON)
    public ResponseEntity<Collection<TagDTO>> getAllTags(
            @ApiParam(value = "Query to filter tags by")
            @RequestParam("query") final String query) {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Optional<DocumentDTO> createDocument(final MultipartFile documentParam, final Long personId) {
        final DocumentDTO document = new DocumentDTO();

        if (!Optional.ofNullable(documentParam.getOriginalFilename()).isPresent() || !documentParam.getOriginalFilename().contains(".")) {
            return Optional.empty();
        }

        try {
            document.setUploadedBy(TisSecurityHelper.getProfileFromContext().getFirstName() + " " + TisSecurityHelper.getProfileFromContext().getLastName());
            document.setPersonId(personId);
            document.setFileName(documentParam.getOriginalFilename());
            document.setName(Optional.ofNullable(documentParam.getName()).orElse(documentParam.getOriginalFilename()));
            document.setFileExtension(documentParam.getOriginalFilename().substring(documentParam.getOriginalFilename().lastIndexOf('.') + 1));
            document.setContentType(documentParam.getContentType());
            document.setSize(documentParam.getSize());
            document.setBytes(documentParam.getBytes());
            document.setStatus(Status.CURRENT);
        } catch (final Exception ex) {
            LOG.error("Error creating {} object from '{}' object '{}'",
                    DocumentDTO.class.getSimpleName(), documentParam.getClass().getSimpleName(), documentParam.toString(), ex);
            return Optional.empty();
        }

        return Optional.of(document);
    }

    @ApiModel("DocumentId")
    private class DocumentId {
        private final Long id;

        DocumentId(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
