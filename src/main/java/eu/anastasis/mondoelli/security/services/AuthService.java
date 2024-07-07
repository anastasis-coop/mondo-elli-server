package eu.anastasis.mondoelli.security.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.account.AccountRepository;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.security.UserDetailsImpl;
import eu.anastasis.mondoelli.security.dto.JwtResponse;
import eu.anastasis.mondoelli.security.exceptions.InvalidPasswordTokenException;
import eu.anastasis.mondoelli.security.jwt.JwtUtils;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	PasswordTokenService passwordTokenService;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	JwtUtils jwtUtils;

	public UserDetailsImpl loadUserById(Integer id) {
		return (UserDetailsImpl) userDetailsServiceImpl.loadUserById(id);
	}

	public UserDetailsImpl authenticate(HttpServletRequest request, String token) throws AuthenticationException {
		UserDetailsImpl jwtUser = null;
		if (this.validatePasswordToken(token)) {
			Integer userId = passwordTokenService.getIdFromToken(token);
			try {
				jwtUser = loadUserById(userId);
			} catch (NotFoundException e) {
				throw new InvalidPasswordTokenException("Password token specifica un id inesistente: " + userId);
			}
			if (jwtUser.canLogin()) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUser, null,
						jwtUser.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				logger.debug("authenticated user " + jwtUser.getUsername() + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				throw new InvalidPasswordTokenException("Account disabilitato");
			}
		} else {
			throw new InvalidPasswordTokenException("Password token non valido: " + token);
		}
		return jwtUser;
	}

	private boolean validatePasswordToken(String passwordToken) {
		Integer userId = passwordTokenService.getIdFromToken(passwordToken);
		if (userId != null) {
			Optional<Account> accountOpt = accountRepository.findById(userId);
			if (accountOpt.isPresent()) {
				Account account = accountOpt.get();
				return passwordTokenService.validateToken(passwordToken, userId, account.getDataCreazionePassword());
			}
		}
		return false;
	}

	public UserDetailsImpl getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
			return null;
		}
		UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
		return user;
	}

	public boolean getImpersonate() {
		return getLoggedUser().getImpersonate();
	}

	public JwtResponse generateJwtResponse(UserDetailsImpl userDetails, boolean impersonate) {
		String jwtToken = jwtUtils.generateJwtToken(userDetails, impersonate);
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setToken(jwtToken);
		jwtResponse.setId(userDetails.getId());
		jwtResponse.setUsername(userDetails.getUsername());
		jwtResponse.setRoles(roles);
		jwtResponse.setPrivacy(userDetails.getPrivacy());
		return jwtResponse;
	}

	public boolean checkPasswordHash(String password, String hash) {
		return passwordEncoder.matches(password, hash);
	}

}
