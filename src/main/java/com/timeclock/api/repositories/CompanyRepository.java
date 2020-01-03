package com.timeclock.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.timeclock.api.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Transactional(readOnly = true)
	Company findByCnpj(final String cnpj);

}
