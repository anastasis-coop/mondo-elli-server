package eu.anastasis.mondoelli.quartiere;

import eu.anastasis.mondoelli.enums.Quartiere;
import lombok.Data;

@Data
public class QuartiereDto {

	private Quartiere quartiere;
	private Boolean percorsoNonIniziato;
	private Boolean percorsoCompletato;

}
