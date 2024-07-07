package eu.anastasis.mondoelli.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import eu.anastasis.mondoelli.security.SecurityAuditorAware;
import eu.anastasis.mondoelli.security.jwt.AuthEntryPointJwt;
import eu.anastasis.mondoelli.security.jwt.AuthTokenFilter;
import eu.anastasis.mondoelli.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class WebSecurityConfig {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationBasicOrJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
			UserDetailsService userDetailsService) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder)
				.and()
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuditorAware<Integer> auditorAware() {
		return new SecurityAuditorAware();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.addFilterBefore(authenticationBasicOrJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.httpBasic().and()
				.authorizeRequests()
				.antMatchers("/auth/**").permitAll() // Permette di fare login
				.antMatchers("/cron/**").permitAll() // Permette di eseguire i cron
				.antMatchers("/accounts/exists").permitAll() // Permette il controllo dell'esistenza di un utente
				.antMatchers("/accounts/recover").permitAll() // Permette il ripristino della password
				.anyRequest().authenticated();
		return http.build();
	}

}
