package eu.anastasis.mondoelli.security.dto;

import lombok.Data;

@Data
public class TokenDto {

	private String token;

	public TokenDto() {
		super();
	}

	public TokenDto(String token) {
		super();
		this.token = token;
	}

}
