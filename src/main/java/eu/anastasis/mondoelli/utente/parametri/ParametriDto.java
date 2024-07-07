package eu.anastasis.mondoelli.utente.parametri;

import java.util.Set;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class ParametriDto {

	private String nomeEllo;
	private Integer accessorio1;
	private Integer accessorio2;
	private Integer accessorio3;

	private Boolean demo;
	private Quartiere quartiereCorrente;
	private Boolean percorsoNonIniziato;
	private Boolean percorsoCompletato;
	private Set<Quartiere> quartieriCompletati;

}
