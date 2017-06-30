package com.transformuk.hee.tis.tcs.service.api;

import com.transformuk.hee.tis.tcs.TestUtils;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMapper;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import org.apache.commons.codec.net.URLCodec;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProgrammeResource REST controller.
 *
 * @see ProgrammeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProgrammeResourceIntTest {

	private static final Status DEFAULT_STATUS = Status.CURRENT;
	private static final Status UPDATED_STATUS = Status.INACTIVE;

	private static final String DEFAULT_INTREPID_ID= "1234";
	private static final String UPDATED_INTREPID_ID= "4567";

	private static final String DEFAULT_MANAGING_DEANERY = "Health Education England Kent, Surrey and Sussex";
	private static final String UPDATED_MANAGING_DEANERY = "Health Education England North West London";

	private static final String DEFAULT_PROGRAMME_NAME = "AAAAAAAAAA";
	private static final String UPDATED_PROGRAMME_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_PROGRAMME_NUMBER = "AAAAAAAAAA";
	private static final String UPDATED_PROGRAMME_NUMBER = "BBBBBBBBBB";

	private static final String DEFAULT_LEAD_PROVIDER = "AAAAAAAAAA";
	private static final String UPDATED_LEAD_PROVIDER = "BBBBBBBBBB";

	@Autowired
	private ProgrammeRepository programmeRepository;

	@Autowired
	private ProgrammeMapper programmeMapper;

	@Autowired
	private ProgrammeService programmeService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	private MockMvc restProgrammeMockMvc;

	private Programme programme;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Programme createEntity() {
		Programme programme = new Programme()
				.status(DEFAULT_STATUS)
				.intrepidId(DEFAULT_INTREPID_ID)
				.managingDeanery(DEFAULT_MANAGING_DEANERY)
				.programmeName(DEFAULT_PROGRAMME_NAME)
				.programmeNumber(DEFAULT_PROGRAMME_NUMBER)
				.leadProvider(DEFAULT_LEAD_PROVIDER);
		return programme;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ProgrammeResource programmeResource = new ProgrammeResource(programmeService);
		this.restProgrammeMockMvc = MockMvcBuilders.standaloneSetup(programmeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
		TestUtils.mockUserprofile("jamesh", "1-AIIDR8", "1-AIIDWA");
	}

	@Before
	public void initTest() {
		programme = createEntity();
	}

	@Test
	@Transactional
	public void createProgramme() throws Exception {
		int databaseSizeBeforeCreate = programmeRepository.findAll().size();

		// Create the Programme
		ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);
		restProgrammeMockMvc.perform(post("/api/programmes")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
				.andExpect(status().isCreated());

		// Validate the Programme in the database
		List<Programme> programmeList = programmeRepository.findAll();
		assertThat(programmeList).hasSize(databaseSizeBeforeCreate + 1);
		Programme testProgramme = programmeList.get(programmeList.size() - 1);
		assertThat(testProgramme.getStatus()).isEqualTo(DEFAULT_STATUS);
		assertThat(testProgramme.getIntrepidId()).isEqualTo(DEFAULT_INTREPID_ID);
		assertThat(testProgramme.getManagingDeanery()).isEqualTo(DEFAULT_MANAGING_DEANERY);
		assertThat(testProgramme.getProgrammeName()).isEqualTo(DEFAULT_PROGRAMME_NAME);
		assertThat(testProgramme.getProgrammeNumber()).isEqualTo(DEFAULT_PROGRAMME_NUMBER);
		assertThat(testProgramme.getLeadProvider()).isEqualTo(DEFAULT_LEAD_PROVIDER);
	}

	@Test
	@Transactional
	public void createProgrammeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = programmeRepository.findAll().size();

		// Create the Programme with an existing ID
		programme.setId(1L);
		ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);

		// An entity with an existing ID cannot be created, so this API call must fail
		restProgrammeMockMvc.perform(post("/api/programmes")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Programme> programmeList = programmeRepository.findAll();
		assertThat(programmeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllProgrammes() throws Exception {
		// Initialize the database
		programmeRepository.saveAndFlush(programme);

		// Get all the programmeList
		restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
				.andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID.toString())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
				.andExpect(jsonPath("$.[*].managingDeanery").value(hasItem(DEFAULT_MANAGING_DEANERY.toString())))
				.andExpect(jsonPath("$.[*].programmeName").value(hasItem(DEFAULT_PROGRAMME_NAME.toString())))
				.andExpect(jsonPath("$.[*].programmeNumber").value(hasItem(DEFAULT_PROGRAMME_NUMBER.toString())))
				.andExpect(jsonPath("$.[*].leadProvider").value(hasItem(DEFAULT_LEAD_PROVIDER.toString())));
	}

	@Test
	@Transactional
	public void getProgramme() throws Exception {
		// Initialize the database
		programmeRepository.saveAndFlush(programme);

		// Get the programme
		restProgrammeMockMvc.perform(get("/api/programmes/{id}", programme.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(programme.getId().intValue()))
				.andExpect(jsonPath("$.intrepidId").value(DEFAULT_INTREPID_ID.toString()))
				.andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
				.andExpect(jsonPath("$.managingDeanery").value(DEFAULT_MANAGING_DEANERY.toString()))
				.andExpect(jsonPath("$.programmeName").value(DEFAULT_PROGRAMME_NAME.toString()))
				.andExpect(jsonPath("$.programmeNumber").value(DEFAULT_PROGRAMME_NUMBER.toString()))
				.andExpect(jsonPath("$.leadProvider").value(DEFAULT_LEAD_PROVIDER.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingProgramme() throws Exception {
		// Get the programme
		restProgrammeMockMvc.perform(get("/api/programmes/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateProgramme() throws Exception {
		// Initialize the database
		programmeRepository.saveAndFlush(programme);
		int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

		// Update the programme
		Programme updatedProgramme = programmeRepository.findOne(programme.getId());
		updatedProgramme
				.status(UPDATED_STATUS)
				.intrepidId(UPDATED_INTREPID_ID)
				.managingDeanery(UPDATED_MANAGING_DEANERY)
				.programmeName(UPDATED_PROGRAMME_NAME)
				.programmeNumber(UPDATED_PROGRAMME_NUMBER)
				.leadProvider(UPDATED_LEAD_PROVIDER);
		ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(updatedProgramme);

		restProgrammeMockMvc.perform(put("/api/programmes")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
				.andExpect(status().isOk());

		// Validate the Programme in the database
		List<Programme> programmeList = programmeRepository.findAll();
		assertThat(programmeList).hasSize(databaseSizeBeforeUpdate);
		Programme testProgramme = programmeList.get(programmeList.size() - 1);
		assertThat(testProgramme.getStatus()).isEqualTo(UPDATED_STATUS);
		assertThat(testProgramme.getIntrepidId()).isEqualTo(UPDATED_INTREPID_ID);
		assertThat(testProgramme.getManagingDeanery()).isEqualTo(UPDATED_MANAGING_DEANERY);
		assertThat(testProgramme.getProgrammeName()).isEqualTo(UPDATED_PROGRAMME_NAME);
		assertThat(testProgramme.getProgrammeNumber()).isEqualTo(UPDATED_PROGRAMME_NUMBER);
		assertThat(testProgramme.getLeadProvider()).isEqualTo(UPDATED_LEAD_PROVIDER);
	}

	@Test
	@Transactional
	public void updateNonExistingProgramme() throws Exception {
		int databaseSizeBeforeUpdate = programmeRepository.findAll().size();

		// Create the Programme
		ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restProgrammeMockMvc.perform(put("/api/programmes")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(programmeDTO)))
				.andExpect(status().isCreated());

		// Validate the Programme in the database
		List<Programme> programmeList = programmeRepository.findAll();
		assertThat(programmeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void shouldFilterByDeaneries() throws Exception {
		//given
		// Initialize the database
		programmeRepository.saveAndFlush(programme);
		Programme otherDeaneryProgramme = createEntity();
		otherDeaneryProgramme.setManagingDeanery("Health Education England West Midlands");
		programmeRepository.saveAndFlush(otherDeaneryProgramme);

		//when & then
		// Get all the programmeList
		restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(programme.getId().intValue())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
				.andExpect(jsonPath("$.[*].intrepidId").value(hasItem(DEFAULT_INTREPID_ID.toString())))
				.andExpect(jsonPath("$.[*].managingDeanery").value(hasItem(DEFAULT_MANAGING_DEANERY.toString())))
				.andExpect(jsonPath("$.[*].programmeName").value(hasItem(DEFAULT_PROGRAMME_NAME.toString())))
				.andExpect(jsonPath("$.[*].programmeNumber").value(hasItem(DEFAULT_PROGRAMME_NUMBER.toString())))
				.andExpect(jsonPath("$.[*].leadProvider").value(hasItem(DEFAULT_LEAD_PROVIDER.toString())));
	}

	@Test
	@Transactional
	public void shouldTextSearch() throws Exception {
		//given
		// Initialize the database
		programmeRepository.saveAndFlush(programme);
		Programme otherDeaneryProgramme = createEntity();
		otherDeaneryProgramme.setManagingDeanery("Health Education England West Midlands");
		programmeRepository.saveAndFlush(otherDeaneryProgramme);
		Programme otherNameProgramme = createEntity();
		otherNameProgramme.setProgrammeName("other name");
		programmeRepository.saveAndFlush(otherNameProgramme);
		//when & then
		// Get all the programmeList
		restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc&searchQuery=other"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].programmeName").value("other name"));
	}

	@Test
	@Transactional
	public void shouldFilterColumns() throws Exception {
		//given
		// Initialize the database
		programmeRepository.saveAndFlush(programme);
		programmeRepository.saveAndFlush(createEntity());
		Programme otherStatusProgramme = createEntity();
		otherStatusProgramme.setStatus(Status.INACTIVE);
		programmeRepository.saveAndFlush(otherStatusProgramme);

		//when & then
		String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"],\"managingDeanery\":[\"" +
				DEFAULT_MANAGING_DEANERY + "\"]}");
		// Get all the programmeList
		restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc&columnFilters=" +
				colFilters))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].status").value("INACTIVE"));
	}

	@Test
	@Transactional
	public void shouldTextSearchAndFilterColumns() throws Exception {
		//given
		// Initialize the database
		programmeRepository.saveAndFlush(programme);
		Programme otherDeaneryProgramme = createEntity();
		programmeRepository.saveAndFlush(otherDeaneryProgramme);
		Programme otherStatusProgramme = createEntity();
		otherStatusProgramme.setStatus(Status.INACTIVE);
		programmeRepository.saveAndFlush(otherStatusProgramme);
		Programme otherNameProgramme = createEntity();
		otherNameProgramme.setProgrammeName("other name");
		otherNameProgramme.setStatus(Status.INACTIVE);
		programmeRepository.saveAndFlush(otherNameProgramme);
		//when & then
		String colFilters = new URLCodec().encode("{\"status\":[\"INACTIVE\"],\"managingDeanery\":[\"" +
				DEFAULT_MANAGING_DEANERY + "\"]}");
		// Get all the programmeList
		restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc&searchQuery=other&columnFilters=" +
				colFilters))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[*].status").value("INACTIVE"));
	}

	@Test
	public void shouldComplainIfBadRequest() throws Exception {
		//given
		URLCodec codec = new URLCodec();
		String colFilters = codec.encode("{\"status\":[\"malformed json\"");
		//when & then
		// Get all the programmeList
		restProgrammeMockMvc.perform(get("/api/programmes?sort=id,desc&columnFilters=" + colFilters))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Bad request"));
	}

	@Test
	@Transactional
	public void deleteProgramme() throws Exception {
		// Initialize the database
		programmeRepository.saveAndFlush(programme);
		int databaseSizeBeforeDelete = programmeRepository.findAll().size();

		// Get the programme
		restProgrammeMockMvc.perform(delete("/api/programmes/{id}", programme.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Programme> programmeList = programmeRepository.findAll();
		assertThat(programmeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Programme.class);
	}
}
