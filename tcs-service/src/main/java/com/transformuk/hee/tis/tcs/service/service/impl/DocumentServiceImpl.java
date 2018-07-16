package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.microsoft.azure.storage.StorageException;
import com.transformuk.hee.tis.filestorage.repository.FileStorageRepository;
import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.config.AzureProperties;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Document;
import com.transformuk.hee.tis.tcs.service.repository.DocumentRepository;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import com.transformuk.hee.tis.tcs.service.service.mapper.DocumentMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.TagMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final FileStorageRepository fileStorageRepository;
    private final DocumentMapper documentMapper;
    private final TagMapper tagMapper;
    private final AzureProperties azureProperties;

    public DocumentServiceImpl(final DocumentRepository documentRepository, final FileStorageRepository fileStorageRepository, final DocumentMapper documentMapper, final TagMapper tagMapper, final AzureProperties azureProperties) {
        this.documentRepository = documentRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.documentMapper = documentMapper;
        this.tagMapper = tagMapper;
        this.azureProperties = azureProperties;
    }

    @Override
    public Optional<DocumentDTO> findOne(final Long id) {
        LOG.debug("Received request to load '{}' with ID '{}'",
                DocumentDTO.class.getSimpleName(),
                id);

        return documentRepository
                .findOneByIdAndStatus(id, Status.CURRENT)
                .map(documentMapper::toDto);
    }

    @Override
    public Page<DocumentDTO> findAll(final Long personId, final String query, final List<ColumnFilter> columnFilters, final Pageable pageable) {
        LOG.debug("Received request to load all '{}' with person id '{}' and query '{}'",
                DocumentDTO.class.getSimpleName(),
                personId,
                query);

        final Specification<Document> personSpec = (root, criteriaQuery, sb) -> sb.equal(root.get("personId"), personId);
        Specifications<Document> fullSpec = Specifications.where(personSpec);

        fullSpec = addStatusFilterToSpec(fullSpec, Status.CURRENT);

        fullSpec = addColumnFiltersToSpec(fullSpec, columnFilters);

        fullSpec = addSearchQueryToSpec(fullSpec, query);

        return mapDocuments(documentRepository.findAll(fullSpec, pageable));
    }

    @Override
    public void download(final DocumentDTO document, final OutputStream outputStream) throws IOException {
        LOG.debug("Received request to download '{}' with ID '{}'",
                DocumentDTO.class.getSimpleName(),
                document.getId());

        try {
            fileStorageRepository.download(azureProperties.getContainerName(),
                    azureProperties.getPersonFolder() + "/" + document.getId() + "/" + document.getFileName(),
                    outputStream);
        } catch (final URISyntaxException | InvalidKeyException | StorageException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public DocumentDTO save(final DocumentDTO documentDTO) throws IOException {
        LOG.debug("Received request to save '{}' with name '{}'",
                documentDTO.getClass().getSimpleName(),
                documentDTO.getFileName());

        Document document = documentMapper.toEntity(documentDTO);

        if (document.getId() == null) {
            LOG.debug("Document is new; creating");

            try {
                document = create(document);
            } catch (final IOException ex) {
                // rollback
                try {
                    fileStorageRepository.deleteFile(document.getId(), azureProperties.getContainerName() + "/" + azureProperties.getPersonFolder(), document.getFileName());
                } catch (final URISyntaxException | InvalidKeyException | StorageException exx) {
                    LOG.warn("Error while rolling back; could not delete file from remote storage", exx);
                }

                throw ex;
            }
        } else {
            LOG.debug("Document already exists; updating");

            document = update(document);
        }

        return documentMapper.toDto(document);
    }

    @Override
    public Optional<DocumentDTO> delete(final Long personId, final Long documentId) {
        final Optional<Document> documentOptional = documentRepository.findOneBypersonIdAndIdAndStatus(personId, documentId, Status.CURRENT);

        if (!documentOptional.isPresent()) {
            LOG.warn("Document with id '{}' and person id '{}' does not exist", documentId, personId);

            return Optional.empty();
        }

        final Document document = documentOptional.get();
        document.setStatus(Status.INACTIVE);
        document.setInactiveDate(LocalDateTime.now());

        documentRepository.save(document);

        return Optional.of(documentMapper.toDto(document));
    }

    private Document create(final Document document) throws IOException {
        if (document.getBytes() == null || document.getBytes().length == 0) {
            LOG.warn("File is empty; not creating metadata nor saving file to storage");
            throw new IOException("File is empty");
        }

        saveMetadata(document);

        try {
            saveFile(document);
        } catch (final InvalidKeyException | StorageException | URISyntaxException ex) {
            throw new IOException("Failed to save document to storage", ex);
        }

        return saveMetadata(document);
    }

    private Document update(final Document document) {
        return saveMetadata(document);
    }

    private String saveFile(final Document document) throws
            InvalidKeyException, StorageException, URISyntaxException {
        return fileStorageRepository.store(document.getId(), azureProperties.getContainerName() + "/" + azureProperties.getPersonFolder(), Lists.newArrayList(getFileAsMultipart(document)));
    }

    private Document saveMetadata(final Document document) {
        return documentRepository.saveAndFlush(document);
    }

    private MultipartFile getFileAsMultipart(final Document document) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return document.getName();
            }

            @Override
            public String getOriginalFilename() {
                return document.getFileName();
            }

            @Override
            public String getContentType() {
                return document.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return document.getBytes() == null || document.getBytes().length == 0;
            }

            @Override
            public long getSize() {
                return document.getBytes().length;
            }

            @Override
            public byte[] getBytes() {
                return document.getBytes();
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(document.getBytes());
            }

            @Override
            public void transferTo(final File dest) {
                // intentionally left empty
            }
        };
    }

    private Page<DocumentDTO> mapDocuments(final Page<Document> page) {
        return page.map(documentMapper::toDto);
    }

    private Specifications<Document> addStatusFilterToSpec(Specifications<Document> fullSpec, final Status status) {
        final Specification<Document> statusFilter = (root, criteriaQuery, sb) -> sb.equal(root.get("status"), status);

        if (fullSpec == null) {
            fullSpec = Specifications.where(statusFilter);
        } else {
            fullSpec = fullSpec.and(statusFilter);
        }

        return fullSpec;
    }

    private Specifications<Document> addColumnFiltersToSpec(Specifications<Document> fullSpec, final List<ColumnFilter> columnFilters) {
        if (columnFilters == null || columnFilters.isEmpty()) {
            return fullSpec;
        }

        final List<Specification<Document>> columnFilterSpecs = new ArrayList<>();
        columnFilters.forEach(cf -> columnFilterSpecs.add(in(cf.getName(), cf.getValues())));

        int i = 0;
        if (fullSpec == null) {
            fullSpec = Specifications.where(columnFilterSpecs.get(0));
            i++;
        }

        for (; i < columnFilterSpecs.size(); i++) {
            fullSpec = fullSpec.and(columnFilterSpecs.get(i));
        }

        return fullSpec;
    }

    private Specifications<Document> addSearchQueryToSpec(Specifications<Document> fullSpec, final String query) {
        final List<Specification<Document>> querySpecs = new ArrayList<>();

        if (StringUtils.isEmpty(query)) {
            return fullSpec;
        }

        querySpecs.add((root, criteriaQuery, sb) -> sb.like(root.get("name"), "%" + query + "%"));
        querySpecs.add((root, criteriaQuery, sb) -> sb.like(root.get("fileName"), "%" + query + "%"));
        querySpecs.add((root, criteriaQuery, sb) -> sb.like(root.get("fileExtension"), "%" + query + "%"));
        querySpecs.add((root, criteriaQuery, sb) -> sb.like(root.get("contentType"), "%" + query + "%"));

        int i = 0;
        Specifications<Document> orSpec = Specifications.where(querySpecs.get(0));
        i++;

        for (; i < querySpecs.size(); i++) {
            orSpec = orSpec.or(querySpecs.get(i));
        }

        fullSpec = fullSpec.and(orSpec);


        return fullSpec;
    }
}
