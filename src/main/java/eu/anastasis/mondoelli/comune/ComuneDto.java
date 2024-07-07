package eu.anastasis.mondoelli.comune;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ComuneDto {
	private Integer id;
	private String nome;
	private String targa;
}
