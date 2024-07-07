package eu.anastasis.mondoelli.account;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.configuration.AppConfiguration;
import eu.anastasis.mondoelli.exceptions.ForbiddenException;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.operatore.OperatoreService;
import eu.anastasis.mondoelli.security.UserDetailsImpl;
import eu.anastasis.mondoelli.security.dto.PasswordDto;
import eu.anastasis.mondoelli.security.dto.TokenDto;
import eu.anastasis.mondoelli.security.dto.UsernameDto;
import eu.anastasis.mondoelli.security.jwt.JwtUtils;
import eu.anastasis.mondoelli.security.services.AccessValidatorService;
import eu.anastasis.mondoelli.security.services.AuthService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	AppConfiguration appConfiguration;

	@Autowired
	AccountService accountService;

	@Autowired
	OperatoreService operatoreService;

	@Autowired
	AccessValidatorService accessValidatorService;

	@Autowired
	AuthService authService;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/exists")
	public Boolean accountExists(@RequestBody AccountCheckDto dto, HttpServletRequest request)
			throws ForbiddenException {
		Optional<Account> currentUserOpt = accountService.findLoggedAccount();
		if (currentUserOpt.isPresent()) {
			// Se la chiamata è autenticata non ci sono restrizioni
			return accountService.alreadyExists(dto.getUsername());
		} else {
			// Per le chiamate pubbliche vengono applicate restrizioni
			Integer maxRequestsPerMinute = appConfiguration.getMaxRequestsPerMinuteFromSingleIp();
			Integer minDelayBetweenRequests = appConfiguration.getMinDelayBetweenRequestsFromSingleIp();
			boolean canAccess = accessValidatorService.canAccessByIpAddressWithLimitations(request.getRemoteAddr(),
					maxRequestsPerMinute, minDelayBetweenRequests);
			if (canAccess) {
				return accountService.alreadyExists(dto.getUsername());
			} else {
				throw new ForbiddenException();
			}
		}
	}

	@PostMapping("/same-password")
	public Boolean samePassword(@RequestBody PasswordDto dto, HttpServletRequest request)
			throws ForbiddenException {
		Account account = accountService.findLoggedAccount().orElseThrow(ForbiddenException::new);
		return accountService.samePassword(account, dto.getPassword());
	}

	@PostMapping("/recover")
	public ResponseEntity<Void> recoverPassword(@RequestBody AccountCheckDto dto) throws ForbiddenException {
		operatoreService.recoverPassword(dto.getUsername());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/username")
	public TokenDto changeUsername(@PathVariable Integer id, @RequestBody UsernameDto dto)
			throws ForbiddenException {
		boolean canAccess = accessValidatorService.canAccessByAuthorizedAccountId(id);
		if (canAccess) {
			boolean impersonate = authService.getImpersonate();
			Account account = accountService.findAccountById(id).orElseThrow(NotFoundException::new);
			accountService.setUsername(account, dto.getUsername());
			return new TokenDto(jwtUtils.generateJwtToken(UserDetailsImpl.build(account), impersonate));
		} else {
			throw new ForbiddenException();
		}
	}

	@PostMapping("/{id}/password")
	public ResponseEntity<Void> changePassword(@PathVariable Integer id, @RequestBody PasswordDto dto)
			throws ForbiddenException {
		boolean canAccess = accessValidatorService.canAccessByAuthorizedAccountId(id);
		if (canAccess) {
			Account account = accountService.findAccountById(id).orElseThrow(NotFoundException::new);
			accountService.setPassword(account, dto.getPassword());
			return ResponseEntity.ok().build();
		} else {
			throw new ForbiddenException();
		}
	}

	@PostMapping("/{id}/privacy")
	public ResponseEntity<Void> acceptPrivacy(@PathVariable Integer id) throws ForbiddenException {
		boolean canAccess = accessValidatorService.canAccessByAuthorizedAccountId(id);
		if (canAccess) {
			Account account = accountService.findAccountById(id).orElseThrow(NotFoundException::new);
			accountService.acceptPrivacy(account);
			return ResponseEntity.ok().build();
		} else {
			throw new ForbiddenException();
		}
	}

}
