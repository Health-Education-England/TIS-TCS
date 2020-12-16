package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionDetailDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionHiddenDto;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionRecordDto;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import java.util.List;
import java.util.Map;

public interface RevalidationService {

  RevalidationRecordDto findRevalidationByGmcId(String gmcId);

  Map<String, RevalidationRecordDto> findAllRevalidationsByGmcIds(List<String> gmcIds);

  Map<String, ConnectionRecordDto> findAllConnectionsByGmcIds(List<String> gmcIds);

  ConnectionDetailDto findAllConnectionsHistoryByGmcId(String gmcId);

  public ConnectionHiddenDto getHiddenTrainees(final int pageNumber);
}
