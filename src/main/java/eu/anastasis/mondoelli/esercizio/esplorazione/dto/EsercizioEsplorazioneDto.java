package eu.anastasis.mondoelli.esercizio.esplorazione.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EsercizioEsplorazioneDto {

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Date inizio;

}
