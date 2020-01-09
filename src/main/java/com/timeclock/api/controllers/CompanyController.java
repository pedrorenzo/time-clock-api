package com.timeclock.api.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timeclock.api.dtos.CompanyDto;
import com.timeclock.api.entities.Company;
import com.timeclock.api.response.Response;
import com.timeclock.api.services.CompanyService;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	private CompanyService companyService;

	/**
	 * Return a company based on the CNPJ.
	 * 
	 * @param cnpj
	 * @return ResponseEntity<Response<CompanyDto>>
	 */
	@GetMapping(value = "/cnpj/{cnpj}")
	public ResponseEntity<Response<CompanyDto>> findByCnpj(@PathVariable("cnpj") final String cnpj) {
		LOGGER.info("Finding company by CNPJ: {}", cnpj);
		final Response<CompanyDto> response = new Response<CompanyDto>();
		final Optional<Company> company = companyService.findByCnpj(cnpj);

		if (!company.isPresent()) {
			LOGGER.info("Company not found for CNPJ: {}", cnpj);
			response.getErrors().add("Company not found for CNPJ " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.convertCompanyToDto(company.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Convert the company into a dto.
	 * 
	 * @param company
	 * @return CompanyDto
	 */
	private CompanyDto convertCompanyToDto(final Company company) {
		final CompanyDto companyDto = new CompanyDto();
		companyDto.setId(company.getId());
		companyDto.setCnpj(company.getCnpj());
		companyDto.setCompanyName(company.getCompanyName());

		return companyDto;
	}

}
