package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import com.transformuk.hee.tis.tcs.service.service.TagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_API;
import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_DOCUMENTS;
import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_DOWNLOADS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class DocumentResourceTest {

  private DocumentResource testObj;
  private MockMvc mockMvc;

  @MockBean
  private DocumentService documentServiceMock;
  @MockBean
  private TagService tagServiceMock;
  @MockBean
  private DocumentDTO documentDTOMock;

  @Before
  public void setup() {
    testObj = new DocumentResource(documentServiceMock, tagServiceMock);
    mockMvc = MockMvcBuilders.standaloneSetup(testObj).build();
  }

  @Test
  public void downloadDocumentByIdV2ShouldRedirectWithLocationWhenDocumentExists() throws Exception {
    Long documentId = 1L;
    String expectDownloadUrl = "blah";

    when(documentServiceMock.findOne(documentId)).thenReturn(Optional.of(documentDTOMock));
    when(documentServiceMock.getDownloadUrl(documentDTOMock)).thenReturn(expectDownloadUrl);

    mockMvc.perform(get(PATH_API + PATH_DOCUMENTS + PATH_DOWNLOADS + "/{documentId}", documentId))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", expectDownloadUrl));

    verify(documentServiceMock).findOne(documentId);
    verify(documentServiceMock).getDownloadUrl(documentDTOMock);
  }
}
