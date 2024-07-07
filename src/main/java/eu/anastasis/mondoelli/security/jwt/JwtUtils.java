package eu.anastasis.mondoelli.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import eu.anastasis.mondoelli.security.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {

	private static final String IMPERSONATE = "impersonate";

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${application.security.jwtSecret}")
	private String jwtSecret;

	@Value("${application.security.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {
		return generateJwtToken((UserDetailsImpl) authentication.getPrincipal(), false);
	}

	public String generateJwtToken(UserDetailsImpl userPrincipal, boolean impersonate) {
		return Jwts.builder()
				.setId(userPrincipal.getId().toString())
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.claim(IMPERSONATE, impersonate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getIdFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getId();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.debug("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.debug("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.debug("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.debug("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.debug("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}

}
