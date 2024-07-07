package eu.anastasis.mondoelli.percorso;

import lombok.Data;

@Data
public class SerieUtentiDto {

	private Integer numeroUtenti;
	private String prefissoUsername;
	private Integer indicePrimoUtente;
	private String password;

}
