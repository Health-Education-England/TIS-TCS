package com.transformuk.hee.tis.tcs.service.api.util;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

@RunWith(MockitoJUnitRunner.class)
public class PaginationUtilTest {

  public static final boolean HAS_NEXT_TRUE = true;

  @Test
  public void generateBasicPaginationHttpHeadersShouldReturnLinkWithNoCommaAtTheEnd() {
    Pageable page = new PageRequest(0, 100);
    BasicPage<Integer> basicPage = new BasicPage<>(Lists.newArrayList(1, 2, 3, 4, 5), page,
        HAS_NEXT_TRUE);

    HttpHeaders result = PaginationUtil.generateBasicPaginationHttpHeaders(basicPage, "tcs.com");

    List<String> link_header = result.get(HttpHeaders.LINK);
    Assert.assertEquals(1, link_header.size());
    Assert.assertEquals(-1, link_header.get(0).indexOf("rel=\"next\","));
  }

}
