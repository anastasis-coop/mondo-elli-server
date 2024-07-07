package eu.anastasis.mondoelli.esercizio.stanza.dto;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.enums.RoomChannel;
import lombok.Data;

@Data
public class InitEsercizioDto {

	private Quartiere funzioneEsecutiva;
	private RoomChannel canale;

}
