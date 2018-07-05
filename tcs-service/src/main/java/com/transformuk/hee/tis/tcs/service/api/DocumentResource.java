package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.api.dto.TagDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import com.transformuk.hee.tis.tcs.service.service.TagService;
import io.swagger.annotations.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.io.IOException;
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
    private final TagService tagService;

    DocumentResource(final DocumentService documentService, final TagService tagService) {
        this.documentService = documentService;
        this.tagService = tagService;
    }

    @ApiOperation(value = "Retrieves a list documents", response = DocumentDTOPage.class, responseContainer = "DocumentDTOPage", produces = APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation performed successfully", response = DocumentDTOPage.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "DocumentDTO could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @GetMapping(value = PATH_DOCUMENTS + "/{entity}/{personId}", produces = APPLICATION_JSON)
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(@PathVariable(value = "entity") final String entity,
                                                             @PathVariable(value = "personId") final Long personId,
                                                             @RequestParam(value = "query", required = false) final String query,
                                                             @RequestParam(value = "columnFilters", required = false) final String columnFilterJson,
                                                             final Pageable pageable) throws IOException {
        LOG.info("Received 'getAllDocuments' request for entity '{}', person id '{}' and query '{}'",
                entity,
                personId,
                query);

        if (StringUtils.isBlank(entity) || !entity.equalsIgnoreCase("person")) {
            LOG.warn("Invalid or not implemented entity received '{}'",
                    entity);
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }

        if (personId == null) {
            LOG.warn("Invalid personId received '{}'",
                    personId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final List<Class> filterEnumList = Collections.singletonList(Status.class);
        final List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);

        return ResponseEntity.ok(documentService.findAll(personId, query, columnFilters, pageable));
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
        LOG.info("Received 'getDocumentById' request for document id '{}'",
                documentId);

        LOG.debug("Accessing service to load document with id '{}'",
                documentId);

        final Optional<DocumentDTO> documentOptional = documentService.findOne(documentId);

        if (documentOptional.isPresent()) {
            return ResponseEntity.ok(documentOptional.get());
        } else {
            LOG.warn("Document with id '{}' not found for 'getDocumentById'",
                    documentId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    @GetMapping(value = PATH_DOCUMENTS + PATH_DOWNLOADS + "/{documentId}")
    public void downloadDocumentById(final HttpServletResponse response,
                                     @ApiParam(value = "The document id", required = true)
                                     @PathVariable(value = "documentId") final Long documentId) throws IOException {
        LOG.info("Received 'DownloadDocument' request for document '{}'", documentId);

        if (documentId == null) {
            LOG.warn("Received null documentId for 'DownloadDocument'; rejecting request");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        LOG.debug("Accessing service to load document with id '{}'",
                documentId);

        final Optional<DocumentDTO> documentOptional = documentService.findOne(documentId);

        if (!documentOptional.isPresent()) {
            LOG.warn("Document with id '{}' not found for 'DownloadDocument'",
                    documentId);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        streamFile(documentOptional.get(), response);
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
    ) throws IOException {
        LOG.info("Received 'UploadDocument' request with person '{}' and document name '{}'",
                personId, documentParam.getOriginalFilename());

        final Optional<DocumentDTO> newDocument = createDocument(documentParam, personId);

        if (!newDocument.isPresent()) {
            LOG.warn("Document with person '{}' and document name '{}' failed validation; rejecting request",
                    personId, documentParam.getOriginalFilename());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Accessing service to save document with person '{}' and document name '{}'",
                personId, documentParam.getOriginalFilename());

        final DocumentDTO savedDocument = documentService.save(newDocument.get());

        LOG.debug("Document with person '{}' and document name '{}' saved successfully",
                personId, documentParam.getOriginalFilename());

        return ResponseEntity.status(HttpStatus.CREATED).body(new DocumentId(savedDocument.getId()));
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
            @RequestBody @Validated final Collection<DocumentDTO> documents) throws IOException {
        LOG.info("Received 'BulkUpdateDocuments' request with '{}' documents",
                documents.size());

        for (final DocumentDTO documentParam : documents) {
            LOG.debug("Accessing service to load document with id '{}'",
                    documentParam.getId());

            final Optional<DocumentDTO> existingDocumentOptional = documentService.findOne(documentParam.getId());

            if (!existingDocumentOptional.isPresent()) {
                LOG.warn("Document with id '{}' not found",
                        documentParam.getId());
                return ResponseEntity.notFound().build();
            }

            updateDocumentMetadata(existingDocumentOptional.get(), documentParam);
        }

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Deletes documents", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Documents deleted successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "DocumentDTO could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @DeleteMapping(PATH_DOCUMENTS + "/{entity}/{personId}")
    public ResponseEntity<Void> deleteAllDocuments(
            @PathVariable(value = "entity") final String entity,
            @PathVariable(value = "personId") final Long personId,
            @ApiParam(value = "The list of documents to delete", required = true)
            @RequestBody final Collection<DocumentId> documents) {

        documents.forEach(document -> deleteDocumentById(entity, personId, document.getId()));

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Deletes a document", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Document deleted successfully", response = String.class),
            @ApiResponse(code = 400, message = "Invalid parameters", response = String.class),
            @ApiResponse(code = 401, message = "User not authenticated", response = String.class),
            @ApiResponse(code = 403, message = "User not authorised to perform operation", response = String.class),
            @ApiResponse(code = 404, message = "DocumentDTO could not be found", response = String.class),
            @ApiResponse(code = 500, message = "Error occurred while performing operation", response = String.class)
    })
    @DeleteMapping(PATH_DOCUMENTS + "/{entity}/{personId}/{documentId}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable(value = "entity") final String entity,
                                                   @PathVariable(value = "personId") final Long personId,
                                                   @PathVariable(value = "documentId") final Long documentId) {

        if (StringUtils.isBlank(entity) || !entity.equalsIgnoreCase("person")) {
            LOG.warn("Invalid or not implemented entity received '{}'",
                    entity);
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }

        if (personId == null) {
            LOG.warn("Invalid personId received '{}'",
                    personId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (documentId == null) {
            LOG.warn("Invalid documentId received '{}'",
                    documentId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final Optional<DocumentDTO> deletedDocumentOptional = documentService.delete(personId, documentId);

        if (deletedDocumentOptional.isPresent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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
            @QueryParam("query") final String query) {
        LOG.info("Received 'SearchTags' request with query '{}'",
                query);

        if (StringUtils.isEmpty(query)) {
            LOG.warn("Received empty query to 'SearchTags'; rejecting request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOG.debug("Accessing service to find all '{}' with name starting with '{}'",
                TagDTO.class.getSimpleName(), query);

        final Collection<TagDTO> tags = tagService.findByNameStartingWithOrderByName(query);

        if (CollectionUtils.isEmpty(tags)) {
            LOG.debug("No '{}' found with name starting with '{}'",
                    TagDTO.class.getSimpleName(), query);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            LOG.debug("Found '{}' '{}' with name starting with '{}'",
                    tags.size(), TagDTO.class.getSimpleName(), query);

            return ResponseEntity.status(HttpStatus.OK).body(tags);
        }
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
            document.setName(documentParam.getOriginalFilename());
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

    private void updateDocumentMetadata(final DocumentDTO existingDocument, final DocumentDTO documentParam) throws IOException {
        LOG.debug("Merging tags changes on Document with id '{}'",
                documentParam.getId());

        // filters deleted tags
        final Set<TagDTO> deletedTags = existingDocument.getTags().stream()
                .filter(tag -> Optional.ofNullable(documentParam.getTags()).orElse(Collections.emptySet()).contains(new TagDTO(tag.getName())))
                .collect(Collectors.toSet());

        // combines added tags
        final Stream<TagDTO> combinedTags = Stream.concat(
                Optional.ofNullable(deletedTags).orElse(Collections.emptySet()).stream(),
                Optional.ofNullable(documentParam.getTags()).orElse(Collections.emptySet()).stream()
        );

        existingDocument.setName(documentParam.getName());
        existingDocument.setStatus(documentParam.getStatus());
        existingDocument.setVersion(documentParam.getVersion());
        existingDocument.setTags(combinedTags.collect(Collectors.toSet()));

        LOG.debug("Accessing service to update document metadata on document with id '{}'",
                documentParam.getId());

        documentService.save(existingDocument);

        LOG.debug("Document with id '{}' updated successfully",
                documentParam.getId());
    }

    private void streamFile(final DocumentDTO document, final HttpServletResponse response) throws IOException {
        LOG.trace("Setting response headers to download document with id '{}' ",
                document.getId());

        response.setStatus(HttpStatus.OK.value());
        response.addHeader("Content-disposition", "attachment;filename=" + document.getFileName());
        response.setContentType(document.getContentType());
        response.setContentLengthLong(document.getSize());

        response.flushBuffer();

        LOG.debug("Preparing to stream document with id '{}'",
                document.getId());

        documentService.download(document, response.getOutputStream());

        LOG.debug("Finished streaming document with id '{}'",
                document.getId());
    }

    @ApiModel("DocumentId")
    private static class DocumentId {
        private Long id;

        DocumentId() {

        }

        DocumentId(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(final Long id) {
            this.id = id;
        }
    }

    public interface DocumentDTOPage extends Page<DocumentDTO> {

    }
}
