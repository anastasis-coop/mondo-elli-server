package eu.anastasis.mondoelli.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "application.constants")
@Data
public class ConstantsConfiguration {

	private Integer sogliaAccuratezzaPassato;
	private Integer sogliaAccuratezzaFallito;
	private Integer durataIntroduzioneGiorni;
	private Integer durataMediaLiteracyGiorni;
	private Integer tempoMassimoIntroduzione;
	private Integer tempoMassimoEsplorazione;

}
