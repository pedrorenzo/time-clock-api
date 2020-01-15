package com.timeclock.api.security.dto;

public class TokenDto {

	private String token;

	public TokenDto(final String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

}
