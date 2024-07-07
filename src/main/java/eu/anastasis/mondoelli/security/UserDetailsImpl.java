package eu.anastasis.mondoelli.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.anastasis.mondoelli.account.Account;
import eu.anastasis.mondoelli.account.Role;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	@JsonIgnore
	private String password;

	private Boolean enabled;

	private Boolean privacy;

	private Boolean impersonate;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Integer id, String username, String password, Boolean enabled, Boolean privacy,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.privacy = privacy;
		this.authorities = authorities;
	}

	public static UserDetailsImpl build(Account account) {
		List<GrantedAuthority> authorities = account.getRuoli().stream()
				.map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());
		String username = null;
		if (account.isOperatore()) {
			username = account.getOperatore().getUsername();
		}
		if (account.isUtente()) {
			username = account.getUtente().getUsername();
		}
		return new UserDetailsImpl(account.getId(), username,
				account.getPassword(), account.getEnabled(),
				account.getPrivacy(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Integer getId() {
		return id;
	}

	public boolean canLogin() {
		return this.enabled;
	}

	public Boolean getPrivacy() {
		return privacy;
	}

	public boolean getImpersonate() {
		return (this.impersonate != null && this.impersonate == true);
	}

	public void setImpersonate(Boolean impersonate) {
		this.impersonate = impersonate;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	public boolean hasRole(Role role) {
		return this.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(role.name()));
	}

}
