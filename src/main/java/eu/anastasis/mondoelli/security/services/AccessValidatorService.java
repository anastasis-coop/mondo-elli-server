package eu.anastasis.mondoelli.security.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.account.AccountService;
import eu.anastasis.mondoelli.account.Role;

@Service
public class AccessValidatorService {

	@Autowired
	AccountService accountService;

	private static final Logger logger = LoggerFactory.getLogger(AccessValidatorService.class);

	public boolean canAccessLoggedAccount() {
		Optional<Account> optionalAccount = accountService.findLoggedAccount();
		return optionalAccount.isPresent();
	}

	public boolean canAccessByRole(Role role) {
		boolean canAccess;

		Optional<Account> optionalAccount = accountService.findLoggedAccount();
		if (optionalAccount.isPresent()) {
			canAccess = hasRole(optionalAccount, role);
		} else {
			canAccess = false;

		}

		return canAccessGeneric(optionalAccount, canAccess);
	}

	public boolean canAccessByAuthorizedAccountId(Integer authorizedAccountId) {
		boolean canAccess;

		Optional<Account> optionalAccount = accountService.findLoggedAccount();
		if (optionalAccount.isPresent()) {
			Account account = optionalAccount.get();
			canAccess = isAccountAuthorized(account, authorizedAccountId);
		} else {
			canAccess = false;

		}

		return canAccessGeneric(optionalAccount, canAccess);
	}

	private boolean canAccessGeneric(Optional<Account> optionalAccount, boolean canAccess) {
		return canAccess || isAdmin(optionalAccount);
	}

	private boolean isAdmin(Optional<Account> optionalAccount) {
		return hasRole(optionalAccount, Role.ROLE_ADMIN);
	}

	private boolean hasRole(Optional<Account> optionalAccount, Role role) {
		if (!optionalAccount.isPresent()) {
			return false;
		}
		Account account = optionalAccount.get();
		boolean hasRole = account.getRuoli().contains(role);
		return hasRole;
	}

	private boolean isAccountAuthorized(Account account, Integer authorizedAccountId) {
		return account.getId().equals(authorizedAccountId);
	}

	private List<IpAccess> lastIpAccesses = new ArrayList<IpAccess>();

	public boolean canAccessByIpAddressWithLimitations(String ip, Integer maxRequestsPerMinute, Integer minDelay) {
		// Elimina tutte le richieste più vecchie di un minuto
		Long nowMillis = new Date().getTime();
		Date aMinuteAgo = new Date(nowMillis - 60000);
		Date delayLimit = new Date(nowMillis - minDelay * 1000);
		lastIpAccesses = lastIpAccesses.stream().filter(a -> a.getWhen().after(aMinuteAgo)).collect(Collectors.toList());
		// Conta quante richieste sono state fatte dallo stesso ip
		long numRequestsFromIp = lastIpAccesses.stream().filter(a -> a.getIp().equals(ip)).count();
		if (numRequestsFromIp > maxRequestsPerMinute) {
			// Troppe richieste al minuto
			logger.debug("Too many requests");
			return false;
		}
		// Trova, se c'è, l'ultimo accesso fatto da questo ip
		IpAccess lastAccessFromIp = lastIpAccesses.stream().filter(a -> a.getIp().equals(ip)).findFirst().orElse(null);
		if (lastAccessFromIp != null && lastAccessFromIp.getWhen().after(delayLimit)) {
			// Troppo poco tempo dall'ultima richiesta fatta
			logger.debug("Delay limit");
			return false;
		}
		// Aggiunge in testa alla lista il nuovo accesso
		lastIpAccesses.add(0, new IpAccess(ip));
		return true;
	}

}
