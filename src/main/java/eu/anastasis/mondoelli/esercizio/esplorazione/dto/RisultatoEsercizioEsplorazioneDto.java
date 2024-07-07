package eu.anastasis.mondoelli.esercizio.esplorazione.dto;

import java.util.Date;

import eu.anastasis.mondoelli.enums.Prospettiva;
import lombok.Data;

@Data
public class RisultatoEsercizioEsplorazioneDto {

	private Date inizio;

	private String compito; // es :fflrff è forward forward left right forward forward

	private String esecuzione;

	private Boolean corretto;

	private Boolean finale;

	private Integer tempoImpiegato; // sec

	private Boolean ombra = false;

	private Prospettiva prospettiva;

}
