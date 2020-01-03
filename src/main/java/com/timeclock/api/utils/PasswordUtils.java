package com.timeclock.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUtils.class);

	private PasswordUtils() {
		super();
	}

	/**
	 * Generate a hash using BCrypt.
	 * 
	 * @param password
	 * @return String
	 */
	public static String generateBCrypt(final String password) {
		if (password == null) {
			return password;
		}

		LOGGER.info("Generating a hash using BCrypt.");
		final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		return bCryptEncoder.encode(password);
	}

}
