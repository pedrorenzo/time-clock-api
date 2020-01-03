package com.timeclock.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeclock.api.entities.Company;
import com.timeclock.api.repositories.CompanyRepository;
import com.timeclock.api.services.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public Optional<Company> findByCnpj(final String cnpj) {
		LOGGER.info("Finding a company for the CNPJ {}", cnpj);
		return Optional.ofNullable(companyRepository.findByCnpj(cnpj));
	}

	@Override
	public Company persist(final Company company) {
		LOGGER.info("Persisting company: {}", company);
		return this.companyRepository.save(company);
	}

}
