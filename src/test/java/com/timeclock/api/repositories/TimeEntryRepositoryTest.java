package com.timeclock.api.repositories;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.timeclock.api.entities.Company;
import com.timeclock.api.entities.Employee;
import com.timeclock.api.entities.TimeEntry;
import com.timeclock.api.enums.ProfileEnum;
import com.timeclock.api.enums.TypeEnum;
import com.timeclock.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TimeEntryRepositoryTest {

	private Long employeeId;

	@Autowired
	private TimeEntryRepository timeEntryRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Before
	public void setUp() throws Exception {
		final Company company = this.companyRepository.save(getCompanyData());

		final Employee employee = this.employeeRepository.save(getEmployeeData(company));
		this.employeeId = employee.getId();

		this.timeEntryRepository.save(getTimeEntryData(employee));
		this.timeEntryRepository.save(getTimeEntryData(employee));
	}

	@After
	public void tearDown() throws Exception {
		this.companyRepository.deleteAll();
	}

	@Test
	public void testFindTimeEntryByEmployeeId() {
		final List<TimeEntry> timeEntries = this.timeEntryRepository.findByEmployeeId(employeeId);
		assertEquals(2, timeEntries.size());
	}

	@Test
	public void testFindTimeEntryByEmployeeIdPaginated() {
		final Page<TimeEntry> lancamentos = this.timeEntryRepository.findByEmployeeId(employeeId,
				PageRequest.of(0, 10));
		assertEquals(2, lancamentos.getTotalElements());
	}

	private TimeEntry getTimeEntryData(final Employee employee) {
		final TimeEntry timeEntry = new TimeEntry();
		timeEntry.setDate(new Date());
		timeEntry.setType(TypeEnum.START_LUNCH);
		timeEntry.setEmployee(employee);
		return timeEntry;
	}

	private Employee getEmployeeData(final Company company) throws NoSuchAlgorithmException {
		final Employee employee = new Employee();
		employee.setName("Employee Name");
		employee.setProfile(ProfileEnum.ROLE_USER);
		employee.setPassword(PasswordUtils.generateBCrypt("123456"));
		employee.setCpf("24291173474");
		employee.setEmail("email@email.com");
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
