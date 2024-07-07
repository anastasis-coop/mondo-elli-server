package eu.anastasis.mondoelli.security.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.account.Role;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.UnauthorizedException;
import eu.anastasis.mondoelli.security.UserDetailsImpl;
import eu.anastasis.mondoelli.security.dto.JwtResponse;
import eu.anastasis.mondoelli.security.dto.LoginRequest;
import eu.anastasis.mondoelli.security.dto.TokenDto;
import eu.anastasis.mondoelli.security.services.AuthService;
import eu.anastasis.mondoelli.utente.Utente;
import eu.anastasis.mondoelli.utente.UtenteRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AuthService authService;

	@Autowired
	UtenteRepository utenteRepository;

	@PostMapping("/login")
	public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request)
			throws UnauthorizedException, ForbiddenException {
		Utente utente = utenteRepository.findByUsernameAndPlainPassword(loginRequest.getUsername(), loginRequest.getPassword())
				.orElseThrow(UnauthorizedException::new);
		String username = utente.getId().toString() + "\0-";
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		if (userDetails.canLogin()) {
			logger.info("Utente " + loginRequest.getUsername() + ": accesso effettuato da IP "
					+ request.getRemoteAddr() + ", invio il token.");
			// Ritorno l'utente loggato
			return authService.generateJwtResponse(userDetails, false);
		} else {
			throw new ForbiddenException();
		}
	}

	@PostMapping("/console-login")
	public JwtResponse authenticateOperator(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request)
			throws UnauthorizedException, ForbiddenException {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		if (userDetails.canLogin()) {
			if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Role.ROLE_OPERATORE.name()))) {
				logger.info("Operatore " + loginRequest.getUsername() + ": accesso effettuato da IP "
						+ request.getRemoteAddr() + ", invio il token.");
				// Ritorno l'utente loggato
				return authService.generateJwtResponse(userDetails, false);
			} else {
				throw new UnauthorizedException();
			}
		} else {
			throw new ForbiddenException();
		}
	}

	@PostMapping("/token-login")
	public JwtResponse authenticateUserWithToken(@RequestBody TokenDto tokenDto, HttpServletRequest request)
			throws ForbiddenException {
		UserDetailsImpl userDetails;
		try {
			userDetails = authService.authenticate(request, tokenDto.getToken());
		} catch (AuthenticationException e) {
			throw new ForbiddenException();
		}
		logger.info("Utente " + userDetails.getUsername() + ": accesso effettuato tramite token");
		// Ritorno l'utente loggato
		return authService.generateJwtResponse(userDetails, false);
	}

	@PostMapping("/impersonate/{id}")
	public JwtResponse impersonate(@PathVariable Integer id, HttpServletResponse response)
			throws ForbiddenException {
		UserDetailsImpl loggedUser = authService.getLoggedUser();
		if (loggedUser != null) {
			UserDetailsImpl targetUser = authService.loadUserById(id);
			// L'account che richiede di impersonare deve essere ADMIN o ASSISTENZA
			if (loggedUser.hasRole(Role.ROLE_ADMIN) &&
			// L'account da impersonare deve essere OPERATORE
					targetUser.hasRole(Role.ROLE_OPERATORE)) {
				// Genero Token e lo aggiungo all'header
				logger.info("Impersonato account " + id + " dall'utente " + loggedUser.getUsername());
				return authService.generateJwtResponse(targetUser, true);
			} else {
				throw new ForbiddenException();
			}
		} else {
			throw new ForbiddenException();
		}
	}

}
