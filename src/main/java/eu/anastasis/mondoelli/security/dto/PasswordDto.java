package eu.anastasis.mondoelli.security.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PasswordDto {

	@NotBlank
	private String password;

}
