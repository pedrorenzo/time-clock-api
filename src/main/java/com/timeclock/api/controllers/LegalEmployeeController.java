package com.timeclock.api.controllers;

import java.security.NoSuchAlgorithmException;

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

import com.timeclock.api.dtos.LegalEmployeeRegisterDto;
import com.timeclock.api.entities.Company;
import com.timeclock.api.entities.Employee;
import com.timeclock.api.enums.ProfileEnum;
import com.timeclock.api.response.Response;
import com.timeclock.api.services.CompanyService;
import com.timeclock.api.services.EmployeeService;
import com.timeclock.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/legal-employee")
@CrossOrigin(origins = "*")
public class LegalEmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LegalEmployeeController.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CompanyService companyService;

	/**
	 * Register a legal employee.
	 * 
	 * @param legalEmployeeRegisterDto
	 * @param result
	 * @return ResponseEntity<Response<LegalEmployeeRegisterDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<LegalEmployeeRegisterDto>> register(
			@Valid @RequestBody final LegalEmployeeRegisterDto legalEmployeeRegisterDto, final BindingResult result)
			throws NoSuchAlgorithmException {
		LOGGER.info("Registering Legal Employee: {}", legalEmployeeRegisterDto.toString());

		final Response<LegalEmployeeRegisterDto> response = new Response<LegalEmployeeRegisterDto>();

		validateExistingData(legalEmployeeRegisterDto, result);
		final Company company = this.convertDtoToCompany(legalEmployeeRegisterDto);
		final Employee employee = this.convertDtoToEmployee(legalEmployeeRegisterDto, result);

		if (result.hasErrors()) {
			LOGGER.error("Error validating legal employee data to be registered: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.companyService.persist(company);
		employee.setCompany(company);
		this.employeeService.persist(employee);

		response.setData(this.convertEmployeeToDto(employee));
		return ResponseEntity.ok(response);
	}

	/**
	 * Validate if the company or the employee already exists.
	 * 
	 * @param legalEmployeeRegisterDto
	 * @param result
	 */
	private void validateExistingData(final LegalEmployeeRegisterDto legalEmployeeRegisterDto,
			final BindingResult result) {
		this.companyService.findByCnpj(legalEmployeeRegisterDto.getCnpj())
				.ifPresent(comp -> result.addError(new ObjectError("company", "Company already registered.")));

		this.employeeService.findByCpf(legalEmployeeRegisterDto.getCpf())
				.ifPresent(emp -> result.addError(new ObjectError("employee", "CPF already registered.")));

		this.employeeService.findByEmail(legalEmployeeRegisterDto.getEmail())
				.ifPresent(emp -> result.addError(new ObjectError("employee", "Email already registered.")));
	}

	/**
	 * Convert the dto into a company.
	 * 
	 * @param legalEmployeeRegisterDto
	 * @return Company
	 */
	private Company convertDtoToCompany(final LegalEmployeeRegisterDto legalEmployeeRegisterDto) {
		final Company company = new Company();
		company.setCnpj(legalEmployeeRegisterDto.getCnpj());
		company.setCompanyName(legalEmployeeRegisterDto.getCompanyName());

		return company;
	}

	/**
	 * Convert the dto into a employee.
	 * 
	 * @param legalEmployeeRegisterDto
	 * @param result
	 * @return Employee
	 * @throws NoSuchAlgorithmException
	 */
	private Employee convertDtoToEmployee(final LegalEmployeeRegisterDto legalEmployeeRegisterDto,
			final BindingResult result) throws NoSuchAlgorithmException {
		final Employee employee = new Employee();
		employee.setName(legalEmployeeRegisterDto.getName());
		employee.setEmail(legalEmployeeRegisterDto.getEmail());
		employee.setCpf(legalEmployeeRegisterDto.getCpf());
		employee.setProfile(ProfileEnum.ROLE_ADMIN);
		employee.setPassword(PasswordUtils.generateBCrypt(legalEmployeeRegisterDto.getPassword()));

		return employee;
	}

	/**
	 * Convert the employee into dto.
	 * 
	 * @param employee
	 * @return CadastroPJDto
	 */
	private LegalEmployeeRegisterDto convertEmployeeToDto(final Employee employee) {
		final LegalEmployeeRegisterDto legalEmployeeRegisterDto = new LegalEmployeeRegisterDto();
		legalEmployeeRegisterDto.setId(employee.getId());
		legalEmployeeRegisterDto.setName(employee.getName());
		legalEmployeeRegisterDto.setEmail(employee.getEmail());
		legalEmployeeRegisterDto.setCpf(employee.getCpf());
		legalEmployeeRegisterDto.setCompanyName(employee.getCompany().getCompanyName());
		legalEmployeeRegisterDto.setCnpj(employee.getCompany().getCnpj());

		return legalEmployeeRegisterDto;
	}

}
