package eu.anastasis.mondoelli.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.account.AccountRepository;
import eu.anastasis.mondoelli.exceptions.NotFoundException;
import eu.anastasis.mondoelli.operatore.Operatore;
import eu.anastasis.mondoelli.operatore.OperatoreRepository;
import eu.anastasis.mondoelli.security.UserDetailsImpl;
import eu.anastasis.mondoelli.utente.UtenteRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	OperatoreRepository operatoreRepository;

	@Autowired
	UtenteRepository utenteRepository;

	public UserDetails loadUserById(Integer id) throws NotFoundException {
		Account user = accountRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User Not Found with id: " + id));
		return UserDetailsImpl.build(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String[] parts = username.split("\0");
		if (parts.length == 1) {
			Operatore operatore = operatoreRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));
			return UserDetailsImpl.build(operatore.getAccount());
		} else {
			return loadUserById(Integer.parseInt(parts[0]));
		}
	}

}
