package com.timeclock.api.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class JwtAuthenticationDto {

	private String email;
	private String password;

	@NotEmpty(message = "Email can not be empty.")
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

	@Override
	public String toString() {
		return "JwtAuthenticationRequestDto [email=" + email + ", password=" + password + "]";
	}

}
