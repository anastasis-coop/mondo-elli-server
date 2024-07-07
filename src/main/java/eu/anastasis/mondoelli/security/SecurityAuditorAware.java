package eu.anastasis.mondoelli.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import eu.anastasis.mondoelli.security.services.UserDetailsServiceImpl;

@Component
public class SecurityAuditorAware implements AuditorAware<Integer> {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Override
	public Optional<Integer> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return Optional.of(userDetails.getId());
	}
}
