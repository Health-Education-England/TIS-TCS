package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.api.dto.TagDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import com.transformuk.hee.tis.tcs.service.service.TagService;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_API;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

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

    @GetMapping(value = PATH_DOCUMENTS + "/{entity}/{personId}", produces = APPLICATION_JSON)
    public ResponseEntity<Page<DocumentDTO>> getAllDocuments(@PathVariable(value = "entity") final String entity,
                                                             @PathVariable(value = "personId") final Long personId,
                                                             @RequestParam(value = "query", required = false) final String query,
                                                             @RequestParam(value = "columnFilters", required = false) final String columnFilterJson,
                                                             @RequestParam(value = "tags", required = false) final List<String> tagNames,
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

        return ResponseEntity.ok(documentService.findAll(personId, query, columnFilters, tagNames, pageable));
    }

    @GetMapping(value = PATH_DOCUMENTS + "/{documentId}", produces = APPLICATION_JSON)
    public ResponseEntity<DocumentDTO> getDocumentById(
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

    @GetMapping(value = PATH_DOCUMENTS + PATH_DOWNLOADS + "/{documentId}")
    public void downloadDocumentById(final HttpServletResponse response,
                                     @PathVariable(value = "documentId") final Long documentId,
                                     @QueryParam("view") final boolean view) throws IOException {
      UserProfile profileFromContext = null;
      try {
        profileFromContext = TisSecurityHelper.getProfileFromContext();
      } catch (RuntimeException e) {
        //oh wells - we tried
      }
      String username = profileFromContext != null ? profileFromContext.getUserName() : "No profile info available";
      LOG.info("Received 'DownloadDocument' request for document [{}], for user [{}]", documentId, username);

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

        try {
            streamFile(documentOptional.get(), response, view);
        } catch (final IOException ex) {
            LOG.error("Failed to stream file with name '{}' on document with id '{}'",
                    documentOptional.get().getFileName(),
                    documentOptional.get().getId());
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    @PreAuthorize("hasPermission('tis:people::person:', 'Create')")
    @PostMapping(value = PATH_DOCUMENTS, consumes = MULTIPART_FORM_DATA, produces = APPLICATION_JSON)
    public ResponseEntity<DocumentId> uploadDocument(
            @RequestParam("personId") final Long personId,
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

    @PreAuthorize("hasPermission('tis:people::person:', 'Update')")
    @PatchMapping(value = PATH_DOCUMENTS, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<Void> bulkUpdateDocuments(
            @RequestBody @Validated final Collection<DocumentDTO> documents) throws IOException {
        LOG.info("Received 'BulkUpdateDocuments' request with '{}' documents",
                documents.size());

        LOG.debug("Accessing service to load '{}' documents",
                documents.size());

        documentService.save(documents);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(PATH_DOCUMENTS + "/{entity}/{personId}")
    public ResponseEntity<Void> deleteAllDocuments(
            @PathVariable(value = "entity") final String entity,
            @PathVariable(value = "personId") final Long personId,
            @RequestBody final Collection<DocumentId> documents) {

        documents.forEach(document -> deleteDocumentById(entity, personId, document.getId()));

        return ResponseEntity.noContent().build();
    }

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

    @GetMapping(value = PATH_DOCUMENTS + PATH_TAGS, produces = APPLICATION_JSON)
    public ResponseEntity<Collection<TagDTO>> getAllTags(
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
            document.setTitle(documentParam.getOriginalFilename());
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

    private void streamFile(final DocumentDTO document, final HttpServletResponse response, final boolean view) throws IOException {
        LOG.trace("Setting response headers to download document with id '{}' ",
                document.getId());

        response.setStatus(HttpStatus.OK.value());
        response.addHeader("Content-disposition", (view ? "inline" : "attachment") + ";filename=\"" + document.getFileName() + "\"");
        response.setContentType(document.getContentType());
        response.setContentLengthLong(document.getSize());

        response.flushBuffer();

        LOG.debug("Preparing to stream document with id '{}'",
                document.getId());

        documentService.download(document, response.getOutputStream());

        LOG.debug("Finished streaming document with id '{}'",
                document.getId());
    }

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
