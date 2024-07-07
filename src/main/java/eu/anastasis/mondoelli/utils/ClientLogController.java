package eu.anastasis.mondoelli.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientLogController {

	private static final Logger logger = LoggerFactory.getLogger(ClientLogController.class);

	@PostMapping(value = "/log")
	public ResponseEntity<Void> appendLog(@RequestHeader(value = "User-Agent") String userAgent, @RequestBody ClientLogDto dto) {
		String message = "CLIENT: " + dto.getText();
		message = dto.getLevel().toString() + ": " + message + "\nUser-Agent: " + userAgent;
		switch (dto.getLevel()) {
		case DEBUG:
			logger.debug(message);
			break;
		case ERROR:
		case FATAL:
			logger.error(message);
			break;
		case INFO:
			logger.info(message);
			break;
		case TRACE:
			logger.trace(message);
			break;
		case WARN:
			logger.warn(message);
			break;
		default:
			break;
		}
		return ResponseEntity.ok().build();
	}

}
