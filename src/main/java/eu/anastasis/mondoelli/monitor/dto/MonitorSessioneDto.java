package eu.anastasis.mondoelli.monitor.dto;

import java.util.Date;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class MonitorSessioneDto {
	private Integer id;
	private Date data;
	private Boolean inStudio;
	private Integer tempoNetto; // in minuti
	private Integer accuratezzaMedia;
	private Integer score;
	private String steps;
	Quartiere quartiere;
	Boolean completata;
}
