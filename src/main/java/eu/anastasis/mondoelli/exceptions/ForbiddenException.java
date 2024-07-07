package eu.anastasis.mondoelli.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends Exception {

	private static final long serialVersionUID = -2566682759144586325L;

	public ForbiddenException() {
		super("L'utente corrente non ha i permessi per accedere a questa risorsa.");
	}

}
