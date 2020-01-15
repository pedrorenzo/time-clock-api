package com.timeclock.api.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_ROLE = "role";
	static final String CLAIM_KEY_AUDIENCE = "audience";
	static final String CLAIM_KEY_CREATED = "created";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	/**
	 * Get the username (email) inside the JWT Token.
	 * 
	 * @param token
	 * @return String
	 */
	public String getUsernameFromToken(final String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (final Exception e) {
			username = null;
		}
		return username;
	}

	/**
	 * Returns the expiration date from a JWT Token.
	 * 
	 * @param token
	 * @return Date
	 */
	public Date getExpirationDateFromToken(final String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (final Exception e) {
			expiration = null;
		}
		return expiration;
	}

	/**
	 * Create a new token (refresh).
	 * 
	 * @param token
	 * @return String
	 */
	public String refreshToken(final String token) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = generateToken(claims);
		} catch (final Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	/**
	 * Verify and returns if a JWT Token is valid.
	 * 
	 * @param token
	 * @return boolean
	 */
	public boolean validateToken(final String token) {
		return !isTokenExpired(token);
	}

	/**
	 * Returns a new JWT Token based in the user data.
	 * 
	 * @param userDetails
	 * @return String
	 */
	public String getToken(final UserDetails userDetails) {
		final Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		userDetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
		claims.put(CLAIM_KEY_CREATED, new Date());

		return generateToken(claims);
	}

	/**
	 * Parse the JWT Token to extract the info inside of its body.
	 * 
	 * @param token
	 * @return Claims
	 */
	private Claims getClaimsFromToken(final String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	/**
	 * Returns the expiration date based in the actual date.
	 * 
	 * @return Date
	 */
	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	/**
	 * Verify if a JWT Token is expired.
	 * 
	 * @param token
	 * @return boolean
	 */
	private boolean isTokenExpired(final String token) {
		final Date expirationDate = this.getExpirationDateFromToken(token);
		if (expirationDate == null) {
			return false;
		}
		return expirationDate.before(new Date());
	}

	/**
	 * Generate a new JWT Token with the data (claims) received.
	 * 
	 * @param claims
	 * @return String
	 */
	private String generateToken(final Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

}
