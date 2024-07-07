package eu.anastasis.mondoelli.monitor.dto;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class MonitorPercorsoDto {
	Boolean corrente;
	Boolean completato;
	Quartiere quartiere;
	String visivo;
	String verbale;
	Integer numeroSessioni;
}
