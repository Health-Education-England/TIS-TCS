package com.transformuk.hee.tis.tcs.service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.api.dto.DocumentDTO;
import com.transformuk.hee.tis.tcs.api.dto.TagDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
import com.transformuk.hee.tis.tcs.service.service.TagService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DocumentResourceIntTest {
    private MockMvc mockMvc;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private TagService tagService;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final long PERSON_BASE_ID = 10;
    private static final long DOCUMENT_BASE_ID = 100;
    private static final long TAG_BASE_ID = 1000;

    private static final String SQL_INSERT_PERSON =
            " INSERT INTO `Person` (`id`, `intrepidId`, `addedDate`, `amendedDate`, `role`, `status`, `comments`, `inactiveDate`, `inactiveNotes`, `publicHealthNumber`, `regulator`) " +
                    " VALUES " +
                    " (%d, '98798797987', '2012-06-20 00:00:00', '2012-06-20 00:00:00.000', 'AAAAA', 'CURRENT', 'XXXX', NULL, NULL, NULL, 'HEELIVE');";

    private static final String SQL_INSERT_DOCUMENT =
            "INSERT INTO `Document` (`id`, `addedDate`, `amendedDate`, `inactiveDate`, `uploadedBy`, `name`, `fileName`, `fileExtension`, `contentType`, `size`, `personId`, `fileLocation`, `status`, `version`, `intrepidDocumentUId`, `intrepidParentRecordId`, `intrepidFolderPath`) " +
                    " VALUES " +
                    " (%d, '2018-02-16 14:32:06', '2018-02-19 11:07:11.882', NULL, 'James Hudson', 'Test Update', 'LargeTestFile.txt', 'txt', 'text/plain', 512000, %d, '\"0x8D5754A0E0E2F93\"', 'INACTIVE', 1, NULL, NULL, NULL);";

    private static final String SQL_INSERT_TAG =
            "INSERT INTO `Tag` (`id`, `name`) " +
                    " VALUES " +
                    " (%d, '%s');";

    private static boolean loaded = false;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);
        final DocumentResource documentResource = new DocumentResource(documentService, tagService);
        mockMvc = MockMvcBuilders.standaloneSetup(documentResource).build();

        TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");

        initDB();
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
    }

    @Test
    public void createDocument_shouldReturnHTTP400_WhenPersonIdIsMissing() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile("document", "document.jpg", "text/plain", "DataDataDataDataDataData".getBytes());

        mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDocument_shouldReturnHTTP400_WhenPersonDocumentIsMissing() throws Exception {
        mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .param("personId", String.valueOf(PERSON_BASE_ID))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDocument_shouldReturnHTTP201WithId_WhenUploadingValidDocument() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile("document", "document.jpg", "text/plain", "DataDataDataDataDataData".getBytes());

        mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .file(mockFile)
                .param("personId", String.valueOf(PERSON_BASE_ID))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("{\"id\":")));
    }

    // TODO fix validations
    @Ignore
    @Test
    public void bulkUpdateDocuments_shouldReturnHTTP400_WhenDocumentIsInvalid() throws Exception {
        final long documentId = DOCUMENT_BASE_ID + 1;

        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_DOCUMENT, documentId, PERSON_BASE_ID).getBytes()));

        final DocumentDTO document = new DocumentDTO();
        document.setId(documentId);
        document.setName("Non-existing document");
//        document.setPersonId(PERSON_BASE_ID);

        mockMvc.perform(patch(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(document))))
                .andExpect(status().isBadRequest());
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
        final long documentId = DOCUMENT_BASE_ID + 2;

        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(getSql(SQL_INSERT_DOCUMENT, documentId, PERSON_BASE_ID)).getBytes()));

        updateMetadataWith2Tags(documentId);
    }

    @Test
    public void bulkUpdateDocuments_shouldReturnHTTP200_WhenMetadataIsUpdatedWithNewTag() throws Exception {
        final long documentId = DOCUMENT_BASE_ID + 3;

        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_DOCUMENT, documentId, PERSON_BASE_ID).getBytes()));

        final DocumentDTO updatedDocumentWith2Tags = updateMetadataWith2Tags(documentId);

        updatedDocumentWith2Tags.getTags().add(new TagDTO("Tag 3"));

        mockMvc.perform(patch(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(updatedDocumentWith2Tags))))
                .andExpect(status().isOk());

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS + "/" + documentId))
                .andExpect(status().isOk())
                .andReturn();

        final DocumentDTO updatedDocumentWith3Tags = new ObjectMapper().readValue(response.getResponse().getContentAsString(), DocumentDTO.class);

        assertThat(updatedDocumentWith3Tags.getTags()).hasSize(3).containsAll(updatedDocumentWith2Tags.getTags());
    }

    @Test
    public void bulkUpdateDocuments_shouldReturnHTTP200_WhenMetadataIsUpdatedWithNewAndDeletedTag() throws Exception {
        final long documentId = DOCUMENT_BASE_ID + 5;

        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(getSql(SQL_INSERT_DOCUMENT, documentId, PERSON_BASE_ID).getBytes()));

        final DocumentDTO updatedDocumentWith2Tags = updateMetadataWith2Tags(documentId);

        updatedDocumentWith2Tags.getTags().remove(new TagDTO("Tag 1"));
        updatedDocumentWith2Tags.getTags().add(new TagDTO("Tag 3"));

        mockMvc.perform(patch(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(Collections.singletonList(updatedDocumentWith2Tags))))
                .andExpect(status().isOk());

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS + "/" + documentId))
                .andExpect(status().isOk())
                .andReturn();

        final DocumentDTO updatedDocumentWith1Tag = new ObjectMapper().readValue(response.getResponse().getContentAsString(), DocumentDTO.class);

        assertThat(updatedDocumentWith1Tag.getTags()).hasSize(2).containsAll(updatedDocumentWith2Tags.getTags());
    }

    @Test
    public void getAllTags_shouldReturnTags_WhenQueryStartsWithOrderAsc() throws Exception {
        final String query = "abc";

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS + DocumentResource.PATH_TAGS + "/?query=" + query))
                .andExpect(status().isOk())
                .andReturn();

        final Type tagDTOListType = new TypeToken<ArrayList<TagDTO>>() {
        }.getType();

        final List<TagDTO> responseBody = new Gson().fromJson(response.getResponse().getContentAsString(), tagDTOListType);

        assertThat(responseBody).hasSize(2);
        assertThat(responseBody).containsSequence(new TagDTO("abcdef"), new TagDTO("abcxyz"));
    }

    @Test
    public void getAllTags_shouldNotReturnTags_WhenQueryDoesNotStartsWith() throws Exception {
        final String query = "xyz";

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS + DocumentResource.PATH_TAGS + "/?query=" + query))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(response.getResponse().getContentLength()).isZero();
        assertThat(response.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    public void getAllTags_shouldReturnBadRequest_WhenQueryIsNullOrEmpty() throws Exception {
        final String query = "";

        this.mockMvc.perform(get(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS + DocumentResource.PATH_TAGS + "/?query=" + query))
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

        final MvcResult response = this.mockMvc.perform(get(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS + "/" + documentId))
                .andExpect(status().isOk())
                .andReturn();

        final DocumentDTO updatedDocument = new ObjectMapper().readValue(response.getResponse().getContentAsString(), DocumentDTO.class);

        assertThat(document.getId()).isEqualTo(updatedDocument.getId());
        assertThat(document.getName()).isEqualTo(updatedDocument.getName());
        assertThat(document.getPersonId()).isEqualTo(updatedDocument.getPersonId());
        assertThat(document.getStatus()).isEqualTo(updatedDocument.getStatus());
        assertThat(document.getVersion()).isEqualTo(updatedDocument.getVersion());
        assertThat(updatedDocument.getTags()).hasSize(2).containsAll(document.getTags());

        return updatedDocument;
    }

    private String getSql(final String sql, final Object... args) {
        return String.format(sql, args);
    }
}