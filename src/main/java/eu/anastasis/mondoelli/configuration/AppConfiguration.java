package eu.anastasis.mondoelli.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class AppConfiguration {

	private String clientAppUrl;

	private Boolean mailCommunicationEnabled;
	private Boolean mailOverride;
	private String mailOverrideAddress;

	private String mailAddressFrom;
	private String mailAddressReplyTo;

	// Limitazioni attive solo per le richieste pubbliche di esistenza account
	private Integer maxRequestsPerMinuteFromSingleIp;
	private Integer minDelayBetweenRequestsFromSingleIp;

	private Long accessTokenNormalValiditySeconds;
	private Long accessTokenLongValiditySeconds;

	private String emailAssistenza;

}
