package eu.anastasis.mondoelli.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.anastasis.mondoelli.security.dto.CheckPasswordDto;
import eu.anastasis.mondoelli.security.dto.PasswordDto;
import eu.anastasis.mondoelli.security.services.PasswordService;

@RestController
@RequestMapping("/password")
public class PasswordController {

	@Autowired
	PasswordService passwordService;

	@PostMapping("/check")
	public CheckPasswordDto checkPasswordComplexity(@RequestBody PasswordDto dto) {
		return passwordService.checkPasswordComplexity(dto.getPassword());
	}

}
