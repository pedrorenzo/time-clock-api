package com.timeclock.api.dtos;

import java.util.Optional;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class IndividualEmployeeUpdateDto {

	private Long id;
	private String name;
	private String email;
	private Optional<String> password = Optional.empty();
	private Optional<String> valuePerHour = Optional.empty();
	private Optional<String> hoursWorkedPerDay = Optional.empty();
	private Optional<String> hoursLunchPerDay = Optional.empty();

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

	public Optional<String> getPassword() {
		return password;
	}

	public void setPassword(final Optional<String> password) {
		this.password = password;
	}

	public Optional<String> getValuePerHour() {
		return valuePerHour;
	}

	public void setValuePerHour(final Optional<String> valuePerHour) {
		this.valuePerHour = valuePerHour;
	}

	public Optional<String> getHoursWorkedPerDay() {
		return hoursWorkedPerDay;
	}

	public void setHoursWorkedPerDay(final Optional<String> hoursWorkedPerDay) {
		this.hoursWorkedPerDay = hoursWorkedPerDay;
	}

	public Optional<String> getHoursLunchPerDay() {
		return hoursLunchPerDay;
	}

	public void setHoursLunchPerDay(final Optional<String> hoursLunchPerDay) {
		this.hoursLunchPerDay = hoursLunchPerDay;
	}

	@Override
	public String toString() {
		return "IndividualEmployeeUpdateDto [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", valuePerHour=" + valuePerHour + ", hoursWorkedPerDay=" + hoursWorkedPerDay + ", hoursLunchPerDay="
				+ hoursLunchPerDay + "]";
	}

}
