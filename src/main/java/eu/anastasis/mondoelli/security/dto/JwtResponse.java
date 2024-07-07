package eu.anastasis.mondoelli.security.dto;

import java.util.List;

import lombok.Data;

@Data
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private Integer id;
	private String username;
	private List<String> roles;
	private Boolean privacy;
	private Boolean passwordExpired;

}
