package eu.anastasis.mondoelli.monitor.dto;

import java.util.Date;

import eu.anastasis.mondoelli.enums.LevelStatus;
import eu.anastasis.mondoelli.enums.RoomChannel;
import eu.anastasis.mondoelli.enums.RoomLevel;
import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class MonitorEsercizioStanzaDto {

	private Date inizio;
	private Integer durata;
	private RoomLevel livello;
	private Quartiere funzioneEsecutiva;
	private RoomChannel canale;
	private Integer numeroStimoliCorretti;
	private Integer numeroStimoliErrati;
	private Integer numeroStimoliSaltati;
	private Integer tempoReazioneMedio; // in ms
	private Integer tempoEsposizioneOggetti; // in ms
	private Integer feedbackAttenzione;
	private Date fine;
	private LevelStatus statoLivello;
	private Integer accuratezza;

	public boolean isEmpty() {
		return ((inizio == null) && (durata == null) && (livello == null) && (funzioneEsecutiva == null) && (canale == null) && (numeroStimoliCorretti == null) && (numeroStimoliErrati == null) && (numeroStimoliSaltati == null) && (tempoReazioneMedio == null) && (tempoEsposizioneOggetti == null) && (feedbackAttenzione == null) && (fine == null) && (statoLivello == null) && (accuratezza == null));
	}

}
