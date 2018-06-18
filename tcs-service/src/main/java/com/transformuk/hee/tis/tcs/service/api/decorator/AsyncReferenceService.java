package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class AsyncReferenceService {
  private static final Logger log = LoggerFactory.getLogger(AsyncReferenceService.class);

  private ReferenceService referenceService;

  @Autowired
  public AsyncReferenceService(ReferenceService referenceService) {
    this.referenceService = referenceService;
  }

  @Async
  public CompletableFuture<Void> doWithGradesAsync(Set<Long> ids, Consumer<Map<Long, GradeDTO>> consumer) {
    return doWithGradesAsync(() -> true, ids, consumer);
  }

  @Async
  public CompletableFuture<Void> doWithGradesAsync(Supplier<Boolean> precondition, Set<Long> ids, Consumer<Map<Long, GradeDTO>> consumer) {
    log.debug("Start grades {}", Thread.currentThread().getName());
    if (CollectionUtils.isNotEmpty(ids) && precondition.get()) {
      try {
        List<GradeDTO> grades = referenceService.findGradesIdIn(ids);

        if (CollectionUtils.isNotEmpty(grades)) {
          Map<Long, GradeDTO> gradeMap = grades.stream().collect(Collectors.toMap(GradeDTO::getId, g -> g));
          consumer.accept(gradeMap);
        }
      } catch (Exception e) {
        log.warn("Reference call to grades failed", e);
      }
    }
    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> doWithSitesAsync(Set<Long> ids, Consumer<Map<Long, SiteDTO>> consumer) {
    return doWithSitesAsync(() -> true, ids, consumer);
  }

  @Async
  public CompletableFuture<Void> doWithSitesAsync(Supplier<Boolean> precondition, Set<Long> ids, Consumer<Map<Long, SiteDTO>> consumer) {
    if (CollectionUtils.isNotEmpty(ids) && precondition.get()) {
      try {
        List<SiteDTO> sites = referenceService.findSitesIdIn(ids);
        if (CollectionUtils.isNotEmpty(sites)) {
          Map<Long, SiteDTO> siteMap = sites.stream().collect(Collectors.toMap(SiteDTO::getId, s -> s));
          consumer.accept(siteMap);
        }
      } catch (Exception e) {
        log.warn("Reference decorator call to sites failed", e);
      }
    }
    return CompletableFuture.completedFuture(null);
  }

  @Async
  public CompletableFuture<Void> doWithLocalOfficeAsync(Consumer<List<LocalOfficeDTO>> consumer) {
    try {
      List<LocalOfficeDTO> localOfficeDTOS = referenceService.findAllLocalOffice();
      consumer.accept(localOfficeDTOS);
    } catch (Exception e) {
      log.warn("Reference decorator call to local office failed", e);
    }

    return CompletableFuture.completedFuture(null);
  }
}
