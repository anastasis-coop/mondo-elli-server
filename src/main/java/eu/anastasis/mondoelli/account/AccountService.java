package eu.anastasis.mondoelli.account;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.anastasis.mondoelli.operatore.Operatore;
import eu.anastasis.mondoelli.operatore.OperatoreRepository;
import eu.anastasis.mondoelli.security.UserDetailsImpl;
import eu.anastasis.mondoelli.security.services.AuthService;
import eu.anastasis.mondoelli.utente.UtenteRepository;

@Service
public class AccountService {

	@Autowired
	AuthService authService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	OperatoreRepository operatoreRepository;

	@Autowired
	UtenteRepository utenteRepository;

	public Optional<Account> findLoggedAccount() {
		UserDetailsImpl loggedUser = authService.getLoggedUser();
		if (loggedUser == null) {
			return Optional.empty();
		} else {
			return this.findAccountById(loggedUser.getId());
		}
	}

	public Optional<Account> findAccountById(Integer id) {
		return accountRepository.findById(id);
	}

	public Optional<Account> findAccountByUsername(String username) {
		Optional<Operatore> operatore = operatoreRepository.findByUsername(username);
		if (operatore.isPresent()) {
			return Optional.of(operatore.get().getAccount());
		} else {
			return Optional.empty();
		}
	}

	public boolean alreadyExists(String username) {
		Optional<Account> account = findAccountByUsername(username);
		return account.isPresent();
	}

	public boolean samePassword(Account account, String password) {
		return authService.checkPasswordHash(password, account.getPassword());
	}

	@Transactional
	public void setUsername(Account account, String username) {
		if (account.isOperatore()) {
			account.getOperatore().setUsername(username);
			accountRepository.save(account);
		}
		if (account.isUtente()) {
			account.getUtente().setUsername(username);
			accountRepository.save(account);
		}
	}

	@Transactional
	public void setPassword(Account account, String password) {
		account.setPassword(passwordEncoder.encode(password));
		account.setDataCreazionePassword(new Date());
		accountRepository.save(account);
	}

	@Transactional
	public void acceptPrivacy(Account account) {
		account.setPrivacy(true);
		accountRepository.save(account);
	}

	@Transactional
	public Account createNewAccount(Set<Role> ruoli) {
		Account account = new Account();
		account.setEnabled(true);
		account.setRuoli(ruoli);
		return accountRepository.save(account);
	}

	public Account createNewAccount() {
		return createNewAccount(null);
	}

}
