package com.timeclock.api.dtos;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;

public class TimeEntryDto {

	private Optional<Long> id = Optional.empty();
	private String date;
	private String type;
	private String description;
	private String location;
	private Long employeeId;

	public Optional<Long> getId() {
		return id;
	}

	public void setId(final Optional<Long> id) {
		this.id = id;
	}

	@NotEmpty(message = "Date can not be empty.")
	public String getDate() {
		return date;
	}

	public void setDate(final String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(final Long employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public String toString() {
		return "TimeEntryDto [id=" + id + ", date=" + date + ", type=" + type + ", description=" + description
				+ ", location=" + location + ", employeeId=" + employeeId + "]";
	}

}
