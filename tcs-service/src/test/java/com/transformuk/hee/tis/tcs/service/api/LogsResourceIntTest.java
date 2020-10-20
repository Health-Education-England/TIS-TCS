package com.transformuk.hee.tis.tcs.service.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.api.vm.LoggerVM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the LogsResource REST controller.
 *
 * @see LogsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LogsResourceIntTest {

  private MockMvc restLogsMockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    LogsResource logsResource = new LogsResource();
    this.restLogsMockMvc = MockMvcBuilders
        .standaloneSetup(logsResource)
        .build();
  }

  @Test
  public void getAllLogs() throws Exception {
    restLogsMockMvc.perform(get("/management/logs"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
  }

  @Test
  public void changeLogs() throws Exception {
    LoggerVM logger = new LoggerVM();
    logger.setLevel("INFO");
    logger.setName("ROOT");

    restLogsMockMvc.perform(put("/management/logs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(logger)))
        .andExpect(status().isNoContent());
  }
}
