package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.service.DocumentService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Connection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DocumentResourceIntTest {
    private MockMvc mockMvc;
    @Autowired
    private DocumentService documentService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String sqlInsertPerson =
            " INSERT INTO `Person` (`id`, `intrepidId`, `addedDate`, `amendedDate`, `role`, `status`, `comments`, `inactiveDate`, `inactiveNotes`, `publicHealthNumber`, `regulator`) " +
                    " VALUES " +
                    " (1, '98798797987', '2012-06-20 00:00:00', '2012-06-20 00:00:00.000', 'AAAAA', 'CURRENT', 'XXXX', NULL, NULL, NULL, 'HEELIVE');";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentResource documentResource = new DocumentResource(documentService);
        mockMvc = MockMvcBuilders.standaloneSetup(documentResource).build();

        TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");
    }

    @Ignore
    @Test
    public void listFiles() throws Exception {
        this.mockMvc.perform(get("/api/listFiles"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnHTTP400_WhenPersonIdIsMissing() throws Exception {
        final MockMultipartFile mockFile = new MockMultipartFile("document", "document.jpg", "text/plain", "DataDataDataDataDataData".getBytes());

        mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnHTTP400_WhenPersonDocumentIsMissing() throws Exception {
        mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .param("personId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnHTTP201WithId_WhenUploadingValidDocument() throws Exception {
        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ByteArrayResource(sqlInsertPerson.getBytes()));

        final MockMultipartFile mockFile = new MockMultipartFile("document", "document.jpg", "text/plain", "DataDataDataDataDataData".getBytes());

        mockMvc.perform(fileUpload(DocumentResource.PATH_API + DocumentResource.PATH_DOCUMENTS)
                .file(mockFile)
                .param("personId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{id:1}"));
    }
}