package eu.anastasis.mondoelli.sessione.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class DatiSessioneDto {

	private Integer id;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Date inizio;

	private Quartiere quartiere;

	private Integer tempoMassimoEsplorazione;

	private Boolean demo;

	private Boolean mediaLiteracy;

	private Boolean quartiereCompletato;

	private Boolean percorsoNonIniziato;
	private Boolean percorsoCompletato;

	private Integer livello;
	private Integer tessere;

}
