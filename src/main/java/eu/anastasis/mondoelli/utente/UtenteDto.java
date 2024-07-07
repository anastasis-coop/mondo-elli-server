package eu.anastasis.mondoelli.utente;

import lombok.Data;

@Data
public class UtenteDto {

	private Integer id;
	private String username;
	private Boolean facilitato;
	private String password;
	private String nomeEllo;

	private Boolean utenteOperatore;

}
