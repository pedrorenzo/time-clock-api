package com.timeclock.api.services;

import java.util.Optional;

import com.timeclock.api.entities.Company;

public interface CompanyService {

	/**
	 * Returns a company based on the CNPJ.
	 * 
	 * @param cnpj
	 * @return Optional<Company>
	 */
	Optional<Company> findByCnpj(final String cnpj);
	
	/**
	 * Persists a new company in the database.
	 * 
	 * @param company
	 * @return Company
	 */
	Company persist(final Company company);
	
}
