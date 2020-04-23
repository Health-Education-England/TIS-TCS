package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDTO;

import java.util.List;
import java.util.Map;

public interface RevalidationService {

  Map<String, RevalidationRecordDTO> findAllRevalidationsByGmcIds(List<String> gmcIds);
}
