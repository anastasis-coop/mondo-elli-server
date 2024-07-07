package eu.anastasis.mondoelli.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidPasswordTokenException extends AuthenticationException {

	private static final long serialVersionUID = 539590503774671055L;

	public InvalidPasswordTokenException(String msg) {
		super(msg);
	}

}
