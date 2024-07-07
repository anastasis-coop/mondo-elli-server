package eu.anastasis.mondoelli.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.security.dto.TokenDto;
import eu.anastasis.mondoelli.security.jwt.JwtUtils;

@RestController
@RequestMapping("/token")
public class TokenController {

	@Autowired
	JwtUtils jwtUtils;

	@GetMapping("/check")
	public ResponseEntity<Void> checkToken() {
		// Il controllo viene fatto dai filtri
		return ResponseEntity.ok().build();
	}

	@GetMapping("/extend")
	public TokenDto extendToken(Authentication authentication) {
		return new TokenDto(jwtUtils.generateJwtToken(authentication));
	}

}
