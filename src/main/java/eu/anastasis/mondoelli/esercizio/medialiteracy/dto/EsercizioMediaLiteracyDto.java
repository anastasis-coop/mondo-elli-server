package eu.anastasis.mondoelli.esercizio.medialiteracy.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EsercizioMediaLiteracyDto {

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Date inizio;

}
