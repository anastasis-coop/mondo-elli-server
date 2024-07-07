package eu.anastasis.mondoelli.monitor.dto;

import java.util.List;


import eu.anastasis.mondoelli.enums.RoomChannel;
import eu.anastasis.mondoelli.enums.RoomLevel;
import lombok.Data;

@Data
public class MonitorFunzioneCanaleDto {
	RoomLevel livelloRaggiunto;
	RoomChannel canale;
	List<MonitorEsercizioStanzaDto> esercizi;
}
