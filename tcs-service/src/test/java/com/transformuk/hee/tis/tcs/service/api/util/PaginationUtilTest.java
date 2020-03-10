package com.transformuk.hee.tis.tcs.service.api.util;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

@RunWith(MockitoJUnitRunner.class)
public class PaginationUtilTest {

  @Test
  public void generateBasicPaginationHttpHeadersShouldReturnLinkWithNoCommaAtTheEnd() {
    Pageable page = PageRequest.of(0, 100);
    Page<Integer> basicPage = new PageImpl<>(Lists.newArrayList(1, 2, 3, 4, 5), page, 5);

    HttpHeaders result = PaginationUtil.generateBasicPaginationHttpHeaders(basicPage, "tcs.com");

    List<String> link_header = result.get(HttpHeaders.LINK);
    Assert.assertEquals(1, link_header.size());
    Assert.assertEquals(-1, link_header.get(0).indexOf("rel=\"next\","));
  }
}
