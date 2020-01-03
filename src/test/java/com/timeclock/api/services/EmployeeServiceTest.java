package com.timeclock.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.timeclock.api.entities.Employee;
import com.timeclock.api.repositories.EmployeeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeServiceTest {

	@MockBean
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeService employeeService;

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.employeeRepository.save(Mockito.any(Employee.class))).willReturn(new Employee());
		BDDMockito.given(this.employeeRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Employee()));
		BDDMockito.given(this.employeeRepository.findByEmail(Mockito.anyString())).willReturn(new Employee());
		BDDMockito.given(this.employeeRepository.findByCpf(Mockito.anyString())).willReturn(new Employee());
	}

	@Test
	public void testPersistEmployee() {
		final Employee employee = this.employeeService.persist(new Employee());
		assertNotNull(employee);
	}

	@Test
	public void testFindEmployeeById() {
		final Optional<Employee> employee = this.employeeService.findById(1L);
		assertTrue(employee.isPresent());
	}

	@Test
	public void testFindEmployeeByEmail() {
		final Optional<Employee> employee = this.employeeService.findByEmail("email@email.com");
		assertTrue(employee.isPresent());
	}

	@Test
	public void testFindEmployeeByCpf() {
		final Optional<Employee> employee = this.employeeService.findByCpf("24291173474");
		assertTrue(employee.isPresent());
	}

}
