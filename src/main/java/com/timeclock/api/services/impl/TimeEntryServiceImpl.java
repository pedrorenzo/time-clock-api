package com.timeclock.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.timeclock.api.entities.TimeEntry;
import com.timeclock.api.repositories.TimeEntryRepository;
import com.timeclock.api.services.TimeEntryService;

@Service
public class TimeEntryServiceImpl implements TimeEntryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeEntryServiceImpl.class);

	@Autowired
	private TimeEntryRepository timeEntryRepository;

	@Override
	public Page<TimeEntry> findByEmployeeId(final Long employeeId, final PageRequest pageRequest) {
		LOGGER.info("Finding time entries by employee id {}", employeeId);
		return this.timeEntryRepository.findByEmployeeId(employeeId, pageRequest);
	}

	@Override
	@Cacheable("timeEntryById")
	public Optional<TimeEntry> findById(final Long id) {
		LOGGER.info("Finding time entry by id {}", id);
		return this.timeEntryRepository.findById(id);
	}

	@Override
	@CachePut("timeEntryById")
	public TimeEntry persist(final TimeEntry timeEntry) {
		LOGGER.info("Persisting the time entry: {}", timeEntry);
		return this.timeEntryRepository.save(timeEntry);
	}

	@Override
	public void remove(final Long id) {
		LOGGER.info("Removing the time entry id {}", id);
		this.timeEntryRepository.deleteById(id);
	}

}
