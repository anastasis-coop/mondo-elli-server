package eu.anastasis.mondoelli.operatore;

import java.util.Date;

import lombok.Data;

@Data
public class OperatoreDto {

	private Integer id;
	private String username;
	private String nome;
	private String cognome;
	private Boolean referente;
	private Date dataCreazionePassword;

}
