package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.microsoft.azure.storage.StorageException;
import com.transformuk.hee.tis.filestorage.repository.FileStorageRepository;
import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.service.config.AzureProperties;
import com.transformuk.hee.tis.tcs.service.model.Document;
import com.transformuk.hee.tis.tcs.service.repository.DocumentRepository;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import com.transformuk.hee.tis.tcs.service.service.mapper.DocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Optional;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final FileStorageRepository fileStorageRepository;
    private final DocumentMapper documentMapper;
    private final AzureProperties azureProperties;

    public DocumentServiceImpl(final DocumentRepository documentRepository, final FileStorageRepository fileStorageRepository, final DocumentMapper documentMapper, final AzureProperties azureProperties) {
        this.documentRepository = documentRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.documentMapper = documentMapper;
        this.azureProperties = azureProperties;
    }

    @Override
    public Optional<DocumentDTO> findOne(final Long id) {
        LOG.debug("Received request to load '{}' with ID '{}'", DocumentDTO.class.getSimpleName(), id);

        final Document document = documentRepository.findOne(id);

        if (document == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(documentMapper.toDto(document));
    }

    @Override
    public void download(final DocumentDTO document, final OutputStream outputStream) throws IOException {
        LOG.debug("Received request to download '{}' with ID '{}'", DocumentDTO.class.getSimpleName(), document.getId());

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
        LOG.debug("Received request to save '{}' with name '{}'", documentDTO.getClass().getSimpleName(), documentDTO.getFileName());

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

    private String saveFile(final Document document) throws InvalidKeyException, StorageException, URISyntaxException {
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
}
