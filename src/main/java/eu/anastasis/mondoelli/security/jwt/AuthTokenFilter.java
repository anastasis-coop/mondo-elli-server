package eu.anastasis.mondoelli.security.jwt;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import eu.anastasis.mondoelli.security.UserDetailsImpl;
import eu.anastasis.mondoelli.security.dto.LoginRequest;
import eu.anastasis.mondoelli.security.exceptions.InvalidUsernameOrPasswordException;
import eu.anastasis.mondoelli.security.services.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String headerAuth = request.getHeader("Authorization");
			String basic = parseBasic(headerAuth);
			if (basic != null) {
				processBasic(request, basic);
			} else {
				String jwt = parseJwt(headerAuth);
				if (jwt != null) {
					processJwt(request, jwt);
				} else {
					SecurityContextHolder.getContext().setAuthentication(null);
				}
			}
		} catch (Exception e) {
			logger.warn("Cannot set user authentication: {}", e);
		}
		filterChain.doFilter(request, response);
	}

	private String parseBasic(String headerAuth) {
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Basic ")) {
			return headerAuth.substring(6, headerAuth.length());
		}
		return null;
	}

	private String parseJwt(String headerAuth) {
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}

	private void processBasic(HttpServletRequest request, String basic) {
		String usernamePassword = new String(Base64.getDecoder().decode(basic));
		int separatorIndex = usernamePassword.indexOf(':');
		LoginRequest login = new LoginRequest();
		login.setUsername(usernamePassword.substring(0, separatorIndex));
		login.setPassword(usernamePassword.substring(separatorIndex + 1));
		try {
			UserDetails basicUser = null;
			try {
				logger.debug("Verifica nome utente: " + login.getUsername());
				basicUser = userDetailsService.loadUserByUsername(login.getUsername());
			} catch (UsernameNotFoundException e) {
				throw new InvalidUsernameOrPasswordException("Nome utente non valido");
			}
			final Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(basicUser, login, basicUser.getAuthorities()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}

	private void processJwt(HttpServletRequest request, String jwt) {
		UsernamePasswordAuthenticationToken authentication = null;
		if (jwtUtils.validateJwtToken(jwt)) {
			String idStr = jwtUtils.getIdFromJwtToken(jwt);
			Integer id = Integer.parseInt(idStr);
			UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserById(id);
			authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
