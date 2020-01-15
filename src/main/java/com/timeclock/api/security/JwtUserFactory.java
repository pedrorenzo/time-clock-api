package com.timeclock.api.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.timeclock.api.entities.Employee;
import com.timeclock.api.enums.ProfileEnum;

public class JwtUserFactory {

	private JwtUserFactory() {
		super();
	}

	/**
	 * Converts and generate a JwtUser based on the employee data.
	 * 
	 * @param employee
	 * @return JwtUser
	 */
	public static JwtUser create(final Employee employee) {
		return new JwtUser(employee.getId(), employee.getEmail(), employee.getPassword(),
				mapToGrantedAuthorities(employee.getProfile()));
	}

	/**
	 * Converts the user profile to the format used in Spring Security.
	 * 
	 * @param profileEnum
	 * @return List<GrantedAuthority>
	 */
	private static List<GrantedAuthority> mapToGrantedAuthorities(final ProfileEnum profileEnum) {
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
		return authorities;
	}

}
