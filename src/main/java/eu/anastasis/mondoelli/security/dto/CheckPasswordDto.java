package eu.anastasis.mondoelli.security.dto;

import lombok.Data;

@Data
public class CheckPasswordDto {

	private Boolean isValid;
	private Boolean isNullOrEmpty;
	private Boolean isTooShort;
	private Boolean containsInvalidChars;
	private Boolean missingUppercaseLetters;
	private Boolean missingLowercaseLetters;
	private Boolean missingNumbers;
	private Boolean missingSymbols;

}
