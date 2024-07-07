package eu.anastasis.mondoelli.configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Data;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
@ConfigurationProperties("application.webclient")
@Data
public class WebClientConfiguration {
	private int maxConnections;
	private int timeoutSeconds;

	@Bean
	public WebClient webClient(WebClient.Builder webClientBuilder) {
		Integer maxConnections = getMaxConnections();
		Integer timeoutSec = getTimeoutSeconds();
		ConnectionProvider fixedPool = ConnectionProvider.builder("fixedPool")
				.maxConnections(maxConnections)
				.pendingAcquireTimeout(Duration.ofSeconds(timeoutSec))
				.build();

		HttpClient httpClient = HttpClient.create(fixedPool)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSec * 1000)
				.doOnConnected(conn -> conn
						.addHandlerLast(new ReadTimeoutHandler(timeoutSec, TimeUnit.SECONDS))
						.addHandlerLast(new WriteTimeoutHandler(timeoutSec, TimeUnit.SECONDS)));

		ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
		return WebClient.builder()
				.clientConnector(connector)
				.exchangeStrategies(createNonNullStrategies())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	private ExchangeStrategies createNonNullStrategies() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return ExchangeStrategies.builder()
				.codecs(clientDefaultCodecsConfigurer -> {
					clientDefaultCodecsConfigurer.defaultCodecs()
							.jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
					clientDefaultCodecsConfigurer.defaultCodecs()
							.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
				}).build();
	}

}
