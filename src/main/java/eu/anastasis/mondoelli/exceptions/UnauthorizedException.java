package eu.anastasis.mondoelli.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {

	private static final long serialVersionUID = -4316934630769939859L;

	public UnauthorizedException() {
		super("L'utente corrente non ha i permessi per accedere a questa risorsa.");
	}

}
