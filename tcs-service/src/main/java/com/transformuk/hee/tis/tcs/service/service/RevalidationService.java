package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import java.util.List;
import java.util.Map;

public interface RevalidationService {

  RevalidationRecordDto findRevalidationByGmcId(String gmcId);

  Map<String, RevalidationRecordDto> findAllRevalidationsByGmcIds(List<String> gmcIds);
}
