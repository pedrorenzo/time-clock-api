package com.timeclock.api.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.timeclock.api.entities.Employee;
import com.timeclock.api.security.JwtUserFactory;
import com.timeclock.api.services.EmployeeService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private EmployeeService employeeService;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Optional<Employee> employee = employeeService.findByEmail(username);

		if (employee.isPresent()) {
			return JwtUserFactory.create(employee.get());
		}

		throw new UsernameNotFoundException("Email not found.");
	}

}
