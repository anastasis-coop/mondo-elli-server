package eu.anastasis.mondoelli.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenGenerationException extends Exception {

	private static final long serialVersionUID = 7175467020976704638L;

	public TokenGenerationException(String msg, Exception e) {
		super(msg, e);
	}
}
