package com.timeclock.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.timeclock.api.enums.ProfileEnum;

@Entity
@Table(name = "employee")
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String email;
	private String password;
	private String cpf;
	private BigDecimal valuePerHour;
	private Float hoursWorkedPerDay;
	private Float hoursLunchPerDay;
	private ProfileEnum profile;
	private Date createDate;
	private Date updateDate;
	private Company company;
	private List<TimeEntry> timeEntries;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "cpf", nullable = false)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Column(name = "value_per_hour")
	public BigDecimal getValuePerHour() {
		return valuePerHour;
	}

	@Transient
	public Optional<BigDecimal> getValuePerHourOpt() {
		return Optional.ofNullable(valuePerHour);
	}

	public void setValuePerHour(BigDecimal valuePerHour) {
		this.valuePerHour = valuePerHour;
	}

	@Column(name = "hours_worked_per_day")
	public Float getHoursWorkedPerDay() {
		return hoursWorkedPerDay;
	}

	@Transient
	public Optional<Float> getHoursWorkedPerDayOpt() {
		return Optional.ofNullable(hoursWorkedPerDay);
	}

	public void setHoursWorkedPerDay(Float hoursWorkedPerDay) {
		this.hoursWorkedPerDay = hoursWorkedPerDay;
	}

	@Column(name = "hours_lunch_per_day")
	public Float getHoursLunchPerDay() {
		return hoursLunchPerDay;
	}

	@Transient
	public Optional<Float> getHoursLunchPerDayOpt() {
		return Optional.ofNullable(hoursLunchPerDay);
	}

	public void setHoursLunchPerDay(Float hoursLunchPerDay) {
		this.hoursLunchPerDay = hoursLunchPerDay;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "profile", nullable = false)
	public ProfileEnum getProfile() {
		return profile;
	}

	public void setProfile(ProfileEnum profile) {
		this.profile = profile;
	}

	@Column(name = "create_date", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "update_date", nullable = false)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<TimeEntry> getTimeEntries() {
		return timeEntries;
	}

	public void setTimeEntries(List<TimeEntry> timeEntries) {
		this.timeEntries = timeEntries;
	}

	@PreUpdate
	public void preUpdate() {
		updateDate = new Date();
	}

	@PrePersist
	public void prePersist() {
		final Date date = new Date();
		createDate = date;
		updateDate = date;
	}

}
