package com.timeclock.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timeclock.api.dtos.TimeEntryDto;
import com.timeclock.api.entities.Employee;
import com.timeclock.api.entities.TimeEntry;
import com.timeclock.api.enums.TypeEnum;
import com.timeclock.api.response.Response;
import com.timeclock.api.services.EmployeeService;
import com.timeclock.api.services.TimeEntryService;

@RestController
@RequestMapping("/api/time-entries")
@CrossOrigin(origins = "*")
public class TimeEntryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeEntryController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private TimeEntryService timeEntryService;

	@Autowired
	private EmployeeService employeeService;

	@Value("${pagination.qtt_per_page}")
	private int qttPerPage;

	/**
	 * Returns the time entries of an employee.
	 * 
	 * @param employeeId
	 * @return ResponseEntity<Response<Page<TimeEntryDto>>>
	 */
	@GetMapping(value = "/employee/{employeeId}")
	public ResponseEntity<Response<Page<TimeEntryDto>>> listByEmployeeId(
			@PathVariable("employeeId") final Long employeeId,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "order", defaultValue = "id") final String order,
			@RequestParam(value = "direction", defaultValue = "DESC") final String direction) {
		LOGGER.info("Finding time entries by employee id: {}, page: {}", employeeId, page);
		final Response<Page<TimeEntryDto>> response = new Response<Page<TimeEntryDto>>();

		final Page<TimeEntry> timeEntries = this.timeEntryService.findByEmployeeId(employeeId,
				PageRequest.of(page, this.qttPerPage, Direction.valueOf(direction), order));
		final Page<TimeEntryDto> timeEntriesDto = timeEntries.map(timeEntry -> this.convertTimeEntryToDto(timeEntry));

		response.setData(timeEntriesDto);
		return ResponseEntity.ok(response);
	}

	/**
	 * Returns a time entry by ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<TimeEntryDto>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<TimeEntryDto>> listById(@PathVariable("id") final Long id) {
		LOGGER.info("Finding time entry by ID: {}", id);
		final Response<TimeEntryDto> response = new Response<TimeEntryDto>();
		final Optional<TimeEntry> timeEntry = this.timeEntryService.findById(id);

		if (!timeEntry.isPresent()) {
			LOGGER.info("Time Entry not found by ID: {}", id);
			response.getErrors().add("Time Entry not found by ID " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.convertTimeEntryToDto(timeEntry.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Adding a new time entry.
	 * 
	 * @param timeEntryDto
	 * @param result
	 * @return ResponseEntity<Response<TimeEntryDto>>
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<TimeEntryDto>> add(@Valid @RequestBody final TimeEntryDto timeEntryDto,
			final BindingResult result) throws ParseException {
		LOGGER.info("Adding time entry: {}", timeEntryDto.toString());
		final Response<TimeEntryDto> response = new Response<TimeEntryDto>();
		validateEmployee(timeEntryDto, result);
		TimeEntry timeEntry = this.convertDtoToTimeEntry(timeEntryDto, result);

		if (result.hasErrors()) {
			LOGGER.error("Error validating time entry: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		timeEntry = this.timeEntryService.persist(timeEntry);
		response.setData(this.convertTimeEntryToDto(timeEntry));
		return ResponseEntity.ok(response);
	}

	/**
	 * Update time entry data.
	 * 
	 * @param id
	 * @param timeEntryDto
	 * @param result
	 * @return ResponseEntity<Response<TimeEntryDto>>
	 * @throws ParseException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<TimeEntryDto>> update(@PathVariable("id") final Long id,
			@Valid @RequestBody final TimeEntryDto timeEntryDto, final BindingResult result) throws ParseException {
		LOGGER.info("Updating time entry: {}", timeEntryDto.toString());
		final Response<TimeEntryDto> response = new Response<TimeEntryDto>();
		validateEmployee(timeEntryDto, result);
		timeEntryDto.setId(Optional.of(id));
		TimeEntry timeEntry = this.convertDtoToTimeEntry(timeEntryDto, result);

		if (result.hasErrors()) {
			LOGGER.error("Error validating time entry: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		timeEntry = this.timeEntryService.persist(timeEntry);
		response.setData(this.convertTimeEntryToDto(timeEntry));
		return ResponseEntity.ok(response);
	}

	/**
	 * Removes a time entry by its ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<String>>
	 */
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remove(@PathVariable("id") final Long id) {
		LOGGER.info("Removing time entry: {}", id);
		final Response<String> response = new Response<String>();
		final Optional<TimeEntry> timeEntry = this.timeEntryService.findById(id);

		if (!timeEntry.isPresent()) {
			LOGGER.info("Error removing time entry. Register not found for the id {}.", id);
			response.getErrors().add("Error removing time entry. Register not found for the id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.timeEntryService.remove(id);
		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Validate a employee, verifying if it exists and if its valid.
	 * 
	 * @param timeEntryDto
	 * @param result
	 */
	private void validateEmployee(final TimeEntryDto timeEntryDto, final BindingResult result) {
		if (timeEntryDto.getEmployeeId() == null) {
			result.addError(new ObjectError("employee", "Employee not informed."));
			return;
		}

		LOGGER.info("Validating employee id {}: ", timeEntryDto.getEmployeeId());
		final Optional<Employee> employee = this.employeeService.findById(timeEntryDto.getEmployeeId());
		if (!employee.isPresent()) {
			result.addError(new ObjectError("employee", "Employee not found. Nonexistent ID."));
		}
	}

	/**
	 * Converts a time entry into a DTO.
	 * 
	 * @param timeEntry
	 * @return TimeEntryDto
	 */
	private TimeEntryDto convertTimeEntryToDto(final TimeEntry timeEntry) {
		final TimeEntryDto timeEntryDto = new TimeEntryDto();
		timeEntryDto.setId(Optional.of(timeEntry.getId()));
		timeEntryDto.setDate(this.dateFormat.format(timeEntry.getDate()));
		timeEntryDto.setType(timeEntry.getType().toString());
		timeEntryDto.setDescription(timeEntry.getDescription());
		timeEntryDto.setLocation(timeEntry.getLocation());
		timeEntryDto.setEmployeeId(timeEntry.getEmployee().getId());

		return timeEntryDto;
	}

	/**
	 * Converts a time entry dto into a time entry.
	 * 
	 * @param timeEntryDto
	 * @param result
	 * @return TimeEntry
	 * @throws ParseException
	 */
	private TimeEntry convertDtoToTimeEntry(final TimeEntryDto timeEntryDto, final BindingResult result)
			throws ParseException {
		TimeEntry timeEntry = new TimeEntry();

		if (timeEntryDto.getId().isPresent()) {
			final Optional<TimeEntry> timeEntryFound = this.timeEntryService.findById(timeEntryDto.getId().get());
			if (timeEntryFound.isPresent()) {
				timeEntry = timeEntryFound.get();
			} else {
				result.addError(new ObjectError("timeEntry", "Time Entry not found."));
			}
		} else {
			timeEntry.setEmployee(new Employee());
			timeEntry.getEmployee().setId(timeEntryDto.getEmployeeId());
		}

		timeEntry.setDescription(timeEntryDto.getDescription());
		timeEntry.setLocation(timeEntryDto.getLocation());
		timeEntry.setDate(this.dateFormat.parse(timeEntryDto.getDate()));

		if (EnumUtils.isValidEnum(TypeEnum.class, timeEntryDto.getType())) {
			timeEntry.setType(TypeEnum.valueOf(timeEntryDto.getType()));
		} else {
			result.addError(new ObjectError("type", "Invalid type."));
		}

		return timeEntry;
	}

}
