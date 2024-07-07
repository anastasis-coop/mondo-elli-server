package eu.anastasis.mondoelli.percorso;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.utente.UtenteDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UtentePercorsoDto extends UtenteDto {

	private Date primaSessione;

	@Enumerated(EnumType.STRING)
	private Quartiere quartiere;

}
