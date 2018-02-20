package com.transformuk.hee.tis.tcs.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class ProgrammeResourceTest {

  public static final String DBC = "1-AIIDR8";
  public static final String USER_ID = "James H";

  @Mock
  private ProgrammeService programmeService;

  @InjectMocks
  private ProgrammeResource controller;


  @Before
  public void setup() {
    TestUtils.mockUserprofile(USER_ID, DBC);
  }

  @Test
  public void shouldExcludeNonAlphanumericFromSearchInput() throws Exception {
    // given
    Pageable p = new PageRequest(1, 20);
    PageImpl<ProgrammeDTO> page = new PageImpl<>(Lists.newArrayList(new ProgrammeDTO()));

    ArgumentCaptor<String> searchStringCaptor = ArgumentCaptor.forClass(String.class);
    given(programmeService.advancedSearch(searchStringCaptor.capture(), anyList(), eq(p), eq(false))).willReturn(page);

    // when
    controller.getAllProgrammes(p, "#$$)alp%*h(&^)a ..?'';;\\numer1`~c", null);

    // then
    assertThat(searchStringCaptor.getValue()).isEqualTo(")alph()a numer1c");
  }

  @Test
  public void shouldExcludeNonAlphanumericFromColumnFilters() throws Exception {
    // given
    Pageable p = new PageRequest(1, 20);
    PageImpl<ProgrammeDTO> page = new PageImpl<>(Lists.newArrayList(new ProgrammeDTO()));
    String colFilter = "{\"owner\":[\"Health Ed:uc#%ation §±England@$% West Mid^la*nds\"]}";

    ArgumentCaptor<List> searchStringCaptor = ArgumentCaptor.forClass(List.class);
    given(programmeService.advancedSearch(anyString(), searchStringCaptor.capture(), eq(p), eq(false))).willReturn(page);

    // when
    controller.getAllProgrammes(p, null, colFilter);

    // then
    ColumnFilter f = ((ColumnFilter) searchStringCaptor.getValue().get(0));
    assertThat(f.getName()).isEqualTo("owner");
    assertThat(f.getValues().size()).isEqualTo(1);
    assertThat(f.getValues().get(0)).isEqualTo("Health Education England West Midlands");
  }

  @Test
  public void shouldIgnoreBadStatusColumnFilters() throws Exception {
    // given
    Pageable p = new PageRequest(1, 20);
    PageImpl<ProgrammeDTO> page = new PageImpl<>(Lists.newArrayList(new ProgrammeDTO()));
    String colFilter = "{\"status\":[\"badstatus\",\"CURRENT\"]}";

    ArgumentCaptor<List> searchStringCaptor = ArgumentCaptor.forClass(List.class);
    given(programmeService.advancedSearch(anyString(), searchStringCaptor.capture(), eq(p), eq(false))).willReturn(page);

    // when
    controller.getAllProgrammes(p, null, colFilter);

    // then
    ColumnFilter f = ((ColumnFilter) searchStringCaptor.getValue().get(0));
    assertThat(f.getName()).isEqualTo("status");
    assertThat(f.getValues().size()).isEqualTo(1);
    assertThat(f.getValues().get(0)).isEqualTo(Status.CURRENT);
  }

  @Test
  public void shouldIgnoreBadEmptyColumnFilters() throws Exception {
    // given
    Pageable p = new PageRequest(1, 20);
    PageImpl<ProgrammeDTO> page = new PageImpl<>(Lists.newArrayList(new ProgrammeDTO()));
    String colFilter = "{\"status\":[\"badstatus\"]}";

    ArgumentCaptor<List> searchStringCaptor = ArgumentCaptor.forClass(List.class);
    given(programmeService.advancedSearch(anyString(), searchStringCaptor.capture(), eq(p), eq(false))).willReturn(page);

    // when
    controller.getAllProgrammes(p, null, colFilter);

    // then
    assertThat(searchStringCaptor.getValue()).isEmpty();
  }
}
