package eu.anastasis.mondoelli.utils;

import org.springframework.boot.logging.LogLevel;

import lombok.Data;

@Data
public class ClientLogDto {

	private LogLevel level;
	private String text;

}
