package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DocumentService {
    Optional<DocumentDTO> findOne(final Long id);

    Page<DocumentDTO> findAll(final Long personId, String searchQuery, List<ColumnFilter> columnFilters, final List<String> tagNames, final Pageable pageable);

    DocumentDTO save(final DocumentDTO documentDTO) throws IOException;

    void save(final Collection<DocumentDTO> documentDTOs) throws IOException;

    void download(final DocumentDTO document, final OutputStream outputStream) throws IOException;

    String getDownloadUrl(final DocumentDTO document) throws IOException;

    Optional<DocumentDTO> delete(final Long personId, final Long documentId);
}
