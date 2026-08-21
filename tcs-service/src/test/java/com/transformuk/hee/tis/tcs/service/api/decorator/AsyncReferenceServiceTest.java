/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.api.decorator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AsyncReferenceServiceTest {

  private static final UUID ID_1 = UUID.randomUUID();
  private static final UUID ID_2 = UUID.randomUUID();
  private static final String LABEL_1 = "Subtype1";
  private static final String LABEL_2 = "Subtype2";

  @Mock
  private ReferenceService referenceService;
  @Mock
  private Consumer<Map<UUID, String>> fundingSubtypeConsumer;

  @InjectMocks
  private AsyncReferenceService asyncReferenceService;

  @Test
  void shouldFetchFundingSubtypesAndPassIdToLabelMapToConsumer() {
    Set<UUID> ids = Set.of(ID_1, ID_2);

    FundingSubTypeDto fundingSubtypeOne = new FundingSubTypeDto();
    fundingSubtypeOne.setId(ID_1);
    fundingSubtypeOne.setLabel(LABEL_1);

    FundingSubTypeDto fundingSubtypeTwo = new FundingSubTypeDto();
    fundingSubtypeTwo.setId(ID_2);
    fundingSubtypeTwo.setLabel(LABEL_2);

    when(referenceService.findFundingSubtypesIdIn(ids))
        .thenReturn(List.of(fundingSubtypeOne, fundingSubtypeTwo));

    CompletableFuture<Void> result = asyncReferenceService.doWithFundingSubtypeAsync(ids,
        fundingSubtypeConsumer);

    assertTrue(result.isDone());
    verify(referenceService).findFundingSubtypesIdIn(ids);
    verify(fundingSubtypeConsumer).accept(Map.of(
        ID_1, LABEL_1,
        ID_2, LABEL_2
    ));
  }

  @Test
  void shouldNotCallReferenceServiceWhenIdsAreEmpty() {
    CompletableFuture<Void> result = asyncReferenceService.doWithFundingSubtypeAsync(Set.of(),
        fundingSubtypeConsumer);

    assertTrue(result.isDone());
    verifyNoInteractions(referenceService);
    verifyNoInteractions(fundingSubtypeConsumer);
  }

  @Test
  void shouldNotCallConsumerWhenNoFundingSubtypesAreFound() {
    Set<UUID> ids = Set.of(ID_1);

    when(referenceService.findFundingSubtypesIdIn(ids)).thenReturn(List.of());

    CompletableFuture<Void> result = asyncReferenceService.doWithFundingSubtypeAsync(ids,
        fundingSubtypeConsumer);

    assertTrue(result.isDone());
    verify(referenceService).findFundingSubtypesIdIn(ids);
    verify(fundingSubtypeConsumer, never()).accept(anyMap());
  }

  @Test
  void shouldSwallowReferenceServiceExceptions() {
    Set<UUID> ids = Set.of(ID_1);

    when(referenceService.findFundingSubtypesIdIn(ids))
        .thenThrow(new RuntimeException("Reference failure"));

    assertDoesNotThrow(() -> asyncReferenceService.doWithFundingSubtypeAsync(ids,
        fundingSubtypeConsumer));

    verify(referenceService).findFundingSubtypesIdIn(ids);
    verifyNoInteractions(fundingSubtypeConsumer);
  }
}
