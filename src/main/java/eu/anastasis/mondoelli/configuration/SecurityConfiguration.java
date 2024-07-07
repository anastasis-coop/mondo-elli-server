package eu.anastasis.mondoelli.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "application.security")
@Data
public class SecurityConfiguration {

	private String jwtSecret;
	private Long jwtExpirationMs;

}
