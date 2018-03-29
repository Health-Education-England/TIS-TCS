package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public interface DocumentService {
    Optional<DocumentDTO> findOne(final Long id);

    DocumentDTO save(final DocumentDTO documentDTO) throws IOException;

    void download(final DocumentDTO document, final OutputStream outputStream) throws IOException;
}
