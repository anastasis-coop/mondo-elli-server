package eu.anastasis.mondoelli.monitor.dto;

import java.util.Date;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class MonitorQuizDto {
	Date data;
	Quartiere quartiere;
	String domanda;
	String risposta;
	Boolean corretto;
	Integer tempoImpiegato;
}
