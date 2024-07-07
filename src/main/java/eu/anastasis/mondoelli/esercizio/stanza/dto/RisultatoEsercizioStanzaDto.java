package eu.anastasis.mondoelli.esercizio.stanza.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RisultatoEsercizioStanzaDto {

	private Date inizio;
	private Integer durata;
	private Integer numeroStimoliCorretti;
	private Integer numeroStimoliErrati;
	private Integer numeroStimoliSaltati;
	private Integer tempoReazioneMedio; // in ms
	private Integer tempoEsposizioneOggetti; // in ms
	private Integer feedbackAttenzione;

}
