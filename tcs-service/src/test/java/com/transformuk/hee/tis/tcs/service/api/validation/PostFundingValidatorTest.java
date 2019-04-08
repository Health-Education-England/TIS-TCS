package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PostFundingValidatorTest {

  String FUNDING_TYPE_EMPTY = "funding type is empty";
  String FUNDING_TYPE_NOT_OTHER_ERROR = "found multiple funding type.";
  String NOT_FOUND_ERROR = "funding type does not exist.";
  String MULTIPLE_FOUND_ERROR = "found multiple funding type.";
  private Set<PostFundingDTO> pfDTOs;
  private Map<PostFundingDTO, List<String>> checkedMap;
  private PostFundingDTO idMissingDTO;
  private PostFundingDTO notOtherDTO;
  private PostFundingDTO validDTO;

  @Mock
  private ReferenceServiceImpl referenceService;

  @InjectMocks
  private PostFundingValidator postFundingValidator;

  private Map<PostFundingDTO, List<String>> buildCheckedMap(PostFundingDTO pfDTO) {
    pfDTOs = new HashSet<>();
    pfDTOs.add(pfDTO);

    // prepare a map
    Iterator iter = pfDTOs.iterator();
    checkedMap = new HashMap<>();
    while (iter.hasNext()) {
      checkedMap.put((PostFundingDTO)iter.next(), new ArrayList<>());
    }
    return checkedMap;
  }

  @Before
  public void setup() {
    validDTO = new PostFundingDTO();
    validDTO.setId(1L);
    validDTO.setFundingType("Other");
    validDTO.setInfo("info");
    validDTO.setFundingBodyId("foo");
    validDTO.setStartDate(LocalDate.now());
    validDTO.setStartDate(LocalDate.now().plusMonths(1));

    idMissingDTO = new PostFundingDTO();
    validDTO.setFundingType("Other");
    validDTO.setInfo("info");
    validDTO.setFundingBodyId("foo");
    validDTO.setStartDate(LocalDate.now());
    validDTO.setStartDate(LocalDate.now().plusMonths(1));


    notOtherDTO = new PostFundingDTO();
    validDTO.setFundingType("type1234");
    validDTO.setInfo("info");
    validDTO.setFundingBodyId("foo");
    validDTO.setStartDate(LocalDate.now());
    validDTO.setStartDate(LocalDate.now().plusMonths(1));
  }

  @Test
  public void testValidateFailsIfIdIsEmpty() {
    checkedMap = buildCheckedMap(idMissingDTO);

    Map<PostFundingDTO, List<String>> result = postFundingValidator.validateFundingType(checkedMap);
    assertThat(result.get(idMissingDTO).contains(FUNDING_TYPE_EMPTY), is(true));
  }

  @Test
  public void testValidateFailsInfoGivenForFundingTypeNoOther() {
    checkedMap = buildCheckedMap(notOtherDTO);
    Map<PostFundingDTO, List<String>> result = postFundingValidator.validateFundingType(checkedMap);
    assertThat(result.get(idMissingDTO).contains(FUNDING_TYPE_NOT_OTHER_ERROR), is(true));

  }

  @Test
  public void testValidateFailsIfFundingTypeIsNotUnique() {
    // Map<PostFundingDTO, List<String>> result = postFundingValidator.validateFundingType(checkedMap);
  }

}
