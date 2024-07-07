package eu.anastasis.mondoelli.utente;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UtentiDto {
	private List<UtenteDto> content = new ArrayList<UtenteDto>();
	private int numberOfElements = 0;
	private int totalPages = 0;
	private long totalElements = 0;
}
