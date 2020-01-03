package com.timeclock.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timeclock.api.entities.Employee;
import com.timeclock.api.repositories.EmployeeRepository;
import com.timeclock.api.services.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee persist(final Employee employee) {
		LOGGER.info("Persisting employee: {}", employee);
		return this.employeeRepository.save(employee);
	}

	@Override
	public Optional<Employee> findByCpf(final String cpf) {
		LOGGER.info("Finding employee by CPF {}", cpf);
		return Optional.ofNullable(this.employeeRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Employee> findByEmail(final String email) {
		LOGGER.info("Finding employee by email {}", email);
		return Optional.ofNullable(this.employeeRepository.findByEmail(email));
	}

	@Override
	public Optional<Employee> findById(final Long id) {
		LOGGER.info("Finding employee by id {}", id);
		return this.employeeRepository.findById(id);
	}

}
