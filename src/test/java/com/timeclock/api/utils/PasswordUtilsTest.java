package com.timeclock.api.utils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtilsTest {

	private static final String PASSWORD = "123456";
	private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

	@Test
	public void testNullPassword() throws Exception {
		assertNull(PasswordUtils.generateBCrypt(null));
	}

	@Test
	public void testGeneratePasswordHash() throws Exception {
		final String hash = PasswordUtils.generateBCrypt(PASSWORD);
		assertTrue(bCryptEncoder.matches(PASSWORD, hash));
	}

}
