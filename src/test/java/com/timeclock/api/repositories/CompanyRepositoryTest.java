package com.timeclock.api.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.timeclock.api.entities.Company;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CompanyRepositoryTest {

	private static final String CNPJ = "51463645000100";

	@Autowired
	private CompanyRepository companyRepository;

	@Before
	public void setUp() throws Exception {
		final Company company = new Company();
		company.setCompanyName("Company");
		company.setCnpj(CNPJ);
		this.companyRepository.save(company);
	}

	@After
	public final void tearDown() {
		this.companyRepository.deleteAll();
	}

	@Test
	public void testFindByCnpj() {
		final Company company = this.companyRepository.findByCnpj(CNPJ);
		assertEquals(CNPJ, company.getCnpj());
	}

}
