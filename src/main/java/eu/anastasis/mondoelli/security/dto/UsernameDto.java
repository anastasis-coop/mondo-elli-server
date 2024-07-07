package eu.anastasis.mondoelli.security.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UsernameDto {

	@NotBlank
	private String username;

}
