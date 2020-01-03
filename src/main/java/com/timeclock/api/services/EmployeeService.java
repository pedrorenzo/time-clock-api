package com.timeclock.api.services;

import java.util.Optional;

import com.timeclock.api.entities.Employee;

public interface EmployeeService {

	/**
	 * Persists an employee in the database.
	 * 
	 * @param employee
	 * @return Employee
	 */
	Employee persist(final Employee employee);

	/**
	 * Find and returns an employee based on the CPF.
	 * 
	 * @param cpf
	 * @return Optional<Employee>
	 */
	Optional<Employee> findByCpf(final String cpf);

	/**
	 * Find and returns an employee based on the email.
	 * 
	 * @param email
	 * @return Optional<Employee>
	 */
	Optional<Employee> findByEmail(final String email);

	/**
	 * Find and returns an employee based on the id.
	 * 
	 * @param id
	 * @return Optional<Employee>
	 */
	Optional<Employee> findById(final Long id);

}
