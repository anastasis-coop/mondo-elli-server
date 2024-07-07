package eu.anastasis.mondoelli.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidUsernameOrPasswordException extends AuthenticationException {

	private static final long serialVersionUID = -3580617948698679805L;

	public InvalidUsernameOrPasswordException(String msg) {
		super(msg);
	}

	public InvalidUsernameOrPasswordException() {
		super("Username o password non validi");
	}
}
