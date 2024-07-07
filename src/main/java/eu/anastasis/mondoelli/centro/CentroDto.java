package eu.anastasis.mondoelli.centro;

import eu.anastasis.mondoelli.comune.ComuneDto;
import lombok.Data;

@Data
public class CentroDto {

	private Integer id;
	private String nome;
	private String codice;
	private ComuneDto comune;

}
