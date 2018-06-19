package com.transformuk.hee.tis.tcs.service.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.azure.storage.StorageException;
import com.transformuk.hee.tis.filestorage.repository.FileStorageRepository;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.api.dto.TagDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.config.AzureProperties;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import com.transformuk.hee.tis.tcs.service.service.TagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DocumentResourceIntTest {
    private static final long PERSON_BASE_ID = 10;
    private static final long DOCUMENT_BASE_ID = 100;
    private static final long TAG_BASE_ID = 1000;
    private static final byte[] TEST_FILE_CONTENT = "DataDataDataDataDataData".getBytes();
    private static final String TEST_FILE_NAME = "document.txt";
    private static final String TEST_FILE_CONTENT_TYPE = "text/plain";
    private static final String TEST_FILE_FORM_FIELD_NAME = "document";

    private MockMvc mockMvc;

    @Resource
    private DocumentService documentService;
    @Resource
    private TagService tagService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private AzureProperties azureProperties;
    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    // fixme: replace with API endpoint to delete when implemented
    @Resource
    private FileStorageRepository fileStorageRepository;

    private static final String SQL_INSERT_PERSON =
            " INSERT INTO `Person` (`id`, `intrepidId`, `addedDate`, `amendedDate`, `role`, `status`, `comments`, `inactiveDate`, `inactiveNotes`, `publicHealthNumber`, `regulator`) " +
                    " VALUES " +
                    " (%d, '98798797987', '2012-06-20 00:00:00', '2012-06-20 00:00:00.000', 'AAAAA', 'CURRENT', 'XXXX', NULL, NULL, NULL, 'HEELIVE');";

    private static final String SQL_INSERT_DOCUMENT =
            "INSERT INTO `Document` (`id`, `addedDate`, `amendedDate`, `inactiveDate`, `uploadedBy`, `name`, `fileName`, `fileExtension`, `contentType`, `size`, `personId`, `status`, `version`, `intrepidDocumentUId`, `intrepidParentRecordId`, `intrepidFolderPath`) " +
                    " VALUES " +
                    " (%d, '2018-02-16 14:32:06', '2018-02-19 11:07:11.882', NULL, 'James Hudson', 'Test Update', 'LargeTestFile.txt', 'txt', 'text/plain', 512000, %d, 'INACTIVE', 1, NULL, NULL, NULL);";

    private static final String SQL_INSERT_TAG =
            "INSERT INTO `Tag` (`id`, `name`) " +
                    " VALUES " +
                    " (%d, '%s');";

    private static boolean loaded = false;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);
        final DocumentResource documentResource = new DocumentResource(documentService, tagService);
        mockMvc = MockMvcBuilders.standaloneSetup(documentResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build();

        TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");

        initDB();
    }

    @Test
    public void uploadDocument_shouldReturnHTTP400_WhenPersonIsMissing() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile(TEST_FILE_FORM_FIELD_NAME, TEST_FILE_NAME, TEST_FILE_CONTENT_TYPE, TEST_FILE_CONTENT);

        mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void uploadDocument_shouldReturnHTTP400_WhenDocumentIsMissing() throws Exception {
        mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .param("personId", String.valueOf(PERSON_BASE_ID))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void uploadDocument_shouldReturnHTTP201_WhenUploadingValidDocument() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile(TEST_FILE_FORM_FIELD_NAME, TEST_FILE_NAME, TEST_FILE_CONTENT_TYPE, TEST_FILE_CONTENT);

        final MvcResult uploadResponse = mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .file(mockFile)
                .param("personId", String.valueOf(PERSON_BASE_ID))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("{\"id\":"))).andReturn();

        final DocumentId documentId = new Gson().fromJson(uploadResponse.getResponse().getContentAsString(), DocumentId.class);

        deleteTestFile(documentId.getId());
    }

    @Test
    public void bulkUpdateDocuments_shouldReturnHTTP404_WhenDocumentDoesNotExist() throws Exception {
        final DocumentDTO document = new DocumentDTO();
        document.setId(9999999999L);
        document.setName("Non-existing document");
        document.setPersonId(PERSON_BASE_ID);

        mockMvc.perform(patch(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(document))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void bulkUpdateDocuments_shouldReturnHTTP200_WhenMetadataIsUpdatedWith2Tags() throws Exception {
        final long documentId = DOCUMENT_BASE_ID + 20;

        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(getSql(SQL_INSERT_DOCUMENT, documentId, PERSON_BASE_ID)).getBytes()));

        updateMetadataWith2Tags(documentId);
    }

    @Test
    public void bulkUpdateDocuments_shouldReturnHTTP200_WhenMetadataIsUpdatedWithNewTag() throws Exception {
        final long documentId = DOCUMENT_BASE_ID + 30;

        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_DOCUMENT, documentId, PERSON_BASE_ID).getBytes()));

        final DocumentDTO updatedDocumentWith2Tags = updateMetadataWith2Tags(documentId);

        updatedDocumentWith2Tags.getTags().add(new TagDTO("Tag 3"));

        mockMvc.perform(patch(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(updatedDocumentWith2Tags))))
                .andExpect(status().isOk());

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/" +
                documentId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final DocumentDTO updatedDocumentWith3Tags = new Gson().fromJson(response.getResponse().getContentAsString(), DocumentDTO.class);

        assertThat(updatedDocumentWith3Tags.getTags()).hasSize(3).containsAll(updatedDocumentWith2Tags.getTags());
    }

    @Test
    public void bulkUpdateDocuments_shouldReturnHTTP200_WhenMetadataIsUpdatedWithNewAndDeletedTag() throws Exception {
        final long documentId = DOCUMENT_BASE_ID + 40;

        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_DOCUMENT, documentId, PERSON_BASE_ID).getBytes()));

        final DocumentDTO updatedDocumentWith2Tags = updateMetadataWith2Tags(documentId);

        updatedDocumentWith2Tags.getTags().remove(new TagDTO("Tag 1"));
        updatedDocumentWith2Tags.getTags().add(new TagDTO("Tag 3"));

        mockMvc.perform(patch(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(updatedDocumentWith2Tags))))
                .andExpect(status().isOk());

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/" +
                documentId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final DocumentDTO updatedDocumentWith1Tag = new Gson().fromJson(response.getResponse().getContentAsString(), DocumentDTO.class);

        assertThat(updatedDocumentWith1Tag.getTags()).hasSize(2).containsAll(updatedDocumentWith2Tags.getTags());
    }

    @Test
    public void downloadDocumentById_shouldReturnHTTP200_WhenDocumentDoesExist() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile(TEST_FILE_FORM_FIELD_NAME, TEST_FILE_NAME, TEST_FILE_CONTENT_TYPE, TEST_FILE_CONTENT);

        final MvcResult uploadResponse = mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .file(mockFile)
                .param("personId", String.valueOf(PERSON_BASE_ID))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("{\"id\":"))).andReturn();

        final DocumentId documentId = new Gson().fromJson(uploadResponse.getResponse().getContentAsString(), DocumentId.class);

        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                DocumentResource.PATH_DOWNLOADS +
                "/" +
                documentId.getId()))
                .andExpect(header().string("Content-Disposition", "attachment;filename=document.txt"))
                .andExpect(header().string("Content-Length", String.valueOf(TEST_FILE_CONTENT.length)))
                .andExpect(content().contentType(TEST_FILE_CONTENT_TYPE))
                .andExpect(content().bytes(TEST_FILE_CONTENT))
                .andExpect(status().isOk());

        deleteTestFile(documentId.getId());
    }

    @Test
    public void downloadDocumentById_shouldReturnHTTP404_WhenDocumentDoesNotExist() throws Exception {
        final long documentId = DOCUMENT_BASE_ID + 999999999;

        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                DocumentResource.PATH_DOWNLOADS +
                "/" +
                documentId))
                .andExpect(content().string(""))
                .andExpect(status().isNotFound());
    }

    @Test
    public void downloadDocumentById_shouldReturnHTTP400_WhenDocumentIdIsEmpty() throws Exception {
        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                DocumentResource.PATH_DOWNLOADS +
                "/" +
                ""))
                .andExpect(content().string(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void downloadDocumentById_shouldReturnHTTP400_WhenDocumentIdIsNaN() throws Exception {
        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                DocumentResource.PATH_DOWNLOADS +
                "/" +
                "NaN"))
                .andExpect(content().string(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getDocumentById_shouldReturnHTTP400_WhenDocumentIdIsNaN() throws Exception {
        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/NaN"))
                .andExpect(content().string(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getDocumentById_shouldReturnHTTP404_WhenDocumentDoesNotExist() throws Exception {
        final long documentId = DOCUMENT_BASE_ID + 999999999;

        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/" +
                documentId))
                .andExpect(content().string(""))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDocumentById_shouldReturnHTTP200_WhenDocumentDoesExist() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile(TEST_FILE_FORM_FIELD_NAME, TEST_FILE_NAME, TEST_FILE_CONTENT_TYPE, TEST_FILE_CONTENT);

        final MvcResult uploadResponse = mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .file(mockFile)
                .param("personId", String.valueOf(PERSON_BASE_ID))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("{\"id\":"))).andReturn();

        final DocumentId documentId = new Gson().fromJson(uploadResponse.getResponse().getContentAsString(), DocumentId.class);

        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/" +
                documentId.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        final DocumentDTO document = new Gson().fromJson(response.getResponse().getContentAsString(), DocumentDTO.class);

        assertThat(document.getId()).isEqualTo(documentId.getId());
        assertThat(document.getName()).isEqualTo(TEST_FILE_NAME);
        assertThat(document.getFileName()).isEqualTo(TEST_FILE_NAME);
        assertThat(document.getFileExtension()).isEqualTo(TEST_FILE_NAME.substring(TEST_FILE_NAME.lastIndexOf(".") + 1));
        assertThat(document.getContentType()).isEqualTo(TEST_FILE_CONTENT_TYPE);
        assertThat(document.getSize()).isEqualTo(TEST_FILE_CONTENT.length);
        assertThat(document.getPersonId()).isEqualTo(PERSON_BASE_ID);
        assertThat(document.getTags()).isEmpty();

        deleteTestFile(documentId.getId());
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP501_WhenEntityDoesNotExist() throws Exception {
        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/UnknownEntity/" +
                "1"
        ))
                .andExpect(content().string(""))
                .andExpect(status().isNotImplemented());
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP400_WhenPersonIdIsEmpty() throws Exception {
        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/ "))
                .andExpect(content().string(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP400_WhenPersonIdIsNaN() throws Exception {
        final String personId = "NaN";

        mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                personId
        ))
                .andExpect(content().string(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenPersonDoesNotExist() throws Exception {
        final Long personId = 999999999L;

        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                personId
        ))
                .andExpect(status().isOk())
                .andReturn();

        assertPaginationDocumentsDoNotExist(response.getResponse().getContentAsString());
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenSearchQueryDoesNotMatch() throws Exception {
        final String query = "NonExistingThing";

        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                PERSON_BASE_ID +
                "?query=" + query
        ))
                .andExpect(status().isOk())
                .andReturn();

        assertPaginationDocumentsDoNotExist(response.getResponse().getContentAsString());
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenSearchQueryMatchesName() throws Exception {
        final String query = "AAAAA";

        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                PERSON_BASE_ID +
                "?query=" + query
        ))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final List<DocumentDTO> documents = assertPaginationDocumentsExist(response.getResponse().getContentAsString(), 1);

        assertThat(documents).hasSize(1);

        final DocumentDTO document = documents.get(0);

        assertThat(document.getName()).isEqualTo("Document AAAAA");
        assertThat(document.getFileName()).isEqualTo("document1.jpg");
        assertThat(document.getFileExtension()).isEqualTo("jpg");
        assertThat(document.getContentType()).isEqualTo("image/jpeg");
        assertThat(document.getSize()).isEqualTo(56353);
        assertThat(document.getPersonId()).isEqualTo(PERSON_BASE_ID);
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenSearchQueryMatchesFileName() throws Exception {
        final String query = "document1";

        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                PERSON_BASE_ID +
                "?query=" + query
        ))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        final List<DocumentDTO> documents = assertPaginationDocumentsExist(response.getResponse().getContentAsString(), 1);

        assertThat(documents).hasSize(1);

        final DocumentDTO document = documents.get(0);

        assertThat(document.getName()).isEqualTo("Document AAAAA");
        assertThat(document.getFileName()).isEqualTo("document1.jpg");
        assertThat(document.getFileExtension()).isEqualTo("jpg");
        assertThat(document.getContentType()).isEqualTo("image/jpeg");
        assertThat(document.getSize()).isEqualTo(56353);
        assertThat(document.getPersonId()).isEqualTo(PERSON_BASE_ID);
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenSearchQueryMatchesFileExtension() throws Exception {
        final String query = "jpg";

        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                PERSON_BASE_ID +
                "?query=" + query
        ))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        final List<DocumentDTO> documents = assertPaginationDocumentsExist(response.getResponse().getContentAsString(), 2);

        assertThat(documents).hasSize(2);

        DocumentDTO document = documents.get(0);

        assertThat(document.getName()).isEqualTo("Document AAAAA");
        assertThat(document.getFileName()).isEqualTo("document1.jpg");
        assertThat(document.getFileExtension()).isEqualTo("jpg");
        assertThat(document.getContentType()).isEqualTo("image/jpeg");
        assertThat(document.getSize()).isEqualTo(56353);
        assertThat(document.getPersonId()).isEqualTo(PERSON_BASE_ID);

        document = documents.get(1);

        assertThat(document.getName()).isEqualTo("Document CCCCC");
        assertThat(document.getFileName()).isEqualTo("document3.jpg");
        assertThat(document.getFileExtension()).isEqualTo("jpg");
        assertThat(document.getContentType()).isEqualTo("image/jpeg");
        assertThat(document.getSize()).isEqualTo(12982);
        assertThat(document.getPersonId()).isEqualTo(PERSON_BASE_ID);
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenSearchQueryMatchesContentType() throws Exception {
        final String query = "image/png";

        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                PERSON_BASE_ID +
                "?query=" + query
        ))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        final List<DocumentDTO> documents = assertPaginationDocumentsExist(response.getResponse().getContentAsString(), 1);

        assertThat(documents).hasSize(1);

        final DocumentDTO document = documents.get(0);

        assertThat(document.getName()).isEqualTo("Document BBBBB");
        assertThat(document.getFileName()).isEqualTo("document2.png");
        assertThat(document.getFileExtension()).isEqualTo("png");
        assertThat(document.getContentType()).isEqualTo("image/png");
        assertThat(document.getSize()).isEqualTo(12123);
        assertThat(document.getPersonId()).isEqualTo(PERSON_BASE_ID);
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenRequestedFistPage() throws Exception {
        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                (PERSON_BASE_ID + 2) +
                "?page=0&size=2"
        ))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        final PageImplTest<DocumentDTO> documents = new Gson().fromJson(response.getResponse().getContentAsString(), new TypeToken<PageImplTest<DocumentDTO>>() {
        }.getType());

        assertThat(documents.hasContent()).isTrue();
        assertThat(documents.isFirst()).isTrue();
        assertThat(documents.isLast()).isFalse();
        assertThat(documents.getNumber()).isEqualTo(0);
        assertThat(documents.getNumberOfElements()).isEqualTo(2);
        assertThat(documents.getSize()).isEqualTo(2);
        assertThat(documents.getTotalElements()).isEqualTo(6);
        assertThat(documents.getTotalPages()).isEqualTo(3);
        assertThat(documents.getContent()).hasSize(2);
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenRequestedMiddlePage() throws Exception {
        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                (PERSON_BASE_ID + 2) +
                "?page=1&size=2"
        ))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        final PageImplTest<DocumentDTO> documents = new Gson().fromJson(response.getResponse().getContentAsString(), new TypeToken<PageImplTest<DocumentDTO>>() {
        }.getType());

        assertThat(documents.hasContent()).isTrue();
        assertThat(documents.isFirst()).isFalse();
        assertThat(documents.isLast()).isFalse();
        assertThat(documents.getNumber()).isEqualTo(1);
        assertThat(documents.getNumberOfElements()).isEqualTo(2);
        assertThat(documents.getSize()).isEqualTo(2);
        assertThat(documents.getTotalElements()).isEqualTo(6);
        assertThat(documents.getTotalPages()).isEqualTo(3);
        assertThat(documents.getContent()).hasSize(2);
    }

    @Test
    public void getAllDocuments_shouldReturnHTTP200_WhenRequestedLastPage() throws Exception {
        final MvcResult response = mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/person/" +
                (PERSON_BASE_ID + 2) +
                "?page=2&size=2"
        ))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andReturn();

        final PageImplTest<DocumentDTO> documents = new Gson().fromJson(response.getResponse().getContentAsString(), new TypeToken<PageImplTest<DocumentDTO>>() {
        }.getType());

        assertThat(documents.hasContent()).isTrue();
        assertThat(documents.isFirst()).isFalse();
        assertThat(documents.isLast()).isTrue();
        assertThat(documents.getNumber()).isEqualTo(2);
        assertThat(documents.getNumberOfElements()).isEqualTo(2);
        assertThat(documents.getSize()).isEqualTo(2);
        assertThat(documents.getTotalElements()).isEqualTo(6);
        assertThat(documents.getTotalPages()).isEqualTo(3);
        assertThat(documents.getContent()).hasSize(2);
    }

    @Test
    public void getAllTags_shouldReturnHTTP200_WhenQueryStartsWithOrderAsc() throws Exception {
        final String query = "abc";

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                DocumentResource.PATH_TAGS +
                "/?query=" + query))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final Type tagDTOListType = new TypeToken<ArrayList<TagDTO>>() {
        }.getType();

        final List<TagDTO> responseBody = new Gson().fromJson(response.getResponse().getContentAsString(), tagDTOListType);

        assertThat(responseBody).hasSize(2);
        assertThat(responseBody).containsSequence(new TagDTO("abcdef"), new TagDTO("abcxyz"));
    }

    @Test
    public void getAllTags_shouldReturnHTTP404_WhenQueryDoesNotStartsWith() throws Exception {
        final String query = "xyz";

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                DocumentResource.PATH_TAGS +
                "/?query=" + query))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(response.getResponse().getContentType()).isNullOrEmpty();
        assertThat(response.getResponse().getContentLength()).isZero();
        assertThat(response.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    public void getAllTags_shouldReturnHTTP400_WhenQueryIsNullOrEmpty() throws Exception {
        final String query = "";

        this.mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                DocumentResource.PATH_TAGS +
                "/?query=" + query))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andReturn();
    }

    private DocumentDTO updateMetadataWith2Tags(final long documentId) throws Exception {
        final DocumentDTO document = new DocumentDTO();
        document.setId(documentId);
        document.setName("Document Name");
        document.setPersonId(PERSON_BASE_ID);
        document.setStatus(Status.CURRENT);
        document.setVersion(1);

        final Set<TagDTO> tags = new HashSet<>();
        tags.add(new TagDTO("Tag 1"));
        tags.add(new TagDTO("Tag 2"));
        document.setTags(tags);

        mockMvc.perform(patch(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(document))))
                .andExpect(status().isOk());

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API +
                DocumentResource.PATH_DOCUMENTS +
                "/" +
                documentId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final DocumentDTO updatedDocument = new Gson().fromJson(response.getResponse().getContentAsString(), DocumentDTO.class);

        assertThat(document.getId()).isEqualTo(updatedDocument.getId());
        assertThat(document.getName()).isEqualTo(updatedDocument.getName());
        assertThat(document.getPersonId()).isEqualTo(updatedDocument.getPersonId());
        assertThat(document.getStatus()).isEqualTo(updatedDocument.getStatus());
        assertThat(document.getVersion()).isEqualTo(updatedDocument.getVersion());
        assertThat(updatedDocument.getTags()).hasSize(2).containsAll(document.getTags());

        return updatedDocument;
    }

    private void deleteTestFile(final long documentId) throws URISyntaxException, InvalidKeyException, StorageException {
        fileStorageRepository.deleteFile(documentId, azureProperties.getContainerName() + "/" + azureProperties.getPersonFolder(), TEST_FILE_NAME);
    }

    private void initDB() throws SQLException {
        if (loaded) {
            return;
        }

        loaded = true;

        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_PERSON, PERSON_BASE_ID).getBytes()));

        long tagId = TAG_BASE_ID;

        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_TAG, tagId++, "abcdef").getBytes()));
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_TAG, tagId++, "abcxyz").getBytes()));
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_TAG, tagId++, "xabcxyz").getBytes()));
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_TAG, tagId++, "ghijkl").getBytes()));

        insertBaseDocuments(connection);
    }

    private void insertBaseDocuments(final Connection connection) {
        final String query = "INSERT INTO `Document` (`id`, `addedDate`, `amendedDate`, `inactiveDate`, `uploadedBy`, `name`, `fileName`, `fileExtension`, `contentType`, `size`, `personId`, `status`, `version`, `intrepidDocumentUId`, `intrepidParentRecordId`, `intrepidFolderPath`)\n" +
                "VALUES" +
                "(" + (DOCUMENT_BASE_ID + 1001) + ", '2018-03-27 14:14:20', '2018-03-27 14:14:20.367', NULL, 'User 1', 'Document AAAAA', 'document1.jpg', 'jpg', 'image/jpeg', 56353, " + PERSON_BASE_ID + ", 'CURRENT', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1002) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 1', 'Document BBBBB', 'document2.png', 'png', 'image/png', 12123, " + PERSON_BASE_ID + ", 'CURRENT', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1003) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 1', 'Document CCCCC', 'document3.jpg', 'jpg', 'image/jpeg', 12982, " + PERSON_BASE_ID + ", 'DELETE', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1004) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 2', 'Document DDDDD', 'document4.jpg', 'jpg', 'image/jpeg', 23423, " + (PERSON_BASE_ID + 1) + ", 'INACTIVE', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1005) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 3', 'Document XXXXX', 'documentX.jpg', 'jpg', 'image/jpeg', 98123, " + (PERSON_BASE_ID + 2) + ", 'INACTIVE', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1006) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 3', 'Document XXXXX', 'documentX.jpg', 'jpg', 'image/jpeg', 98123, " + (PERSON_BASE_ID + 2) + ", 'INACTIVE', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1007) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 3', 'Document XXXXX', 'documentX.jpg', 'jpg', 'image/jpeg', 98123, " + (PERSON_BASE_ID + 2) + ", 'INACTIVE', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1008) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 3', 'Document XXXXX', 'documentX.jpg', 'jpg', 'image/jpeg', 98123, " + (PERSON_BASE_ID + 2) + ", 'INACTIVE', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1009) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 3', 'Document XXXXX', 'documentX.jpg', 'jpg', 'image/jpeg', 98123, " + (PERSON_BASE_ID + 2) + ", 'INACTIVE', NULL, NULL, NULL, NULL)," +
                "(" + (DOCUMENT_BASE_ID + 1010) + ", '2018-03-27 15:10:59', '2018-03-27 15:14:36.624', NULL, 'User 3', 'Document XXXXX', 'documentX.jpg', 'jpg', 'image/jpeg', 98123, " + (PERSON_BASE_ID + 2) + ", 'INACTIVE', NULL, NULL, NULL, NULL);";

        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(query.getBytes()));
    }

    private String getSql(final String sql, final Object... args) {
        return String.format(sql, args);
    }

    private void assertPaginationDocumentsDoNotExist(final String json) {
        final PageImplTest<DocumentDTO> documents = new Gson().fromJson(json, new TypeToken<PageImplTest<DocumentDTO>>() {
        }.getType());

        assertThat(documents.hasContent()).isFalse();
        assertThat(documents.isFirst()).isTrue();
        assertThat(documents.isLast()).isTrue();
        assertThat(documents.getNumber()).isZero();
        assertThat(documents.getNumberOfElements()).isZero();
        assertThat(documents.getSize()).isEqualTo(20);
        assertThat(documents.getTotalElements()).isZero();
        assertThat(documents.getTotalPages()).isZero();
    }

    private List<DocumentDTO> assertPaginationDocumentsExist(final String json, final int expectedElements) {
        final PageImplTest<DocumentDTO> documents = new Gson().fromJson(json, new TypeToken<PageImplTest<DocumentDTO>>() {
        }.getType());

        assertThat(documents.hasContent()).isTrue();
        assertThat(documents.isFirst()).isTrue();
        assertThat(documents.isLast()).isTrue();
        assertThat(documents.getNumber()).isEqualTo(0);
        assertThat(documents.getNumberOfElements()).isEqualTo(expectedElements);
        assertThat(documents.getSize()).isEqualTo(20);
        assertThat(documents.getTotalElements()).isEqualTo(expectedElements);
        assertThat(documents.getTotalPages()).isEqualTo(1);

        return documents.getContent();
    }

    private class DocumentId {
        private final Long id;

        public DocumentId(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    /**
     * The intention of this class is to ease the asserts in tests and was created because
     * {@link Gson} cannot instantiate {@link org.springframework.data.domain.PageImpl}.
     */
    private class PageImplTest<T> implements Page<T> {
        protected boolean first;
        protected boolean last;
        protected Integer number;
        protected Integer numberOfElements;
        protected Integer size;
        protected Sort sort;
        protected Integer totalElements;
        protected Integer totalPages;
        protected List<T> content;

        @Override
        public int getTotalPages() {
            return totalPages;
        }

        @Override
        public long getTotalElements() {
            return totalElements;
        }

        @Override
        public int getNumber() {
            return number;
        }

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public int getNumberOfElements() {
            return numberOfElements;
        }

        @Override
        public List<T> getContent() {
            return content;
        }

        @Override
        public boolean hasContent() {
            return content != null && !content.isEmpty();
        }

        @Override
        public Sort getSort() {
            return sort;
        }

        @Override
        public boolean isFirst() {
            return first;
        }

        @Override
        public boolean isLast() {
            return last;
        }

        @Override
        public boolean hasNext() {
            return getNumber() + 1 < getTotalPages();
        }

        @Override
        public boolean hasPrevious() {
            return getNumber() > 0;
        }

        @Override
        public Pageable nextPageable() {
            return null;
        }

        @Override
        public Pageable previousPageable() {
            return null;
        }

        @Override
        public <S> Page<S> map(final Converter<? super T, ? extends S> converter) {
            return null;
        }

        @Override
        public Iterator<T> iterator() {
            return null;
        }
    }
}
