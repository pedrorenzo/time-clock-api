package com.timeclock.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timeclock.api.dtos.TimeEntryDto;
import com.timeclock.api.entities.Employee;
import com.timeclock.api.entities.TimeEntry;
import com.timeclock.api.enums.TypeEnum;
import com.timeclock.api.services.EmployeeService;
import com.timeclock.api.services.TimeEntryService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TimeEntryControllerTest {

	private static final String BASE_URL = "/api/time-entries/";
	private static final Long EMPLOYEE_ID = 1L;
	private static final Long TIME_ENTRY_ID = 1L;
	private static final String TYPE = TypeEnum.START_WORK.name();
	private static final Date DATE = new Date();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private TimeEntryService timeEntryService;

	@MockBean
	private EmployeeService employeeService;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	@WithMockUser
	public void testRegisterTimeEntry() throws Exception {
		final TimeEntry timeEntry = getTimeEntryData();
		BDDMockito.given(this.employeeService.findById(Mockito.anyLong())).willReturn(Optional.of(new Employee()));
		BDDMockito.given(this.timeEntryService.persist(Mockito.any(TimeEntry.class))).willReturn(timeEntry);

		mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(this.getPostRequestJson())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(TIME_ENTRY_ID)).andExpect(jsonPath("$.data.type").value(TYPE))
				.andExpect(jsonPath("$.data.date").value(this.dateFormat.format(DATE)))
				.andExpect(jsonPath("$.data.employeeId").value(EMPLOYEE_ID)).andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@WithMockUser
	public void testRegisterTimeEntryWithInvalidEmployeeId() throws Exception {
		BDDMockito.given(this.employeeService.findById(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(this.getPostRequestJson())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Employee not found. Nonexistent ID."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@WithMockUser(username = "admin@admin.com", roles = { "ADMIN" })
	public void testRemoveTimeEntry() throws Exception {
		BDDMockito.given(this.timeEntryService.findById(Mockito.anyLong())).willReturn(Optional.of(new TimeEntry()));

		mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + TIME_ENTRY_ID).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	@Ignore
	public void testRemoveTimeEntryAccessForbidden() throws Exception {
		BDDMockito.given(this.timeEntryService.findById(Mockito.anyLong())).willReturn(Optional.of(new TimeEntry()));

		mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + TIME_ENTRY_ID).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	private String getPostRequestJson() throws JsonProcessingException {
		final TimeEntryDto timeEntryDto = new TimeEntryDto();
		timeEntryDto.setId(null);
		timeEntryDto.setDate(this.dateFormat.format(DATE));
		timeEntryDto.setType(TYPE);
		timeEntryDto.setEmployeeId(EMPLOYEE_ID);
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(timeEntryDto);
	}

	private TimeEntry getTimeEntryData() {
		final TimeEntry timeEntry = new TimeEntry();
		timeEntry.setId(TIME_ENTRY_ID);
		timeEntry.setDate(DATE);
		timeEntry.setType(TypeEnum.valueOf(TYPE));
		timeEntry.setEmployee(new Employee());
		timeEntry.getEmployee().setId(EMPLOYEE_ID);
		return timeEntry;
	}

}
