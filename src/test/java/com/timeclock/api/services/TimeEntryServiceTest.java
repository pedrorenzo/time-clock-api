package com.timeclock.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.timeclock.api.entities.TimeEntry;
import com.timeclock.api.repositories.TimeEntryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TimeEntryServiceTest {

	@MockBean
	private TimeEntryRepository timeEntryRepository;

	@Autowired
	private TimeEntryService timeEntryService;

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.timeEntryRepository.findByEmployeeId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
				.willReturn(new PageImpl<TimeEntry>(new ArrayList<TimeEntry>()));
		BDDMockito.given(this.timeEntryRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new TimeEntry()));
		BDDMockito.given(this.timeEntryRepository.save(Mockito.any(TimeEntry.class))).willReturn(new TimeEntry());
	}

	@Test
	public void testFindTimeEntryByEmployeeId() {
		final Page<TimeEntry> timeEntry = this.timeEntryService.findByEmployeeId(1L, PageRequest.of(0, 10));
		assertNotNull(timeEntry);
	}

	@Test
	public void testFindTimeEntryById() {
		final Optional<TimeEntry> timeEntry = this.timeEntryService.findById(1L);
		assertTrue(timeEntry.isPresent());
	}

	@Test
	public void testPersistTimeEntry() {
		final TimeEntry timeEntry = this.timeEntryService.persist(new TimeEntry());
		assertNotNull(timeEntry);
	}

}
