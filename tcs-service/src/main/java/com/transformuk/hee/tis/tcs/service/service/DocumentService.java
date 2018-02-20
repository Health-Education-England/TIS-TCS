package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;

public interface DocumentService {
    DocumentDTO save(DocumentDTO documentDTO) throws Exception;
}
