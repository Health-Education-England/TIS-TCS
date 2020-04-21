package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;

import java.util.List;

public interface RevalidationService {

  List<RevalidationRecordDTO> findAllRevalidationsByGmcIds(List<String> gmcIds);
}
