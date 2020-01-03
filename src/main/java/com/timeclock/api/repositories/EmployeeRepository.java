package com.timeclock.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.timeclock.api.entities.Employee;

@Transactional(readOnly = true)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByCpf(final String cpf);

	Employee findByEmail(final String email);

	Employee findByCpfOrEmail(final String cpf, final String email);
}
