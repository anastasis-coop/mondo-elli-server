package eu.anastasis.mondoelli.percorso;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import eu.anastasis.mondoelli.enums.Quartiere;
import eu.anastasis.mondoelli.enums.StatoPercorso;
import eu.anastasis.mondoelli.enums.TipoPercorso;
import lombok.Data;

@Data
public class PercorsoDto {

	private Integer id;

	@Enumerated(EnumType.STRING)
	private TipoPercorso tipo;

	private String nome;
	private Date inizioPercorso;
	private Date inizioPercorsoEffettivo;
	private Integer durataFunzioneEsecutivaGiorni;
	private Boolean periodoIntroduzione;
	private Boolean mediaLiteracy;
	private Boolean archiviato;

	private Integer numeroUtenti;
	private Integer numeroOperatori;
	private Date finePercorso;
	private Quartiere quartiere;
	private StatoPercorso stato;

}
