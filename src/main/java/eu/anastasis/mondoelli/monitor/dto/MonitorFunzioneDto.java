package eu.anastasis.mondoelli.monitor.dto;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class MonitorFunzioneDto {
	Quartiere funzione;
	MonitorFunzioneCanaleDto canaleVisivo;
	MonitorFunzioneCanaleDto canaleUditivoVerbale;
}
