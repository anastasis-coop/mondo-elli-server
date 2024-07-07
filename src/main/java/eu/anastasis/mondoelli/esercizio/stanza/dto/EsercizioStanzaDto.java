package eu.anastasis.mondoelli.esercizio.stanza.dto;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.enums.RoomChannel;
import eu.anastasis.mondoelli.enums.RoomLevel;
import lombok.Data;

@Data
public class EsercizioStanzaDto {

	@Enumerated(EnumType.STRING)
	private RoomLevel livello;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Date inizio;

	private Integer idEsercizio;

	@Enumerated(EnumType.STRING)
	private Quartiere quartiere;

	@Enumerated(EnumType.STRING)
	private RoomChannel canale;

	private Boolean finito;

}
