package com.timeclock.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timeclock.api.dtos.IndividualEmployeeRegisterDto;
import com.timeclock.api.entities.Company;
import com.timeclock.api.entities.Employee;
import com.timeclock.api.enums.ProfileEnum;
import com.timeclock.api.response.Response;
import com.timeclock.api.services.CompanyService;
import com.timeclock.api.services.EmployeeService;
import com.timeclock.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/individual-employees")
@CrossOrigin(origins = "*")
public class IndividualEmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndividualEmployeeController.class);

	@Autowired
	private CompanyService companyService;

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Register an individual employee.
	 * 
	 * @param individualEmployeeRegisterDto
	 * @param result
	 * @return ResponseEntity<Response<IndividualEmployeeRegisterDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<IndividualEmployeeRegisterDto>> register(
			@Valid @RequestBody final IndividualEmployeeRegisterDto individualEmployeeRegisterDto,
			final BindingResult result) throws NoSuchAlgorithmException {
		LOGGER.info("Registering Individual Employee: {}", individualEmployeeRegisterDto.toString());

		final Response<IndividualEmployeeRegisterDto> response = new Response<IndividualEmployeeRegisterDto>();

		validateExistingData(individualEmployeeRegisterDto, result);
		if (result.hasErrors()) {
			LOGGER.error("Error validating individual employee data to be registered: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		final Employee employee = this.convertDtoToEmployee(individualEmployeeRegisterDto, result);

		final Optional<Company> company = this.companyService.findByCnpj(individualEmployeeRegisterDto.getCnpj());
		company.ifPresent(comp -> employee.setCompany(comp));
		this.employeeService.persist(employee);

		response.setData(this.convertEmployeeToDto(employee));
		return ResponseEntity.ok(response);
	}

	/**
	 * Validate if the company exists and if the employee e-mail or CPF are already registered.
	 * 
	 * @param individualEmployeeRegisterDto
	 * @param result
	 */
	private void validateExistingData(final IndividualEmployeeRegisterDto individualEmployeeRegisterDto,
			final BindingResult result) {
		final Optional<Company> company = this.companyService.findByCnpj(individualEmployeeRegisterDto.getCnpj());
		if (!company.isPresent()) {
			result.addError(new ObjectError("company", "Company not registered."));
		}

		this.employeeService.findByCpf(individualEmployeeRegisterDto.getCpf())
				.ifPresent(emp -> result.addError(new ObjectError("employee", "CPF already registered.")));

		this.employeeService.findByEmail(individualEmployeeRegisterDto.getEmail())
				.ifPresent(emp -> result.addError(new ObjectError("employee", "Email already registered.")));
	}

	/**
	 * Convert the dto into a employee.
	 * 
	 * @param individualEmployeeRegisterDto
	 * @param result
	 * @return Employee
	 * @throws NoSuchAlgorithmException
	 */
	private Employee convertDtoToEmployee(final IndividualEmployeeRegisterDto individualEmployeeRegisterDto,
			final BindingResult result) throws NoSuchAlgorithmException {
		final Employee employee = new Employee();
		employee.setName(individualEmployeeRegisterDto.getName());
		employee.setEmail(individualEmployeeRegisterDto.getEmail());
		employee.setCpf(individualEmployeeRegisterDto.getCpf());
		employee.setProfile(ProfileEnum.ROLE_USER);
		employee.setPassword(PasswordUtils.generateBCrypt(individualEmployeeRegisterDto.getPassword()));
		individualEmployeeRegisterDto.getHoursLunchPerDay()
				.ifPresent(lunchHours -> employee.setHoursLunchPerDay(Float.valueOf(lunchHours)));
		individualEmployeeRegisterDto.getHoursWorkedPerDay()
				.ifPresent(workedHours -> employee.setHoursWorkedPerDay(Float.valueOf(workedHours)));
		individualEmployeeRegisterDto.getValuePerHour()
				.ifPresent(valuePerHour -> employee.setValuePerHour(new BigDecimal(valuePerHour)));

		return employee;
	}

	/**
	 * Convert the employee into dto.
	 * 
	 * @param employee
	 * @return IndividualEmployeeRegisterDto
	 */
	private IndividualEmployeeRegisterDto convertEmployeeToDto(final Employee employee) {
		final IndividualEmployeeRegisterDto cadastroPFDto = new IndividualEmployeeRegisterDto();
		cadastroPFDto.setId(employee.getId());
		cadastroPFDto.setName(employee.getName());
		cadastroPFDto.setEmail(employee.getEmail());
		cadastroPFDto.setCpf(employee.getCpf());
		cadastroPFDto.setCnpj(employee.getCompany().getCnpj());
		employee.getHoursLunchPerDayOpt()
				.ifPresent(lunchHours -> cadastroPFDto.setHoursLunchPerDay(Optional.of(Float.toString(lunchHours))));
		employee.getHoursWorkedPerDayOpt()
				.ifPresent(workHours -> cadastroPFDto.setHoursWorkedPerDay(Optional.of(Float.toString(workHours))));
		employee.getValuePerHourOpt()
				.ifPresent(valuePerHour -> cadastroPFDto.setValuePerHour(Optional.of(valuePerHour.toString())));

		return cadastroPFDto;
	}

}
