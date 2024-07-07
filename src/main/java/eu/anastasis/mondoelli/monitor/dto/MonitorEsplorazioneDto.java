package eu.anastasis.mondoelli.monitor.dto;

import java.util.Date;

import eu.anastasis.mondoelli.enums.Prospettiva;
import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class MonitorEsplorazioneDto {

	private Date inizio;

	private Quartiere quartiere;

	private String compito; // es :fflrff è forward forward left right forward forward

	private String esecuzione;

	private Boolean corretto;

	private Boolean finale;

	private Integer tempoImpiegato; // sec

	private Boolean ombra = false;

	private Prospettiva prospettiva;

}
