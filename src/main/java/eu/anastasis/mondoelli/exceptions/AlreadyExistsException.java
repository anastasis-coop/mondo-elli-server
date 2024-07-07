package eu.anastasis.mondoelli.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends Exception {

	private static final long serialVersionUID = -5620634484826350488L;

	public AlreadyExistsException() {
		super("Risorsa specificata già esistente.");
	}

}
