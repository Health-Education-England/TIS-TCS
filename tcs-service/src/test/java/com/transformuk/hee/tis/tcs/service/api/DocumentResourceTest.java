package com.transformuk.hee.tis.tcs.service.api;

import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_API;
import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_DOCUMENTS;
import static com.transformuk.hee.tis.tcs.service.api.DocumentResource.PATH_DOWNLOADS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import com.transformuk.hee.tis.tcs.service.service.TagService;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
public class DocumentResourceTest {

  private static final long PERSON_BASE_ID = 10;
  private static final byte[] TEST_FILE_CONTENT = "DataDataDataDataDataData".getBytes();
  private static final String TEST_FILE_NAME = "document.txt";
  private static final String TEST_FILE_CONTENT_TYPE = "text/plain";
  private static final String TEST_FILE_FORM_FIELD_NAME = "document";

  private MockMvc mockMvc;

  @MockBean
  private DocumentService documentServiceMock;
  @MockBean
  private TagService tagServiceMock;
  @MockBean
  private DocumentDTO documentDTOMock;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Before
  public void setup() {
    DocumentResource testObj = new DocumentResource(documentServiceMock, tagServiceMock);
    mockMvc = MockMvcBuilders.standaloneSetup(testObj).build();

    TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");
  }

  @Test
  public void downloadDocumentByIdV2ShouldRedirectWithLocationWhenDocumentExists()
      throws Exception {
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

  @Test
  public void uploadDocument_shouldReturnHTTP201_WhenUploadingValidDocument() throws Exception {
    final MockMultipartFile mockFile = new MockMultipartFile(TEST_FILE_FORM_FIELD_NAME,
        TEST_FILE_NAME, TEST_FILE_CONTENT_TYPE, TEST_FILE_CONTENT);

    DocumentDTO documentDto = new DocumentDTO();
    documentDto.setId(1L);
    documentDto.setTitle(TEST_FILE_NAME);
    documentDto.setFileName(TEST_FILE_NAME);
    documentDto.setFileExtension("txt");
    documentDto.setContentType(TEST_FILE_CONTENT_TYPE);
    documentDto.setSize((long) TEST_FILE_CONTENT.length);
    documentDto.setPersonId(PERSON_BASE_ID);

    when(documentServiceMock.save(any(DocumentDTO.class))).thenReturn(documentDto);

    mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
        .file(mockFile)
        .param("personId", String.valueOf(PERSON_BASE_ID))
        .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(containsString("{\"id\":1}")));
  }

  @Test
  public void getDocumentById_shouldReturnHTTP200_WhenDocumentDoesExist() throws Exception {
    DocumentDTO documentDto = new DocumentDTO();
    documentDto.setId(1L);
    documentDto.setTitle(TEST_FILE_NAME);
    documentDto.setFileName(TEST_FILE_NAME);
    documentDto.setFileExtension("txt");
    documentDto.setContentType(TEST_FILE_CONTENT_TYPE);
    documentDto.setSize((long) TEST_FILE_CONTENT.length);
    documentDto.setPersonId(PERSON_BASE_ID);

    when(documentServiceMock.findOne(1L)).thenReturn(Optional.of(documentDto));

    final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
        DocumentResource.PATH_DOCUMENTS +
        "/" +
        1L))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    final DocumentDTO document = objectMapper
        .readValue(response.getResponse().getContentAsString(), DocumentDTO.class);

    assertThat(document.getId()).isEqualTo(1L);
    assertThat(document.getTitle()).isEqualTo(TEST_FILE_NAME);
    assertThat(document.getFileName()).isEqualTo(TEST_FILE_NAME);
    assertThat(document.getFileExtension())
        .isEqualTo(TEST_FILE_NAME.substring(TEST_FILE_NAME.lastIndexOf(".") + 1));
    assertThat(document.getContentType()).isEqualTo(TEST_FILE_CONTENT_TYPE);
    assertThat(document.getSize()).isEqualTo(TEST_FILE_CONTENT.length);
    assertThat(document.getPersonId()).isEqualTo(PERSON_BASE_ID);
  }
}
