package eu.anastasis.mondoelli.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7886928948463565026L;

	public NotFoundException() {
		this("Oggetto non trovato");
	}

	public NotFoundException(String msg) {
		super(msg);
	}

	public NotFoundException(String msg, Exception e) {
		super(msg, e);
	}

}
