package com.timeclock.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.timeclock.api.entities.Company;
import com.timeclock.api.entities.Employee;
import com.timeclock.api.enums.ProfileEnum;
import com.timeclock.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

	private static final String EMAIL = "email@email.com";
	private static final String CPF = "24291173474";

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Before
	public void setUp() throws Exception {
		final Company company = this.companyRepository.save(getCompanyData());
		this.employeeRepository.save(getEmployeeData(company));
	}

	@After
	public final void tearDown() {
		this.companyRepository.deleteAll();
	}

	@Test
	public void testFindEmployeeByEmail() {
		final Employee employee = this.employeeRepository.findByEmail(EMAIL);
		assertEquals(EMAIL, employee.getEmail());
	}

	@Test
	public void testFindEmployeeByCpf() {
		final Employee employee = this.employeeRepository.findByCpf(CPF);
		assertEquals(CPF, employee.getCpf());
	}

	@Test
	public void testFindEmployeeByEmailAndCpf() {
		final Employee employee = this.employeeRepository.findByCpfOrEmail(CPF, EMAIL);
		assertNotNull(employee);
	}

	@Test
	public void testFindEmployeeByEmailOrCpfWithInvalidEmail() {
		final Employee employee = this.employeeRepository.findByCpfOrEmail(CPF, "email@invalido.com");
		assertNotNull(employee);
	}

	@Test
	public void testFindEmployeeByEmailAndCpfWithInvalidCpf() {
		final Employee employee = this.employeeRepository.findByCpfOrEmail("12345678901", EMAIL);
		assertNotNull(employee);
	}

	private Employee getEmployeeData(final Company company) throws NoSuchAlgorithmException {
		final Employee employee = new Employee();
		employee.setName("Example Name");
		employee.setProfile(ProfileEnum.ROLE_USER);
		employee.setPassword(PasswordUtils.generateBCrypt("123456"));
		employee.setCpf(CPF);
		employee.setEmail(EMAIL);
		employee.setCompany(company);
		return employee;
	}

	private Company getCompanyData() {
		final Company company = new Company();
		company.setCompanyName("Example Company");
		company.setCnpj("51463645000100");
		return company;
	}

}
