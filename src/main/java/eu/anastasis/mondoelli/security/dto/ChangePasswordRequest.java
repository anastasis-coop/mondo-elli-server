package eu.anastasis.mondoelli.security.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ChangePasswordRequest {

	@NotNull
	private Long idUtente;

	@NotBlank
	private String oldPassword;

	@NotBlank
	private String newPassword;
}
