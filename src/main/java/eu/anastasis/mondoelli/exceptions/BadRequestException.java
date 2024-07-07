package eu.anastasis.mondoelli.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

	private static final long serialVersionUID = -149872971154044681L;

	public BadRequestException() {
		super("La chiamata specifica parametri non validi.");
	}

}
