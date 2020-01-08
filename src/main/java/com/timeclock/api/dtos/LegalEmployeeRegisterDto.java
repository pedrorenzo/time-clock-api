package com.timeclock.api.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class LegalEmployeeRegisterDto {

	private Long id;
	private String name;
	private String email;
	private String password;
	private String cpf;
	private String companyName;
	private String cnpj;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@NotEmpty(message = "Name can not be empty.")
	@Length(min = 3, max = 200, message = "Name must contain between 3 and 200 characters.")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotEmpty(message = "Email can not be empty.")
	@Length(min = 5, max = 200, message = "Email must contain between 5 and 200 characters.")
	@Email(message = "Invalid email.")
	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@NotEmpty(message = "Password can not be empty.")
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@NotEmpty(message = "CPF can not be empty.")
	@CPF(message = "Invalid CPF.")
	public String getCpf() {
		return cpf;
	}

	public void setCpf(final String cpf) {
		this.cpf = cpf;
	}

	@NotEmpty(message = "Company name can not be empty.")
	@Length(min = 5, max = 200, message = "Company name must contain between 5 and 200 characters.")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	@NotEmpty(message = "CNPJ can not be empty.")
	@CNPJ(message = "Invalid CNPJ.")
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(final String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public String toString() {
		return "LegalEmployeeRegisterDto [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", cpf=" + cpf + ", companyName=" + companyName + ", cnpj=" + cnpj + "]";
	}

}
